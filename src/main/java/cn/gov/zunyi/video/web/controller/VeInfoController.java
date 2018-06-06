package cn.gov.zunyi.video.web.controller;

import cn.gov.zunyi.video.common.util.StringUtil;
import cn.gov.zunyi.video.mapper.VideoMapper;
import cn.gov.zunyi.video.model.*;
import cn.gov.zunyi.video.service.VeAddressService;
import cn.gov.zunyi.video.service.VeInfoService;
import cn.gov.zunyi.video.service.VeTypeService;
import cn.gov.zunyi.video.service.VideoService;
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
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.*;
import java.util.logging.Logger;

@Api("视频设备相关接口")
@RestController
@RequestMapping("/veInfo")
public class VeInfoController extends BaseController {

    @Autowired
    private VeInfoService veInfoService;
    @Autowired
    private VideoService videoService;
    @Autowired
    private VeTypeService veTypeService;
    @Autowired
    private VeAddressService veAddressService;

    /**
     * 查询视频源列表
     */
    @RequestMapping("/getTypelist")
    public ResponseEntity<Map<String,Object>> typelist(){
        Map<String,Object> map = new HashMap<>();
        Page<VeType> pages = this.getPage();
        try {
            pages = veTypeService.selectPage(pages);
            map.put("typeList",pages);
            return ResponseEntity.ok(map);
        }catch (Exception e){
            map.put("status","500");
            map.put("message","服务器忙！");
            logger.error("查询视频源列表时出错");
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }

    /**
     * 查询视频地区列表
     */
    @RequestMapping("/getAddresslist")
    public ResponseEntity<Map<String,Object>> addresslist(){
        Map<String,Object> map = new HashMap<>();
        Page<VeAddress> pages = this.getPage();
        try {
            pages = veAddressService.selectPage(pages);
            map.put("addresslist",pages);
            return ResponseEntity.ok(map);
        }catch (Exception e){
            map.put("status","500");
            map.put("message","服务器忙！");
            logger.error("查询视频源列表时出错");
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }


    /**
     * 新增或修改设备信息
     */
    @RequestMapping(value = "/edit",method = RequestMethod.POST)
    public ResponseEntity<Map<String, Object>> addOrUpadateVeInfo(VeInfo veInfo){
        Map<String,Object> map = new HashMap<>();
        try {
            Wrapper<VeInfo> ew = new EntityWrapper<>();
            if(veInfo.getId() == null){ //id为空，新增设备
                if(StringUtils.isNoneBlank(veInfo.getVeName())){
                    veInfo.setVeName(veInfo.getVeName().trim());
                    ew.eq("ve_name",veInfo.getVeName());
                    VeInfo v = veInfoService.selectOne(ew);
                    if(v != null){
                        map.put("status","400");
                        map.put("message","设备名称已存在！");
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(map);
                    }
                }
                if(StringUtils.isNoneBlank(veInfo.getVideoUrl())){
                    veInfo.setVideoUrl(veInfo.getVideoUrl().trim());
                }
                if(veInfo.getVeRunstus() == 1){
                    veInfo.setVeStrtime(new Date(System.currentTimeMillis()));
                }
                boolean rel = false;
                veInfo.setCreateTime(new Date(System.currentTimeMillis()));
                rel = veInfoService.insert(veInfo);
                if(!rel){
                    map.put("status","500");
                    map.put("message","服务器忙！");
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(map);
                }
                map.put("status","201");
                map.put("message","添加成功！");
                return ResponseEntity.status(HttpStatus.CREATED).body(map);
            }else{  //id不为空，修改设备信息
                veInfo.setUpdateTime(new Date(System.currentTimeMillis()));
                if(StringUtils.isNoneBlank(veInfo.getVeName())){
                    veInfo.setVeName(veInfo.getVeName().trim());
                    ew.eq("ve_name",veInfo.getVeName());
                    VeInfo v = veInfoService.selectOne(ew);
                    if(v != null){
                        map.put("status","400");
                        map.put("message","设备名称已存在");
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(map);
                    }
                }
                if(StringUtils.isNoneBlank(veInfo.getVideoUrl())){
                    veInfo.setVideoUrl(veInfo.getVideoUrl().trim());
                }

                boolean rel = false;
                rel = veInfoService.updateById(veInfo);
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
     * 根据视频源分类查询视频设备信息
     */
    @ApiImplicitParams({
            @ApiImplicitParam(name = "_index", value = "分页起始偏移量", required = false, paramType = "query", dataType = "Integer"),
            @ApiImplicitParam(name = "_size", value = "返回条数", required = false, paramType = "query", dataType = "Integer")
    })
    @RequestMapping(value = "/getVe",method = RequestMethod.GET)
    public ResponseEntity<Map<String,Object>> listVe(@RequestParam("veType") Integer veType){
        Map<String,Object> map = new HashMap<>();
        Page<VeInfo> page = getPage();
        VeCount veCount = null;
        try {
            Wrapper<VeInfo> ew = new EntityWrapper<>();
            ew.eq("is_deleted","0");
            if (veType == 0){ //默认查询所有设备
                Page<VeInfo> pages = veInfoService.selectPage(page,ew);
                List<VeInfo> veInfos = page.getRecords();
                veCount = veInfoService.veCount(veInfos);

                map.put("veCount",veCount);
                map.put("veInfos",veInfos);
                return ResponseEntity.ok(map);
            }else{ //根据分类id不同查询不同的设备信息
                ew.eq("ve_type",veType);
                Page<VeInfo> pages = veInfoService.selectPage(page,ew);
                List<VeInfo> veInfos = pages.getRecords();
                veCount = veInfoService.veCount(veInfos);

                map.put("veCount",veCount);
                map.put("veInfos",veInfos);
                return ResponseEntity.ok(map);
            }
        }catch (Exception e){
            logger.error("查询设备信息出错！");
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }

    /**
     * 根据视频源分类查询录像信息
     */
    @ApiImplicitParams({
            @ApiImplicitParam(name = "_index", value = "分页起始偏移量", required = false, paramType = "query", dataType = "Integer"),
            @ApiImplicitParam(name = "_size", value = "返回条数", required = false, paramType = "query", dataType = "Integer")
    })
    @RequestMapping(value = "/getVideo",method = RequestMethod.GET)
    public ResponseEntity<Map<String,Object>> ListVideo(@RequestParam("typeid") String typeid,@RequestParam("addressid") int addressid,@RequestParam("veStatus") int veStatus){
        Map<String,Object> map = new HashMap<>();
        Wrapper<Video> ew = new EntityWrapper<>();
        Page<Video> page = getPage();
        VeCount veCount = new VeCount();
        try {
            List<Video> videos = videoService.getVideo(page,typeid,addressid,veStatus);
            veCount = videoService.getVeCount(videos);
            veCount.setVideoNum(videos.size());
            map.put("veCount",veCount);
            map.put("videos",videos);
            return ResponseEntity.ok(map);
        }catch (Exception e){
            logger.error("查询录像信息出错！");
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }

    /**
     * 删除设备信息，将是否删除变为1
     */
    @Transactional
    @RequestMapping(value = "/deleteVe",method = RequestMethod.DELETE)
    public ResponseEntity<Map<String,Object>> deleteVe(VeInfo veInfo){
        Map<String,Object> map = new HashMap<>();
        try {
            veInfo.setIsDeleted("1");
            boolean rel = false;
            rel = veInfoService.updateById(veInfo);
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
