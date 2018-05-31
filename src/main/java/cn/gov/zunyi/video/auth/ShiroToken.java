package cn.gov.zunyi.video.auth;

import org.apache.shiro.authc.UsernamePasswordToken;

public class ShiroToken extends UsernamePasswordToken implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	public ShiroToken(String username, String passwd) {
		super(username, passwd);
		this.passwd = passwd;
	}

	/** 登录密码[字符串类型] 因为父类是char[] ] **/
	private String passwd;

	public String getPasswd() {
		return passwd;
	}

	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}

}
