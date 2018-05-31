package cn.gov.zunyi.video.web.controller;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import cn.gov.zunyi.video.model.Permission;
import cn.gov.zunyi.video.model.RolePermission;
import org.apache.commons.lang3.StringUtils;
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
import com.baomidou.mybatisplus.plugins.Page;

import cn.gov.zunyi.video.service.PermissionService;
import cn.gov.zunyi.video.service.RolePermissionService;

/**
 * 角色管理Controller
 */
@RestController
@RequestMapping(value = "/permission")
public class PermissionController extends BaseController {

	private static final Logger logger = LoggerFactory.getLogger(PermissionController.class);

	@Autowired
	private PermissionService permissionService;

	@Autowired
	private RolePermissionService rolePermissionService;

	// 获取的顶级权限列表
	@RequestMapping(value = "/getTopPermissionList", method = RequestMethod.GET)
	@RequiresPermissions("/permission/getPermissionList")
	public ResponseEntity<List<Permission>> getTopPermissionList(EntityWrapper<Permission> ew) {
		try {


			Wrapper<Permission> wrapper = new EntityWrapper<>();
			wrapper.eq("pid", 0);
			List<Permission> permissionList = this.permissionService.selectList(wrapper);
			return ResponseEntity.ok(this.permissionService.selectList(wrapper));
		} catch (Exception e) {
			logger.error("查询角色列表出错!", e);
		}
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
	}


	// 获取角色分页对象
	@RequestMapping(value = "/getPermissionList", method = RequestMethod.POST)
	@RequiresPermissions("/permission/getPermissionList")
	public ResponseEntity<Page<Permission>> getPermissionList(EntityWrapper<Permission> ew,String name,String math) {
		try {
			Page<Permission> page = this.getPage();
			ew.orderBy("id", false);
			if(StringUtils.isNotBlank(name)){
				ew.like("name", name.trim());
			}
			if(StringUtils.isNotBlank(math)&&StringUtils.isNotEmpty(math)){
				ew.like("url", math.trim());
			}


			return ResponseEntity.ok(this.permissionService.selectPage(page, ew));
		} catch (Exception e) {
			logger.error("查询角色列表出错!", e);
		}
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
	}

	// 添加
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	@RequiresPermissions("/permission/add")
	public ResponseEntity<Void> add(Permission permission) {
		try {
			boolean ret = false;

			if (!permission.getPid().equals(0)) {
				Permission parent = this.permissionService.selectById(permission.getPid());
				String parentName = parent.getName();
				permission.setName(parentName + "-" + permission.getName());
			}

			permission.setCreateTime(new Date(System.currentTimeMillis()));
			permission.setUpdateTime(permission.getCreateTime());
			ret = this.permissionService.insert(permission);
			if (!ret) {
				// 更新失败, 400
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
			}
			// 204
			return ResponseEntity.status(HttpStatus.CREATED).build();
		} catch (Exception e) {
			logger.error("删除角色错误!", e);
		}
		// 500
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
	}

	// 修改
	@RequestMapping(value = "/edit", method = RequestMethod.PUT)
	@RequiresPermissions("/permission/edit")
	public ResponseEntity<Void> edit(Permission permission) {
		try {
			boolean ret = false;
			if (permission.getId() != null) {
				if (!permission.getPid().equals(0)) {
					Permission parent = this.permissionService.selectById(permission.getPid());
					String parentName = parent.getName();
					permission.setName(parentName + "-" + permission.getName());
				}
				ret = this.permissionService.updateById(permission);
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
			logger.error("编辑角色错误!", e);
		}
		// 500
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
	}

	// 刪除
	@RequestMapping(value = "/delete", method = RequestMethod.DELETE)
	@RequiresPermissions("/permission/delete")
	public ResponseEntity<Void> delete(@RequestParam(value = "permissionId", required = true) String permissionId) {
		try {
			boolean ret = false;
			if (StringUtils.isNotBlank(permissionId)) {
				RolePermission rolePermission = new RolePermission();
				rolePermission.setPid(Integer.valueOf(permissionId));
				EntityWrapper<RolePermission> ew = new EntityWrapper<RolePermission>();
				ew.setEntity(rolePermission);
				List<RolePermission> roleRightList = this.rolePermissionService.selectList(ew);
				// 如果存在权限，先进行删除
				if (roleRightList.size() > 0) {
					for (RolePermission rp : roleRightList) {
						this.rolePermissionService.delete(new EntityWrapper<RolePermission>(rp));
					}
				}
				ret = this.permissionService.deleteById(Integer.valueOf(permissionId));
			} else {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
			}
			if (!ret) {
				// 删除失败, 500
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
			}
			// 204
			return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
		} catch (Exception e) {
			logger.error("删除角色错误!", e);
		}
		// 500
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
	}

	// 刪除
	@RequestMapping(value = "/batchDel", method = RequestMethod.DELETE)
	@RequiresPermissions("/permission/batchDel")
	public ResponseEntity<Void> batchDel(@RequestParam(value = "permissionIds", required = true) String permissionIds) {
		try {
			boolean ret = false;
			if (StringUtils.isNotBlank(permissionIds)) {
				EntityWrapper<RolePermission> ew = new EntityWrapper<RolePermission>();
				ew.in("pid", StringUtils.split(permissionIds, ","));
				List<RolePermission> roleRightList = this.rolePermissionService.selectList(ew);
				// 如果存在权限，先进行删除
				if (roleRightList.size() > 0) {
					for (RolePermission rp : roleRightList) {
						this.rolePermissionService.delete(new EntityWrapper<RolePermission>(rp));
					}
				}
				ret = this.permissionService.deleteBatchIds(Arrays.asList(StringUtils.split(permissionIds, ",")));
			} else {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
			}
			if (!ret) {
				// 删除失败, 500
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
			}
			// 204
			return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
		} catch (Exception e) {
			logger.error("删除角色错误!", e);
		}
		// 500
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
	}

}
