package cn.gov.zunyi.video.web.controller;

import cn.gov.zunyi.video.model.VeAddress;
import cn.gov.zunyi.video.model.VeCount;
import cn.gov.zunyi.video.model.VeType;
import cn.gov.zunyi.video.model.Video;
import cn.gov.zunyi.video.service.VeAddressService;
import cn.gov.zunyi.video.service.VeTypeService;
import cn.gov.zunyi.video.service.VideoService;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Api("视频设备相关接口")
@RestController
@RequestMapping("/video")
public class VideoController extends BaseController{

    @Autowired
    private VideoService videoService;
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
     * 根据视频源分类查询录像信息
     */
    @ApiImplicitParams({
            @ApiImplicitParam(name = "_index", value = "分页起始偏移量", required = false, paramType = "query", dataType = "Integer"),
            @ApiImplicitParam(name = "_size", value = "返回条数", required = false, paramType = "query", dataType = "Integer")
    })
    @RequestMapping(value = "/getVideo",method = RequestMethod.GET)
    public ResponseEntity<Map<String,Object>> ListVideo(@RequestParam("typeid") String typeid, @RequestParam("addressid") int addressid, @RequestParam("veStatus") int veStatus){

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }

    /**
     * 根据日期范围查询录像信息
     */
    @RequestMapping(value = "/dateBetWeenVideo",method = RequestMethod.GET)
    public ResponseEntity<List<Video>> dateBetween(@RequestParam("beforeDate") String beforeDate,
                                                           @RequestParam("after") String afterDate){
        Page<Video> page = this.getPage();
        try {
            return ResponseEntity.ok(videoService.getVideoByDateBetween(page,beforeDate,afterDate));
        }catch (Exception e){
            logger.error("查询录像信息出错！");
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }

    /**
     * 根据id删除录像信息
     */
    @RequestMapping(value = "/deleteVideo",method = RequestMethod.DELETE)
    public ResponseEntity<Map<String,Object>> deleteVideo(String id){
        Map<String,Object> map = new HashMap<>();
        boolean res = false;
        try {
            Video video = new Video();
            video.setId(id);
            video.setIsDeleted("1");
            res = videoService.updateById(video);
            if (!res){
                map.put("status","500");
                map.put("message","服务器忙!");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(map);
            }
            map.put("status","201");
            map.put("message","删除成功!");
            return ResponseEntity.status(HttpStatus.CREATED).body(map);
        }catch (Exception e){
            logger.error("删除视频时出错！");
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }
}
