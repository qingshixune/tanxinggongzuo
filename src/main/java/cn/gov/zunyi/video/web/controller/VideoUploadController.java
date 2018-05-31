package cn.gov.zunyi.video.web.controller;

import javax.servlet.http.HttpServletResponse;

import cn.gov.zunyi.video.model.PicUploadResult;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import cn.gov.zunyi.video.common.util.FastDFSUtils;

@RestController
@RequestMapping("/file")
@ConfigurationProperties
public class VideoUploadController {

	private static final Logger log = LoggerFactory.getLogger(VideoUploadController.class);

	@Value("${IMAGE_BASE_URL}")
	private String IMAGE_BASE_URL;

	// 允许上传的格式
	private static final String[] IMAGE_TYPE = new String[] { ".mp4", ".flv", ".mov", ".mkv", ".avi", ".wmv", ".mpg" };

	@RequestMapping(value = "/upload", method = RequestMethod.POST)
	@RequiresPermissions("/file/upload")
	public ResponseEntity<PicUploadResult> upload(
			@RequestParam(value = "bc_file", required = true) MultipartFile uploadFile, HttpServletResponse response)
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
		// 状态
		// fileUploadResult.setError(0);
		if (!isLegal) {
			// 不合法，将磁盘上的文件删除
			FastDFSUtils.deletePic(picUrl);
		}
		return ResponseEntity.status(HttpStatus.CREATED).body(fileUploadResult);
	}

}
