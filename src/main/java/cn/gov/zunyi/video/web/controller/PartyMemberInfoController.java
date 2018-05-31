package cn.gov.zunyi.video.web.controller;

import cn.gov.zunyi.video.model.PartyMemberInfo;
import cn.gov.zunyi.video.service.PartyMemberInfoService;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Api("用户相关接口")
@RestController
@RequestMapping("/partymemberinfo")
@ConfigurationProperties
public class PartyMemberInfoController extends BaseController{

    @Autowired
    private PartyMemberInfoService partyMemberInfoService;

    /**
     * 增加党员
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    //@RequiresPermissions("/partymemberinfo/add")
    public ResponseEntity<Map<String, Object>> add(PartyMemberInfo partyMemberInfo) {

        Map<String, Object> resultMap = new LinkedHashMap<String, Object>();
        try {

            Wrapper<PartyMemberInfo> ew = new EntityWrapper<>();
            String name = partyMemberInfo.getName();
            if (StringUtils.isNoneBlank(name)) {
                partyMemberInfo.setName(name.trim());
//此处不需要验证
//                ew.eq("name", name);
//                PartyMemberInfo one = this.partyMemberInfoService.selectOne(ew);
//                if (one != null) {
//                    resultMap.put("status", 400);
//                    resultMap.put("message", name+"已存在！");
//                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resultMap);
//                }
            }
            boolean ret = false;
            partyMemberInfo.setCreateTime(new Date(System.currentTimeMillis()));
            ret = this.partyMemberInfoService.insert(partyMemberInfo);

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
            logger.error("添加党员错误!", e);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resultMap);
    }

    /**
     * 删除党员
     * @param partyMemberInfoId
     * @return
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
//    @RequiresPermissions("/partymemberinfo/delete")
    public ResponseEntity<Map<String, Object>> deletepartyMember(String partyMemberInfoId) {
        try {
            Map<String, Object> resultMap = new LinkedHashMap<String, Object>();
            PartyMemberInfo entity = new PartyMemberInfo();
            entity.setId(Integer.valueOf(partyMemberInfoId));
            entity.setEnabled(0);
            boolean ret = this.partyMemberInfoService.updateById(entity);
            if (!ret) {
                resultMap.put("status", 404);
                resultMap.put("message", "该党员不存在！");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resultMap);
            }
            resultMap.put("status",200);
            resultMap.put("message","删除成功");
            return ResponseEntity.status(HttpStatus.OK).body(resultMap);
        } catch (Exception e) {
            logger.error("删除党员出错!", e);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }

    /**
     * 查询党员列表
     *
     * @return
     */
    @ApiImplicitParams({
            @ApiImplicitParam(name = "_index", value = "分页起始偏移量", required = false, paramType = "query", dataType = "Integer"),
            @ApiImplicitParam(name = "_size", value = "返回条数", required = false, paramType = "query", dataType = "Integer")})
    @RequestMapping(value = "/getPartyMemberInfoList", method = RequestMethod.GET)
//    @RequiresPermissions("/partymemberinfo/getPartyMemberInfoList")
    public ResponseEntity<Page<PartyMemberInfo>> queryPartyMemberInfoList() {
        try {
            Page<PartyMemberInfo> page = getPage();
            page = partyMemberInfoService.getPartyBranchName(page);
            return ResponseEntity.ok(page);
        } catch (Exception e) {
            logger.error("查询党员列表出错!", e);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }

    @RequestMapping(value = "/getPmInfoByMemberId", method = RequestMethod.GET)
//    @RequiresPermissions("/partymemberinfo/getPmInfoByMemberId")
    public ResponseEntity<PartyMemberInfo> getPmInfoByMemberId(String memberId) {
        try {
            PartyMemberInfo partyMemberInfo = partyMemberInfoService.getPmInfoByMemberId(memberId);
            return ResponseEntity.ok(partyMemberInfo);
        } catch (Exception e) {
            logger.error("查询党员信息出错!", e);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }

    // 修改党员信息
    @RequestMapping(value = "/modify", method = RequestMethod.PUT)
//    @RequiresPermissions("/partymemberinfo/modify")
    public ResponseEntity<Void> edit(PartyMemberInfo partyMemberInfo) {
        try {
            boolean ret = false;
            if (partyMemberInfo.getId() != null) {
                partyMemberInfo.setUpdateTime(new Date(System.currentTimeMillis()));
                //更新
                ret = this.partyMemberInfoService.updateById(partyMemberInfo);
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
            logger.error("更新党员错误!", e);
        }
        // 500
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    /**
     *  查询党员男女人数以及比例
     *
     * @return
     */
    @RequestMapping(value = "/getPartyMemberSexCount", method = RequestMethod.GET)
//    @RequiresPermissions("/partymemberinfo/getPartyMemberSexCount")
    public ResponseEntity<List<PartyMemberInfo>> getPartyMemberSexCount() {
        try {
            List<PartyMemberInfo> sexCountList = partyMemberInfoService.getPartyMemberSexCount();
            return ResponseEntity.ok(sexCountList);
        } catch (Exception e) {
            logger.error(" 查询党员男女人数以及比例出错!", e);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }

    /**
     * 查询党员年龄分布
     *
     * @return
     */
    @RequestMapping(value = "/getPartyMemberAgeDistribution", method = RequestMethod.GET)
//    @RequiresPermissions("/partymemberinfo/getPartyMemberAgeDistribution")
    public ResponseEntity<List<PartyMemberInfo>> getPartyMemberAgeDistribution() {
        try {
            List<PartyMemberInfo> ageDistributionList = partyMemberInfoService.getPartyMemberAgeDistribution();
            return ResponseEntity.ok(ageDistributionList);
        } catch (Exception e) {
            logger.error("查询党员年龄分布出错!", e);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }

}
