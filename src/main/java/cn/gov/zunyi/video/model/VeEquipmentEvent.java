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
 * 设备-事件关联关系
 * </p>
 *
 * @author ZTY
 * @since 2018-6-8
 */
@ApiModel("设备-事件关联关系")
@TableName("ve_equipment")
public class VeEquipmentEvent extends Model<VeEquipmentEvent> {

    @TableId(value="id",type = IdType.AUTO)
    private String id;

    @ApiModelProperty(value="创建时间",hidden = true)
    @TableField("create_time")
    private Date createTime;

    @ApiModelProperty(value="更新时间",hidden = true)
    @TableField("update_time")
    private Date updateTime;

    private String eqid;

    private String eid;

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

    public String getEqid() {
        return eqid;
    }

    public void setEqid(String eqid) {
        this.eqid = eqid;
    }

    public String getEid() {
        return eid;
    }

    public void setEid(String eid) {
        this.eid = eid;
    }

    @Override
    protected Serializable pkVal() {
        return id;
    }
}
