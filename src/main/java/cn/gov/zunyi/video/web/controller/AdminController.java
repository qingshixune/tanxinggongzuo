package cn.gov.zunyi.video.web.controller;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import cn.gov.zunyi.video.auth.SHA256PasswordEncryptionService;
import cn.gov.zunyi.video.auth.SecureRandomSaltService;
import cn.gov.zunyi.video.model.Role;
import cn.gov.zunyi.video.model.User;
import cn.gov.zunyi.video.model.UserRole;
import cn.gov.zunyi.video.service.UserRoleService;
import cn.gov.zunyi.video.service.UserService;
import com.baomidou.mybatisplus.plugins.Page;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;

import cn.gov.zunyi.video.service.RoleService;

@RestController
@RequestMapping("/admin")
public class AdminController extends BaseController {

	private static final Logger logger = LoggerFactory.getLogger(AdminController.class);

	@Autowired
	private UserService adminService;

	@Autowired
	private RoleService roleService;

	@Autowired
	private UserRoleService adminRoleService;

	/**
	 * 查询管理员列表
	 *
	 * @return
	 */
	@RequestMapping(value = "/getAdminList", method = RequestMethod.GET)
	@RequiresPermissions("/admin/getAdminList")
	public ResponseEntity<Page<User>> queryAdminList(EntityWrapper<User> ew) {
		try {
//			账号 手机号
			ew.orderBy("id", false);
			ew.eq("user_type",2);//只查询后台用户的
			Page<User> page = getPage();
			Page<User> pageList = this.adminService.selectPage(page, ew);
			List<User> records = pageList.getRecords();
			Role role;
			for (User admin : records) {
				role = this.roleService.selectById(admin.getRoleId());
				admin.setRole(role);
			}
			return ResponseEntity.ok(pageList);
		} catch (Exception e) {
			logger.error("查询管理员列表出错!", e);
		}
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
	}

