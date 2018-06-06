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
    @TableField("ve_id")
    private String veId;

    @ApiModelProperty(value="录像地址")
    @TableField("video_url")
    private String videoUrl;

    @ApiModelProperty(value="录像预警状态")
    @TableField("video_status")
    private Integer videoStatus;

    @ApiModelProperty(value="录像时长")
    @TableField("video_long")
    private Integer videoLong;

    @ApiModelProperty(value="设备名称")
    @TableField(exist = false)
    private String veName;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getVideoLong() {
        return videoLong;
    }

    public void setVideoLong(Integer videoLong) {
        this.videoLong = videoLong;
    }

    public String getVeName() {
        return veName;
    }

    public void setVeName(String veName) {
        this.veName = veName;
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

    public String getVeId() {
        return veId;
    }

    public void setVeId(String veId) {
        this.veId = veId;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public Integer getVideoStatus() { return videoStatus; }

    public void setVideoStatus(Integer videoStatus) { this.videoStatus = videoStatus; }

    public Video() {
    }

    public Video(String id, Date createTime, Date updateTime, String isDeleted, String veId, String videoUrl,Integer videoStatus) {
        this.id = id;
        this.createTime = createTime;
        this.updateTime = updateTime;
        this.isDeleted = isDeleted;
        this.veId = veId;
        this.videoUrl = videoUrl;
        this.videoStatus = videoStatus;
    }

    @Override
    public String toString() {
        return "Video{" +
                "id='" + id + '\'' +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", isDeleted='" + isDeleted + '\'' +
                ", veId='" + veId + '\'' +
                ", videoUrl='" + videoUrl + '\'' +
                ", videoStatus=" + videoStatus +
                ", videoLong=" + videoLong +
                ", veName='" + veName + '\'' +
                '}';
    }

    @Override
    protected Serializable pkVal() {
        return null;
    }
}
