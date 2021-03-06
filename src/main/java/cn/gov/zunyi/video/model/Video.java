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
 * 视频录像信息
 * </p>
 *
 * @author ZTY
 * @since 2018-5-24
 */
@ApiModel("视频设备信息")
@TableName("video")
public class Video extends Model<Video> {

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

    @ApiModelProperty(value="视频设备id")
    @TableField("eqid")
    private String eqid;

    @ApiModelProperty(value="录像地址")
    @TableField("video_url")
    private String videoUrl;

    @ApiModelProperty(value="录像时长")
    @TableField("video_long")
    private Integer videoLong;

    @ApiModelProperty(value="录像时间")
    @TableField("video_time")
    private String videoTime;

    @ApiModelProperty(value="录像描述")
    @TableField("video_describe")
    private String videoDescribe;

    @ApiModelProperty(value="设备名称")
    @TableField(exist = false)
    private String veName;

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

    public String getEqid() {
        return eqid;
    }

    public void setEqid(String eqid) {
        this.eqid = eqid;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public Integer getVideoLong() {
        return videoLong;
    }

    public void setVideoLong(Integer videoLong) {
        this.videoLong = videoLong;
    }

    public String getVideoTime() {
        return videoTime;
    }

    public void setVideoTime(String videoTime) {
        this.videoTime = videoTime;
    }

    public String getVideoDescribe() {
        return videoDescribe;
    }

    public void setVideoDescribe(String videoDescribe) {
        this.videoDescribe = videoDescribe;
    }

    public String getVeName() {
        return veName;
    }

    public void setVeName(String veName) {
        this.veName = veName;
    }

    @Override
    public String toString() {
        return "Video{" +
                "id='" + id + '\'' +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", isDeleted='" + isDeleted + '\'' +
                ", eqid='" + eqid + '\'' +
                ", videoUrl='" + videoUrl + '\'' +
                ", videoLong=" + videoLong +
                ", videoTime='" + videoTime + '\'' +
                ", videoDescribe='" + videoDescribe + '\'' +
                ", veName='" + veName + '\'' +
                '}';
    }

    @Override
    protected Serializable pkVal() {
        return null;
    }
}
