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

    @ApiModelProperty(value="电梯事件发生时间")
    @TableField("ve_occurtime")
    private String veOccurtime;

    @ApiModelProperty(value="电梯事件结束时间")
    @TableField("ve_endtime")
    private String veEndtime;

    @ApiModelProperty(value="电梯事件设备id")
    @TableField("ve_veid")
    private String veVeid;

    @ApiModelProperty(value="电梯事件发生状态")
    @TableField("ve_occurstus")
    private Integer veOccurstus;

    @ApiModelProperty(value="电梯事件紧急状态")
    @TableField("ve_exigrncystus")
    private Integer veExigrncystus;

    @ApiModelProperty(value="电梯事件事故状态")
    @TableField("ve_accident")
    private Integer veAccident;

    @ApiModelProperty(value="电梯事件标题")
    @TableField("ve_title")
    private String veTitle;

    @ApiModelProperty(value="电梯事件内容")
    @TableField("ve_content")
    private String veContent;

    /**
     * 0：电梯事件，1：交通事件
     */
    @ApiModelProperty(value="事件类型")
    @TableField("ve_type")
    private String veType;

    @ApiModelProperty(value="用户表id")
    @TableField("ve_userid")
    private String veUserid;

    @ApiModelProperty(value="系统表id")
    @TableField("ve_systemid")
    private String veSystemid;

    @ApiModelProperty(value="处理结果")
    @TableField("ve_result")
    private String veResult;

    /**
     * 0：智能上报，1：人工上报
     */
    @ApiModelProperty(value="事件上报情况")
    @TableField("ve_report")
    private String veReport;

    @ApiModelProperty(value="电梯事件类型id")
    @TableField("ve_eetypeid")
    private String veEetypeid;

    @ApiModelProperty(value="电梯事件类型id")
    @TableField("ve_tetypeid")
    private String veTetypeid;

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

    public String getVeOccurtime() {
        return veOccurtime;
    }

    public void setVeOccurtime(String veOccurtime) {
        this.veOccurtime = veOccurtime;
    }

    public String getVeEndtime() {
        return veEndtime;
    }

    public void setVeEndtime(String veEndtime) {
        this.veEndtime = veEndtime;
    }

    public String getVeVeid() {
        return veVeid;
    }

    public void setVeVeid(String veVeid) {
        this.veVeid = veVeid;
    }

    public Integer getVeOccurstus() {
        return veOccurstus;
    }

    public void setVeOccurstus(Integer veOccurstus) {
        this.veOccurstus = veOccurstus;
    }

    public Integer getVeExigrncystus() {
        return veExigrncystus;
    }

    public void setVeExigrncystus(Integer veExigrncystus) {
        this.veExigrncystus = veExigrncystus;
    }

    public Integer getVeAccident() {
        return veAccident;
    }

    public void setVeAccident(Integer veAccident) {
        this.veAccident = veAccident;
    }

    public String getVeTitle() {
        return veTitle;
    }

    public void setVeTitle(String veTitle) {
        this.veTitle = veTitle;
    }

    public String getVeContent() {
        return veContent;
    }

    public void setVeContent(String veContent) {
        this.veContent = veContent;
    }

    public String getVeType() {
        return veType;
    }

    public void setVeType(String veType) {
        this.veType = veType;
    }

    public String getVeUserid() {
        return veUserid;
    }

    public void setVeUserid(String veUserid) {
        this.veUserid = veUserid;
    }

    public String getVeSystemid() {
        return veSystemid;
    }

    public void setVeSystemid(String veSystemid) {
        this.veSystemid = veSystemid;
    }

    public String getVeResult() {
        return veResult;
    }

    public void setVeResult(String veResult) {
        this.veResult = veResult;
    }

    public String getVeReport() {
        return veReport;
    }

    public void setVeReport(String veReport) {
        this.veReport = veReport;
    }

    public String getVeEetypeid() {
        return veEetypeid;
    }

    public void setVeEetypeid(String veEetypeid) {
        this.veEetypeid = veEetypeid;
    }

    public String getVeTetypeid() {
        return veTetypeid;
    }

    public void setVeTetypeid(String veTetypeid) {
        this.veTetypeid = veTetypeid;
    }

    public ElevatorEvent() {

    }

    public ElevatorEvent(String id, Date createTime, Date updateTime, String isDeleted, String veOccurtime, String veEndtime, String veVeid, Integer veOccurstus, Integer veExigrncystus, Integer veAccident, String veTitle, String veContent, String veType, String veUserid, String veSystemid, String veResult, String veReport, String veEetypeid, String veTetypeid, String veName) {
        this.id = id;
        this.createTime = createTime;
        this.updateTime = updateTime;
        this.isDeleted = isDeleted;
        this.veOccurtime = veOccurtime;
        this.veEndtime = veEndtime;
        this.veVeid = veVeid;
        this.veOccurstus = veOccurstus;
        this.veExigrncystus = veExigrncystus;
        this.veAccident = veAccident;
        this.veTitle = veTitle;
        this.veContent = veContent;
        this.veType = veType;
        this.veUserid = veUserid;
        this.veSystemid = veSystemid;
        this.veResult = veResult;
        this.veReport = veReport;
        this.veEetypeid = veEetypeid;
        this.veTetypeid = veTetypeid;
        this.veName = veName;
    }

    @Override
    public String toString() {
        return "ElevatorEvent{" +
                "id='" + id + '\'' +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", isDeleted='" + isDeleted + '\'' +
                ", veOccurtime='" + veOccurtime + '\'' +
                ", veEndtime='" + veEndtime + '\'' +
                ", veVeid='" + veVeid + '\'' +
                ", veOccurstus=" + veOccurstus +
                ", veExigrncystus=" + veExigrncystus +
                ", veAccident=" + veAccident +
                ", veTitle='" + veTitle + '\'' +
                ", veContent='" + veContent + '\'' +
                ", veType='" + veType + '\'' +
                ", veUserid='" + veUserid + '\'' +
                ", veSystemid='" + veSystemid + '\'' +
                ", veResult='" + veResult + '\'' +
                ", veReport='" + veReport + '\'' +
                ", veEetypeid='" + veEetypeid + '\'' +
                ", veTetypeid='" + veTetypeid + '\'' +
                ", veName='" + veName + '\'' +
                '}';
    }

    @Override
    protected Serializable pkVal() {
        return id;
    }
}
