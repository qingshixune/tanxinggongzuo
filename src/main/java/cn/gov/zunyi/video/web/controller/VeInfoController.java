package cn.gov.zunyi.video.web.controller;

import cn.gov.zunyi.video.common.util.StringUtil;
import cn.gov.zunyi.video.mapper.VideoMapper;
import cn.gov.zunyi.video.model.*;
import cn.gov.zunyi.video.service.VeInfoService;
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

    /**
     * 新增或修改设备信息
     */
    @RequestMapping(value = "/exit",method = RequestMethod.POST)
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
                if(StringUtils.isNoneBlank(veInfo.getVeAddress())){
                    veInfo.setVeAddress(veInfo.getVeAddress().trim());
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
                if(StringUtils.isNoneBlank(veInfo.getVeAddress())){
                    veInfo.setVeAddress(veInfo.getVeAddress().trim());
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
    public ResponseEntity<Map<String,Object>> ListVideo(VeInfo veInfo){
        Map<String,Object> map = new HashMap<>();
        Wrapper<Video> ew = new EntityWrapper<>();
        Page<Video> page = getPage();
        List<Video> videos = new ArrayList<>();
        VeCount veCount = new VeCount();
        //默认查询全部录像信息
        if(veInfo.getVeAddress() == null && veInfo.getVeName() == null && veInfo.getId() == null){
            ew.eq("is_deleted","0");
            Page<Video> pages = videoService.selectPage(page,ew);
            videos = pages.getRecords();
            veCount = videoService.getVeCount(videos);
            veCount.setVideoNum(videos.size());
        }else if(veInfo.getVeAddress() != null){  //根据地址信息查询录像信息
            videos = videoService.getVideoByAddress(page,veInfo.getVeAddress());
            if(videos.size()<=0){
                map.put("status","400");
                map.put("message","查询地址有误！");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(map);
            }
            veCount = videoService.getVeCount(videos);
            veCount.setVideoNum(videos.size());
        }else if(veInfo.getId() != null){
            videos = videoService.getVideoById(page,veInfo.getId());
            if(videos.size()<=0){
                map.put("status","400");
                map.put("message","设备不存在！");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(map);
            }
            veCount = videoService.getVeCount(videos);
            veCount.setVideoNum(videos.size());
        }else if(veInfo.getVeName() != null){
            videos = videoService.getVideoByName(page,veInfo.getVeName());
            if(videos.size()<=0){
                map.put("status","400");
                map.put("message","设备不存在！");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(map);
            }
            veCount = videoService.getVeCount(videos);
            veCount.setVideoNum(videos.size());
        }
        map.put("veCount",veCount);
        map.put("videos",videos);
        return ResponseEntity.ok(map);
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

    @RequestMapping(value = "/getPhotoName",method = RequestMethod.GET)
    public List<Map<String,Object>> getPhotoName(String photoPath){
        String pic = "D://板桥镇村（社区）党组织概况及照片/"+photoPath;
        List<Map<String,Object>> list = new ArrayList<>();
        try {
            list = veInfoService.readfile(pic);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }
}
