package cn.gov.zunyi.video.model;

import java.io.Serializable;
import java.util.Date;

import org.springframework.data.annotation.Id;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;

@TableName("tb_news")
public class News extends Model<News> {

	@TableField(exist = false)
	private static final long serialVersionUID = 1L;

	@Id
	@TableId(value = "id", type = IdType.AUTO)
	private Integer id;
	private String cover;
	private String thumbnail;

	private String title;
	private String author;
	@TableField("channel_id")
	private String channelId;

	private Date createTime;
	private Date updateTime;
	private Integer clicks;
	//
	// <el-option value="全部" label="0"></el-option>
	// <el-option value="开始报名" label="1"></el-option>
	// <el-option value="关闭报名" label="2"></el-option>
	private Integer type;
	private String tag;
	private String content;
	@TableField("video_type")
	private Integer videoType;
	private Integer approved;
	@TableField("commentCount")
	private Integer commentCount;
	private String newsSource;
	private String videoUrl;

	@TableField(exist = false)
	private Boolean favorite = false;
	@TableField(exist = false)
	private String realname;
	@TableField("videostate_id")
	private Integer videostateId;
	@TableField("slides_id")
	private Integer slidesId;

	/* 开始时间 */
	@TableField("start_time")
	private Date startTime;
	/* 结束时间 */
	@TableField("end_time")
	private Date endTime;

	// 允许报名总次数
	@TableField("sign_up_total_num")
	private Integer signUpTotlNum;

	// 报名次数
	@TableField("sign_up_num")
	private Integer signUpNum;
	// 留言数量
	@TableField("comments_num")
	private Integer commentsNum;

	// 点赞数量
	@TableField("thumbs_up")
	private Integer thumbsUp;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getCover() {
		return cover;
	}

	public void setCover(String cover) {
		this.cover = cover;
	}

	public String getThumbnail() {
		return thumbnail;
	}

	public void setThumbnail(String thumbnail) {
		this.thumbnail = thumbnail;
	}

	public String getVideoUrl() {
		return videoUrl;
	}

	public void setVideoUrl(String videoUrl) {
		this.videoUrl = videoUrl;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getChannelId() {
		return channelId;
	}

	public void setChannelId(String channelId) {
		this.channelId = channelId;
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

	public Integer getClicks() {
		return clicks;
	}

	public void setClicks(Integer clicks) {
		this.clicks = clicks;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

	public Integer getVideoType() {
		return videoType;
	}

	public void setVideoType(Integer videoType) {
		this.videoType = videoType;
	}

	public Integer getCommentCount() {
		return commentCount;
	}

	public void setCommentCount(Integer commentCount) {
		this.commentCount = commentCount;
	}

	public Boolean getFavorite() {
		return favorite;
	}

	public void setFavorite(Boolean favorite) {
		this.favorite = favorite;
	}

	public void setFavorite(boolean favorite) {
		this.favorite = favorite;
	}

	public Integer getApproved() {
		return approved;
	}

	public void setApproved(Integer approved) {
		this.approved = approved;
	}

	public String getNewsSource() {
		return newsSource;
	}

	public void setNewsSource(String newsSource) {
		this.newsSource = newsSource;
	}

	public Integer getVideostateId() {
		return videostateId;
	}

	public void setVideostateId(Integer videostateId) {
		this.videostateId = videostateId;
	}

	public String getRealname() {
		return realname;
	}

	public void setRealname(String realname) {
		this.realname = realname;
	}

	public Integer getSlidesId() {
		return slidesId;
	}

	public void setSlidesId(Integer slidesId) {
		this.slidesId = slidesId;
	}

	public Integer getCommentsNum() {
		return commentsNum;
	}

	public void setCommentsNum(Integer commentsNum) {
		this.commentsNum = commentsNum;
	}

	public Integer getThumbsUp() {
		return thumbsUp;
	}

	public void setThumbsUp(Integer thumbsUp) {
		this.thumbsUp = thumbsUp;
	}

	public Integer getSignUpNum() {
		return signUpNum;
	}

	public void setSignUpNum(Integer signUpNum) {
		this.signUpNum = signUpNum;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public Integer getSignUpTotlNum() {
		return signUpTotlNum;
	}

	public void setSignUpTotlNum(Integer signUpTotlNum) {
		this.signUpTotlNum = signUpTotlNum;
	}

	@Override
	public String toString() {
		return "News [id=" + id + ", cover=" + cover + ", title=" + title + ", author=" + author + ", channelId="
				+ channelId + ", createTime=" + createTime + ", updateTime=" + updateTime + ", clicks=" + clicks
				+ ", type=" + type + ", tag=" + tag + ", content=" + content + ", videoType=" + videoType
				+ ", approved=" + approved + ", favorite=" + favorite + "]";
	}

}
