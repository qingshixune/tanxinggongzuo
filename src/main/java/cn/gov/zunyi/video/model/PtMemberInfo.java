package cn.gov.zunyi.video.model;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;

import java.io.Serializable;
import java.util.Date;

@TableName("pt_member_info")
public class PtMemberInfo extends Model<PtMemberInfo> implements Comparable<PtMemberInfo>{

    /**
     * 主键Id
     */
    private Integer id;

    /**
     * 统计时间
     */
    @TableField("count_time")
    private Date countTime;

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
     * 工作总数
     */
    @TableField("work_info")
    private Integer workNum;

    /**
     * 学习总数
     */
    @TableField("two_learn")
    private Integer learnNum;

    /**
     * 年份，用来统计每一年学习和工作的总数
     */
    @TableField(exist = false)
    private int year;

    /**
     * 月份，用来统计每一个月学习和工作的总数
     */
    @TableField(exist = false)
    private int month;

    @Override
    public int compareTo(PtMemberInfo p) {
        int i = this.getYear() - p.getYear();//先按照年份排序
        if(i == 0){
            return this.month - p.getMonth();//如果年份相等了再用月份进行排序
        }
        return i;
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

    public Integer getWorkNum() {
        return workNum;
    }

    public void setWorkNum(Integer workNum) {
        this.workNum = workNum;
    }

    public Integer getLearnNum() {
        return learnNum;
    }

    public void setLearnNum(Integer learnNum) {
        this.learnNum = learnNum;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public Date getCountTime() { return countTime; }

    public void setCountTime(Date countTime) { this.countTime = countTime; }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }
}
