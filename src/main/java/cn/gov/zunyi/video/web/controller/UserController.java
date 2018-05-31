package cn.gov.zunyi.video.web.controller;

import cn.gov.zunyi.video.auth.SHA256PasswordEncryptionService;
import cn.gov.zunyi.video.auth.SecureRandomSaltService;
import cn.gov.zunyi.video.common.util.AreaUtil;
import cn.gov.zunyi.video.common.util.ExcelUtil;
import cn.gov.zunyi.video.common.util.NetWorkUtil;
import cn.gov.zunyi.video.conf.Constant;
import cn.gov.zunyi.video.model.Permission;
import cn.gov.zunyi.video.model.RolePermission;
import cn.gov.zunyi.video.model.User;
import cn.gov.zunyi.video.model.UserRole;
import cn.gov.zunyi.video.service.*;
import cn.gov.zunyi.video.service.*;
import cn.gov.zunyi.video.common.util.FastDFSUtils;
import cn.gov.zunyi.video.service.*;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import io.swagger.annotations.Api;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;

@Api("用户相关接口")
@RestController
@RequestMapping("/user")
@ConfigurationProperties
public class UserController extends BaseController {

	private static final Logger logger = LoggerFactory.getLogger(UserController.class);
	//短信发送端接口
	@Autowired
	private MsSmsAuthService msSmsAuthService;
	@Autowired
	private UserService userService;

	@Value("${IMAGE_BASE_URL}")
	private String IMAGE_BASE_URL;

	@Autowired
	private UserRoleService userRoleService;

	@Autowired
	private PermissionService permissionService;

	@Autowired
	private RolePermissionService rolePermissionService;



	@RequestMapping(value = "/getPermissionMenuTree", method = RequestMethod.GET)
	@RequiresPermissions("/user/getPermissionMenuTree")
	public ResponseEntity<List<Map<String, String>>> getPermissionMenu(EntityWrapper<UserRole> ew, Integer user_id) {
		try {
			User user = (User) SecurityUtils.getSubject().getPrincipal();

			ew.eq("uid", user.getId());
			UserRole userRole = this.userRoleService.selectOne(ew);
			Wrapper<Permission> permissionWrapper = new EntityWrapper<>();
			permissionWrapper.eq("url", "#");
			List<Permission> list = this.permissionService.selectList(permissionWrapper);
			Wrapper<RolePermission> wrapper = new EntityWrapper<>();
			wrapper.eq("rid", userRole.getRid()).setSqlSelect("pid");
			List<Object> roleRightsList = this.rolePermissionService.selectObjs(wrapper);
			List<Map<String, String>> rightsList = new ArrayList<>();
			for (Permission p : list) {
				Map<String, String> map = new HashMap<>();
				map.put("id", p.getId().toString());
				map.put("parentId", p.getPid().toString());
				map.put("name", p.getName());
				// 如果角色已有该权限，则默认选中
				if (roleRightsList.contains(p.getId())) {
					map.put("enabled", "1");
					rightsList.add(map);
				} else {
					map.put("enabled", "0");
				}

			}
			return ResponseEntity.status(HttpStatus.CREATED).body(rightsList);
		} catch (Exception e) {
			logger.error("获取角色权限失败", e.getMessage());
			e.printStackTrace();
		}
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
	}



	@RequestMapping(value = "/getPermissionTree", method = RequestMethod.GET)
	@RequiresPermissions("/user/getPermissionTree")
	public ResponseEntity<List<Map<String, String>>> getPermissionTree(EntityWrapper<UserRole> ew, Integer user_id) {
		try {
			User user = (User) SecurityUtils.getSubject().getPrincipal();
			ew.eq("uid", user.getId());
			UserRole userRole = this.userRoleService.selectOne(ew);

			Wrapper<Permission> permissionWrapper = new EntityWrapper<>();
			permissionWrapper.eq("url", "#");
			List<Permission> list = this.permissionService.selectList(permissionWrapper);
			Wrapper<RolePermission> wrapper = new EntityWrapper<>();
			wrapper.eq("rid", userRole.getRid()).setSqlSelect("pid");
			List<Object> roleRightsList = this.rolePermissionService.selectObjs(wrapper);
			List<Map<String, String>> rightsList = new ArrayList<>();
			for (Permission p : list) {
				Map<String, String> map = new HashMap<>();
				map.put("id", p.getId().toString());
				map.put("parentId", p.getPid().toString());
				map.put("name", p.getName());
				// 如果角色已有该权限，则默认选中
				if (roleRightsList.contains(p.getId())) {
					map.put("enabled", "1");
					rightsList.add(map);
				}
			}
			return ResponseEntity.status(HttpStatus.CREATED).body(rightsList);
		} catch (Exception e) {
			logger.error("获取角色权限失败", e.getMessage());
			e.printStackTrace();
		}
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
	}

