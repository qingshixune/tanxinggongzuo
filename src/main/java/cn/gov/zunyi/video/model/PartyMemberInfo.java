package cn.gov.zunyi.video.model;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;

import java.io.Serializable;
import java.util.Date;

@TableName("partymember_info")
public class PartyMemberInfo extends Model<PartyMemberInfo> {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @TableField("create_time")
    private Date createTime;

    @TableField("update_time")
    private Date updateTime;

    /**
     * 姓名
     * */
    private String name;

    /**
     * 曾用名
     * */
    @TableField("used_name")
    private String usedName;

    /**
     * 所属党支部ID
     * */
    @TableField("partybranch_id")
    private Integer partybranchId;

    /**
     * 身份证号
     * */
    @TableField("id_number")
    private String idNumber;

    /**
     * 性别 男:1；女：0
     * */
    private Integer sex;

    /**
     * 年龄
     * */
    private Integer age;

    /**
     * 积分
     * */
    private Integer integral;

    /**
     * 手机号
     * */
    private String mobile;

    /**
     * 电子邮箱
     * */
    private String email;

    /**
     * 个人头像
     * */
    private String avatar;

    /**
     * 省份
     * */
    private String province;

    /**
     * 城市
     * */
    private String city;

    /**
     * 区
     * */
    private String district;

    /**
     * 状态：1正常  0 逻辑删除
     * */
    private Integer enabled;

    /**
     * 职务
     * */
    private String post;

    /**
     * 备注
     * */
    private String note;

    /**
     * 所属党支部名称 虚拟字段
     * */
    @TableField(exist = false)
    private String partybranchname;

    /**
     * 男生人数 虚拟字段
     * */
    @TableField(exist = false)
    private String mancount;

    /**
     * 男生比例 虚拟字段
     * */
    @TableField(exist = false)
    private String manproportion;

    /**
     * 女生人数 虚拟字段
     * */
    @TableField(exist = false)
    private String womancount;

    /**
     * 女生比例 虚拟字段
     * */
    @TableField(exist = false)
    private String womanproportion;

    @TableField(exist = false)
    private String onecount;

    @TableField(exist = false)
    private String twocount;

    @TableField(exist = false)
    private String threecount;

    @TableField(exist = false)
    private String fourcount;

    @TableField(exist = false)
    private String fivecount;

    @TableField(exist = false)
    private String sixcount;

    public String getMancount() {
        return mancount;
    }

    public void setMancount(String mancount) {
        this.mancount = mancount;
    }

    public String getManproportion() {
        return manproportion;
    }

    public void setManproportion(String manproportion) {
        this.manproportion = manproportion;
    }

    public String getWomancount() {
        return womancount;
    }

    public void setWomancount(String womancount) {
        this.womancount = womancount;
    }

    public String getWomanproportion() {
        return womanproportion;
    }

    public void setWomanproportion(String womanproportion) {
        this.womanproportion = womanproportion;
    }

    public String getOnecount() {
        return onecount;
    }

    public void setOnecount(String onecount) {
        this.onecount = onecount;
    }

    public String getTwocount() {
        return twocount;
    }

    public void setTwocount(String twocount) {
        this.twocount = twocount;
    }

    public String getThreecount() {
        return threecount;
    }

    public void setThreecount(String threecount) {
        this.threecount = threecount;
    }

    public String getFourcount() {
        return fourcount;
    }

    public void setFourcount(String fourcount) {
        this.fourcount = fourcount;
    }

    public String getFivecount() {
        return fivecount;
    }

    public void setFivecount(String fivecount) {
        this.fivecount = fivecount;
    }

    public String getSixcount() {
        return sixcount;
    }

    public void setSixcount(String sixcount) {
        this.sixcount = sixcount;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsedName() {
        return usedName;
    }

    public void setUsedName(String usedName) {
        this.usedName = usedName;
    }

    public Integer getPartybranchId() {
        return partybranchId;
    }

    public void setPartybranchId(Integer partybranchId) {
        this.partybranchId = partybranchId;
    }

    public String getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Integer getIntegral() {
        return integral;
    }

    public void setIntegral(Integer integral) {
        this.integral = integral;
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

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
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

    public Integer getEnabled() {
        return enabled;
    }

    public void setEnabled(Integer enabled) {
        this.enabled = enabled;
    }

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getPartybranchname() {
        return partybranchname;
    }

    public void setPartybranchname(String partybranchname) {
        this.partybranchname = partybranchname;
    }

    public PartyMemberInfo(Integer id, Date createTime, Date updateTime, String name, String usedName, Integer partybranchId, String idNumber, Integer sex, Integer age, Integer integral, String mobile, String email, String avatar, String province, String city, String district, Integer enabled, String post, String note) {
        this.id = id;
        this.createTime = createTime;
        this.updateTime = updateTime;
        this.name = name;
        this.usedName = usedName;
        this.partybranchId = partybranchId;
        this.idNumber = idNumber;
        this.sex = sex;
        this.age = age;
        this.integral = integral;
        this.mobile = mobile;
        this.email = email;
        this.avatar = avatar;
        this.province = province;
        this.city = city;
        this.district = district;
        this.enabled = enabled;
        this.post = post;
        this.note = note;
    }

    public PartyMemberInfo(){

    }

    @Override
    public String toString() {
        return this.name;
    }
}
