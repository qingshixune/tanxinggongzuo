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
 * 设备运行情况
 * </p>
 *
 * @author ZTY
 * @since 2018-6-8
 */
@ApiModel("视频设备运行情况")
@TableName("ve_equipment_runstus")
public class VeEquipmentRunstus extends Model<VeEquipmentRunstus> {

    @TableId(value="id",type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value="创建时间",hidden = true)
    @TableField("create_time")
    private Date createTime;

    @ApiModelProperty(value="更新时间",hidden = true)
    @TableField("update_time")
    private Date updateTime;

    @ApiModelProperty(value="是否删除")
    @TableField("is_deleted")
    private String isDeleted;

    @ApiModelProperty(value="设备开启时间")
    @TableField("runstart_time")
    private Date runstartTime;

    @ApiModelProperty(value="设备关闭时间")
    @TableField("runend_time")
    private Date runendTime;

    @ApiModelProperty(value="是否预警")
    @TableField("is_warningstus")
    private Integer isWarningstus;

    @ApiModelProperty(value="设备id")
    @TableField("eqid")
    private String eqid;

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

    public String getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(String isDeleted) {
        this.isDeleted = isDeleted;
    }

    public Date getRunstartTime() {
        return runstartTime;
    }

    public void setRunstartTime(Date runstartTime) {
        this.runstartTime = runstartTime;
    }

    public Date getRunendTime() {
        return runendTime;
    }

    public void setRunendTime(Date runendTime) {
        this.runendTime = runendTime;
    }

    public Integer getIsWarningstus() {
        return isWarningstus;
    }

    public void setIsWarningstus(Integer isWarningstus) {
        this.isWarningstus = isWarningstus;
    }

    public String getEqid() {
        return eqid;
    }

    public void setEqid(String eqid) {
        this.eqid = eqid;
    }

    public VeEquipmentRunstus() {
    }

    @Override
    public String toString() {
        return "VeEquipmentRunstus{" +
                "id=" + id +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", isDeleted='" + isDeleted + '\'' +
                ", runstartTime='" + runstartTime + '\'' +
                ", runendTime='" + runendTime + '\'' +
                ", isWarningstus=" + isWarningstus +
                ", eqid='" + eqid + '\'' +
                '}';
    }

    @Override
    protected Serializable pkVal() {
        return id;
    }
}
