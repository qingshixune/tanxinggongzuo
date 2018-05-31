package cn.gov.zunyi.video.web.controller;

import cn.gov.zunyi.video.model.PtMemberInfo;
import cn.gov.zunyi.video.service.PtMemberInfoService;
import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.*;

@Api("党内工作学习统计")
@RestController
@RequestMapping(value="/PtMemberInfo")
@ConfigurationProperties
public class PtMemberInfoController extends BaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(PtCareController.class);

    @Autowired
    private PtMemberInfoService ptMemberInfoService;

    /**
     * 查询每个月的学习次数和工作次数
     * @param beforeYear
     * @param afterYear
     * @return
     */
    @RequestMapping(value="/getPartyInfo",method = RequestMethod.GET)
    public ResponseEntity<Map> getPartyInfo(@RequestParam("beforeYear") String beforeYear,
                                           @RequestParam("afterYear") String afterYear){
        Map<String,Object> map = new HashMap<>();
        try {
            //从数据库中获取开始时间到结束时间的每个月的数据
            List<PtMemberInfo> pts = ptMemberInfoService.getPartyInfo(beforeYear, afterYear);
            //获取从开始时间到结束时间包括没有数据的（yyyy-mm格式）时间
            List<String> list = ptMemberInfoService.getMonthBetween(beforeYear, afterYear);
            //将没有数据的时间填充到每个月的数据中
            pts = ptMemberInfoService.getPtsSupplement(pts,list);
            map.put("pts",pts);
            return ResponseEntity.ok(map);
        } catch (Exception e){
            LOGGER.error("查询工作学习统计信息出错!", e);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }

    /**
     * 新增或修改 工作和学习次数
     * @param ptMemberInfo
     * @return
     */
    @RequestMapping(value="/editPartyInfo",method = RequestMethod.POST)
    public ResponseEntity<Map> exidPartyInfo(PtMemberInfo ptMemberInfo,String countDate){
        Map<String,Object> map = new HashMap<>();
        try {
            boolean rel;
            if(ptMemberInfo.getId() != null){
                ptMemberInfo.setUpdateTime(new Date(System.currentTimeMillis()));
                rel = ptMemberInfoService.updateById(ptMemberInfo);
                map.put("status","200");
                map.put("message","修改成功!");
                return ResponseEntity.ok(map);
            }else {
                Boolean result = ptMemberInfoService.getPartyInfoByTime(countDate);//判断数据库中是否有该月数据
                if(result){
                    ptMemberInfo.setCreateTime(new Date(System.currentTimeMillis()));
                    ptMemberInfo.setUpdateTime(ptMemberInfo.getCreateTime());
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                    ptMemberInfo.setCountTime(format.parse(countDate));
                    rel = ptMemberInfoService.insert(ptMemberInfo);
                    map.put("status","201");
                    map.put("message","新增成功!");
                    return ResponseEntity.ok(map);
                }else {
                    map.put("status","400");
                    map.put("message","无法新增，当月已有数据!");
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(map);
                }
            }
        }catch (Exception e){
            LOGGER.error("新增或修改工作学习统计信息出错!", e);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }

    @RequestMapping(value="/deletePartyInfo",method = RequestMethod.DELETE)
    public ResponseEntity<Map> deletePartyInfo(Integer id){
        Map<String,Object> map = new HashMap<>();
        try {
            boolean result;
            if(id != null){ //不能删除没有数据的月份的数据
                result = ptMemberInfoService.deleteById(id);
                map.put("status","201");
                map.put("message","删除成功!");
                return ResponseEntity.ok(map);
            }else {
                map.put("status","400");
                map.put("message","删除失败，没有本月的信息!");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(map);
            }
        }catch (Exception e){
            LOGGER.error("删除工作学习统计信息出错!", e);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }
}
