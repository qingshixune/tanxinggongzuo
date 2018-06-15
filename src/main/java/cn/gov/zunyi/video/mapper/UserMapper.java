package cn.gov.zunyi.video.mapper;

import java.util.List;

import cn.gov.zunyi.video.model.User;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;

public interface UserMapper extends BaseMapper<User> {
	
	/*-- 根据活动id 获取都有哪些用户报名 --*/
	List<User> getUserListByNewsId(Page<User> page,
                                   @Param(value = "newsId") String newsId);
	List<User> getLogListByNewsId(
            @Param(value = "newsId") String newsId);
//	************************月*******************************************	
	/*-- 微信用户数 (月)--*/
	List<User> getWeiNum(
            @Param(value = "startTime") String startTime,
            @Param(value = "endTime") String endTime);
	
	/*-- 注册用户数 (月)  --*/
	List<User> getRegistNum(
            @Param(value = "startTime") String startTime,
            @Param(value = "endTime") String endTime);
	
	/*-- 报名人数  (月)--*/
	List<User> getSignUpNum(
            @Param(value = "startTime") String startTime,
            @Param(value = "endTime") String endTime);
	
	/*-- 访问人数 (月)--*/
	List<User> getVisitRNum(
            @Param(value = "startTime") String startTime,
            @Param(value = "endTime") String endTime);
	
	/*-- 访问次数 (月)--*/
	List<User> getVisitCNum(
            @Param(value = "startTime") String startTime,
            @Param(value = "endTime") String endTime);
	
//	*************************周******************************************
	/*-- 微信用户数 (周)--*/
	List<User> getWeiNumW(
            @Param(value = "startTime") String startTime,
            @Param(value = "endTime") String endTime);
	
	/*-- 注册用户数 (周)  --*/
	List<User> getRegistNumW(
            @Param(value = "startTime") String startTime,
            @Param(value = "endTime") String endTime);
	
	/*-- 报名人数  (周)--*/
	List<User> getSignUpNumW(
            @Param(value = "startTime") String startTime,
            @Param(value = "endTime") String endTime);
	
	/*-- 访问人数 (周)--*/
	List<User> getVisitRNumW(
            @Param(value = "startTime") String startTime,
            @Param(value = "endTime") String endTime);
	
	/*-- 访问次数 (周)--*/
	List<User> getVisitCNumW(
            @Param(value = "startTime") String startTime,
            @Param(value = "endTime") String endTime);
	
//	*************************天******************************************
	/*-- 微信用户数 (天)--*/
	List<User> getWeiNumD(
            @Param(value = "startTime") String startTime,
            @Param(value = "endTime") String endTime);
	
	/*-- 注册用户数 (天)  --*/
	List<User> getRegistNumD(
            @Param(value = "startTime") String startTime,
            @Param(value = "endTime") String endTime);
	
	/*-- 报名人数  (天)--*/
	List<User> getSignUpNumD(
            @Param(value = "startTime") String startTime,
            @Param(value = "endTime") String endTime);
	
	/*-- 访问人数 (天)--*/
	List<User> getVisitRNumD(
            @Param(value = "startTime") String startTime,
            @Param(value = "endTime") String endTime);
	
	/*-- 访问次数 (天)--*/
	List<User> getVisitCNumD(
            @Param(value = "startTime") String startTime,
            @Param(value = "endTime") String endTime);

    List<User> selectRolenameList(Page<User> page, String name);
}