	/**
	 * 获取单个用户信息
	 *
	 * @param Id
	 * @return
	 */
	@RequestMapping(value = "/get/{Id}", method = RequestMethod.GET)
	@RequiresPermissions("/user/get/")
	public ResponseEntity<User> get(@PathVariable(value = "Id", required = true) String Id) {
		try {
			User user = userService.selectById(Id);
			if (null == user) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
			}
			return ResponseEntity.ok(user);
		} catch (Exception e) {
			logger.error("查询用户错误！", e);
		}
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
	}

	//禁用或启动的状态
	@RequestMapping(value = "/queryUserPerms/{Id}", method = RequestMethod.GET)
	@RequiresPermissions("/user/queryUserPerms/")
	public ResponseEntity<Map<String, Integer>> queryUserPerms(@PathVariable(value = "Id", required = true) String Id) {
		Map<String, Integer> map = new LinkedHashMap<>();
		try {
			User user = userService.selectById(Id);
			if (null == user) {
				map.put("isLive", 0);
				map.put("enabled", 0);
				return ResponseEntity.ok(map);
			}
			map.put("enabled", user.getEnabled());
			return ResponseEntity.ok(map);
		} catch (Exception e) {
			logger.error("查询用户错误！", e);
		}
		map.put("isLive", 0);
		map.put("enabled", 0);
		return ResponseEntity.ok(map);
	}







	/**
	 * 查询用户列表
	 *
	 * @return
	 */
	@RequestMapping(value = "/getWXUserList", method = RequestMethod.GET)
	@RequiresPermissions("/user/getWXUserList")
	public ResponseEntity<Page<User>> getWXUserList(String type, String phone, String name, EntityWrapper<User> ew) {
		try {
			if (null != ew) {
				ew.orderBy("id", false);
				ew.eq("enabled", 1);
				ew.eq("user_type", 3);//获取微信用户
				//1是 注册用户  0是未注册用户
				if (!StringUtils.isNotBlank(type)||type.equals("")) {

				}else{
					if(type.trim().equals("1")){
						ew.isNotNull("mobile");//电话为空
					}else if(type.trim().equals("0")){
						ew.isNull("mobile");//电话为空
					}
				}
				if (StringUtils.isNotBlank(phone)) {
					ew.like("mobile", phone.trim());
				}
				if (StringUtils.isNotBlank(name)) {
					ew.like("realname", name.trim());
				}
				ew.eq("is_live", 1);

			} else {
				ew = new EntityWrapper<User>();
				ew.orderBy("id", false);
			}
			Page<User> page = getPage();
			return ResponseEntity.ok(userService.selectPage(page, ew));
		} catch (Exception e) {
			logger.error("查询用户列表出错!", e);
		}
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
	}




	/**
	 * 查询用户列表
	 *
	 * @param column
	 * @param mobile
	 * @return
	 */
	@RequestMapping(value = "/getUserList", method = RequestMethod.GET)
	@RequiresPermissions("/user/getUserList")
	public ResponseEntity<Page<User>> queryUserList( String column, String keyword, Integer isLive, String mobile,
													 String username, EntityWrapper<User> ew) {
		try {
			if (null != ew) {
				ew.orderBy("id", false);
				ew.eq("enabled", 1);
				ew.eq("user_type", 2);//获取管理人员的
				if (StringUtils.isNotBlank(keyword)) {
					if ("id".equals(column)) {
						ew.eq(column, keyword);
					} else {
						ew.like(column, keyword);
					}
				}
				if (isLive != null) {
					ew.eq("is_live", isLive);
				}
				if (StringUtils.isNotBlank(mobile)) {
					ew.like("mobile", mobile);
				}
				if (StringUtils.isNotBlank(username)) {
					ew.like("username", username);
				}
			} else {
				ew = new EntityWrapper<User>();
				ew.orderBy("id", false);
			}
			Page<User> page = getPage();
			return ResponseEntity.ok(userService.selectPage(page, ew));
		} catch (Exception e) {
			logger.error("查询用户列表出错!", e);
		}
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
	}


	// 员工的增加
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	@RequiresPermissions("/user/add")
	public ResponseEntity<Map<String, Object>> add(User user) {
		Map<String, Object> resultMap = new LinkedHashMap<String, Object>();
		try {

			Wrapper<User> ew = new EntityWrapper<>();
			if (StringUtils.isNoneBlank(user.getUsername())) {
				user.setUsername(user.getUsername().trim());

				ew.eq("username", user.getUsername());
				User one = this.userService.selectOne(ew);
				if (one != null) {
					resultMap.put("status", 400);
					resultMap.put("message", "登录名已存在！");
					return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resultMap);
				}
			}
			if (StringUtils.isNoneBlank(user.getMobile())) {
				ew.eq("mobile", user.getMobile());
				User one = this.userService.selectOne(ew);
				if (one != null) {
					resultMap.put("status", 400);
					resultMap.put("message", "手机号已存在！");
					return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resultMap);
				}
			}
			if (StringUtils.isNoneBlank(user.getEmail())) {
				ew.eq("email", user.getEmail());
				User one = this.userService.selectOne(ew);
				if (one != null) {
					resultMap.put("status", 400);
					resultMap.put("message", "邮箱已存在！");
					return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resultMap);
				}
			}
			if (StringUtils.isBlank(user.getPassword())) {
				user.setPassword("123456");
			}
			boolean ret = false;
			byte[] passwordSalt = SecureRandomSaltService.generateSalt();
			byte[] passwordHash = SHA256PasswordEncryptionService.createPasswordHash(user.getPassword(), passwordSalt);
			user.setPasswordSalt(passwordSalt);
			user.setPasswordHash(passwordHash);
			user.setCreateTime(new Date(System.currentTimeMillis()));
			user.setUpdateTime(user.getCreateTime());
			user.setSex(user.getSex());
			ret = this.userService.insert(user);

			UserRole entity = new UserRole();
			entity.setUid(user.getId());
			entity.setRid(user.getRoleId());
			entity.setCreateTime(new Date(System.currentTimeMillis()));
			entity.setUpdateTime(entity.getCreateTime());
			this.userRoleService.insert(entity);

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

	// 管理员编辑用户信息
	@RequestMapping(value = "/modify", method = RequestMethod.PUT)
	@RequiresPermissions("/user/modify")
	public ResponseEntity<Void> edit(User user) {
		try {
			boolean ret = false;
			if (user.getId() != null) {
				user.setUpdateTime(new Date(System.currentTimeMillis()));
				if(user.getRoleId()!=null){
					//更新
					ret = userService.updateById(user);
					Wrapper<UserRole> userRoleWa = new EntityWrapper<>();
					userRoleWa.eq("uid",user.getId());
					UserRole entity = new UserRole();
					entity.setRid(user.getRoleId());
					this.userRoleService.update(entity,userRoleWa);
				}
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

	/**
	 * 禁用
	 *
	 * @param userId
	 * @return
	 */
	@RequestMapping(value = "/disable", method = RequestMethod.POST)
	@RequiresPermissions("/user/disable")
	public ResponseEntity<Void> disableUser(String userId) {
		try {
			User entity = new User();
			entity.setId(Integer.valueOf(userId));
			entity.setIsLive(0);
			boolean ret = this.userService.updateById(entity);
			if (!ret) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
			}
			return ResponseEntity.status(HttpStatus.CREATED).build();
		} catch (Exception e) {
			logger.error("禁用用户出错!", e);
		}
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
	}
	/**
	 * 删除
	 *
	 * @param userId
	 * @return
	 */
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	@RequiresPermissions("/user/delete")
	public ResponseEntity<Void> deleteUser(String userId) {
		try {
			User entity = new User();
			entity.setId(Integer.valueOf(userId));
			entity.setEnabled(0);
			boolean ret = this.userService.updateById(entity);
			if (!ret) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
			}
			return ResponseEntity.status(HttpStatus.CREATED).build();
		} catch (Exception e) {
			logger.error("删除用户出错!", e);
		}
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
	}

	//启动
	@RequestMapping(value = "/enable", method = RequestMethod.POST)
	@RequiresPermissions("/user/enable")
	public ResponseEntity<Void> enableUser(String userId) {
		try {
			User entity = new User();
			entity.setId(Integer.valueOf(userId));
			entity.setIsLive(1);
			boolean ret = this.userService.updateById(entity);
			if (!ret) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
			}
			return ResponseEntity.status(HttpStatus.CREATED).build();
		} catch (Exception e) {
			logger.error("用户启动出错!", e);
		}
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
	}

	//批量禁用
	@RequestMapping(value = "/batchDel", method = RequestMethod.POST)
	@RequiresPermissions("/user/batchDel")
	public ResponseEntity<Void> batchDel(String userIds) {
		try {
			boolean flag = false;
			if (StringUtils.isNotBlank(userIds)) {
				String[] userIdArr = userIds.split(",");
				User entity = new User();
				entity.setEnabled(0);
				for (String userId : userIdArr) {
					entity.setId(Integer.valueOf(userId));
					flag = this.userService.updateById(entity);
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
			logger.error("用户禁用失败!", e);
		}
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST)
	@RequiresPermissions("/user/edit")
	public ResponseEntity<Map<String, Object>> editUser(@RequestParam(value = "mobile", required = true) String mobile,
														@RequestParam(value = "nickname", required = true) String nickname,
														@RequestParam(value = "uploadFile", required = false) MultipartFile uploadFile,
														HttpServletRequest request) {
		Map<String, Object> resultMap = new LinkedHashMap<String, Object>();
		try {
			if (Pattern.matches(Constant.mobile, mobile)) {
				Wrapper<User> wrapper = new EntityWrapper<User>();
				wrapper.eq("mobile", mobile);
				User selectOne = userService.selectOne(wrapper);
				if (selectOne == null) {
					resultMap.put("status", 404);
					resultMap.put("message", "该用户不存在");
					return ResponseEntity.status(HttpStatus.CREATED).body(resultMap);
				} else {
					String avatar = selectOne.getAvatar();
					String avaUrl = null;
					User entity = new User();
					if (StringUtils.isNotBlank(avatar) && uploadFile != null) {
						FastDFSUtils.deletePic(avatar);
					}
					if (uploadFile != null) {
						avaUrl = FastDFSUtils.uploadPic(uploadFile.getBytes(), uploadFile.getOriginalFilename(),
								uploadFile.getSize());
						entity.setAvatar(IMAGE_BASE_URL + avaUrl);
					}
					entity.setUsername(nickname);
					entity.setUpdateTime(new Date(System.currentTimeMillis()));
					boolean ret = this.userService.update(entity, wrapper);
					if (!ret) {
						resultMap.put("status", 500);
						resultMap.put("message", "服务器忙");
						return ResponseEntity.status(HttpStatus.CREATED).body(resultMap);
					}
					request.getSession().removeAttribute("mobiCode");
					resultMap.put("status", 201);
					resultMap.put("message", "更新成功");
					// 204
					return ResponseEntity.status(HttpStatus.CREATED).body(resultMap);
				}
			} else {
				resultMap.put("status", 400);
				resultMap.put("message", "手机号格式有误");
				return ResponseEntity.status(HttpStatus.CREATED).body(resultMap);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		resultMap.put("status", 500);
		resultMap.put("message", "服务器忙");
		return ResponseEntity.status(HttpStatus.CREATED).body(resultMap);
	}

	@RequestMapping(value = "/editUser", method = RequestMethod.POST)
	@RequiresPermissions("/user/editUser")
	public ResponseEntity<Map<String, Object>> edit(@RequestParam(value = "mobile", required = true) String mobile,
													@RequestParam(value = "nickname", required = false) String nickname,
													@RequestParam(value = "isAvatar", required = false, defaultValue = "1") Integer isAvatar, // 0修改头像1不修改头像
													@RequestParam(value = "sex", required = false) Integer sex, HttpServletRequest request) {
		Map<String, Object> resultMap = new LinkedHashMap<String, Object>();
		try {
			if (Pattern.matches(Constant.mobile, mobile)) {
				Wrapper<User> wrapper = new EntityWrapper<User>();
				wrapper.eq("mobile", mobile);
				User selectOne = userService.selectOne(wrapper);
				if (selectOne == null) {
					resultMap.put("status", 404);
					resultMap.put("message", "该用户不存在");
					return ResponseEntity.status(HttpStatus.CREATED).body(resultMap);
				} else {
					String avatar = selectOne.getAvatar();
					User entity = new User();
					if (isAvatar == 0) {
						MultipartHttpServletRequest mRequest = (MultipartHttpServletRequest) request;
						List<MultipartFile> files = mRequest.getFiles("file");

						if (StringUtils.isNotBlank(avatar) && files.size() > 0) {
							FastDFSUtils.deletePic(avatar);
						}

						for (int i = 0; i < files.size(); i++) {
							MultipartFile file = files.get(i);
							String cover = FastDFSUtils.uploadPic(file.getBytes(), file.getOriginalFilename(),
									file.getSize());
							if (i == 0) {
								entity.setAvatar(IMAGE_BASE_URL + cover);
							}
						}
					}
					if (StringUtils.isNoneBlank(nickname)) {
						entity.setUsername(nickname);
					}
					entity.setSex(sex);
					entity.setUpdateTime(new Date(System.currentTimeMillis()));
					boolean ret = this.userService.update(entity, wrapper);
					if (!ret) {
						resultMap.put("status", 500);
						resultMap.put("message", "服务器忙");
						return ResponseEntity.status(HttpStatus.CREATED).body(resultMap);
					}
					request.getSession().removeAttribute("mobiCode");
					resultMap.put("status", 201);
					resultMap.put("avatar", entity.getAvatar());
					resultMap.put("message", "更新成功");
					// 204
					return ResponseEntity.status(HttpStatus.CREATED).body(resultMap);
				}
			} else {
				resultMap.put("status", 400);
				resultMap.put("message", "手机号格式有误");
				return ResponseEntity.status(HttpStatus.CREATED).body(resultMap);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		resultMap.put("status", 500);
		resultMap.put("message", "服务器忙");
		return ResponseEntity.status(HttpStatus.CREATED).body(resultMap);
	}

	/*  根据旧密码修改用户密码*/
	@RequestMapping(value = "/updatePass", method = RequestMethod.POST)
	public ResponseEntity<Map<String, Object>> updatePass(@RequestParam(value = "mobile", required = true) String mobile,
														  @RequestParam(value = "oldPassWord", required = true) String oldPassWord,
														  @RequestParam(value = "password", required = true) String password,
														  @RequestParam(value = "id", required = true) String id,
														  HttpServletRequest request) {
		Map<String, Object> resultMap = new LinkedHashMap<String, Object>();
		try {

			User user = (User) SecurityUtils.getSubject().getPrincipal();
			if (user == null) {
				// 500 未登录
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
						.build();
			}else if(user.getUserType()!=2&&!user.getId().equals(Integer.valueOf(id))){
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
			}


			Wrapper<User> wrapper = new EntityWrapper<User>();
			wrapper.eq("mobile", mobile);
			wrapper.eq("id", id);
			User selectOne = userService.selectOne(wrapper);




			if (selectOne == null) {
				resultMap.put("status", 400);
				resultMap.put("message", "该用户不存在");
				return ResponseEntity.status(HttpStatus.CREATED).body(resultMap);
			}
			byte[] salt = selectOne.getPasswordSalt();
			if (new String(SHA256PasswordEncryptionService.createPasswordHash(oldPassWord, salt))
					.equals(new String(selectOne.getPasswordHash()))) {

				Wrapper<User> ew = new EntityWrapper<User>();
				ew.eq("mobile", mobile);
				ew.eq("id", id);
				User entity = new User();
				byte[] passwordSalt = selectOne.getPasswordSalt();
				entity.setPasswordHash(SHA256PasswordEncryptionService.createPasswordHash(password, passwordSalt));
				entity.setUpdateTime(new Date(System.currentTimeMillis()));
				boolean ret = this.userService.update(entity, ew);
				if (!ret) {
					resultMap.put("status", 500);
					resultMap.put("message", "服务器忙");
					return ResponseEntity.status(HttpStatus.CREATED).body(resultMap);
				}
				request.getSession().removeAttribute("mobiCode");
				resultMap.put("status", 201);
				resultMap.put("message", "修改成功");
				// 204
				return ResponseEntity.status(HttpStatus.CREATED).body(resultMap);
			}else {
				resultMap.put("status", 500);
				resultMap.put("message", "旧密码错误！");
				return ResponseEntity.status(HttpStatus.CREATED).body(resultMap);
			}


		} catch (Exception e) {
			e.printStackTrace();
		}
		resultMap.put("status", 500);
		resultMap.put("message", "服务器忙");
		return ResponseEntity.status(HttpStatus.CREATED).body(resultMap);
	}




	/*  根据验证码修改用户密码*/
	@RequestMapping(value = "/findpwd", method = RequestMethod.POST)
	public ResponseEntity<Map<String, Object>> findpwd(@RequestParam(value = "mobile", required = true) String mobile,
													   @RequestParam(value = "password", required = true) String password,
													   @RequestParam(value = "verifyCode", required = false) String verifyCode, HttpServletRequest request) {
		Map<String, Object> resultMap = new LinkedHashMap<String, Object>();
		try {
			if (Pattern.matches(Constant.mobile, mobile)) {
				Wrapper<User> wrapper = new EntityWrapper<User>();
				wrapper.eq("mobile", mobile);
				User selectOne = userService.selectOne(wrapper);
				if (selectOne == null) {
					resultMap.put("status", 400);
					resultMap.put("message", "该用户不存在");
					return ResponseEntity.status(HttpStatus.CREATED).body(resultMap);
				}

				String mobiCode =this.msSmsAuthService.getSendCode(mobile.trim(),MsSmsAuthService.UPDATE_PASS);
				if (null == verifyCode || null == mobiCode) {
					// 参数有误, 400
					resultMap.put("status", 400);
					resultMap.put("message", "短信验证码不能为空");
					return ResponseEntity.status(HttpStatus.CREATED).body(resultMap);
				}

				if (!mobiCode.equals(verifyCode)) {
					// 参数有误, 400
					resultMap.put("status", 400);
					resultMap.put("message", "短信验证码不正确");
					return ResponseEntity.status(HttpStatus.CREATED).body(resultMap);
				} else {
					Wrapper<User> ew = new EntityWrapper<User>();
					ew.eq("mobile", mobile);
					User entity = new User();
					byte[] passwordSalt = selectOne.getPasswordSalt();
					entity.setPasswordHash(SHA256PasswordEncryptionService.createPasswordHash(password, passwordSalt));
					entity.setUpdateTime(new Date(System.currentTimeMillis()));
					boolean ret = this.userService.update(entity, ew);
					if (!ret) {
						resultMap.put("status", 500);
						resultMap.put("message", "服务器忙");
						return ResponseEntity.status(HttpStatus.CREATED).body(resultMap);
					}
					request.getSession().removeAttribute("mobiCode");
					resultMap.put("status", 201);
					resultMap.put("message", "修改成功");
					// 204
					return ResponseEntity.status(HttpStatus.CREATED).body(resultMap);
				}
			} else {
				resultMap.put("status", 400);
				resultMap.put("message", "手机号格式有误");
				return ResponseEntity.status(HttpStatus.CREATED).body(resultMap);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		resultMap.put("status", 500);
		resultMap.put("message", "服务器忙");
		return ResponseEntity.status(HttpStatus.CREATED).body(resultMap);
	}





	/**
	 * ajax登录请求
	 *
	 * @param  mobile
	 * @param password
	 * @return
	 */
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public ResponseEntity<Map<String, Object>> submitLogin(
			@RequestParam(value = "mobile", required = true) String mobile,
			@RequestParam(value = "password", required = true) String password) {
		Map<String, Object> resultMap = new LinkedHashMap<String, Object>();
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("mobile", mobile);
			User user = null;
			// 从数据库获取对应用户名密码的用户
			List<User> userList = userService.selectByMap(map);
			if (userList.size() != 0 && userList.size() == 1) {
				user = userList.get(0);
				if (user.getIsLive().equals(0)) {
					resultMap.put("status", 400);
					resultMap.put("message", "您的账号已被禁用");
					return ResponseEntity.status(HttpStatus.CREATED).body(resultMap);
				}
				byte[] salt = user.getPasswordSalt();
				if (!new String(SHA256PasswordEncryptionService.createPasswordHash(password, salt))
						.equals(new String(user.getPasswordHash()))) {
					resultMap.put("status", 400);
					resultMap.put("message", "帐号或密码不正确");
					return ResponseEntity.status(HttpStatus.CREATED).body(resultMap);
				}
			}
			if (userList.size() != 0 && userList.size() != 1) {
				resultMap.put("status", 400);
				resultMap.put("message", "帐号或密码不正确");
				return ResponseEntity.status(HttpStatus.CREATED).body(resultMap);
			}
			if (null == user) {
				resultMap.put("status", 404);
				resultMap.put("message", "账号不存在");
				return ResponseEntity.status(HttpStatus.CREATED).body(resultMap);
			}

			User entity = new User();
			entity.setId(user.getId());
			entity.setLastLoginIp(NetWorkUtil.getLoggableAddress(request));
			entity.setLastLoginTime(new Date(System.currentTimeMillis()));
			this.userService.updateById(entity);

			resultMap.put("status", 201);
			resultMap.put("userId", userList.get(0).getId());
			resultMap.put("username", userList.get(0).getMobile());
			resultMap.put("nickname", userList.get(0).getUsername());
			resultMap.put("avatar", userList.get(0).getAvatar());
			resultMap.put("sex", userList.get(0).getSex());
			resultMap.put("message", "登录成功");
			return ResponseEntity.status(HttpStatus.CREATED).body(resultMap);
		} catch (Exception e) {
			logger.error("登录出错！", e);
			resultMap.put("status", 500);
			resultMap.put("message", "服务器繁忙");
		}
		return ResponseEntity.status(HttpStatus.CREATED).body(resultMap);
	}

	/**
	 * 注册
	 *
	 * @param user
	 * @param verifyCode
	 * @param weixinUnid
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/regist", method = RequestMethod.POST)
	public ResponseEntity<Map<String, Object>> regist(User user,
													  @RequestParam(value = "verifyCode", required = true) String verifyCode,
													  @RequestParam(value = "weixinUnid", required = false) String weixinUnid, HttpServletRequest request) {
		Map<String, Object> resultMap = new LinkedHashMap<String, Object>();
		String mobile = user.getMobile();
		User userEntity = new User();
		if (Pattern.matches(Constant.mobile, mobile)) {
			String mobiCode =this.msSmsAuthService.getSendCode(mobile,MsSmsAuthService.WX_REGISTER);
			if (null == verifyCode) {
				// 参数错误
				resultMap.put("status", 400);
				resultMap.put("message", "短信验证码不能为空");
				// 参数有误, 400
				return ResponseEntity.status(HttpStatus.CREATED).body(resultMap);
			}
			System.out.println("mobiCode                    " + mobiCode);
			if (!mobiCode.equals(verifyCode)) {
				resultMap.put("status", 400);
				resultMap.put("message", "短信验证码错误");
				return ResponseEntity.status(HttpStatus.CREATED).body(resultMap);
			}

			Wrapper<User> wrapper = new EntityWrapper<User>();
			wrapper.eq("mobile", mobile);
			User selectOne = userService.selectOne(wrapper);
			if (selectOne != null) {
				resultMap.put("status", 400);
				resultMap.put("message", "该用户已注册");
				return ResponseEntity.status(HttpStatus.CREATED).body(resultMap);
			} else {
				request.getSession().removeAttribute("mobiCode");

				byte[] passwordSalt = SecureRandomSaltService.generateSalt();
				byte[] passwordHash = SHA256PasswordEncryptionService.createPasswordHash(user.getPassword(),
						passwordSalt);

				userEntity.setUsername(mobile.substring(0, 3) + "****" + mobile.substring(7, 11));
				userEntity.setMobile(mobile);
				userEntity.setPasswordSalt(passwordSalt);
				userEntity.setPasswordHash(passwordHash);
				userEntity.setCreateTime(new Date(System.currentTimeMillis()));
				userEntity.setUpdateTime(userEntity.getCreateTime());
				userEntity.setEnabled(1);
				userEntity.setRegistTime(userEntity.getCreateTime());
				userEntity.setRegistIp(NetWorkUtil.getLoggableAddress(request));
				userEntity.setLastLoginIp(userEntity.getRegistIp());
				userEntity.setLastLoginTime(userEntity.getCreateTime());
				userEntity.setTotalLoginCounts(1);
				//微信用户 by Cp  暂时不给权限
				userEntity.setUserType(3);
				boolean ret = userService.insert(userEntity);
				if (!ret) {
					resultMap.put("status", 500);
					resultMap.put("message", "服务器繁忙");
					// 保存失败
					return ResponseEntity.status(HttpStatus.CREATED).body(resultMap);
				}
				resultMap.put("status", 201);
				resultMap.put("message", "注册成功");
				return ResponseEntity.status(HttpStatus.CREATED).body(resultMap);
			}
		} else {
			// 参数有误, 400
			resultMap.put("status", 400);
			resultMap.put("message", "手机号格式不正确");
			return ResponseEntity.status(HttpStatus.CREATED).body(resultMap);
		}
	}


	/**
	 * 俱乐部后台管理导出
	 */
	@RequestMapping(value = "/allExport", method = RequestMethod.GET)
	@RequiresPermissions("/user/allExport")
	public ResponseEntity<String> allExport(HttpServletResponse response,
		@RequestParam(value="ids", required=false) String ids,String userType,String clubName){
		User user = (User) SecurityUtils.getSubject().getPrincipal();
		if(user.getUserType()==2){   //管理员     
			String fileName = "俱乐部基本信息"+".xls"; //文件名
			String sheetName = "俱乐部基本信息";//sheet名

		String []title = new String[]{"类型","账号","地域","管理员姓名","职务","邮箱","手机号","说明","用户状态","创建时间"};//标题
		List<User> list = this.userService.getAllFeedbackForExcel(ids,userType,clubName);//内容list

			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				 String [][]values = new String[list.size()][];
				 for(int i=0;i<list.size();i++){
					 values[i] = new String[title.length];
					 //将对象内容转换成string
					 User obj = list.get(i);
					 if(obj.getUserType()==1){
						 values[i][0] ="个人";
					 } else if(obj.getUserType()==0){
						 values[i][0] ="俱乐部";
					 }
					 values[i][1] = obj.getUsername();
					 values[i][2]=AreaUtil.getAreaByKey(obj.getProvince());
					 values[i][3] = obj.getManagerName();
					 values[i][4] = obj.getPost();
					 values[i][5] = obj.getEmail();
					 values[i][6] = obj.getMobile();
					 values[i][7] = obj.getIntroduction();
					 values[i][8] = obj.getIsLive()==1?"启用":"停用";
					 values[i][9] = sdf.format(obj.getCreateTime());
					 
				 }
				 HSSFWorkbook wb = ExcelUtil.getHSSFWorkbook(sheetName, title, values, null);

				//将文件存到指定位置
					try {
						ExcelUtil.setResponseHeader(response, fileName);
						OutputStream os = response.getOutputStream();
						wb.write(os);
						os.flush();
						os.close();
					} catch (Exception e) {
						e.printStackTrace();
					}
					return ResponseEntity.ok("导出成功");
				}
				return null;
			}

	/**
	 * 用户管理导出
	 */
	@RequestMapping(value = "/userExport", method = RequestMethod.GET)
	@RequiresPermissions("/user/userExport")
	public ResponseEntity<String> userExport(HttpServletResponse response,
		@RequestParam(value="ids", required=false) String ids,String userType,String mobile,String username){
		User user = (User) SecurityUtils.getSubject().getPrincipal();
		if(user.getUserType()==2){   //管理员  俱乐部类型  用户   
			String fileName = "用户管理"+".xls"; //文件名
			String sheetName = "用户管理";//sheet名

		String []title = new String[]{"用户头像","昵称","手机号","注册时间","最后访问时间","用户类型"};//标题
		List<User> list = this.userService.getUserFeedbackForExcel(ids,userType,mobile,username);//内容list

			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

			String [][]values = new String[list.size()][];
			for(int i=0;i<list.size();i++){
				values[i] = new String[title.length];
				//将对象内容转换成string
				User obj = list.get(i);
				values[i][0] = obj.getLogo();
				values[i][1] = obj.getRealname();
				values[i][2] = obj.getMobile();
				values[i][3] = sdf.format(obj.getCreateTime());
				values[i][4] = obj.getLastLoginTime()==null?"":sdf.format(obj.getLastLoginTime());
				values[i][5] = obj.getMobile()==null?"注册用户":"微信用户";

			}

			HSSFWorkbook wb = ExcelUtil.getHSSFWorkbook(sheetName, title, values, null);

			//将文件存到指定位置
			try {
				ExcelUtil.setResponseHeader(response, fileName);
				OutputStream os = response.getOutputStream();
				wb.write(os);
				os.flush();
				os.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			return ResponseEntity.ok("导出成功");
		}
		return null;
	}


}

