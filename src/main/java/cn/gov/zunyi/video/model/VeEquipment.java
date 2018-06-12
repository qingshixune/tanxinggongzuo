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
import java.util.List;

/**
 * <p>
 * 视频设备信息
 * </p>
 *
 * @author ZTY
 * @since 2018-5-24
 */
@ApiModel("视频设备信息")
@TableName("ve_equipment")
public class VeEquipment extends Model<VeEquipment> {

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

    @ApiModelProperty(value="设备地址id")
    @TableField("ve_addressid")
    private Integer veAddressid;

    @ApiModelProperty(value="设备详细地址")
    @TableField("ve_specificadds")
    private String veSpecificadds;

    @ApiModelProperty(value="设备来源")
    @TableField("ve_source")
    private String veSource;

    @ApiModelProperty(value="设备预警状态")
    @TableField("ve_warningstus")
    private Integer veWarningstus;

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

    @ApiModelProperty(value="视频源类型id")
    @TableField("ve_typeid")
    private Integer veTypeid;

    @ApiModelProperty(value="设备视频源地址")
    @TableField("video_url")
    private String videoUrl;

    @ApiModelProperty(value="设备描述")
    @TableField("ve_describe")
    private String veDescribe;

    @ApiModelProperty(value="设备地址",hidden = true)
    @TableField(exist = false)
    private String veAddress;

    @ApiModelProperty(value="设备视频源类型",hidden = true)
    @TableField(exist = false)
    private String veType;

    @ApiModelProperty(value="设备工作时长",hidden = true)
    @TableField(exist = false)
    private String velong;

    @ApiModelProperty(value="设备运行情况列表",hidden = true)
    @TableField(exist = false)
    private List<VeEquipmentRunstus> veEquipmentRunstus;

    @ApiModelProperty(hidden = true)
    @TableField(exist = false)
    private List<VeEquipment> veEquipments;

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

    public Integer getVeAddressid() {
        return veAddressid;
    }

    public void setVeAddressid(Integer veAddressid) {
        this.veAddressid = veAddressid;
    }

    public String getVeSpecificadds() {
        return veSpecificadds;
    }

    public void setVeSpecificadds(String veSpecificadds) {
        this.veSpecificadds = veSpecificadds;
    }

    public String getVeSource() {
        return veSource;
    }

    public void setVeSource(String veSource) {
        this.veSource = veSource;
    }

    public Integer getVeWarningstus() {
        return veWarningstus;
    }

    public void setVeWarningstus(Integer veWarningstus) {
        this.veWarningstus = veWarningstus;
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

    public Integer getVeTypeid() {
        return veTypeid;
    }

    public void setVeTypeid(Integer veTypeid) {
        this.veTypeid = veTypeid;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getVeDescribe() {
        return veDescribe;
    }

    public void setVeDescribe(String veDescribe) {
        this.veDescribe = veDescribe;
    }

    public String getVeAddress() {
        return veAddress;
    }

    public void setVeAddress(String veAddress) {
        this.veAddress = veAddress;
    }

    public String getVeType() {
        return veType;
    }

    public void setVeType(String veType) {
        this.veType = veType;
    }

    public String getVelong() {
        return velong;
    }

    public void setVelong(String velong) {
        this.velong = velong;
    }

    public List<VeEquipmentRunstus> getVeEquipmentRunstus() {
        return veEquipmentRunstus;
    }

    public void setVeEquipmentRunstus(List<VeEquipmentRunstus> veEquipmentRunstus) {
        this.veEquipmentRunstus = veEquipmentRunstus;
    }

    public List<VeEquipment> getVeEquipments() {
        return veEquipments;
    }

    public void setVeEquipments(List<VeEquipment> veEquipments) {
        this.veEquipments = veEquipments;
    }

    public VeEquipment() {
    }

    public VeEquipment(String id, Date createTime, Date updateTime, String isDeleted, String veName, Integer veAddressid, String veSpecificadds, String veSource, Integer veWarningstus, Integer veSecuritystus, Integer veRunstus, Double veLatitude, Double veLongitude, Integer veTypeid, String videoUrl, String veDescribe, String veAddress, String veType, String velong, List<VeEquipmentRunstus> veEquipmentRunstus) {
        this.id = id;
        this.createTime = createTime;
        this.updateTime = updateTime;
        this.isDeleted = isDeleted;
        this.veName = veName;
        this.veAddressid = veAddressid;
        this.veSpecificadds = veSpecificadds;
        this.veSource = veSource;
        this.veWarningstus = veWarningstus;
        this.veSecuritystus = veSecuritystus;
        this.veRunstus = veRunstus;
        this.veLatitude = veLatitude;
        this.veLongitude = veLongitude;
        this.veTypeid = veTypeid;
        this.videoUrl = videoUrl;
        this.veDescribe = veDescribe;
        this.veAddress = veAddress;
        this.veType = veType;
        this.velong = velong;
        this.veEquipmentRunstus = veEquipmentRunstus;
    }

    @Override
    public String toString() {
        return "VeEquipment{" +
                "id='" + id + '\'' +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", isDeleted='" + isDeleted + '\'' +
                ", veName='" + veName + '\'' +
                ", veAddressid=" + veAddressid +
                ", veSpecificadds='" + veSpecificadds + '\'' +
                ", veSource='" + veSource + '\'' +
                ", veWarningstus=" + veWarningstus +
                ", veSecuritystus=" + veSecuritystus +
                ", veRunstus=" + veRunstus +
                ", veLatitude=" + veLatitude +
                ", veLongitude=" + veLongitude +
                ", veTypeid=" + veTypeid +
                ", videoUrl='" + videoUrl + '\'' +
                ", veDescribe='" + veDescribe + '\'' +
                ", veAddress='" + veAddress + '\'' +
                ", veType='" + veType + '\'' +
                ", velong='" + velong + '\'' +
                ", veEquipmentRunstus=" + veEquipmentRunstus +
                '}';
    }

    @Override
    protected Serializable pkVal() {
        return id;
    }
}
