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

    @ApiModelProperty(value="事件处理时间")
    @TableField("manage_time")
    private String manageTime;

    @ApiModelProperty(value="处理方式")
    @TableField("process_mode")
    private Integer processMode;

    @ApiModelProperty(value="处理结果")
    @TableField("process_result")
    private String processResult;

    @ApiModelProperty(value="处理人id")
    @TableField("uid")
    private Integer uid;

    @ApiModelProperty(value="备注")
    @TableField("note")
    private String note;

    @ApiModelProperty(value="处理人名字")
    @TableField(exist = false)
    private String uName;

    @ApiModelProperty(value="处理人手机")
    @TableField(exist = false)
    private String uPhone;

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

    public Integer getProcessMode() {
        return processMode;
    }

    public void setProcessMode(Integer processMode) {
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

    public String getManageTime() {
        return manageTime;
    }

    public void setManageTime(String manageTime) {
        this.manageTime = manageTime;
    }

    public String getuName() {
        return uName;
    }

    public void setuName(String uName) {
        this.uName = uName;
    }

    public String getuPhone() {
        return uPhone;
    }

    public void setuPhone(String uPhone) {
        this.uPhone = uPhone;
    }

    public VeEventFlow() {
    }

    @Override
    public String toString() {
        return "VeEventFlow{" +
                "id='" + id + '\'' +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", isDeleted='" + isDeleted + '\'' +
                ", eid='" + eid + '\'' +
                ", manageTime='" + manageTime + '\'' +
                ", processMode=" + processMode +
                ", processResult='" + processResult + '\'' +
                ", uid=" + uid +
                ", note='" + note + '\'' +
                ", uName='" + uName + '\'' +
                ", uPhone='" + uPhone + '\'' +
                '}';
    }

    @Override
    protected Serializable pkVal() {
        return id;
    }
}
