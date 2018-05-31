package cn.gov.zunyi.video.web.controller;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;

import cn.gov.zunyi.video.model.PicUploadResult;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import cn.gov.zunyi.video.common.util.FastDFSUtils;

/**
 * 图片上传
 */
@RestController
@RequestMapping("/pic")
public class PicUploadController {

	private static final Logger log = LoggerFactory.getLogger(PicUploadController.class);

	@Value("${IMAGE_BASE_URL}")
	private String IMAGE_BASE_URL;

	// 允许上传的格式
	private static final String[] IMAGE_TYPE = new String[] { ".bmp", ".jpg", ".jpeg", ".gif", ".png" };

	@RequestMapping(value = "/upload", method = RequestMethod.POST)
	@RequiresPermissions("/pic/upload")
	public ResponseEntity<PicUploadResult> upload(
			@RequestParam(value = "bc_cover", required = true) MultipartFile uploadFile, HttpServletResponse response)
					throws Exception {
		// 校验文件扩展名
		boolean isLegal = false;
		for (String type : IMAGE_TYPE) {
			if (StringUtils.endsWithIgnoreCase(uploadFile.getOriginalFilename(), type)) {
				isLegal = true;
				break;
			}
		}
		// 封装Result对象，并且将文件的byte数组放置到result对象中
		PicUploadResult fileUploadResult = new PicUploadResult();
		// 状态
		// fileUploadResult.setError(isLegal ? 0 : 1);
		// 文件新路径
		String filePath = FastDFSUtils.uploadPic(uploadFile.getBytes(), uploadFile.getOriginalFilename(),
				uploadFile.getSize());

		if (log.isDebugEnabled()) {
			log.debug("Pic file upload .[{}] to [{}] .", uploadFile.getOriginalFilename(), filePath);
		}
		String picUrl = IMAGE_BASE_URL + filePath;
		fileUploadResult.setUrl(picUrl);
		// 校验图片是否合法
		isLegal = false;
		try {
			BufferedImage image = ImageIO.read(new URL(picUrl));
			if (image != null) {
				// fileUploadResult.setWidth(image.getWidth() + "");
				// fileUploadResult.setHeight(image.getHeight() + "");
				isLegal = true;
			}
		} catch (IOException e) {
			log.error("check image is legal error! " + e.getMessage());
		}
		isLegal = true;
		// 状态
		// fileUploadResult.setError(isLegal ? 0 : 1);
		if (!isLegal) {
			// 不合法，将磁盘上的文件删除
			FastDFSUtils.deletePic(picUrl);
		}
		return ResponseEntity.status(HttpStatus.CREATED).body(fileUploadResult);
	}


	@RequestMapping(value = "/uploadFile", method = RequestMethod.POST)
		@RequiresPermissions("/pic/uploadFile")
	public ResponseEntity<PicUploadResult> uploadFile(
			@RequestParam(value = "bc_cover", required = true) MultipartFile uploadFile, HttpServletResponse response)
			throws Exception {

		// 封装Result对象，并且将文件的byte数组放置到result对象中
		PicUploadResult fileUploadResult = new PicUploadResult();
		if(uploadFile.getSize()/(1024*1024)>50){
			//50兆
			log.error("附件大小超过50兆！请重新上传 " );
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		};
		// 文件新路径
		String filePath = FastDFSUtils.uploadPic(uploadFile.getBytes(), uploadFile.getOriginalFilename(),
				uploadFile.getSize());
		if (log.isDebugEnabled()) {
			log.debug("Pic file upload .[{}] to [{}] .", uploadFile.getOriginalFilename(), filePath);
		}
		String picUrl = IMAGE_BASE_URL + filePath;
		fileUploadResult.setUrl(picUrl);
		return ResponseEntity.status(HttpStatus.CREATED).body(fileUploadResult);
	}


}
