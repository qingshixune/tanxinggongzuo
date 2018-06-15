package cn.gov.zunyi.video.service;

import cn.gov.zunyi.video.model.MsSmsAuth;
import cn.gov.zunyi.video.mapper.MsSmsAuthMapper;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class MsSmsAuthService extends ServiceImpl<MsSmsAuthMapper, MsSmsAuth> {

    private final static String FIND_MESSAGE = "WX_REGISTER";//注册和找回密码
    private final static String AUTHEN = "AUTHEN";//身份验证
    private final static String LOGIN_MESSAGE = "AUTHEN";//登录确认
    private final static String LOGIN_ERROR = "AUTHEN";//登录异常
    public final static String WX_REGISTER = "WX_REGISTER";//微信注册认证
    public final static String UPDATE_PASS = "UPDATE_PASS";//更新密码
    private final static String MESSAGW_CHANGE = "AUTHEN";//信息变更

//    SMS_127865103  注册和找回密码
//    SMS_126650117  身份验证
//    SMS_126650116 登录确认
//    SMS_126650115  登录异常
//    SMS_126650114  用户注册验证码
//    SMS_126650113  修改密码
//    SMS_126650112  信息变更

    public String getSmsCode(String type) {
        if (type.equals(MsSmsAuthService.UPDATE_PASS)) {
            return "SMS_126650113";
        } else if (type.equals(MsSmsAuthService.WX_REGISTER)) {
            return "SMS_126650114";
        } else if (type.equals(MsSmsAuthService.FIND_MESSAGE)) {
            return "SMS_127865103";
        } else if (type.equals(MsSmsAuthService.AUTHEN)) {
            return "SMS_126650117";
        } else if (type.equals(MsSmsAuthService.LOGIN_MESSAGE)) {
            return "SMS_126650116";
        } else if (type.equals(MsSmsAuthService.LOGIN_ERROR)) {
            return "SMS_126650115";
        } else if (type.equals(MsSmsAuthService.MESSAGW_CHANGE)) {
            return "SMS_126650112";
        }
        return "";
    }

    /**
     * 验证验证码
     *
     * @param phone
     */
    public String getSendCode(String phone, String type) {
        try {
            if (StringUtils.isNoneBlank(phone)) {
                Wrapper<MsSmsAuth> wrapper = new EntityWrapper<>();
                if (StringUtils.isNoneBlank(phone)) {
                    wrapper.eq("iphone", phone);
                    //当前时间
                    Date d1 = new Date();
                    Date endDate = new Date(d1.getTime() - 60 * 1000);//查看六十秒钱的数据
                    wrapper.orderBy("id", false);
                    wrapper.gt("create_time", endDate);
                    wrapper.eq("type", type);
                    List<MsSmsAuth> msSmsAuthList = this.selectList(wrapper);
                    if (msSmsAuthList.size() > 0) {
                        //说明60秒内已经发送过短信
                        return msSmsAuthList.get(0).getCode();
                    }
                    return null;
                }
            }
            return null;
        } catch (Exception e) {
            System.err.println("未查找到验证码：" + e.getMessage());
        }
        return null;
    }

    /**
     * 验证短信发送时间  一分钟之内一个手机号只能发送一条
     *
     * @param phone
     */
    public boolean getSendDate(String phone, String type) {
        try {
            if (StringUtils.isNoneBlank(phone)) {
                boolean ret = false;
                Wrapper<MsSmsAuth> wrapper = new EntityWrapper<MsSmsAuth>();
                if (StringUtils.isNoneBlank(phone)) {
                    wrapper.eq("iphone", phone);
                    //当前时间
                    Date d1 = new Date();
                    Date endDate = new Date(d1.getTime() - 60 * 1000);//查看六十秒钱的数据
                    wrapper.gt("create_time", endDate);
                    wrapper.eq("type", type);
                    List<MsSmsAuth> msSmsAuthList = this.selectList(wrapper);
                    if (msSmsAuthList.size() > 0) {
                        //说明60秒内已经发送过短信
                        return false;
                    }
                    return true;
                }
            }
            return false;
        } catch (Exception e) {
            System.err.println("获取验证码异常：" + e.getMessage());
        }
        return false;
    }

    public Boolean sendPhoneMessage(String phone, String type, String name) throws ClientException {
        int codeStr = (int) (Math.random() * (9999 - 1000 + 1)) + 1000;

        //设置超时时间-可自行调整
        System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
        System.setProperty("sun.net.client.defaultReadTimeout", "10000");
        //初始化ascClient需要的几个参数
        final String product = "Dysmsapi";//短信API产品名称（短信产品名固定，无需修改）
        final String domain = "dysmsapi.aliyuncs.com";//短信API产品域名（接口地址固定，无需修改）
        //替换成你的AK
        final String accessKeyId = "";//你的accessKeyId,参考本文档步骤2
        final String accessKeySecret = "";//你的accessKeySecret，参考本文档步骤2
        //初始化ascClient,暂时不支持多region（请勿修改）
        IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", accessKeyId,
                accessKeySecret);
        DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", product, domain);

        IAcsClient acsClient = new DefaultAcsClient(profile);
        //组装请求对象
        SendSmsRequest request = new SendSmsRequest();
        //使用post提交
        request.setMethod(MethodType.POST);
        //必填:待发送手机号。支持以逗号分隔的形式进行批量调用，批量上限为1000个手机号码,批量调用相对于单条调用及时性稍有延迟,验证码类型的短信推荐使用单条调用的方式
        request.setPhoneNumbers(phone.trim());
        //必填:短信签名-可在短信控制台中找到
        request.setSignName("玛森马术");//
        //必填:短信模板-可在短信控制台中找到
        if (getSmsCode(type).equals("")) {
            return false;
        }
        request.setTemplateCode(getSmsCode(type));
        //SMS_127865103  注册和找回密码
        //SMS_126650117  身份验证
        //SMS_126650116 登录确认
        //SMS_126650115  登录异常
        //SMS_126650114  用户注册验证码
        //SMS_126650113  修改密码
        //SMS_126650112  信息变更
        //可选:模板中的变量替换JSON串,如模板内容为"亲爱的${name},您的验证码为${code}"时,此处的值为
        //友情提示:如果JSON中需要带换行符,请参照标准的JSON协议对换行符的要求,比如短信内容中包含\r\n的情况在JSON中需要表示成\\r\\n,否则会导致JSON在服务端解析失败
        String sendname = "用户";
        if (StringUtils.isNotBlank(name) && !"".equals(name)) {
            sendname = name;
        }
        request.setTemplateParam("{ \"code\":\"" + codeStr + "\"}");

        //可选-上行短信扩展码(扩展码字段控制在7位或以下，无特殊需求用户请忽略此字段)
        //request.setSmsUpExtendCode("90997");
        //可选:outId为提供给业务方扩展字段,最终在短信回执消息中将此值带回给调用者
        //        request.setOutId("msms");
        //请求失败这里会抛ClientException异常
        SendSmsResponse sendSmsResponse = acsClient.getAcsResponse(request);
        if (sendSmsResponse.getCode() != null && sendSmsResponse.getCode().equals("OK")) {
            MsSmsAuth msSmsAuth = new MsSmsAuth();
            msSmsAuth.setCreateTime(new Date());
            msSmsAuth.setCode(String.valueOf(codeStr));
            msSmsAuth.setType(type);
            msSmsAuth.setIphone(phone);
            //请求成功
            return this.insert(msSmsAuth);
        }
        return false;
    }

}
