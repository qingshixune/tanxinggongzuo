package cn.gov.zunyi.video.web.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.gov.zunyi.video.model.Permission;
import cn.gov.zunyi.video.model.Role;
import cn.gov.zunyi.video.model.RolePermission;
import cn.gov.zunyi.video.model.User;
import cn.gov.zunyi.video.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.github.stuxuhai.jpinyin.PinyinHelper;

import cn.gov.zunyi.video.service.PermissionService;
import cn.gov.zunyi.video.service.RolePermissionService;
import cn.gov.zunyi.video.service.RoleService;

/**
 * 角色管理Controller
 */
@RequestMapping(value = "/role")
@RestController
public class RoleController extends BaseController {

    private static final Logger logger = LoggerFactory.getLogger(RoleController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private PermissionService permissionService;

    @Autowired
    private RolePermissionService rolePermissionService;


    // 获取角色分页对象
    @RequestMapping(value = "/getRoleList", method = RequestMethod.GET)
    @RequiresPermissions("/role/getRoleList")
    public ResponseEntity<Page<Role>> getRoleList(String name, EntityWrapper<Role> ew) {
        try {
            if (StringUtils.isNotBlank(name)) {
                ew.like("name",name);
            }
            ew.orderBy("id",false);
            ew.eq("status",1);
            Page<Role> page = this.getPage();
            return ResponseEntity.ok(this.roleService.selectPage(page, ew));
        } catch (Exception e) {
            logger.error("查询角色列表出错!", e);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }

    // 添加
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @RequiresPermissions("/role/add")
    public ResponseEntity<Void> add(Role role,
                                    @RequestParam(value = "permissionIds[]", required = false) String[] permissionIds
    ) {
        try {
            Wrapper<Role> ew = new EntityWrapper<>();
            if (StringUtils.isNoneBlank(role.getName())) {
                ew.eq("name", role.getName());
                role.setAlias(PinyinHelper.getShortPinyin(role.getName()));
            }
            ew.or();
            if (StringUtils.isNoneBlank(role.getAlias())) {
                ew.eq("alias", role.getAlias());
            }
            Role one = this.roleService.selectOne(ew);
            if (one != null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }

            boolean ret = false;
            role.setCreateTime(new Date(System.currentTimeMillis()));
            role.setUpdateTime(role.getCreateTime());
            ret = this.roleService.insert(role);

            if (permissionIds != null && permissionIds.length > 0) { //添加新分配的权限
                List<RolePermission> permissions = new ArrayList<RolePermission>();
                RolePermission e = null;
                for (String pid : permissionIds) {
                    e = new RolePermission();
                    e.setPid(Integer.valueOf(pid));
                    e.setRid(role.getId());
                    permissions.add(e);
                }
                ret = rolePermissionService.insertBatch(permissions);
            }

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

    @RequestMapping(value = "/edit", method = RequestMethod.PUT)
    @RequiresPermissions("/role/edit")
    public ResponseEntity<Void> edit(Role role) {
        try {
            Wrapper<Role> ew = new EntityWrapper<>();
            if (StringUtils.isNoneBlank(role.getName())) {
                ew.eq("name", role.getName().trim());
                role.setAlias(PinyinHelper.getShortPinyin(role.getName().trim()));
            }
            ew.or();
            if (StringUtils.isNoneBlank(role.getAlias())) {
                ew.eq("alias", role.getAlias().trim());
            }
//            Role one = this.roleService.selectOne(ew);
//            if (one != null) {
//                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
//            }
            role.setUpdateTime(new Date(System.currentTimeMillis()));
            boolean ret = this.roleService.updateById(role);
            if (!ret) {
                // 删除失败, 500
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }else{
                return ResponseEntity.ok().body(null);
            }
        } catch (Exception e) {
            logger.error("删除角色错误!", e);
        }
        // 500
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    // 刪除
    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    @RequiresPermissions("/role/delete")
    public ResponseEntity<String> delete(@RequestParam(value = "roleId", required = true) String roleId) {
        try {
            User entity = new User();
            entity.setRoleId(Integer.valueOf(roleId));
            EntityWrapper<User> ew = new EntityWrapper<User>();
            ew.setEntity(entity);
            int count = this.userService.selectCount(ew);
            boolean ret1;
            boolean ret2;
            if (count > 0) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("该角色下存在未删除的用户，请您删除用户后再删除角色！");
            } else {
                ret1 = this.roleService.deleteById( Integer.valueOf(roleId) );
                RolePermission rp = new RolePermission();
                rp.setRid(Integer.valueOf(roleId));
                ret2 = rolePermissionService.delete(new EntityWrapper<RolePermission>(rp));
            }
            if (!ret1 && !ret2) {
                // 删除失败, 500
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
            // 204
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (Exception e) {
            logger.error("删除角色错误!", e);

        }
        // 500
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("删除角色错误!");

    }

    @RequestMapping(value = "/rights/{roleId}", method = RequestMethod.GET)
    @RequiresPermissions("/role/rights/")
    public ResponseEntity<List<Map<String, String>>> rights(@PathVariable("roleId") Integer roleId) {
        try {
            List<Permission> list = this.permissionService.selectList(null);
            Wrapper<RolePermission> wrapper = new EntityWrapper<>();
            wrapper.eq("rid", roleId).setSqlSelect("pid");
            List<Object> roleRightsList = this.rolePermissionService.selectObjs(wrapper);
            List<Map<String, String>> rightsList = new ArrayList<>();
            for (Permission p : list) {
                Map<String, String> map = new HashMap<>();
                map.put("id", p.getId().toString());
                map.put("pId", p.getPid().toString());
                map.put("name", p.getName());
                // 默认展开树
                map.put("open", "true");
                // 如果角色已有该权限，则默认选中
                if (roleRightsList.contains(p.getId())) {
                    map.put("checked", "true");
                }
                rightsList.add(map);
            }
            return ResponseEntity.ok(rightsList);
        } catch (Exception e) {
            logger.error("获取角色权限失败", e.getMessage());
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }

    /**
     * 更新权限列表
     *
     * @return
     */
    @RequestMapping(value = "/updateRoleRights", method = RequestMethod.POST)
    @RequiresPermissions("/role/updateRoleRights")
    public ResponseEntity<Void> updateRoleRights(Role role,
                                                 @RequestParam(value = "rights", required = false) String rights) {
        try {
            // 查询出本角色已经分配了的权限
            RolePermission rolePermission = new RolePermission();
            rolePermission.setRid(role.getId());
            EntityWrapper<RolePermission> ew = new EntityWrapper<>();
            ew.setEntity(rolePermission);
            List<RolePermission> roleRightList = this.rolePermissionService.selectList(ew);
            boolean ret = false;
            // 如果存在权限，先进行删除
            if (roleRightList.size() > 0) {
                this.rolePermissionService.delete(ew);
            }

            String[] rightIds = StringUtils.split(rights, ",");
            if (StringUtils.isNotBlank(rights) && rightIds != null) {
                // 添加新分配的权限
                List<RolePermission> permissions = new ArrayList<RolePermission>();
                RolePermission e = null;
                for (String pid : rightIds) {
                    e = new RolePermission();
                    e.setPid(Integer.valueOf(pid));
                    e.setRid(role.getId());
                    permissions.add(e);
                }
                ret = rolePermissionService.insertBatch(permissions);
            } else {
                return ResponseEntity.status(HttpStatus.CREATED).build();
            }
            if (!ret) {
                // 更新失败, 500
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
            // 204
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (Exception e) {
            logger.error("删除角色错误!", e);
        }
        // 500
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

}
