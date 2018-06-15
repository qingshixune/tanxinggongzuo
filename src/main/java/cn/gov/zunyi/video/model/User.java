package cn.gov.zunyi.video.model;

import java.io.Serializable;
import java.util.Date;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import org.crazycake.shiro.AuthCachePrincipal;

@TableName("tb_user")
public class User extends Model<User> implements Serializable, AuthCachePrincipal {

    private static final long serialVersionUID = 1L;

    public User() {
    }

    public User(String username, String realname, String mobile, String email, Integer userType, Integer roleId, Date createTime, Date updateTime, String avatar, Integer sex, String province, String city, String district, boolean enabled, String registIp, Date registTime, String lastLoginIp, Date lastLoginTime, Integer totalLoginCounts, String clubName, String managerName, String introduction, String post, String logo, Role role) {
        this.username = username;
        this.realname = realname;
        this.mobile = mobile;
        this.email = email;
        this.userType = userType;
        this.roleId = roleId;
        this.createTime = createTime;
        this.updateTime = updateTime;
        this.avatar = avatar;
        this.sex = sex;
        this.province = province;
        this.city = city;
        this.district = district;
        this.enabled = enabled;
        this.registIp = registIp;
        this.registTime = registTime;
        this.lastLoginIp = lastLoginIp;
        this.lastLoginTime = lastLoginTime;
        this.totalLoginCounts = totalLoginCounts;
        this.clubName = clubName;
        this.managerName = managerName;
        this.introduction = introduction;
        this.post = post;
        this.logo = logo;
        this.role = role;
    }

    /**
     * 用户标识
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 用户名
     */
    private String username;

    /**
     * 真实姓名
     */
    private String realname;
    /**
     * 手机号
     */
    private String mobile;
    /**
     * 邮箱
     */
    private String email;
    /**
     * 用户类型(2:管理员,1:个人,0:俱乐部,微信用户为3)
     */
    @TableField("user_type")
    private Integer userType;
    @TableField("role_id")
    private Integer roleId;
    /**
     * 密码
     */
    @JSONField(serialize = false)
    @TableField(exist = false)
    private String password;

    /**
     * 密码
     */
    @JSONField(serialize = false)
    @TableField("password_hash")
    private byte[] passwordHash;

    @JSONField(serialize = false)
    @TableField("password_salt")
    private byte[] passwordSalt;
    @TableField("create_time")
    private Date createTime;
    @TableField("update_time")
    private Date updateTime;
    /**
     * 个人头像
     */
    private String avatar;
    /**
     * 默认：男 1是男  0是女
     */
    private Integer sex;

    private String province;//省份

    private String city;  //城市

    private String district; //区
    /**
     * 状态： 1 正常 0 逻辑删除
     */
    private Boolean enabled;

    @TableField(exist = false)
    private String rolename;

    /**
     * 状态： 1 启用0  警用
     */
    @TableField("is_live")
    private Integer isLive;



    @TableField("regist_ip")
    private String registIp; 
    @TableField("regist_time")
    private Date registTime;  //注册时间
    @TableField("last_login_ip")//最后登录ip
    private String lastLoginIp;
    @TableField("last_login_time")
    private Date lastLoginTime;//最后登录时间
    @TableField("total_login_counts")
    private Integer totalLoginCounts; //登录次数
    /**
     * 俱乐部名称
     */
    @TableField("CLUB_NAME")
    private String clubName;
    /**
     * 俱乐部管理员姓名
     */
    @TableField("MANAGER_NAME")
    private String managerName;
    /**
     * 信息说明
     */
    private String introduction;
    /**
     * 职务
     */
    private String post;
    /**
     * 公司LOGO
     */
    private String logo;

    @TableField(exist = false)
    private Role role;

   /**
    * ztw添加字段
    */
    @TableField("OPEN_ID")
    private String openId; //微信openId
    
    @TableField("CERTIFICATES_ID")
	private String certificatesId;//骑手 的 证件号码
    
    
    public String getCertificatesId() {
		return certificatesId;
	}

	public void setCertificatesId(String certificatesId) {
		this.certificatesId = certificatesId;
	}

	public String getOpenId() {
		return openId;
	}

	public void setOpenId(String openId) {
		this.openId = openId;
	}

	public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRealname() {
        return realname;
    }

    public void setRealname(String realname) {
        this.realname = realname;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getUserType() {
        return userType;
    }

    public void setUserType(Integer userType) {
        this.userType = userType;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public byte[] getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(byte[] passwordHash) {
        this.passwordHash = passwordHash;
    }

    public byte[] getPasswordSalt() {
        return passwordSalt;
    }

    public void setPasswordSalt(byte[] passwordSalt) {
        this.passwordSalt = passwordSalt;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public String getRegistIp() {
        return registIp;
    }

    public void setRegistIp(String registIp) {
        this.registIp = registIp;
    }

    public Date getRegistTime() {
        return registTime;
    }

    public void setRegistTime(Date registTime) {
        this.registTime = registTime;
    }

    public String getLastLoginIp() {
        return lastLoginIp;
    }

    public void setLastLoginIp(String lastLoginIp) {
        this.lastLoginIp = lastLoginIp;
    }

    public Date getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(Date lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public Integer getTotalLoginCounts() {
        return totalLoginCounts;
    }

    public void setTotalLoginCounts(Integer totalLoginCounts) {
        this.totalLoginCounts = totalLoginCounts;
    }

    public String getClubName() {
        return clubName;
    }

    public void setClubName(String clubName) {
        this.clubName = clubName;
    }

    public String getManagerName() {
        return managerName;
    }

    public void setManagerName(String managerName) {
        this.managerName = managerName;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
    public Integer getIsLive() {
        return isLive;
    }

    public String getRolename() {
        return rolename;
    }

    public void setRolename(String rolename) {
        this.rolename = rolename;
    }

    public void setIsLive(Integer isLive) {
        this.isLive = isLive;
    }
    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return this.username;
    }

    @Override
    public String getAuthCacheKey() {
        return getUsername();
    }

}