	// 添加
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	@RequiresPermissions("/admin/add")
	public ResponseEntity<Map<String, Object>> add(User admin) {
		Map<String, Object> resultMap = new LinkedHashMap<String, Object>();
		try {
			Wrapper<User> ew = new EntityWrapper<>();
			if (StringUtils.isNoneBlank(admin.getUsername())) {
				ew.eq("username", admin.getUsername());
				User one = this.adminService.selectOne(ew);
				if (one != null) {
					resultMap.put("status", 400);
					resultMap.put("message", "登录名已存在！");
					return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resultMap);
				}
			}
			if (StringUtils.isNoneBlank(admin.getMobile())) {
				ew.eq("mobile", admin.getMobile());
				User one = this.adminService.selectOne(ew);
				if (one != null) {
					resultMap.put("status", 400);
					resultMap.put("message", "手机号已存在！");
					return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resultMap);
				}
			}
			if (StringUtils.isNoneBlank(admin.getEmail())) {
				ew.eq("email", admin.getEmail());
				User one = this.adminService.selectOne(ew);
				if (one != null) {
					resultMap.put("status", 400);
					resultMap.put("message", "邮箱已存在！");
					return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resultMap);
				}
			}
			if (StringUtils.isBlank(admin.getPassword())) {
				admin.setPassword("123456");
			}
			boolean ret = false;
			byte[] passwordSalt = SecureRandomSaltService.generateSalt();
			byte[] passwordHash = SHA256PasswordEncryptionService.createPasswordHash(admin.getPassword(), passwordSalt);
			admin.setPasswordSalt(passwordSalt);
			admin.setPasswordHash(passwordHash);
			admin.setCreateTime(new Date(System.currentTimeMillis()));
			admin.setUpdateTime(admin.getCreateTime());
			ret = this.adminService.insert(admin);

			UserRole entity = new UserRole();
			entity.setUid(admin.getId());
			entity.setRid(admin.getRoleId());
			entity.setCreateTime(new Date(System.currentTimeMillis()));
			entity.setUpdateTime(entity.getCreateTime());
			this.adminRoleService.insert(entity);

			if (!ret) {
				// 更新失败, 500
				resultMap.put("status", 500);
				resultMap.put("message", "服务器忙");
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(resultMap);
			}
			resultMap.put("status", 201);
			resultMap.put("message", "添加成功");
			// 201
			return ResponseEntity.status(HttpStatus.CREATED).body(resultMap);
		} catch (Exception e) {
			resultMap.put("status", 500);
			resultMap.put("message", "服务器忙");
			logger.error("更新管理员错误!", e);
		}
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resultMap);
	}

	// 修改
	@RequestMapping(value = "/edit", method = RequestMethod.PUT)
	@RequiresPermissions("/admin/edit")
	public ResponseEntity<Void> edit(User admin) {
		try {
			boolean ret = false;
			if (null != admin.getId()) {
				if (StringUtils.isNoneBlank(admin.getPassword())) {
					byte[] salt = SecureRandomSaltService.generateSalt();
					admin.setPasswordSalt(salt);
					admin.setPasswordHash(
							SHA256PasswordEncryptionService.createPasswordHash(admin.getPassword(), salt));
				}
				admin.setUpdateTime(new Date(System.currentTimeMillis()));
				ret = this.adminService.updateById(admin);

				UserRole entity = new UserRole();
				entity.setRid(admin.getRoleId());
				Wrapper<UserRole> wrapper = new EntityWrapper<>();
				wrapper.eq("uid", admin.getId());
				this.adminRoleService.update(entity, wrapper);
			} else {
				// 更新失败, 400
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
			}
			if (!ret) {
				// 更新失败, 500
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
			}
			// 204
			return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
		} catch (Exception e) {
			logger.error("更新管理员错误!", e);
		}
		// 500
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
	}

	@RequestMapping(value = "/editPwd", method = RequestMethod.PUT)
	@RequiresPermissions("/admin/editPwd")
	public ResponseEntity<Void> editPwd(@RequestParam(value = "oldPassWord") String oldPassWord,
			@RequestParam(value = "password") String password) {
		try {
			boolean ret;
			User entity = (User) SecurityUtils.getSubject().getPrincipal();
			User admin = this.adminService.selectById(entity.getId());
			byte[] salt = admin.getPasswordSalt();
			if (new String(SHA256PasswordEncryptionService.createPasswordHash(oldPassWord, salt))
					.equals(new String(admin.getPasswordHash()))) {
				salt = SecureRandomSaltService.generateSalt();
				admin.setPasswordSalt(salt);
				admin.setPasswordHash((SHA256PasswordEncryptionService.createPasswordHash(password, salt)));
				admin.setUpdateTime(new Date(System.currentTimeMillis()));
				ret = this.adminService.updateById(admin);
			} else {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
			}
			if (!ret) {
				// 更新失败, 500
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
			}
			// 204
			return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
		} catch (Exception e) {
			logger.error("更新密码错误！", e);
		}
		// 500
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
	}

	@RequestMapping(value = "/editInfo", method = RequestMethod.PUT)
	@RequiresPermissions("/admin/editInfo")
	public ResponseEntity<Void> editInfo(@RequestParam(value = "realname") String realname,
			@RequestParam(value = "password", required = true) String password) {
		try {
			boolean ret = false;
			if (StringUtils.isNoneBlank(realname)) {
				User admin = this.adminService.selectById(((User) SecurityUtils.getSubject().getPrincipal()).getId());
				byte[] salt = admin.getPasswordSalt();
				if (new String(SHA256PasswordEncryptionService.createPasswordHash(password, salt))
						.equals(new String(admin.getPasswordHash()))) {
					User entity = new User();
					entity.setId(admin.getId());
					entity.setRealname(realname);
					ret = this.adminService.updateById(entity);
				}
			} else {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
			}
			if (!ret) {
				// 更新失败, 500
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
			}
			// 204
			return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
		} catch (Exception e) {
			logger.error("更新密码错误！", e);
		}
		// 500
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
	}

	/**
	 * 删除管理员
	 * 
	 * @param admin
	 * @return
	 */
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	@RequiresPermissions("/admin/delete")
	public ResponseEntity<Void> deleteAdmin(User admin) {
		try {
			admin.setEnabled(false);
			boolean ret = this.adminService.updateById(admin);
			if (!ret) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
			}
			return ResponseEntity.status(HttpStatus.CREATED).build();
		} catch (Exception e) {
			logger.error("删除管理员出错!", e);
		}
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
	}

	@RequestMapping(value = "/enable", method = RequestMethod.POST)
	@RequiresPermissions("/admin/enable")
	public ResponseEntity<Void> enableAdmin(String adminId) {
		try {
			User entity = new User();
			entity.setId(Integer.valueOf(adminId));
			entity.setEnabled(true);
			boolean ret = this.adminService.updateById(entity);
			if (!ret) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
			}
			return ResponseEntity.status(HttpStatus.CREATED).build();
		} catch (Exception e) {
			logger.error("删除管理员出错!", e);
		}
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
	}

	@RequestMapping(value = "/batchDel", method = RequestMethod.POST)
	@RequiresPermissions("/admin/batchDel")
	public ResponseEntity<Void> batchDel(String adminIds) {
		try {
			boolean flag = false;
			if (StringUtils.isNotBlank(adminIds)) {
				String[] adminIdArr = adminIds.split(",");
				User entity = new User();
				entity.setEnabled(false);
				for (String adminId : adminIdArr) {
					entity.setId(Integer.valueOf(adminId));
					flag = this.adminService.updateById(entity);
					if (!flag) {
						break;
					}
				}
			} else {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
			}
			if (!flag) {
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
			}
			return ResponseEntity.status(HttpStatus.CREATED).build();
		} catch (Exception e) {
			logger.error("删除管理员失败!", e);
		}
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
	}

}
