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
 * 事件流转情况类
 * </p>
 *
 * @author ZTY
 * @since 2018-6-8
 */
@ApiModel("事件流转情况")
@TableName("ve_event_flow")
public class VeEventFlow extends Model<VeEventFlow> {

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

    @ApiModelProperty(value="事件id")
    @TableField("eid")
    private String eid;

    @ApiModelProperty(value="事件处理开始时间")
    @TableField("start_time")
    private String startTime;

    @ApiModelProperty(value="事件处理结束时间")
    @TableField("end_time")
    private String endTime;

    @ApiModelProperty(value="处理方式")
    @TableField("process_mode")
    private String processMode;

    @ApiModelProperty(value="处理结果")
    @TableField("process_result")
    private String processResult;

    @ApiModelProperty(value="处理人id")
    @TableField("uid")
    private Integer uid;

    @ApiModelProperty(value="备注")
    @TableField("note")
    private String note;

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

    public String getEid() {
        return eid;
    }

    public void setEid(String eid) {
        this.eid = eid;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getProcessMode() {
        return processMode;
    }

    public void setProcessMode(String processMode) {
        this.processMode = processMode;
    }

    public String getProcessResult() {
        return processResult;
    }

    public void setProcessResult(String processResult) {
        this.processResult = processResult;
    }

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public VeEventFlow() {
    }

    public VeEventFlow(String id, Date createTime, Date updateTime, String isDeleted, String eid, String startTime, String endTime, String processMode, String processResult, Integer uid, String note) {
        this.id = id;
        this.createTime = createTime;
        this.updateTime = updateTime;
        this.isDeleted = isDeleted;
        this.eid = eid;
        this.startTime = startTime;
        this.endTime = endTime;
        this.processMode = processMode;
        this.processResult = processResult;
        this.uid = uid;
        this.note = note;
    }

    @Override
    public String toString() {
        return "VeEventFlow{" +
                "id='" + id + '\'' +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", isDeleted='" + isDeleted + '\'' +
                ", eid='" + eid + '\'' +
                ", startTime='" + startTime + '\'' +
                ", endTime='" + endTime + '\'' +
                ", processMode='" + processMode + '\'' +
                ", processResult='" + processResult + '\'' +
                ", uid=" + uid +
                ", note='" + note + '\'' +
                '}';
    }

    @Override
    protected Serializable pkVal() {
        return id;
    }
}
