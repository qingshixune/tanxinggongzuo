package cn.gov.zunyi.video.web.controller;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import cn.gov.zunyi.video.model.User;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.session.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import cn.gov.zunyi.video.service.MsSmsAuthService;
import cn.gov.zunyi.video.service.UserService;
import cn.gov.zunyi.video.web.checkcode.Captcha;
import cn.gov.zunyi.video.web.checkcode.SpecCaptcha;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;

/**
 * shiro权限控制登录Controller
 */
@RestController
public class LoginController {
	// 短信发送端接口
	@Autowired
	private MsSmsAuthService msSmsAuthService;

	@Autowired
	private UserService userService;

	private static final Logger logger = LoggerFactory
			.getLogger(LoginController.class);

	// // 首页
	// @RequestMapping(value = "index", method = RequestMethod.GET)
	// @RequiresPermissions("/index")
	// public ModelAndView index() {
	// return new ModelAndView("redirect:/#/login");
	//
	// // return new ModelAndView("redirect:/echarts/user");
	// }

	// // 登录
	// @RequestMapping(value = "/#/login", method = RequestMethod.GET)
	// public ModelAndView login() {
	// ModelAndView mv = new ModelAndView("redirect:/#/backLogin");
	// return mv;
	//
	// }

	// // 权限测试用
	// @RequestMapping(value = "add", method = RequestMethod.GET)
	// public ModelAndView add() {
	// ModelAndView mv = new ModelAndView("add");
	// return mv;
	// }

	// // 未授权跳转的页面
	// @RequestMapping(value = "403", method = RequestMethod.GET)
	// public ModelAndView noPermissions() {
	// ModelAndView mv = new ModelAndView("403");
	// return mv;
	// }


	// session过期
	@RequestMapping(value = "sessionOut", method = RequestMethod.GET)
	public ResponseEntity<String> sessionOut() {
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("sessionTimeOut");

	}

	// 踢出用户
	@RequestMapping(value = "kickouting", method = RequestMethod.GET)
	@RequiresPermissions("/kickouting")
	public ResponseEntity<String> kickouting() {
		return ResponseEntity.ok("kickout");
	}

	// 被踢出后跳转的页面
	@RequestMapping(value = "kickout", method = RequestMethod.GET)
	public ModelAndView kickout() {
		ModelAndView mv = new ModelAndView("kickout");
		return mv;
	}

	/**
	 * 前台ajax登录请求
	 *
	 * @param username
	 * @param password
	 * @return
	 */

