package cn.gov.zunyi.video.web.controller;

import cn.gov.zunyi.video.model.PartyBranchInfo;
import cn.gov.zunyi.video.service.PartyBranchInfoService;
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
import java.util.Map;

@Api("用户相关接口")
@RestController
@RequestMapping("/partybranchinfo")
@ConfigurationProperties
public class PartyBranchInfoController extends BaseController{

    @Autowired
    private PartyBranchInfoService partyBranchInfoService;

    /**
     * 增加党支部
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    //@RequiresPermissions("/partybranchinfo/add")
    public ResponseEntity<Map<String, Object>> add(PartyBranchInfo partyBranchInfo) {

        Map<String, Object> resultMap = new LinkedHashMap<String, Object>();
        try {

            Wrapper<PartyBranchInfo> ew = new EntityWrapper<>();
            String partyBranchname = partyBranchInfo.getPartybranchname();
            if (StringUtils.isNoneBlank(partyBranchname)) {
                partyBranchInfo.setPartybranchname(partyBranchname.trim());

                ew.eq("partybranch_name", partyBranchname);
                PartyBranchInfo one = this.partyBranchInfoService.selectOne(ew);
                if (one != null) {
                    resultMap.put("status", 400);
                    resultMap.put("message", partyBranchname+"已存在！");
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resultMap);
                }
            }
            boolean ret = false;
            partyBranchInfo.setCreateTime(new Date(System.currentTimeMillis()));
            ret = this.partyBranchInfoService.insert(partyBranchInfo);

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
            logger.error("添加党支部错误!", e);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resultMap);
    }

    /**
     * 删除党支部
     * @param partyBranchInfoId
     * @return
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
//    @RequiresPermissions("/partybranchinfo/delete")
    public ResponseEntity<Map<String,Object>> deletepartyBranchInfo(String partyBranchInfoId) {
        Map<String,Object> resultMap = new LinkedHashMap<>();
        try {
            PartyBranchInfo entity = new PartyBranchInfo();
            entity.setId(Integer.valueOf(partyBranchInfoId));
            entity.setEnabled(0);
            boolean ret = this.partyBranchInfoService.updateById(entity);
            if (!ret) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }
            resultMap.put("status",200);
            resultMap.put("message","删除成功");
            return ResponseEntity.status(HttpStatus.OK).body(resultMap);
        } catch (Exception e) {
            logger.error("删除党支部出错!", e);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }

    /**
     * 查询党支部列表
     *
     * @return
     */
    @ApiImplicitParams({
            @ApiImplicitParam(name = "_index", value = "分页起始偏移量", required = false, paramType = "query", dataType = "Integer"),
            @ApiImplicitParam(name = "_size", value = "返回条数", required = false, paramType = "query", dataType = "Integer")})
    @RequestMapping(value = "/getPartyBranchInfoList", method = RequestMethod.GET)
//    @RequiresPermissions("/partybranchinfo/getPartyBranchInfoList")
    public ResponseEntity<Page<PartyBranchInfo>> queryPartyBranchInfoList() {
        try {
            EntityWrapper<PartyBranchInfo> ew = new EntityWrapper<PartyBranchInfo>();
            ew.orderBy("id", false);
            ew.eq("enabled", 1);
//            ew.orderBy("integral", false);
//            ew.orderDesc();
            Page<PartyBranchInfo> page = getPage();
            return ResponseEntity.ok(partyBranchInfoService.selectPage(page, ew));
        } catch (Exception e) {
            logger.error("查询党支部列表出错!", e);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }

    // 修改党支部信息
    @RequestMapping(value = "/modify", method = RequestMethod.PUT)
//    @RequiresPermissions("/partybranchinfo/modify")
    public ResponseEntity<Void> edit(PartyBranchInfo partyBranchInfo) {
        try {
            boolean ret = false;
            if (partyBranchInfo.getId() != null) {
                partyBranchInfo.setUpdateTime(new Date(System.currentTimeMillis()));
                //更新
                ret = this.partyBranchInfoService.updateById(partyBranchInfo);
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
            logger.error("更新党支部错误!", e);
        }
        // 500
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

}
