package cn.gov.zunyi.video.service;

import cn.gov.zunyi.video.auth.SHA256PasswordEncryptionService;
import cn.gov.zunyi.video.auth.SecureRandomSaltService;
import cn.gov.zunyi.video.model.User;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.DefaultSessionKey;
import org.apache.shiro.session.mgt.SessionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;

import cn.gov.zunyi.video.mapper.UserMapper;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class UserService extends ServiceImpl<UserMapper, User> {

    @Autowired
    private SessionManager sessionManager;
    @Autowired
    private UserMapper userMapper;

    // 根据sessionId执行强制退出
    public void kickout(Serializable sessionId) {
        this.getSessionBysessionId(sessionId).setAttribute("kickout", true);
    }

    // 根据sesisonid获取单个session对象
    private Session getSessionBysessionId(Serializable sessionId) {
        return sessionManager.getSession(new DefaultSessionKey(sessionId));
    }

    public boolean insert(User entity) {
        byte[] salt = SecureRandomSaltService.generateSalt();
        entity.setPasswordSalt(salt);
        entity.setPasswordHash(SHA256PasswordEncryptionService.createPasswordHash(entity.getPassword(), salt));
        return super.insert(entity);
    }
    
    //俱乐部管理导出
    public List<User> getAllFeedbackForExcel(String ids,String userType,String clubName) {
        Wrapper<User> wrapper = new EntityWrapper<User>();
        List<User>  userL =new ArrayList<User>();
    	if (StringUtils.isNoneBlank(userType)) {
    			wrapper.eq("user_type", userType);
    	} 
        if (StringUtils.isNoneBlank(clubName)) {
			wrapper.like("CLUB_NAME", clubName);
		}
        
        wrapper.eq("enabled", 1);
        if(StringUtils.isNotBlank(ids)){  //如果ids不为空，根据id查询
          userL =this.selectBatchIds(Arrays.asList(StringUtils.split(ids, ",")));
        }else{   //如果ids为空，查询全部
        	if(userType==null || userType.equals("")){
        		wrapper.addFilter("  (user_type ='0' or user_type ='1')", null);
        	}
          userL=this.selectList(wrapper);
        }
        return userL;
      }
    
  //用户管理导出
    public List<User> getUserFeedbackForExcel(String ids,String userType,String mobile,String username) {
        Wrapper<User> wrapper = new EntityWrapper<User>();
        List<User>  userL =new ArrayList<User>();
        	if (StringUtils.isNoneBlank(userType)) {
        		wrapper.eq("user_type", userType);
        	}
        	if (StringUtils.isNoneBlank(mobile)) {
        		wrapper.eq("mobile", mobile);
        	}
        	if (StringUtils.isNoneBlank(username)) {
        		wrapper.eq("username", username);
        	}
        if(StringUtils.isNotBlank(ids)){  //如果ids不为空，根据id查询
          userL =this.selectBatchIds(Arrays.asList(StringUtils.split(ids, ",")));
        }else{   //如果ids为空，查询全部
          wrapper.eq("user_type", 3);
          wrapper.eq("enabled", 1);
          wrapper.eq("is_live", 1);
          userL=this.selectList(wrapper);
        }
        return userL;
      }
    
    /*--根据活动id 获取都有哪些用户报名--*/
    public Page<User> getUserListByNewsId(Page<User> page, String newsId) {
		page.setRecords(userMapper.getUserListByNewsId(page, newsId));
		return page;
	}
    
    public List<User> getLogListByNewsId(String newsId) {
		List<User> list = userMapper.getLogListByNewsId(newsId);
		return list;
	}
    
    
   
//  *********************月 ************************************************************************ 
    /*-- 微信用户数 --*/
    public List<User> getWeiNum(String startTime, String endTime) {
    	List<User> weiNumList =  userMapper.getWeiNum(startTime, endTime);
		return weiNumList;
	}
    
    /*-- 注册用户数  --*/
    public List<User> getRegistNum(String startTime, String endTime) {
    	List<User> registNumList =  userMapper.getRegistNum(startTime, endTime);
		return registNumList;
	}
	
	/*-- 报名人数 --*/
    public List<User> getSignUpNum(String startTime, String endTime) {
    	List<User> signUpNumList =  userMapper.getSignUpNum(startTime, endTime);
		return signUpNumList;
	}
	
	/*-- 访问人数--*/
    public List<User> getVisitRNum(String startTime, String endTime) {
    	List<User> visitRNumList =  userMapper.getVisitRNum(startTime, endTime);
		return visitRNumList;
	}
	
	/*-- 访问次数--*/
    public List<User> getVisitCNum(String startTime, String endTime) {
    	List<User> visitCNumList =  userMapper.getVisitCNum(startTime, endTime);
		return visitCNumList;
	}
    
//   *********************周 ************************************************************************ 
    
    /*-- 微信用户数 --*/
    public List<User> getWeiNumW(String startTime, String endTime) {
    	List<User> weiNumWList =  userMapper.getWeiNumW(startTime, endTime);
		return weiNumWList;
	}
    
    /*-- 注册用户数  --*/
    public List<User> getRegistNumW(String startTime, String endTime) {
    	List<User> registNumWList =  userMapper.getRegistNumW(startTime, endTime);
		return registNumWList;
	}
	
	/*-- 报名人数 --*/
    public List<User> getSignUpNumW(String startTime, String endTime) {
    	List<User> signUpNumWList =  userMapper.getSignUpNumW(startTime, endTime);
		return signUpNumWList;
	}
	
	/*-- 访问人数--*/
    public List<User> getVisitRNumW(String startTime, String endTime) {
    	List<User> visitRNumWList =  userMapper.getVisitRNumW(startTime, endTime);
		return visitRNumWList;
	}
	
	/*-- 访问次数--*/
    public List<User> getVisitCNumW(String startTime, String endTime) {
    	List<User> visitCNumWList =  userMapper.getVisitCNumW(startTime, endTime);
		return visitCNumWList;
	}
    
//  *********************天 ************************************************************************ 
    
   /*-- 微信用户数 --*/
   public List<User> getWeiNumD(String startTime, String endTime) {
   	List<User> weiNumDList =  userMapper.getWeiNumD(startTime, endTime);
		return weiNumDList;
	}
   
   /*-- 注册用户数  --*/
   public List<User> getRegistNumD(String startTime, String endTime) {
   	List<User> registNumDList =  userMapper.getRegistNumD(startTime, endTime);
		return registNumDList;
	}
	
	/*-- 报名人数 --*/
   public List<User> getSignUpNumD(String startTime, String endTime) {
   	List<User> signUpNumDList =  userMapper.getSignUpNumD(startTime, endTime);
		return signUpNumDList;
	}
	
	/*-- 访问人数--*/
   public List<User> getVisitRNumD(String startTime, String endTime) {
   	List<User> visitRNumDList =  userMapper.getVisitRNumD(startTime, endTime);
		return visitRNumDList;
	}
	
	/*-- 访问次数--*/
   public List<User> getVisitCNumD(String startTime, String endTime) {
   	List<User> visitCNumDList =  userMapper.getVisitCNumD(startTime, endTime);
		return visitCNumDList;
	}


}
