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
 * 电梯事件
 * </p>
 *
 * @author ZTY
 * @since 2018-5-24
 */
@ApiModel("电梯事件表")
@TableName("elevator_event")
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

    @ApiModelProperty(value="电梯事件发生时间")
    @TableField("ee_occurtime")
    private String eeOccurtime;

    @ApiModelProperty(value="电梯事件结束时间")
    @TableField("ee_endtime")
    private String eeEndtime;

    @ApiModelProperty(value="电梯事件设备id")
    @TableField("ee_veid")
    private String eeVeid;

    @ApiModelProperty(value="电梯事件发生状态")
    @TableField("ee_occurstus")
    private Integer eeOccurstus;

    @ApiModelProperty(value="电梯事件紧急状态")
    @TableField("ee_exigrncystus")
    private Integer eeExigrncystus;

    @ApiModelProperty(value="电梯事件事故状态")
    @TableField("ee_accident")
    private Integer eeAccident;

    @ApiModelProperty(value="电梯事件标题")
    @TableField("ee_title")
    private String eeTitle;

    @ApiModelProperty(value="电梯事件内容")
    @TableField("ee_content")
    private String eeContent;

    @ApiModelProperty(value="视频设备名字")
    @TableField(exist = false)
    private String veName;

    public String getVeName() {
        return veName;
    }

    public void setVeName(String veName) {
        this.veName = veName;
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

    public String getEeOccurtime() {
        return eeOccurtime;
    }

    public void setEeOccurtime(String eeOccurtime) {
        this.eeOccurtime = eeOccurtime;
    }

    public String getEeEndtime() {
        return eeEndtime;
    }

    public void setEeEndtime(String eeEndtime) {
        this.eeEndtime = eeEndtime;
    }

    public Integer getEeOccurstus() {
        return eeOccurstus;
    }

    public void setEeOccurstus(Integer eeOccurstus) {
        this.eeOccurstus = eeOccurstus;
    }

    public Integer getEeExigrncystus() {
        return eeExigrncystus;
    }

    public void setEeExigrncystus(Integer eeExigrncystus) {
        this.eeExigrncystus = eeExigrncystus;
    }

    public Integer getEeAccident() {
        return eeAccident;
    }

    public void setEeAccident(Integer eeAccident) {
        this.eeAccident = eeAccident;
    }

    public String getEeVeid() {
        return eeVeid;
    }

    public void setEeVeid(String eeVeid) {
        this.eeVeid = eeVeid;
    }

    public String getEeTitle() {
        return eeTitle;
    }

    public void setEeTitle(String eeTitle) {
        this.eeTitle = eeTitle;
    }

    public String getEeContent() {
        return eeContent;
    }

    public void setEeContent(String eeContent) {
        this.eeContent = eeContent;
    }

    public ElevatorEvent() {
    }

    public ElevatorEvent(String id, Date createTime, Date updateTime, String isDeleted, String eeOccurtime, String eeEndtime, String eeVeid, Integer eeOccurstus, Integer eeExigrncystus, Integer eeAccident, String eeTitle, String eeContent, String veName) {
        this.id = id;
        this.createTime = createTime;
        this.updateTime = updateTime;
        this.isDeleted = isDeleted;
        this.eeOccurtime = eeOccurtime;
        this.eeEndtime = eeEndtime;
        this.eeVeid = eeVeid;
        this.eeOccurstus = eeOccurstus;
        this.eeExigrncystus = eeExigrncystus;
        this.eeAccident = eeAccident;
        this.eeTitle = eeTitle;
        this.eeContent = eeContent;
        this.veName = veName;
    }

    @Override
    public String toString() {
        return "ElevatorEvent{" +
                "id='" + id + '\'' +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", isDeleted='" + isDeleted + '\'' +
                ", eeOccurtime='" + eeOccurtime + '\'' +
                ", eeEndtime='" + eeEndtime + '\'' +
                ", eeVeid='" + eeVeid + '\'' +
                ", eeOccurstus=" + eeOccurstus +
                ", eeExigrncystus=" + eeExigrncystus +
                ", eeAccident=" + eeAccident +
                ", eeTitle='" + eeTitle + '\'' +
                ", eeContent='" + eeContent + '\'' +
                ", veName='" + veName + '\'' +
                '}';
    }

    @Override
    protected Serializable pkVal() {
        return id;
    }
}
