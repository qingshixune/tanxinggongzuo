package cn.gov.zunyi.video.web.controller;

import cn.gov.zunyi.video.model.User;
import cn.gov.zunyi.video.model.UserRole;
import cn.gov.zunyi.video.service.UserRoleService;
import io.swagger.annotations.Api;

import java.util.*;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import cn.gov.zunyi.video.service.UserService;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;

@Api("俱乐部相关接口")
@RestController
@RequestMapping("/msclub")
@ConfigurationProperties
public class MsClubContraller extends BaseController {

    private static final Logger logger = LoggerFactory.getLogger(MsClubContraller.class);

    @Autowired
    private UserService clubService;


    @Autowired
    private UserRoleService userRoleService;

    //系统管理的俱乐部列表
    @RequestMapping(value = "/getClubList", method = RequestMethod.GET)
    @RequiresPermissions("/msclub/getClubList")
    public ResponseEntity<Page<User>> queryUserList(
//            isLive
            String userType,
            String clubName,   //俱乐部名称
            EntityWrapper<User> ew) {
        try {

            ew.orderBy("id", false);
//            if (StringUtils.isNotBlank(type)) {
//                ew.eq("TYPE", type);//类型
//            }

            if (StringUtils.isNotBlank(userType)) {
                ew.eq("user_type", userType);//类型
            }

            ew.eq("enabled", 1); //是否删除   未被删除
            if (StringUtils.isNotBlank(clubName)) {
                ew.like("CLUB_NAME", clubName);//俱乐部名称
            }
            List <Integer> ids=new ArrayList<>();
            ids.add(0);
            ids.add(1);
            ew.in("user_type", ids); //俱乐部人员

            Page<User> page = this.getPage();
            
            Page<User> selectPage = this.clubService.selectPage(page, ew);
            
            List<User> records = selectPage.getRecords();
            
            return ResponseEntity.ok(selectPage);
        } catch (Exception e) {
            logger.error("查询俱乐部列表出错!", e);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }

    // 俱乐部的增加和修改
    @RequestMapping(value = "/edit", method = RequestMethod.PUT)
    @RequiresPermissions("/msclub/edit")
    public    ResponseEntity<Map<String, Object>>  edit(User club) {
        Map<String, Object> resultMap = new LinkedHashMap<String, Object>();
        try {
            User optuUser = (User) SecurityUtils.getSubject().getPrincipal();
            //只有管理员账户才可以进行新增
            if(optuUser.getUserType()!=2){
                User clubSelect= this.clubService.selectById(club.getId());
                if(clubSelect==null||!optuUser.getId().equals(clubSelect.getId())){
                    resultMap.put("status", 400);
                    resultMap.put("message", "您暂无新增用户权限！");
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resultMap);
                }
            }
            if(club.getUserType()!=null&&club.getUserType()!=0&&club.getUserType()!=1){
                resultMap.put("status", 400);
                resultMap.put("message", "您暂无未设置用户类型，或您不能新增该类型的用户！");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resultMap);
            }

            boolean msclub = false;
            if (club.getId() != null) {
                msclub = this.clubService.updateById(club);
                if(club.getRoleId()!=null){
                    //更新 角色信息表
                    Wrapper<UserRole> userRoleWa = new EntityWrapper<>();
                    userRoleWa.eq("uid",club.getId());
                    UserRole entity = new UserRole();
                    entity.setRid(club.getRoleId());
                    this.userRoleService.update(entity,userRoleWa);
                }


            } else {
                Wrapper<User> ew = new EntityWrapper<>();
                if (StringUtils.isNoneBlank(club.getUsername())) {
                    ew.eq("username", club.getUsername());
                    User one = this.clubService.selectOne(ew);
                    if (one != null) {
                        resultMap.put("status", 400);
                        resultMap.put("message", "登录名已存在！");
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resultMap);
                    }
                }
                if (StringUtils.isNoneBlank(club.getMobile())) {
                     ew = new EntityWrapper<>();
                    ew.eq("mobile", club.getMobile());
                    User one = this.clubService.selectOne(ew);
                    if (one != null) {
                        resultMap.put("status", 400);
                        resultMap.put("message", "手机号已存在！");
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resultMap);
                    }
                }
                if (StringUtils.isNoneBlank(club.getEmail())) {
                    ew = new EntityWrapper<>();
                    ew.eq("email", club.getEmail());
                    User one = this.clubService.selectOne(ew);
                    if (one != null) {
                        resultMap.put("status", 400);
                        resultMap.put("message", "邮箱已存在！");
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resultMap);
                    }
                }
                if (StringUtils.isBlank(club.getPassword())) {
                    club.setPassword("123456");
                }


                club.setCreateTime(new Date(System.currentTimeMillis()));
                club.setEnabled(true);
                msclub = this.clubService.insert(club);

                if(club.getRoleId()!=null){
                    //更新
                    UserRole entity = new UserRole();
                    entity.setUid(club.getId());
                    entity.setRid(club.getRoleId());
                    entity.setCreateTime(new Date(System.currentTimeMillis()));
                    entity.setUpdateTime(entity.getCreateTime());
                    this.userRoleService.insert(entity);
                }


            }
            if(msclub){
                resultMap.put("status", 200);
                resultMap.put("message", "操作成功！");
                return ResponseEntity.ok(resultMap);

            }else{
                resultMap.put("status", 400);
                resultMap.put("message", "操作失败！");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(resultMap);
            }

        } catch (Exception e) {
            logger.error("增加或修改俱乐部出错!", e);
        }
        resultMap.put("status", 200);
        resultMap.put("message", "新增用户失败！");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(resultMap);
    }


    //删除系统管理的俱乐部
    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    @RequiresPermissions("/msclub/delete")
    public ResponseEntity<Integer> deleteUser(String clubId) {
        boolean ret = false;
        try {
            User optuUser = (User) SecurityUtils.getSubject().getPrincipal();

            //只有管理员账户才可以进行删除
            if(optuUser.getUserType()!=2){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(1);
            }

            Wrapper<User> ew = new EntityWrapper<>();
            if (!"".equals(clubId)) {
                ew.eq("id", Integer.parseInt(clubId));
                User club = this.clubService.selectOne(ew);
                if (null != club) {
                    club.setEnabled(false);
                    ret = this.clubService.updateById(club);
                }
            }
            return ResponseEntity.ok(ret == true ? 0 : 1);
        } catch (Exception e) {
            logger.error("删除俱乐部出错!", e);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(1);
    }

}
