package cn.gov.zunyi.video.web.controller;

import cn.gov.zunyi.video.model.ElevatorEvent;
import cn.gov.zunyi.video.service.ElevatorEventService;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@Api("视频事件相关接口")
@RestController
@RequestMapping("/ee")
public class ElevatorEventController extends BaseController {

    @Autowired
    private ElevatorEventService elevatorEventService;

    /**
     * 新增或修改事件
     */
    @RequestMapping(value = "/addOrUpdateEE",method = RequestMethod.POST)
    public ResponseEntity<Map<String,Object>> addOrUpdateEE(ElevatorEvent elevatorEvent){
        Map<String,Object> map = new HashMap<>();
        try {
            if(elevatorEvent.getId() == null){
                if(StringUtils.isNoneBlank(elevatorEvent.getVeTitle())){
                    elevatorEvent.setVeTitle(elevatorEvent.getVeTitle().trim());
                }else{
                    map.put("status","400");
                    map.put("message","事件标题不能为空");
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(map);
                }
                if(!StringUtils.isNoneBlank(elevatorEvent.getVeOccurtime())){
                    map.put("status","400");
                    map.put("message","事件发生时间不能为空");
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(map);
                }
                elevatorEvent.setCreateTime(new Date(System.currentTimeMillis()));
                boolean rel = false;
                rel = elevatorEventService.insert(elevatorEvent);
                if(!rel){
                    map.put("status","500");
                    map.put("message","服务器忙！");
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(map);
                }
                map.put("status","201");
                map.put("message","添加成功！");
                return ResponseEntity.status(HttpStatus.CREATED).body(map);
            }else{
                elevatorEvent.setUpdateTime(new Date(System.currentTimeMillis()));
                boolean rel = false;
                rel = elevatorEventService.updateById(elevatorEvent);
                if(!rel){
                    map.put("status","500");
                    map.put("message","服务器忙！");
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(map);
                }
                map.put("status","201");
                map.put("message","更新成功！");
                return ResponseEntity.status(HttpStatus.CREATED).body(map);
            }
        }catch (Exception e){
            map.put("status","500");
            map.put("message","服务器忙！");
            logger.error("更新或增加设备信息错误");
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(map);
    }

    /**
     *默认查询所有事件，并按照时间排序
     */
    @ApiImplicitParams({
            @ApiImplicitParam(name = "_index", value = "分页起始偏移量", required = false, paramType = "query", dataType = "Integer"),
            @ApiImplicitParam(name = "_size", value = "返回条数", required = false, paramType = "query", dataType = "Integer")
    })
    @RequestMapping(value = "/getAllee",method = RequestMethod.GET)
    public ResponseEntity<Map<String,Object>> queryAll(@RequestParam(value="id",required = false) String id){
        Map<String,Object> map = new HashMap<>();
        Page<ElevatorEvent> page = getPage();
        List<ElevatorEvent> list = new ArrayList<>();
        Wrapper<ElevatorEvent> ew = new EntityWrapper<>();
        try {
            if(id==null){
                ew.eq("is_deleted","0");
                ew.orderBy("ee_exigrncystus",false);
                ew.orderBy("ee_occurtime",false);
                ew.eq("ee_occurstus",1);
                Page<ElevatorEvent> pages = elevatorEventService.selectPage(page,ew);
                list = pages.getRecords();
                map.put("list",list);
            }else{
                ElevatorEvent elevatorEvent = elevatorEventService.selectById(id);
                map.put("ee",elevatorEvent);
            }
            return ResponseEntity.ok(map);
        }catch (Exception e){
            map.put("status","500");
            map.put("message","服务器忙！");
            logger.error("更新或增加设备信息错误");
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }

    /**
     *删除时间，将是否删除改为1
     */
    @RequestMapping("/deleteEE")
    public ResponseEntity<Map<String,Object>> deleteEE(String id){
        Map<String,Object> map = new HashMap<>();
        Wrapper<ElevatorEvent> ew = new EntityWrapper<>();
        ElevatorEvent elevatorEvent = new ElevatorEvent();
        try {
            boolean rel = false;
            elevatorEvent.setId(id);
            elevatorEvent.setIsDeleted("1");
            rel = elevatorEventService.updateById(elevatorEvent);
            if(!rel){
                map.put("status","500");
                map.put("message","服务器忙！");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(map);
            }
            map.put("status","201");
            map.put("message","删除成功！");
            return ResponseEntity.ok(map);
        }catch (Exception e){
            logger.error("删除设备信息错误");
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }
}
