package cn.gov.zunyi.video.service;

import cn.gov.zunyi.video.common.util.NetWorkUtil;
import cn.gov.zunyi.video.model.MsSysLog;
import cn.gov.zunyi.video.model.User;
import cn.gov.zunyi.video.mapper.MsSysLogMapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.apache.shiro.SecurityUtils;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@Service
public class MsSysLogService extends ServiceImpl<MsSysLogMapper, MsSysLog> {

    public boolean insertData(HttpServletRequest request, String content,
                              String operation) {
        User user = (User) SecurityUtils.getSubject().getPrincipal();
        MsSysLog insertMsSys = new MsSysLog();
        insertMsSys.setLoginName(user.getUsername());
        insertMsSys.setContent(content);
        insertMsSys.setIp(NetWorkUtil.getLoggableAddress(request));
        insertMsSys.setOperation(operation);
        insertMsSys.setCreateTime(new Date(System.currentTimeMillis()));
        return this.insert(insertMsSys);
    }

}
