package cn.gov.zunyi.video.web.controller;

import cn.gov.zunyi.video.common.util.FileTypeUtil;
import cn.gov.zunyi.video.model.*;
import cn.gov.zunyi.video.service.*;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import java.util.*;

@Api("事件相关接口")
@RestController
@RequestMapping("/event")
public class ElevatorEventController extends BaseController {

    //定义图片上传地址
    public final String filePath = "D://work/idea/idea_project/pt_video/src/main/webapp/static";

    @Autowired
    private ElevatorEventService elevatorEventService;
    @Autowired
    private VeEquipmentService veEquipmentService;
    @Autowired
    private VeEnclosureService veEnclosureService;
    @Autowired
    private VeEventFlowService veEventFlowService;
    @Autowired
    private VeEquipmentEventService veEquipmentEventService;

    /**
     * 新增或修改事件
     */
    @RequestMapping(value = "/addOrUpdateEE",method = RequestMethod.POST)
    public ResponseEntity<Map<String,Object>> addOrUpdateEE(@RequestParam("eqid") String eqid,
                                                             @RequestParam(value = "img",required = false) MultipartFile img,
                                                             ElevatorEvent elevatorEvent){
        Map<String,Object> map = new HashMap<>();
        User user = (User) SecurityUtils.getSubject().getPrincipal();
        try {
            if(elevatorEvent.getId() == null){
                if(StringUtils.isNoneBlank(elevatorEvent.getEventName())){
                    elevatorEvent.setEventName(elevatorEvent.getEventName().trim());
                }else{
                    map.put("status","400");
                    map.put("message","事件名称不能为空");
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(map);
                }

                VeEquipment veEquipment = veEquipmentService.selectById(eqid);
                if(veEquipment == null){
                    map.put("status","400");
                    map.put("message","获取设备定位出错！");
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(map);
                }

                elevatorEvent.setCreateTime(new Date(System.currentTimeMillis()));
                elevatorEvent.setEventLatitude(veEquipment.getVeLatitude());
                elevatorEvent.setEventLongitude(veEquipment.getVeLongitude());
                elevatorEvent.setUid(user.getId());
                elevatorEvent.setEventStatus(0);
                boolean rel = false;

                if(img != null){
                    if(!FileTypeUtil.isImageByExtension(img.getOriginalFilename())){
                        map.put("status","400");
                        map.put("message","获取的不是视频截图！");
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(map);
                    }else {
                        rel = elevatorEventService.getImgFile(filePath, img);
                        if(!rel){
                            map.put("status","400");
                            map.put("message","获取视频截图失败！");
                            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(map);
                        }

                        rel = elevatorEventService.insert(elevatorEvent);
                        if(!rel){
                            map.put("status","500");
                            map.put("message","服务器忙！");
                            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(map);
                        }

                        VeEquipmentEvent veEquipmentEvent = new VeEquipmentEvent();
                        veEquipmentEvent.setEqid(eqid);
                        veEquipmentEvent.setEid(elevatorEvent.getId());
                        veEquipmentEvent.setCreateTime(new Date(System.currentTimeMillis()));
                        rel = veEquipmentEventService.insert(veEquipmentEvent);
                        if(!rel){
                            map.put("status","500");
                            map.put("message","服务器忙！");
                            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(map);
                        }

                        VeEnclosure veEnclosure = new VeEnclosure();
                        veEnclosure.setCreateTime(new Date(System.currentTimeMillis()));
                        veEnclosure.setAppid(elevatorEvent.getId());
                        veEnclosure.setApptype(0);
                        veEnclosure.setUrl(img.getOriginalFilename());
                        rel = veEnclosureService.insert(veEnclosure);
                        if(!rel){
                            map.put("status","500");
                            map.put("message","服务器忙！");
                            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(map);
                        }
                    }
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
            System.out.println(e.toString());
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(map);
    }

    /**
     * 默认查询所有事件，并按照时间排序
     * 有条件则根据条件查询
     */
    @RequestMapping(value = "/getAllevent",method = RequestMethod.GET)
    public ResponseEntity<Map<String,Object>> queryAll(@RequestParam(value="id",required = false) String id,
                                                        @RequestParam(value="eventStatus",required = false) Integer eventStatus,
                                                        @RequestParam(value="typeid",required = false) String typeid,
                                                        @RequestParam(value="beforeDate",required = false) String beforeDate,
                                                        @RequestParam(value="afterDate",required = false) String afterDate){
        Map<String,Object> map = new HashMap<>();
        Page<ElevatorEvent> page = getPage();
        Wrapper<ElevatorEvent> ew = new EntityWrapper<>();
        try {
            Page<ElevatorEvent> pages = elevatorEventService.selectEvent(page,id,eventStatus,typeid,beforeDate,afterDate);
            map.put("pages",pages);
            return ResponseEntity.ok(map);
        }catch (Exception e){
            logger.error(e.toString());
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }

    /**
     *删除事件，将是否删除改为1
     */
    @RequestMapping(value = "/delete",method = RequestMethod.DELETE)
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

    /**
     * 根据事件id查询事件流转情况
     */
    @RequestMapping(value = "/getEventFlow",method = RequestMethod.GET)
    public ResponseEntity<Map<String,Object>> getEventFlow(@RequestParam("id") String id){
        Map<String,Object> map = new HashMap<>();
        Wrapper<VeEventFlow> ew = new EntityWrapper<>();
        try {
            ew.eq("eid",id);
            ew.eq("is_deleted","0");
            List<VeEventFlow> veEventFlows = veEventFlowService.selectList(ew);
            map.put("veEventFlows",veEventFlows);
            return ResponseEntity.ok(map);
        }catch (Exception e){
            logger.error(e.toString());
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }

    /**
     * 根据事件id查询事件详细情况
     */
    public ResponseEntity<Map<String,Object>> getEventDetails(@RequestParam("id") String id){
        Map<String,Object> map = new HashMap<>();
        Wrapper<VeEnclosure> ew = new EntityWrapper<>();
        try {
            ElevatorEvent elevatorEvent = elevatorEventService.selectById(id);
            ew.eq("appid",id);
            List<VeEnclosure> veEnclosures = veEnclosureService.selectList(ew);
            map.put("veEnclosures",veEnclosures);
            map.put("elevatorEvent",elevatorEvent);
            return ResponseEntity.ok(map);
        }catch (Exception e){
            logger.error(e.toString());
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }

    /**
     * 事件分配处理
     */
    @RequestMapping(value = "/manageEvent",method = RequestMethod.POST)
    public ResponseEntity<Map<String,Object>> manageEvent(VeEventFlow veEventFlow){
        Map<String,Object> map = new HashMap<>();
        Wrapper<VeEventFlow> ew = new EntityWrapper<>();
        boolean rel = false;
        try {
            if(veEventFlow.getProcessMode() == 0){
                veEventFlow.setCreateTime(new Date(System.currentTimeMillis()));
                rel = veEventFlowService.insert(veEventFlow);
            }

            if(!rel){
                map.put("status","500");
                map.put("message","服务器忙！");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(map);
            }
            map.put("status","201");
            map.put("message","分配成功！");
            return ResponseEntity.status(HttpStatus.CREATED).body(map);
        }catch (Exception e){
            logger.error(e.toString());
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }
}
