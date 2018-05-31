package cn.gov.zunyi.video.auth;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AccountException;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.DisabledAccountException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;

import cn.gov.zunyi.video.model.Permission;
import cn.gov.zunyi.video.model.Role;
import cn.gov.zunyi.video.model.RolePermission;
import cn.gov.zunyi.video.model.User;
import cn.gov.zunyi.video.model.UserRole;
import cn.gov.zunyi.video.service.PermissionService;
import cn.gov.zunyi.video.service.RolePermissionService;
import cn.gov.zunyi.video.service.RoleService;
import cn.gov.zunyi.video.service.UserRoleService;
import cn.gov.zunyi.video.service.UserService;

/**
 * shiro身份校验核心类
 */
public class MyShiroRealm extends AuthorizingRealm {

	@Autowired
	private UserService userService;

	@Autowired
	private PermissionService permissionService;

	@Autowired
	private StringRedisTemplate stringRedisTemplate;

	@Autowired
	private RoleService roleService;

	@Autowired
	private UserRoleService userRoleService;

	@Autowired
	private RolePermissionService rolePermissionService;

	// 用户登录次数计数 redisKey 前缀
	private static final String SHIRO_LOGIN_COUNT = "shiro_login_count_";

	private static final String SHIRO_REDIS_CACHE = "shiro:cache:shiro_redis_cache:";

	// 用户登录是否被锁定 一小时 redisKey 前缀
	private static final String SHIRO_IS_LOCK = "shiro_is_lock_";

	/**
	 * 认证信息.(身份验证) : Authentication 是用来验证用户身份
	 * 
	 * @param authcToken
	 * @return
	 * @throws AuthenticationException
	 */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authcToken)
			throws AuthenticationException {
		System.out.println("身份认证方法：MyShiroRealm.doGetAuthenticationInfo()");
		UsernamePasswordToken token = (UsernamePasswordToken) authcToken;
		String username = token.getUsername();
		String password = String.valueOf(token.getPassword());
		// 访问一次，计数一次
		ValueOperations<String, String> opsForValue = stringRedisTemplate.opsForValue();
		opsForValue.increment(SHIRO_LOGIN_COUNT + username, 1);
		// 计数大于5时，设置用户被锁定一小时
		if (Integer.parseInt(opsForValue.get(SHIRO_LOGIN_COUNT + username)) >= 50) {
			opsForValue.set(SHIRO_IS_LOCK + username, "LOCK");
			stringRedisTemplate.expire(SHIRO_IS_LOCK + username, 30, TimeUnit.MINUTES);
		}

		// if ("LOCK".equals(opsForValue.get(SHIRO_IS_LOCK + name))) { throw new
		// DisabledAccountException("由于密码输入错误次数大于5次，帐号已经禁止登录！"); }

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("username", username);

		User user = null;
		// 从数据库获取对应用户名密码的用户
		List<User> userList = userService.selectByMap(map);

		if (userList.size() != 0) {
			user = userList.get(0);
			byte[] salt = user.getPasswordSalt();
			if (!new String(SHA256PasswordEncryptionService.createPasswordHash(password, salt))
					.equals(new String(user.getPasswordHash()))) {
				throw new IncorrectCredentialsException("用户名或密码不正确！");
			}
		}
		if (null == user) {
			// throw new UnknownAccountException();// 没找到帐号
			throw new AccountException("帐号或密码不正确！");
		} else if (user.getEnabled() == 0) {
			/*
			 * 如果用户的status为禁用。那么就抛出DisabledAccountException
			 */
			// throw new LockedAccountException(); // 帐号锁定
			throw new DisabledAccountException("此帐号已禁止登录，请联系管理员！");
		} else {
			// 登录成功
			user.setLastLoginTime(new Date(System.currentTimeMillis()));
			this.userService.updateById(user);
			// 清空登录计数
			opsForValue.set(SHIRO_LOGIN_COUNT + username, "0");
		}
		return new SimpleAuthenticationInfo(user, password, getName());
	}

	/**
	 * 授权
	 */
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		System.out.println("权限认证方法：MyShiroRealm.doGetAuthorizationInfo()");
		User user = (User) SecurityUtils.getSubject().getPrincipal();
		Integer userId = user.getId();
		SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
		// 根据用户ID查询角色（role），放入到Authorization里。
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("uid", userId);
		List<UserRole> userRoleList = this.userRoleService.selectByMap(map);
		List<Integer> ridList = new LinkedList<Integer>();
		for (UserRole userRole : userRoleList) {
			ridList.add(userRole.getRid());
		}
		List<Role> roleList = this.roleService.selectBatchIds(ridList);
		Set<String> roleSet = new HashSet<String>();
		for (Role role : roleList) {
			roleSet.add(role.getAlias());
		}
		info.setRoles(roleSet);

		// 根据用户ID查询权限（permission），放入到Authorization里。
		Wrapper<RolePermission> wrapper = new EntityWrapper<RolePermission>();
		wrapper.in("rid", ridList).setSqlSelect("pid");
		List<Object> permissionIdList = this.rolePermissionService.selectObjs(wrapper);
		Wrapper<Permission> ew = new EntityWrapper<>();
		ew.in("id", permissionIdList);
		List<Permission> permissionList = this.permissionService.selectList(ew);

		Set<String> permissionSet = new HashSet<String>();
		for (Permission permission : permissionList) {
			System.out.println(permission.getUrl());
			permissionSet.add(permission.getUrl());
		}
		info.setStringPermissions(permissionSet);
		return info;
	}

	@Override
	public void clearCachedAuthorizationInfo(PrincipalCollection principals) {
		super.clearCachedAuthorizationInfo(principals);
	}

	@Override
	public void clearCachedAuthenticationInfo(PrincipalCollection principals) {
		super.clearCachedAuthenticationInfo(principals);
	}

	@Override
	public void clearCache(PrincipalCollection principals) {
		super.clearCache(principals);
	}

	private void clearAllCachedAuthorizationInfo() {
		getAuthorizationCache().clear();
	}

	private void clearAllCachedAuthenticationInfo() {
		getAuthenticationCache().clear();
	}

	public void clearAllCache() {
		clearAllCachedAuthenticationInfo();
		clearAllCachedAuthorizationInfo();
	}

}
