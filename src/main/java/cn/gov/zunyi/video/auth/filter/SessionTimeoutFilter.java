package cn.gov.zunyi.video.auth.filter;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.AccessControlFilter;
import org.apache.shiro.web.util.WebUtils;

import com.alibaba.fastjson.JSON;

public class SessionTimeoutFilter extends AccessControlFilter {

	private String sessionTimeoutUrl;

	public String getSessionTimeoutUrl() {
		return sessionTimeoutUrl;
	}

	public void setSessionTimeoutUrl(String sessionTimeoutUrl) {
		this.sessionTimeoutUrl = sessionTimeoutUrl;
	}

	@Override
	protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue)
			throws Exception {
		return false;
	}

	@Override
	protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
		long sessionCheckStart = System.currentTimeMillis();
		Subject currentUser = getSubject(request, response);
		if (currentUser == null) {
			return false;
		}
		Session session = currentUser.getSession();
		long age = sessionCheckStart - session.getStartTimestamp().getTime();
		final long sessionTimeoutValue = session.getTimeout();
		if (age >= sessionTimeoutValue) {
			HttpServletRequest httpRequest = WebUtils.toHttp(request);
			// 判断是不是Ajax请求
			if (isAjax(httpRequest)) {
				Map<String, String> resultMap = new HashMap<String, String>();
				resultMap.put("status", "400");
				resultMap.put("message", "登录已超时！");
				out(response, resultMap);
			} else {
				System.out.println("timeouttttttttttttttttt     " + age / 1000 / 60);
				// 重定向
				// this.saveRequestAndRedirectToLogin(request, response);
				WebUtils.issueRedirect(request, response, sessionTimeoutUrl);
			}
			return false;
		}
		return true;
	}

	private void out(ServletResponse hresponse, Map<String, String> resultMap) throws IOException {
		try {
//			hresponse.setCharacterEncoding("UTF-8");
			hresponse.setContentType("text/html;charset=UTF-8");//by cp
			PrintWriter out = hresponse.getWriter();
			out.println(JSON.toJSONString(resultMap));
			out.flush();
			out.close();
		} catch (Exception e) {
			System.err.println("SessionTimeoutFilter.class 输出JSON异常，可以忽略。");
		}
	}

	/**
	 * 判断ajax请求
	 *
	 * @param request
	 * @return
	 */
	private boolean isAjax(HttpServletRequest request) {
		return (request.getHeader("X-Requested-With") != null
				&& "XMLHttpRequest".equalsIgnoreCase(request.getHeader("X-Requested-With").toString()));
	}

}
