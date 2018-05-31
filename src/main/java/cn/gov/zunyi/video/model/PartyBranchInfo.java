package cn.gov.zunyi.video.model;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;

import java.io.Serializable;
import java.util.Date;

@TableName("partybranch_info")
public class PartyBranchInfo extends Model<PartyBranchInfo> {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @TableField("create_time")
    private Date createTime;

    @TableField("update_time")
    private Date updateTime;

    /**
     * 党支部名称
     * */
    @TableField("partybranch_name")
    private String partybranchname;

    /**
     * 积分
     * */
    private Integer integral;

    /**
     * 备注
     * */
    private String note;

    /**
     * 状态：1正常  0 逻辑删除
     * */
    private Integer enabled;

    public PartyBranchInfo(){

    }

    public PartyBranchInfo(Integer id, Date createTime, Date updateTime, String partybranchname, Integer integral, String note, Integer enabled) {
        this.id = id;
        this.createTime = createTime;
        this.updateTime = updateTime;
        this.partybranchname = partybranchname;
        this.integral = integral;
        this.note = note;
        this.enabled = enabled;
    }

    @Override
    public String toString() {
        return this.partybranchname;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

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

    public String getPartybranchname() {
        return partybranchname;
    }

    public void setPartybranchname(String partybranchname) {
        this.partybranchname = partybranchname;
    }

    public Integer getIntegral() {
        return integral;
    }

    public void setIntegral(Integer integral) {
        this.integral = integral;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Integer getEnabled() {
        return enabled;
    }

    public void setEnabled(Integer enabled) {
        this.enabled = enabled;
    }
}