	@RequestMapping(value = "culbAjaxLogin", method = RequestMethod.POST)
	public ResponseEntity<Map<String, Object>> culbAjaxLogin(
			@RequestParam("username") String username,
			@RequestParam("password") String password,
			// @RequestParam("vcode") String vcode,
			@RequestParam("rememberMe") Boolean rememberMe) {
		rememberMe=false;//不记住密码  记住密码有session 上的冲突    需要改前端登录传递的方法
		Map<String, Object> resultMap = new LinkedHashMap<String, Object>();
		User user = null;
		if (StringUtils.isNoneBlank(username)) {
			Wrapper<User> UserWrapper = new EntityWrapper<>();
			UserWrapper.eq("username", username);
			user = userService.selectOne(UserWrapper);
			if (user == null) {
				resultMap.put("status", 400);
				resultMap.put("message", "您的账户不存在，请联系管理员！");
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
						.body(resultMap);
			}

			if (user != null
					&& (user.getUserType() == 1 || user.getUserType() == 0)) {
				// 是俱乐部或者是个人用户
				// 是俱乐部或者是个人用户
				if (user.getEnabled() == null || !user.getEnabled()) {
					resultMap.put("status", 400);
					resultMap.put("message", "您的账户已停用！请联系管理员");
					return ResponseEntity.status(
							HttpStatus.INTERNAL_SERVER_ERROR).body(resultMap);
				}
			} else {
				resultMap.put("status", 400);
				resultMap.put("message", "您的用户权限不足！请联系管理员");
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
						.body(resultMap);
			}

		}

		Session session = SecurityUtils.getSubject().getSession();
		try {
			UsernamePasswordToken token = new UsernamePasswordToken(username,
					password, rememberMe);
			SecurityUtils.getSubject().login(token);
			session.setAttribute("loginName", username);
			resultMap.put("user", user);
			resultMap.put("status", 201);
			resultMap.put("message", "登录成功");
			return ResponseEntity.status(HttpStatus.CREATED).body(resultMap);
		} catch (Exception e) {
			resultMap.put("status", 500);
			resultMap.put("message", e.getMessage());
		}
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
				resultMap);
	}

	/**
	 * ajax登录请求 后端用户登录
	 *
	 * @param username
	 * @param password
	 * @return
	 */
	@RequestMapping(value = "ajaxLogin", method = RequestMethod.POST)
	public ResponseEntity<Map<String, Object>> submitLogin(
			@RequestParam("username") String username,
			@RequestParam("password") String password,
			// @RequestParam("vcode") String vcode,
			@RequestParam("rememberMe") Boolean rememberMe) {
		Map<String, Object> resultMap = new LinkedHashMap<String, Object>();
		rememberMe=false;//不记住密码  记住密码有session 上的冲突    需要改前端登录传递的方法
		if (StringUtils.isNoneBlank(username)) {
			Wrapper<User> UserWrapper = new EntityWrapper<>();
			UserWrapper.eq("username", username);
			User user = userService.selectOne(UserWrapper);

			if (user == null) {
				resultMap.put("status", 400);
				resultMap.put("message", "您的账户不存在，请联系管理员！");
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
						.body(resultMap);
			}

			if (user != null && user.getUserType() == 2) {
				// 是俱乐部或者是个人用户
				if (user.getEnabled() == null || !user.getEnabled()) {
					resultMap.put("status", 400);
					resultMap.put("message", "您的账户已停用！请联系管理员");
					return ResponseEntity.status(
							HttpStatus.INTERNAL_SERVER_ERROR).body(resultMap);
				}
			} else {
				resultMap.put("status", 400);
				resultMap.put("message", "您的用户权限不足！请联系管理员");
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
						.body(resultMap);
			}

		}

		Session session = SecurityUtils.getSubject().getSession();

		/*
		 * if (vcode == null || vcode == "") { resultMap.put("status", 400);
		 * resultMap.put("message", "验证码不能为空！"); return
		 * ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resultMap); }
		 * 
		 * // 转化成小写字母 vcode = vcode.toLowerCase(); String v = (String)
		 * session.getAttribute("_code"); // 还可以读取一次后把验证码清空，这样每次登录都必须获取验证码 //
		 * session.removeAttribute("_come"); if (!vcode.equals(v)) {
		 * resultMap.put("status", 400); resultMap.put("message", "验证码输入有误！");
		 * return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resultMap);
		 * }
		 */

		try {
			UsernamePasswordToken token = new UsernamePasswordToken(username,
					password, rememberMe);
			SecurityUtils.getSubject().login(token);
			session.setAttribute("loginName", username);
			resultMap.put("status", 201);
			resultMap.put("message", "登录成功");
			return ResponseEntity.status(HttpStatus.CREATED).body(resultMap);
		} catch (Exception e) {
			resultMap.put("status", 500);
			resultMap.put("message", e.getMessage());
		}
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
				resultMap);
	}

	/**
	 * 退出
	 *
	 * @return
	 */
	@RequestMapping(value = "userLogout", method = RequestMethod.GET)
	public ResponseEntity<Void> userLogout() {
		try {
			User user = (User) SecurityUtils.getSubject().getPrincipal();
          if(user!=null){
			  // 退出
			  SecurityUtils.getSubject().logout();
		  }

			return ResponseEntity.status(HttpStatus.OK).build();
		} catch (Exception e) {
			logger.error("注销错误！", "用户已经被注销");
		}
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
	}

	/**
	 * 验证码 俱乐部用户修改密码 短信验证 String phone,String code,String type
	 * 
	 * @param
	 */
	@RequestMapping(value = "sendUpdataPass", method = RequestMethod.GET)
	public ResponseEntity<String> sendUpdataPass(
			@RequestParam("phone") String phone) {
		try {

			Wrapper<User> wrapperHis = new EntityWrapper<User>();
			wrapperHis.eq("enabled", 1);
			wrapperHis.eq("is_live", 1);
			wrapperHis.eq("mobile", phone.trim());

			User user1= this.userService.selectOne(wrapperHis);
            if(user1==null){
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("该手机号不存在!");
			}

			if (StringUtils.isNoneBlank(phone)
					&& this.msSmsAuthService.getSendDate(phone.trim(),
							MsSmsAuthService.UPDATE_PASS)) {
				boolean ret = false;
				ret = this.msSmsAuthService.sendPhoneMessage(phone.trim(),
						MsSmsAuthService.UPDATE_PASS, "");
				if (ret) {
					return ResponseEntity.status(HttpStatus.OK).build();
				}
			}
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("发送验证码失败！");
		} catch (Exception e) {
			System.err.println("获取短信验证异常：" + e.getMessage());
		}
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("发送验证码失败！");

	}

	/**
	 * 微信认证发送短信验证 String phone,String code,String type
	 * 
	 * @param
	 */
	@RequestMapping(value = "sendSmsAutho", method = RequestMethod.GET)
	public ResponseEntity<Void> sendSmsAutho(
			@RequestParam("phoneNum") String phone) {
		try {
			if (StringUtils.isNoneBlank(phone)
					&& this.msSmsAuthService.getSendDate(phone.trim(),
							MsSmsAuthService.WX_REGISTER)) {
				boolean ret = false;
				ret = this.msSmsAuthService.sendPhoneMessage(phone.trim(),
						MsSmsAuthService.WX_REGISTER, "");
				if (ret) {
					return ResponseEntity.status(HttpStatus.OK).build();
				}
			}
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();

		} catch (Exception e) {
			System.err.println("获取验证码异常：" + e.getMessage());
		}
		return ResponseEntity.status(HttpStatus.OK).build();
	}

	/**
	 * 获取验证码（Gif版本）
	 *
	 * @param response
	 */
	@RequestMapping(value = "getGifCode", method = RequestMethod.GET)
	public ResponseEntity<Void> getGifCode(HttpServletResponse response,
			HttpServletRequest request) {
		try {
			response.setHeader("Pragma", "no-cache");
			response.setHeader("Cache-Control", "max-age=0");
			response.setDateHeader("Expires", -1L);
			response.setContentType("image/jpeg");
			/**
			 * gif格式动画验证码 宽，高，位数。
			 */
			Captcha captcha = new SpecCaptcha(146, 33, 4);
			// 输出
			captcha.out(response.getOutputStream());
			HttpSession session = request.getSession(true);
			// 存入Session
			session.setAttribute("_code", captcha.text().toLowerCase());
		} catch (Exception e) {
			System.err.println("获取验证码异常：" + e.getMessage());
		}
		return ResponseEntity.status(HttpStatus.OK).build();
	}

}
