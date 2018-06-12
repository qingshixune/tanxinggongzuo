package cn.gov.zunyi.video.web.controller;

import cn.gov.zunyi.video.model.*;
import cn.gov.zunyi.video.service.VeAddressService;
import cn.gov.zunyi.video.service.VeEquipmentService;
import cn.gov.zunyi.video.service.VeTypeService;
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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@Api("视频设备相关接口")
@RestController
@RequestMapping("/veInfo")
public class VeEquipmentController extends BaseController {

    @Autowired
    private VeEquipmentService veEquipmentService;
    @Autowired
    private VeTypeService veTypeService;
    @Autowired
    private VeAddressService veAddressService;

    /**
     * 查询视频源列表
     */
    @RequestMapping(value = "/getTypelist",method = RequestMethod.GET)
    public ResponseEntity<Page<VeType>> typelist(){
        Page<VeType> pages = this.getPage();
        try {
            return ResponseEntity.ok( pages = veTypeService.selectPage(pages));
        }catch (Exception e){
            logger.error("查询视频源列表时出错");
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }

    /**
     * 查询视频地区列表
     */
    @RequestMapping(value = "/getAddresslist",method = RequestMethod.GET)
    public ResponseEntity<Page<VeAddress>> addresslist(){
        Page<VeAddress> pages = this.getPage();
        try {
            return ResponseEntity.ok(veAddressService.selectPage(pages));
        }catch (Exception e){
            logger.error("查询视频源列表时出错");
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }

    /**
     * 新增或修改设备信息
     */
    @RequestMapping(value = "/edit",method = RequestMethod.POST)
    public ResponseEntity<Map<String, Object>> addOrUpadateVeInfo(VeEquipment veEquipment){
        Map<String,Object> map = new HashMap<>();
        try {
            Wrapper<VeEquipment> ew = new EntityWrapper<>();
            if(veEquipment.getId() == null){ //id为空，新增设备
                if(StringUtils.isNoneBlank(veEquipment.getVeName())){
                    veEquipment.setVeName(veEquipment.getVeName().trim());
                }
                if(StringUtils.isNoneBlank(veEquipment.getVideoUrl())){
                    veEquipment.setVideoUrl(veEquipment.getVideoUrl().trim());
                }
                boolean rel = false;
                veEquipment.setCreateTime(new Date(System.currentTimeMillis()));
                rel = veEquipmentService.insert(veEquipment);
                if(!rel){
                    map.put("status","500");
                    map.put("message","服务器忙！");
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(map);
                }
                map.put("status","201");
                map.put("message","添加成功！");
                return ResponseEntity.status(HttpStatus.CREATED).body(map);
            }else{  //id不为空，修改设备信息
                veEquipment.setUpdateTime(new Date(System.currentTimeMillis()));
                if(StringUtils.isNoneBlank(veEquipment.getVeName())){
                    veEquipment.setVeName(veEquipment.getVeName().trim());
                }
                if(StringUtils.isNoneBlank(veEquipment.getVideoUrl())){
                    veEquipment.setVideoUrl(veEquipment.getVideoUrl().trim());
                }

                boolean rel = false;
                rel = veEquipmentService.updateById(veEquipment);
                if(!rel){
                    map.put("status","500");
                    map.put("message","服务器忙！");
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(map);
                }
                map.put("status","201");
                map.put("message","更新成功！");
                return ResponseEntity.ok(map);
            }
        }catch (Exception e){
            map.put("status","500");
            map.put("message","服务器忙！");
            logger.error("更新或增加设备信息错误");
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(map);
    }

    /**
     * 根据视频源分类查询视频设备信息,前台大屏展示
     */
    @ApiImplicitParams({
            @ApiImplicitParam(name = "_index", value = "分页起始偏移量", required = false, paramType = "query", dataType = "Integer"),
            @ApiImplicitParam(name = "_size", value = "返回条数", required = false, paramType = "query", dataType = "Integer")
    })
    @RequestMapping(value = "/getVe",method = RequestMethod.GET)
    public ResponseEntity<Map<String,Object>> listVeByType(@RequestParam("veType") Integer veType){
        Map<String,Object> map = new HashMap<>();
        Page<VeEquipment> page = getPage();
        VeCount veCount = null;
        try {
            if (veType == 0){ //默认查询所有设备
                List<VeEquipment> veEquipments = veEquipmentService.getVeAll(page);
                veCount = veEquipmentService.veCount(veEquipments);
                map.put("veCount",veCount);
                map.put("veEquipments", veEquipments);
                return ResponseEntity.ok(map);
            }else{ //根据分类id不同查询不同的设备信息
                List<VeEquipment> veEquipments = veEquipmentService.getVeAllByType(page,veType);
                veCount = veEquipmentService.veCount(veEquipments);
                map.put("veCount",veCount);
                map.put("veInfos", veEquipments);
                return ResponseEntity.ok(map);
            }
        }catch (Exception e){
            System.out.println(e);
            logger.error("查询设备信息出错！");
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }

    /**
     * 根据条件筛选获取监控列表
     * @return
     */
    @RequestMapping(value = "/getListVe",method = RequestMethod.GET)
    public ResponseEntity<Map<String,Object>> listVe(@RequestParam("typeid") String typeid,
                                                      @RequestParam("addressid") int addressid,
                                                      @RequestParam("veStatus") int veStatus,
                                                      @RequestParam(value = "beforeDate",required = false) String beforeDate,
                                                      @RequestParam(value = "after",required = false) String afterDate,
                                                      @RequestParam(value = "veNames",required = false) String[] veNames){
        Map<String,Object> map = new HashMap<>();
        Page<VeEquipment> page = this.getPage();
        try {
            List<VeEquipment> veEquipments = veEquipmentService.getVeList(page,typeid,addressid,veStatus,beforeDate,afterDate,veNames);
            map.put("veEquipments",veEquipments);
            return ResponseEntity.ok(map);
        }catch (Exception e){
            map.put("status","500");
            map.put("message","服务器忙!");
            logger.error("查询监控列表出错！");
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }



    /**
     * 删除设备信息，将是否删除变为1
     */
    @Transactional
    @RequestMapping(value = "/deleteVe",method = RequestMethod.DELETE)
    public ResponseEntity<Map<String,Object>> deleteVe(String id){
        Map<String,Object> map = new HashMap<>();
        VeEquipment veEquipment = new VeEquipment();
        try {
            veEquipment.setId(id);
            veEquipment.setIsDeleted("1");
            boolean rel = false;
            rel = veEquipmentService.updateById(veEquipment);
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
     * 开启视频设备
     */
    @RequestMapping(value = "/strVe",method = RequestMethod.PUT)
    public void startVe(@RequestParam("id") String id){
        
    }
}
