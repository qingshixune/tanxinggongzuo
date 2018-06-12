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
 * 视频事件
 * </p>
 *
 * @author ZTY
 * @since 2018-5-24
 */
@ApiModel("视频事件表")
@TableName("ve_event")
public class ElevatorEvent extends Model<ElevatorEvent> {

    @TableId(value="id",type = IdType.UUID)
    private String id;

    @ApiModelProperty(value="创建时间",hidden = false)
    @TableField("create_time")
    private Date createTime;

    @ApiModelProperty(value="更新时间",hidden = false)
    @TableField("update_time")
    private Date updateTime;

    @ApiModelProperty(value="是否删除")
    @TableField("is_deleted")
    private String isDeleted;

    @ApiModelProperty(value="电梯事件名称")
    @TableField("event_name")
    private String eventName;

    @ApiModelProperty(value="电梯事件内容")
    @TableField("event_content")
    private String eventContent;

    /**
     * 0：电梯事件，1：交通事件
     */
    @ApiModelProperty(value="事件类型")
    @TableField("event_type")
    private Integer eventType;

    @ApiModelProperty(value="事件上报方式id")
    @TableField("event_methodid")
    private Integer eventMethodid;

    /**
     * 0：智能上报，1：人工上报
     */
    @ApiModelProperty(value="事件上报情况")
    @TableField("event_report")
    private Integer eventReport;

    @ApiModelProperty(value="经度")
    @TableField("event_longitude")
    private String eventLongitude;

    @ApiModelProperty(value="纬度")
    @TableField("event_latitude")
    private String eventLatitude;

    @ApiModelProperty(value="上报人id")
    @TableField("uid")
    private String uid;

    @ApiModelProperty(value="视频设备名字")
    @TableField(exist = false)
    private String veName;

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

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getEventContent() {
        return eventContent;
    }

    public void setEventContent(String eventContent) {
        this.eventContent = eventContent;
    }

    public Integer getEventType() {
        return eventType;
    }

    public void setEventType(Integer eventType) {
        this.eventType = eventType;
    }

    public Integer getEventMethodid() {
        return eventMethodid;
    }

    public void setEventMethodid(Integer eventMethodid) {
        this.eventMethodid = eventMethodid;
    }

    public Integer getEventReport() {
        return eventReport;
    }

    public void setEventReport(Integer eventReport) {
        this.eventReport = eventReport;
    }

    public String getEventLongitude() {
        return eventLongitude;
    }

    public void setEventLongitude(String eventLongitude) {
        this.eventLongitude = eventLongitude;
    }

    public String getEventLatitude() {
        return eventLatitude;
    }

    public void setEventLatitude(String eventLatitude) {
        this.eventLatitude = eventLatitude;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getVeName() {
        return veName;
    }

    public void setVeName(String veName) {
        this.veName = veName;
    }

    @Override
    public String toString() {
        return "ElevatorEvent{" +
                "id='" + id + '\'' +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", isDeleted='" + isDeleted + '\'' +
                ", eventName='" + eventName + '\'' +
                ", eventContent='" + eventContent + '\'' +
                ", eventType=" + eventType +
                ", eventMethodid=" + eventMethodid +
                ", eventReport=" + eventReport +
                ", eventLongitude='" + eventLongitude + '\'' +
                ", eventLatitude='" + eventLatitude + '\'' +
                ", uid='" + uid + '\'' +
                ", veName='" + veName + '\'' +
                '}';
    }

    @Override
    protected Serializable pkVal() {
        return id;
    }
}
