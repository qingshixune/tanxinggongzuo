package cn.gov.zunyi.video.web.controller;

import cn.gov.zunyi.video.model.MsSmsAuth;
import cn.gov.zunyi.video.service.MsSmsAuthService;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import io.swagger.annotations.Api;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Api("验证码")
@RestController
@RequestMapping("/msSmsAuth")
public class MsSmsAuthController extends BaseController {


	@Autowired
	private MsSmsAuthService msSmsAuthService;

	private static final Logger logger = LoggerFactory.getLogger(MsSmsAuthController.class);




	// 根据id获取 信息
	@RequestMapping(value = "/editPage/{Id}", method = RequestMethod.GET)
	@RequiresPermissions("/msSmsAuth/editPage/")
	public ResponseEntity<MsSmsAuth> editPage(@PathVariable(value = "Id", required = true) String Id) {

		try {
			MsSmsAuth msSmsAuth = this.msSmsAuthService.selectById(Id);
			return ResponseEntity.ok(msSmsAuth);
		} catch (Exception e) {
			logger.error("查询出错!", e);
		}
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
	}

	/**
	 * 查询验证码信息
	 *
	 * @return
	 */
	@RequestMapping(value = "/getLogList", method = RequestMethod.GET)
	@RequiresPermissions("/msSmsAuth/getLogList/")
	public ResponseEntity<Page<MsSmsAuth>> queryLogList(EntityWrapper<MsSmsAuth> ew) {
		try {
			Page<MsSmsAuth> page = getPage();
			ew.orderBy("id", false);
			return ResponseEntity.ok(this.msSmsAuthService.selectPage(page, ew));
		} catch (Exception e) {
			logger.error("查询验证码信息!", e);
		}
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
	}





}
