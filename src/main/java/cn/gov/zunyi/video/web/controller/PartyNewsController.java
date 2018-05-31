package cn.gov.zunyi.video.web.controller;

import cn.gov.zunyi.video.model.News;
import cn.gov.zunyi.video.service.PartyNewsService;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 党政府新闻 Conroller(1.通知公告 2.党务公开 3.汇川先锋)
 *
 * @author yangzhiping
 * @Date: 2018/5/7 15:41
 */
@RestController
@RequestMapping(value = "/partNews")
public class PartyNewsController extends BaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(PartyNewsController.class);

    @Autowired
    private PartyNewsService partyNewsService;

    /**
     * 根据type获取相应的政府新闻列表
     *
     * @param ew
     * @param type 1.通知公告 2.党务公开 3.汇川先锋
     * @return
     */
    @ApiOperation(value = "获取党政府新闻", notes = "1.通知公告 2.党务公开 3.汇川先锋")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "_index", value = "分页起始偏移量", required = false, paramType = "query", dataType = "Integer"),
            @ApiImplicitParam(name = "_size", value = "返回条数", required = false, paramType = "query", dataType = "Integer"),
            @ApiImplicitParam(name = "type", value = "根据类型选择需要获取的新闻1.通知公告 2.党务公开 3.汇川先锋", required = true)})
    @RequestMapping(value = "/getPartyNewsList", method = RequestMethod.GET)
    //@RequiresPermissions("/partNews/getPartyNewsList")
    public ResponseEntity<Page<News>> getPartyNewsList(EntityWrapper<News> ew, int type) {
        try {
            Wrapper<News> wrapper = new EntityWrapper<>();
            Page<News> page = this.getPage();
            wrapper.eq("type", type);
            wrapper.orderBy("updateTime", false);
            Page<News> partyNewsList = this.partyNewsService.selectPage(page, wrapper);
            return ResponseEntity.ok(partyNewsList);
        } catch (Exception e) {
            LOGGER.error("查询出错", e);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }

    /**
     * 查询某个时间段的通告并分页
     *
     * @param type      1.通知公告 2.党务公开 3.汇川先锋
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return
     */
    @ApiImplicitParams({
            @ApiImplicitParam(name = "_index", value = "分页起始偏移量", required = false, paramType = "query", dataType = "Integer"),
            @ApiImplicitParam(name = "_size", value = "返回条数", required = false, paramType = "query", dataType = "Integer")})
    @ApiOperation(value = "查询某个时间段的通告", notes = "查询某个时间段的通告")
    @RequestMapping(value = "/queryPartyNewList", method = RequestMethod.GET)
    //@RequiresPermissions("/partNews/queryPartyNewList")
    public ResponseEntity<Page<News>> queryAnnouncementsList(@RequestParam("type") int type, String startTime, String endTime) {
        try {
            Wrapper<News> wrapper = new EntityWrapper<News>();
            /*if (StringUtils.isNoneBlank(startTime) && StringUtils.isNoneBlank(endTime)) {
                String startDate = startTime + " 00:00:00";
                wrapper.ge("start_time", startDate);
                String endDate = endTime + " 23:59:59";
                wrapper.le("start_time", endDate);
            }*/
            if (StringUtils.isNoneBlank(startTime)) {
                String startDate = startTime + " 00:00:00";
                String endDate = endTime + " 23:59:59";
                wrapper.between("start_time", startDate, endDate);
            }
            wrapper.eq("type", type);
            wrapper.orderBy("updateTime", false);
            Page<News> page = getPage();
            Page<News> pageList = this.partyNewsService.selectPage(page, wrapper);
            return ResponseEntity.ok(pageList);
        } catch (Exception e) {
            LOGGER.error("查询通告列表出错!", e);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);

    }

    /**
     * 增加和修改
     *
     * @param news
     * @return
     */
    @ApiOperation(value = "增加和修改党政府新闻", notes = "1.通知公告 2.党务公开 3.汇川先锋")
    @RequestMapping(value = "/addPartyNews", method = RequestMethod.POST)
    //@RequiresPermissions("/partyNews/addPartyNews")
    public ResponseEntity<Map<String,Object>> addPartyNews(News news) {
        try {
            boolean ret;
            Map<String, Object> resultMap = new LinkedHashMap<String, Object>();
            if (news.getId() != null) {
                news.setUpdateTime(new Date(System.currentTimeMillis()));
                ret = this.partyNewsService.updateById(news);
            } else {
                news.setCreateTime(new Date(System.currentTimeMillis()));
                news.setUpdateTime(news.getCreateTime());
                ret = this.partyNewsService.insert(news);
            }
            if (ret) {
                resultMap.put("status",200);
                resultMap.put("message","操作成功");
            }
            return ResponseEntity.status(HttpStatus.CREATED).body(resultMap);
        } catch (Exception e) {
            LOGGER.error("通知公告审核出错!", e);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }

    /**
     * 删除通知公告
     *
     * @param partyNewsId
     * @return
     */
    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    //@RequiresPermissions("/partyNews/delete")
    public ResponseEntity<Map<String,Object>> delete(String partyNewsId) {
        try {
            Map<String,Object> resultMap = new LinkedHashMap<>();
            boolean flag = this.partyNewsService.deleteById(Integer.valueOf(partyNewsId));
            if (flag) {
                resultMap.put("status",200);
                resultMap.put("message","删除成功");
            }
            return ResponseEntity.status(HttpStatus.CREATED).body(resultMap);
        } catch (Exception e) {
            LOGGER.error("删除出错!", e);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }

    /**
     * 根据Id获取单条
     *
     * @param Id
     * @return
     */
    @RequestMapping(value = "/getPartyNewsById/{Id}", method = RequestMethod.GET)
    //@RequiresPermissions("/partyNews/getPartyNewsById")
    public ResponseEntity<News> getPartyNewsById(@PathVariable(value = "Id", required = true) String Id) {
        try {
            News news = this.partyNewsService.selectById(Id);
            if (null == news) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
            return ResponseEntity.ok(news);
        } catch (Exception e) {
            logger.error("查询出错！", e);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }

    /**
     *  根据title和content模糊查询
     * @param type
     * @param title
     * @param content
     * @return
     */
    @ApiImplicitParams({
            @ApiImplicitParam(name = "_index", value = "分页起始偏移量", required = false, paramType = "query", dataType = "Integer"),
            @ApiImplicitParam(name = "_size", value = "返回条数", required = false, paramType = "query", dataType = "Integer")})
    @RequestMapping(value = "/queryPartyNewsByTitleOrContent", method = RequestMethod.GET)
    //@RequiresPermissions("/partyNews/queryPartyNewsByTitleOrContent")
    public ResponseEntity<Page<News>> get(@RequestParam(value = "type", required = true) int type,
                                          @RequestParam(value = "title", required = false) String title,
                                          @RequestParam(value = "content", required = false) String content) {
        try {
            Page<News> page = this.getPage();
            Wrapper<News> wrapper = new EntityWrapper<>();
            wrapper.eq("type", type);
            wrapper.orderBy("updateTime", false);
            if (StringUtils.isNoneBlank(title)) {
                wrapper.like("title", title);
            }
            if (StringUtils.isNoneBlank(content)) {
                wrapper.like("content", content);
            }
            Page<News> pageList = this.partyNewsService.selectPage(page, wrapper);
            if (null == pageList) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
            return ResponseEntity.ok(pageList);
        } catch (Exception e) {
            logger.error("查询出错！", e);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }
}
