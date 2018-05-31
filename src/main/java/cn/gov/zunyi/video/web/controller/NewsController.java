package cn.gov.zunyi.video.web.controller;

import java.util.Date;

import cn.gov.zunyi.video.model.FrontPage;
import cn.gov.zunyi.video.model.News;
import cn.gov.zunyi.video.service.MsSysLogService;
import cn.gov.zunyi.video.service.NewsService;
import cn.gov.zunyi.video.service.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;

import cn.gov.zunyi.video.common.util.FastDFSUtils;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping("/news")
public class NewsController extends BaseController {

	private static final Logger LOGGER = LoggerFactory.getLogger(NewsController.class);

	@Autowired
	private NewsService newsService;

	/* 系统日志 */
	@Autowired
	private MsSysLogService msSysLogService;

	@Value("${IMAGE_BASE_URL}")
	private String IMAGE_BASE_URL;

	@ApiOperation(value = "查看新闻(用户登录的状态下可根据user_id查询用户是否收藏过该新闻)", notes = "查看新闻(用户登录的状态下可根据user_id查询用户是否收藏过该新闻)")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "news_id", value = "新闻id", required = true, paramType = "path", dataType = "Integer"),
			@ApiImplicitParam(name = "user_id", value = "用户id", required = false, paramType = "query", dataType = "Integer") })
	@RequestMapping(value = "/get/{news_id}", method = RequestMethod.GET)
	@ApiResponses(value = { @ApiResponse(code = 400, message = "该新闻不存在") })
	public ResponseEntity<News> getNews(@PathVariable(value = "news_id", required = true) String news_id,
                                        @RequestParam(value = "user_id", required = false) String user_id) {
		try {
			if (StringUtils.isBlank(news_id)) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
			}
			News news = this.newsService.selectById(news_id);
			if (news == null) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
			}
			return ResponseEntity.ok(news);
		} catch (Exception e) {
		}
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
	}

	/**
	 * 查询新闻列表
	 *
	 * @return
	 */
	@ApiOperation(value = "根据频道id查询该频道下的新闻分页列表", notes = "根据频道id查询该频道下的新闻分页列表")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "_index", value = "分页起始偏移量", required = false, paramType = "query", dataType = "Integer"),
			@ApiImplicitParam(name = "_size", value = "返回条数", required = false, paramType = "query", dataType = "Integer"),
			@ApiImplicitParam(name = "author", value = "编写者", required = false, paramType = "query", dataType = "String"),
			@ApiImplicitParam(name = "channel_id", value = "频道id", required = true, paramType = "query", dataType = "Integer") })
	@RequestMapping(value = "/getNewsList", method = RequestMethod.GET)
	public ResponseEntity<Page<News>> queryNewsList(String channel_id, String title, String author,String  startTime,String endTime) {
		try {
			Wrapper<News> wrapper = new EntityWrapper<News>();
			wrapper.ne("type", 0).orderBy("id", false);
			if (StringUtils.isNoneBlank(channel_id)) {
				wrapper.like("channel_id",  channel_id );
				if (StringUtils.isNoneBlank(title)) {
					wrapper.like("title", title);
				}
				if (StringUtils.isNoneBlank(author)) {
					wrapper.like("author", author);
				}

				if (StringUtils.isNoneBlank(startTime)) {
					String startDate = startTime + " 00:00:00";
					String endDate = endTime + " 23:59:59";
					wrapper.between("start_time", startDate, endDate);
				}
			} else {
				if (StringUtils.isNoneBlank(title)) {
					wrapper.like("title", title);
				}
				if (StringUtils.isNoneBlank(author)) {
					wrapper.eq("author", author);
				}
				wrapper.eq("approved", 0);
			}
			Page<News> page = getPage();
			Page<News> pageList = this.newsService.selectPage(page, wrapper);
			return ResponseEntity.ok(pageList);
		} catch (Exception e) {
			LOGGER.error("查询新闻列表出错!", e);
		}
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
	}

	//by CP
	@RequestMapping(value = "/getById/{news_id}", method = RequestMethod.GET)
	@RequiresPermissions("/news/getById/")
	public ResponseEntity<News> getNewsById(@PathVariable(value = "news_id", required = true) String news_id) {
		try {
			if (StringUtils.isBlank(news_id)) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
			}
			News news = this.newsService.selectById(news_id);
			if (news == null) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
			}
			return ResponseEntity.ok(news);
		} catch (Exception e) {
		}
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
	}

	@RequestMapping(value = "/queryList", method = RequestMethod.GET)
	@RequiresPermissions("/news/queryList")
	public ResponseEntity<Page<News>> queryList(FrontPage<News> frontPage, String channel_id, String title,
                                                String author, String approved, String startTime, String endTime) {
		try {
			Wrapper<News> wrapper = new EntityWrapper<News>();
			if (StringUtils.isNoneBlank(channel_id)) {
				wrapper.eq("channel_id", channel_id);
			}
			if (StringUtils.isNoneBlank(title)) {
				wrapper.like("title", title);
			}
			if (StringUtils.isNoneBlank(author)) {
				wrapper.eq("author", author);
			}
			if (StringUtils.isNoneBlank(approved)) {
				wrapper.eq("approved", approved);
			}

			if (StringUtils.isNoneBlank( startTime)) {
				String startDate = startTime + " 00:00:00";
				wrapper.ge("start_time", startDate);
				String endDate =  endTime + " 23:59:59";
				wrapper.le("start_time", endDate);
			}

			wrapper.orderBy("id", false);
			Page<News> page = getPage();
			Page<News> pageList = this.newsService.selectPage(page, wrapper);
			return ResponseEntity.ok(pageList);
		} catch (Exception e) {
			LOGGER.error("查询新闻列表出错!", e);
		}
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
	}

	// 增加和修改
	@RequestMapping(value = "/editNew", method = RequestMethod.POST)
	public ResponseEntity<Integer> editNew(News news) {
		try {
			boolean ret;
			news.setApproved(1);
			if (news.getId() != null) {
				news.setUpdateTime(new Date(System.currentTimeMillis()));
				ret = this.newsService.updateById(news);
				// 新增系统日志
				msSysLogService.insertData(request, "修改活动信息", "新增");
			} else {
				news.setCreateTime(new Date(System.currentTimeMillis()));
				news.setUpdateTime(news.getCreateTime());
				ret = this.newsService.insert(news);
				// 新增系统日志
				msSysLogService.insertData(request, "新增活动信息", "新增");
			}
			Integer flag = 1;
			if (ret) {
				flag = 0;
			}

			return ResponseEntity.ok(flag);
		} catch (Exception e) {
			LOGGER.error("增加或修改活动出错!", e);
		}
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
	}

	// 增加和修改
	@RequestMapping(value = "/edit", method = RequestMethod.POST)
	@RequiresPermissions("/news/edit")
	public ResponseEntity<Integer> edit(News news,
				@RequestParam(value = "channelIdFirst", required = false) String channelIdFirst,
				@RequestParam(value = "channelIdSecond", required = false) String channelIdSecond,
				@RequestParam(value = "channelIdThird", required = false) String channelIdThird,
				@RequestParam(value = "bc_cover", required = false) MultipartFile uploadFile) {
		try {
			boolean ret;
			String channelId = "";
			if (channelIdFirst != null && !"".equals(channelIdFirst)) {
				channelId += channelIdFirst;
				channelId += ",";
			}
			if (channelIdSecond != null && !"".equals(channelIdSecond)) {
				channelId += channelIdSecond;
				channelId += ",";
			}
			if (channelIdThird != null && !"".equals(channelIdThird)) {
				channelId += channelIdThird;
				channelId += ",";
			}

			news.setChannelId(channelId);
			news.setApproved(1);
			if (news.getId() != null) {
				if (uploadFile != null && StringUtils.isNotEmpty(uploadFile.getOriginalFilename())) {
					News news2 = this.newsService.selectById(news.getId());
					if (news2.getCover() != null && !"".equals(news2.getCover())) {
						FastDFSUtils.deletePic(news2.getCover());
					}
					String cover = FastDFSUtils.uploadPic(uploadFile.getBytes(), uploadFile.getOriginalFilename(),
							uploadFile.getSize());
					news.setCover(IMAGE_BASE_URL + cover);
				}
				news.setUpdateTime(new Date(System.currentTimeMillis()));
				ret = this.newsService.updateById(news);

			} else {
				if (uploadFile != null && StringUtils.isNotEmpty(uploadFile.getOriginalFilename())) {
					String cover = FastDFSUtils.uploadPic(uploadFile.getBytes(), uploadFile.getOriginalFilename(),
							uploadFile.getSize());
					news.setCover(IMAGE_BASE_URL + cover);
				}
				news.setCreateTime(new Date(System.currentTimeMillis()));
				news.setUpdateTime(news.getCreateTime());
				ret = this.newsService.insert(news);
			}
			Integer flag = 1;
			if (ret) {
				flag = 0;
			}

			return ResponseEntity.ok(flag);
		} catch (Exception e) {
			LOGGER.error("增加或修改新闻出错!", e);
		}
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
	}

	// 增加和修改
	@RequestMapping(value = "/check", method = RequestMethod.POST)
	@RequiresPermissions("/news/check")
	public ResponseEntity<Integer> check(News news) {
		try {
			boolean ret;
			if (news.getId() != null) {
				news.setUpdateTime(new Date(System.currentTimeMillis()));
				ret = this.newsService.updateById(news);
			} else {
				news.setCreateTime(new Date(System.currentTimeMillis()));
				news.setUpdateTime(news.getCreateTime());
				ret = this.newsService.insert(news);
			}
			Integer flag = 1;
			if (ret) {
				flag = 0;
			}
			return ResponseEntity.ok(flag);
		} catch (Exception e) {
			LOGGER.error("新闻审核出错!", e);
		}
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
	}

	/**
	 * 删除新闻
	 *
	 * @param newsId
	 * @return
	 */
	@RequestMapping(value = "/delete", method = RequestMethod.DELETE)
	@RequiresPermissions("/news/delete")
	public ResponseEntity<Integer> deleteNews(String newsId) {
		try {
			News news = this.newsService.selectById(Integer.valueOf(newsId));
			if (StringUtils.isNotBlank(news.getCover())) {
				FastDFSUtils.deletePic(news.getCover());
			}
			if (StringUtils.isNotBlank(news.getThumbnail())) {
				FastDFSUtils.deletePic(news.getThumbnail());
			}
			if (StringUtils.isNotBlank(news.getVideoUrl())) {
				FastDFSUtils.deletePic(news.getVideoUrl());
			}

			boolean flag = this.newsService.deleteById(Integer.valueOf(newsId));
			Integer type = 1;
			if (flag) {
				type = 0;
			}
			return ResponseEntity.ok(type);
		} catch (Exception e) {
			LOGGER.error("删除新闻出错!", e);
		}
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
	}

	@RequestMapping(value = "/batchDel", method = RequestMethod.DELETE)
	@RequiresPermissions("/news/batchDel")
	public ResponseEntity<Void> batchDel(String newsIds) {
		try {
			if (StringUtils.isNotBlank(newsIds)) {
				String[] newsIdArr = newsIds.split(",");

				News news;
				for (String newsId : newsIdArr) {
					news = this.newsService.selectById(newsId);
					if (StringUtils.isNotBlank(news.getCover())) {
						FastDFSUtils.deletePic(news.getCover());
					}
					if (StringUtils.isNotBlank(news.getThumbnail())) {
						FastDFSUtils.deletePic(news.getThumbnail());
					}
					if (StringUtils.isNotBlank(news.getVideoUrl())) {
						FastDFSUtils.deletePic(news.getVideoUrl());
					}

					this.newsService.deleteById(newsId);
				}
			} else {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
			}
			return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
		} catch (Exception e) {
			LOGGER.error("删除爆料类型失败!", e);
		}
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
	}

}
