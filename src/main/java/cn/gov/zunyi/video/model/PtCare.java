package cn.gov.zunyi.video.model;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;

import java.io.Serializable;
import java.util.Date;

@TableName("pt_care")
public class PtCare extends Model<PtCare> {

    /**
     * 主键id
     */
    private Integer id;

    /**
     * 求助者或捐款者身份
     */
    private Integer identityid;

    /**
     * 求助或捐款资金
     */
    private Double money;

    /**
     * 求助或捐款状态，0为捐款，1为求助
     */
    private Integer status;

    /**
     * 创建时间
     */
    @TableField("create_time")
    private Date createTime;

    /**
     * 更新时间
     */
    @TableField("update_time")
    private Date updateTime;

    /**
     * 身份信息
     */
    @TableField(exist = false)
    private  String identityname;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    public Integer getId() { return id; }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getIdentityid() {
        return identityid;
    }

    public void setIdentityid(Integer identity) {
        this.identityid = identity;
    }

    public Double getMoney() {
        return money;
    }

    public void setMoney(Double money) {
        this.money = money;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getIdentityname() { return identityname; }

    public void setIdentityname(String identityname) { this.identityname = identityname; }

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
}
