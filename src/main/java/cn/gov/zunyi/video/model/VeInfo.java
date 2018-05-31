package cn.gov.zunyi.video.model;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 视频设备信息
 * </p>
 *
 * @author ZTY
 * @since 2018-5-24
 */
@ApiModel("视频设备信息")
@TableName("ve_info")
public class VeInfo extends Model<VeInfo> {

    @TableId(value="id",type = IdType.UUID)
    private String id;

    @ApiModelProperty(value="创建时间",hidden = true)
    @TableField("create_time")
    private Date createTime;

    @ApiModelProperty(value="更新时间",hidden = true)
    @TableField("update_time")
    private Date updateTime;

    @ApiModelProperty(value="是否删除")
    @TableField("is_deleted")
    private String isDeleted;

    @ApiModelProperty(value="视频设备名称")
    @TableField("ve_name")
    private String veName;

    @ApiModelProperty(value="设备开启时间")
    @TableField("ve_strtime")
    private Date veStrtime;

    @ApiModelProperty(value="设备关闭时间")
    @TableField("ve_endtime")
    private Date veEndtime;

    @ApiModelProperty(value="设备地址")
    @TableField("ve_address")
    private String veAddress;

    @ApiModelProperty(value="设备安全状态")
    @TableField("ve_securitystus")
    private Integer veSecuritystus;

    @ApiModelProperty(value="设备运行状态")
    @TableField("ve_runstus")
    private Integer veRunstus;

    @ApiModelProperty(value="设备纬度")
    @TableField("ve_latitude")
    private Double veLatitude;

    @ApiModelProperty(value="设备经度")
    @TableField("ve_longitude")
    private Double veLongitude;

    @ApiModelProperty(value="设备监控类型")
    @TableField("ve_type")
    private Integer veType;

    @ApiModelProperty(value="设备视频源地址")
    @TableField("video_url")
    private String videoUrl;

    @ApiModelProperty(value="设备工作时长")
    @TableField(exist = false)
    private String velong;

    public String getVelong() {
        return velong;
    }

    public void setVelong(String velong) {
        this.velong = velong;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
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

    public String getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(String isDeleted) {
        this.isDeleted = isDeleted;
    }

    public String getVeName() {
        return veName;
    }

    public void setVeName(String veName) {
        this.veName = veName;
    }

    public Date getVeStrtime() {
        return veStrtime;
    }

    public void setVeStrtime(Date veStrtime) {
        this.veStrtime = veStrtime;
    }

    public Date getVeEndtime() {
        return veEndtime;
    }

    public void setVeEndtime(Date veEndtime) {
        this.veEndtime = veEndtime;
    }

    public String getVeAddress() {
        return veAddress;
    }

    public void setVeAddress(String veAddress) {
        this.veAddress = veAddress;
    }

    public Integer getVeSecuritystus() {
        return veSecuritystus;
    }

    public void setVeSecuritystus(Integer veSecuritystus) {
        this.veSecuritystus = veSecuritystus;
    }

    public Integer getVeRunstus() {
        return veRunstus;
    }

    public void setVeRunstus(Integer veRunstus) {
        this.veRunstus = veRunstus;
    }

    public Double getVeLatitude() {
        return veLatitude;
    }

    public void setVeLatitude(Double veLatitude) {
        this.veLatitude = veLatitude;
    }

    public Double getVeLongitude() {
        return veLongitude;
    }

    public void setVeLongitude(Double veLongitude) {
        this.veLongitude = veLongitude;
    }

    public Integer getVeType() {
        return veType;
    }

    public void setVeType(Integer veType) {
        this.veType = veType;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public VeInfo() {
    }

    public VeInfo(String id, Date createTime, Date updateTime, String isDeleted, String veName, Date veStrtime, Date veEndtime, String veAddress, Integer veSecuritystus, Integer veRunstus, Double veLatitude, Double veLongitude, Integer veType, String videoUrl) {
        this.id = id;
        this.createTime = createTime;
        this.updateTime = updateTime;
        this.isDeleted = isDeleted;
        this.veName = veName;
        this.veStrtime = veStrtime;
        this.veEndtime = veEndtime;
        this.veAddress = veAddress;
        this.veSecuritystus = veSecuritystus;
        this.veRunstus = veRunstus;
        this.veLatitude = veLatitude;
        this.veLongitude = veLongitude;
        this.veType = veType;
        this.videoUrl = videoUrl;
    }

    @Override
    public String toString() {
        return "VeInfo{" +
                "id='" + id + '\'' +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", isDeleted='" + isDeleted + '\'' +
                ", veName='" + veName + '\'' +
                ", veStrtime=" + veStrtime +
                ", veEndtime=" + veEndtime +
                ", veAddress='" + veAddress + '\'' +
                ", veSecuritystus=" + veSecuritystus +
                ", veRunstus=" + veRunstus +
                ", veLatitude=" + veLatitude +
                ", veLongitude=" + veLongitude +
                ", veType=" + veType +
                ", videoUrl='" + videoUrl + '\'' +
                '}';
    }

    @Override
    protected Serializable pkVal() {
        return id;
    }
}
