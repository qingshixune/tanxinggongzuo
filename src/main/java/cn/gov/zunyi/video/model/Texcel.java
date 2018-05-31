package cn.gov.zunyi.video.model;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;

import java.io.Serializable;
import java.util.Date;

@TableName("text_excel")
public class Texcel extends Model<Texcel> {

    private Integer id;

    @TableField("pb_name")
    private String pbName;

    @TableField("pb_secretary")
    private String pbSecretary;

    @TableField("pb_linkman")
    private String pbLinkman;

    @TableField("link_phone")
    private String linkPhone;

    @TableField("pb_type")
    private String pbType;

    @TableField("unit_type")
    private String unitType;

    @TableField("unit_address")
    private String unitAddress;

    @TableField("pm_number")
    private Integer pmNumber;

    @TableField("pc_number")
    private Integer pcNumber;

    private Integer totalcount;

    @TableField("barnch_number")
    private Integer barnchNumber;

    @TableField("unit_phone")
    private String unitPhone;

    private Integer pid;

    private Double longitude;

    private Double latitude;

    @TableField("create_time")
    private Date createTime;

    @TableField("update_time")
    private Date update_time;

    private String note;

    public Texcel() {
    }

    public Texcel(Integer id, String pbName, String pbSecretary, String pbLinkman, String linkPhone, String pbType, String unitType, String unitAddress, Integer pmNumber, Integer pcNumber, Integer totalcount, Integer barnchNumber, String unitPhone, Integer pid, Double longitude, Double latitude, Date createTime, Date update_time, String note) {
        this.id = id;
        this.pbName = pbName;
        this.pbSecretary = pbSecretary;
        this.pbLinkman = pbLinkman;
        this.linkPhone = linkPhone;
        this.pbType = pbType;
        this.unitType = unitType;
        this.unitAddress = unitAddress;
        this.pmNumber = pmNumber;
        this.pcNumber = pcNumber;
        this.totalcount = totalcount;
        this.barnchNumber = barnchNumber;
        this.unitPhone = unitPhone;
        this.pid = pid;
        this.longitude = longitude;
        this.latitude = latitude;
        this.createTime = createTime;
        this.update_time = update_time;
        this.note = note;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPbName() {
        return pbName;
    }

    public void setPbName(String pbName) {
        this.pbName = pbName;
    }

    public String getPbSecretary() {
        return pbSecretary;
    }

    public void setPbSecretary(String pbSecretary) {
        this.pbSecretary = pbSecretary;
    }

    public String getPbLinkman() {
        return pbLinkman;
    }

    public void setPbLinkman(String pbLinkman) {
        this.pbLinkman = pbLinkman;
    }

    public String getLinkPhone() {
        return linkPhone;
    }

    public void setLinkPhone(String linkPhone) {
        this.linkPhone = linkPhone;
    }

    public String getPbType() {
        return pbType;
    }

    public void setPbType(String pbType) {
        this.pbType = pbType;
    }

    public String getUnitType() {
        return unitType;
    }

    public void setUnitType(String unitType) {
        this.unitType = unitType;
    }

    public String getUnitAddress() {
        return unitAddress;
    }

    public void setUnitAddress(String unitAddress) {
        this.unitAddress = unitAddress;
    }

    public Integer getPmNumber() {
        return pmNumber;
    }

    public void setPmNumber(Integer pmNumber) {
        this.pmNumber = pmNumber;
    }

    public Integer getPcNumber() {
        return pcNumber;
    }

    public void setPcNumber(Integer pcNumber) {
        this.pcNumber = pcNumber;
    }

    public Integer getTotalcount() {
        return totalcount;
    }

    public void setTotalcount(Integer totalcount) {
        this.totalcount = totalcount;
    }

    public Integer getBarnchNumber() {
        return barnchNumber;
    }

    public void setBarnchNumber(Integer barnchNumber) {
        this.barnchNumber = barnchNumber;
    }

    public String getUnitPhone() {
        return unitPhone;
    }

    public void setUnitPhone(String unitPhone) {
        this.unitPhone = unitPhone;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(Date update_time) {
        this.update_time = update_time;
    }

    public Integer getPid() {
        return pid;
    }

    public void setPid(Integer pid) {
        this.pid = pid;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    @Override
    protected Serializable pkVal() {
        return null;
    }
}
