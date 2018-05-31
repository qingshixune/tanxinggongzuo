package cn.gov.zunyi.video.web.controller;

import cn.gov.zunyi.video.model.MsSysLog;
import cn.gov.zunyi.video.service.MsSysLogService;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

@Api("系统登录日志操作日志等")
@RestController
@RequestMapping("/msSysLog")
public class MsSysLogController extends BaseController {


	@Autowired
	private MsSysLogService msSysLogService;

	private static final Logger logger = LoggerFactory.getLogger(MsSysLogController.class);

	@Value("${IMAGE_BASE_URL}")
	private String IMAGE_BASE_URL;



	/**
	 * 查询日志列表
	 *
	 * @return
	 */
	@RequestMapping(value = "/getLogList", method = RequestMethod.GET)
		@RequiresPermissions("/msSysLog/getLogList")
	public ResponseEntity<Page<MsSysLog>> queryLogList(EntityWrapper<MsSysLog> ew, String loginName, String startTime, String endTime) {
		try {
			Page<MsSysLog> page = getPage();
			ew.orderBy("id", false);
			ew.like("login_name", loginName);

			if (StringUtils.isNoneBlank(startTime)) {
				String startDate = startTime + " 00:00:00";
				String endDate = endTime + " 23:59:59";
				ew.between("create_time", startDate, endDate);
			}
			return ResponseEntity.ok(this.msSysLogService.selectPage(page, ew));
		} catch (Exception e) {
			logger.error("查询日志列表出错!", e);
		}
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
	}

	@ApiOperation(value = "查看系统日志情况", notes = "查看系统日志情况")

	@RequestMapping(value = "/getMsSysLog", method = RequestMethod.GET)
		@RequiresPermissions("/msSysLog/getMsSysLog")
	public ResponseEntity<MsSysLog> getLog(EntityWrapper<MsSysLog> ew) {
		try {
			ew.orderBy("id", false);
			List<MsSysLog> list = this.msSysLogService.selectList(ew);
			if (list != null & list.size() > 0) {
				return ResponseEntity.ok(list.get(0));
			}
			return ResponseEntity.ok(null);
		} catch (Exception e) {
			logger.error("查询用户列表出错!", e);
		}
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
	}

	// 编辑 日志信息
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	@RequiresPermissions("/msSysLog/add")
	public ResponseEntity<MsSysLog> add(MsSysLog msSysLog) {
		try {

			boolean ret = false;
			msSysLog.setCreateTime(new Date(System.currentTimeMillis()));
			ret = this.msSysLogService.insert(msSysLog);
			if (!ret) {
				// 更新失败, 500
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
			}

			return ResponseEntity.ok(msSysLog);
		} catch (Exception e) {
			logger.error("更新日志错误!", e);
		}
		// 500
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
	}

//	// 编系统日志 编辑
//	@RequestMapping(value = "/edit", method = RequestMethod.PUT)
////	@RequiresPermissions("/log/edit")
//	public ResponseEntity<Void> edit(Log log) {
//		try {
//			boolean ret = false;
//			if (log.getId() != null) {
//				ret = this.msSysLogService.updateById(log);
//			} else {
//				// 更新失败, 400
//				return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
//			}
//			if (!ret) {
//				// 更新失败, 500
//				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
//			}
//			// 204
//
//			return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
//		} catch (Exception e) {
//			logger.error("更新日志错误!", e);
//		}
//		// 500
//		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
//	}


	/**
	 * 删除日志
	 *
	 * @param logId
	 * @return
	 */
	@RequestMapping(value = "/delete", method = RequestMethod.DELETE)
	@RequiresPermissions("/msSysLog/delete")
	public ResponseEntity<Void> deleteAbout(String logId) {
		try {
			boolean ret = this.msSysLogService.deleteById(Integer.valueOf(logId));
			if (!ret) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
			}
			return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
		} catch (Exception e) {
			logger.error("删除日志出错!", e);
		}
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
	}
//	/**
//	 * 批量删除日志
//	 *
//	 * @param logIds
//	 * @return
//	 */
//	@RequestMapping(value = "/batchDel", method = RequestMethod.DELETE)
//	@RequiresPermissions("/log/batchDel")
//	public ResponseEntity<Void> batchDel(String logIds) {
//		try {
//			boolean ret = false;
//			if (StringUtils.isNotBlank(logIds)) {
//				ret = this.msSysLogService.deleteBatchIds(Arrays.asList(StringUtils.split(logIds, ",")));
//			} else {
//				return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
//			}
//			if (!ret) {
//				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
//			}
//			return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
//		} catch (Exception e) {
//			logger.error("删除日志失败!", e);
//		}
//		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
//	}

}
