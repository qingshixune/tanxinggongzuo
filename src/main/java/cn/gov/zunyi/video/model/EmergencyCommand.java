package cn.gov.zunyi.video.model;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.enums.IdType;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Date;

public class EmergencyCommand extends Model<EmergencyCommand> {

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

    @ApiModelProperty(value="指挥中心名称")
    @TableField("name")
    private String name;

    @ApiModelProperty(value="指挥中心固定电话")
    @TableField("telephone")
    private String telephone;

    @ApiModelProperty(value="指挥中心联系电话")
    @TableField("phone")
    private String phone;

    @ApiModelProperty(value="备注")
    @TableField("note")
    private String note;

    public EmergencyCommand() {
    }

    public EmergencyCommand(String id, Date createTime, Date updateTime, String isDeleted, String name, String telephone, String phone, String note) {
        this.id = id;
        this.createTime = createTime;
        this.updateTime = updateTime;
        this.isDeleted = isDeleted;
        this.name = name;
        this.telephone = telephone;
        this.phone = phone;
        this.note = note;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    @Override
    public String toString() {
        return "EmergencyCommand{" +
                "id='" + id + '\'' +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", isDeleted='" + isDeleted + '\'' +
                ", name='" + name + '\'' +
                ", telephone='" + telephone + '\'' +
                ", phone='" + phone + '\'' +
                ", note='" + note + '\'' +
                '}';
    }

    @Override
    protected Serializable pkVal() {
        return id;
    }
}
