package weichat.dao.impl;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import weichat.dao.WeiXinDao;
import weichat.method.HttpClientConnectionManager;
import weichat.model.OAuthAccessToken;
import weichat.model.UserEntity;
import weichat.model.useValue;

import java.net.URLEncoder;
import java.util.Map;
import java.util.TreeMap;


public class WeiXinDaoImpl implements WeiXinDao {
    public static  DefaultHttpClient httpclient;
    private static Logger log = Logger.getLogger(WeiXinDaoImpl.class.getName());

    static {
        httpclient = new DefaultHttpClient();
        httpclient = (DefaultHttpClient) HttpClientConnectionManager.getSSLInstance(httpclient); // 接受任何证书的浏览器客户端
    }

    /**
     * 微信OAuth2.0授权（目前微信只支持在微信客户端发送连接，实现授权）
     */
    public String getCodeUrl(String appid, String redirect_uri, String scope, String state) throws Exception {
        redirect_uri = URLEncoder.encode(redirect_uri, "utf-8");
        String getCodeUrl = useValue.getCodeUrl.replace("APPID", appid).replace("REDIRECT_URI", redirect_uri).replace("SCOPE", scope).replace("STATE", state);
       // log.info("getCodeUrl Value:" + getCodeUrl);
        return getCodeUrl;
    }

    /**
     * 微信OAuth2.0授权-获取accessToken(这里通过code换取的网页授权access_token,与基础支持中的access_token不同）
     */
    public synchronized OAuthAccessToken getOAuthAccessToken(String appid, String secret, String code) throws Exception {
        String getOAuthAccessToken = useValue.getOAuthAccessToken.replace("APPID", appid).replace("SECRET", secret).replace("CODE", code);
       // log.info("getOAuthAccessToken Value:" + getOAuthAccessToken);
        HttpGet get = HttpClientConnectionManager.getGetMethod(getOAuthAccessToken);
        HttpResponse response = httpclient.execute(get);
        String jsonStr = EntityUtils.toString(response.getEntity(), "utf-8");
        Map<String, Object> jsonObject = new ObjectMapper().configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true).readValue(jsonStr, TreeMap.class);
       // log.info("jsonObject Value:" + jsonObject);
        OAuthAccessToken accessToken = new OAuthAccessToken();
        accessToken.setAccessToken(jsonObject.get("access_token").toString());
        accessToken.setExpiresIn(Integer.valueOf(jsonObject.get("expires_in").toString()));
        accessToken.setRefreshToken(jsonObject.get("refresh_token").toString());
        accessToken.setOpenid(jsonObject.get("openid").toString());
        accessToken.setScope(jsonObject.get("scope").toString());
        return accessToken;
    }

    /**
     * 微信OAuth2.0授权-刷新access_token（如果需要）
     */
    public synchronized OAuthAccessToken refershOAuthAccessToken(String appid, String refresh_token) throws Exception {
        String getreferAccessUrl = useValue.getreferAccessUrl.replace("APPID", appid).replace("REFRESH_TOKEN", refresh_token);
       // log.info("getreferAccessUrl Value:" + getreferAccessUrl);
        HttpGet get = HttpClientConnectionManager.getGetMethod(getreferAccessUrl);
        HttpResponse response = httpclient.execute(get);
        String jsonStr = EntityUtils.toString(response.getEntity(), "utf-8");
        Map<String, Object> jsonObject = new ObjectMapper().configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true).readValue(jsonStr, TreeMap.class);
        OAuthAccessToken accessToken = new OAuthAccessToken();
        accessToken.setAccessToken(jsonObject.get("access_token").toString());
        accessToken.setExpiresIn(Integer.valueOf(jsonObject.get("expires_in").toString()));
        accessToken.setRefreshToken(jsonObject.get("refresh_token").toString());
        accessToken.setOpenid(jsonObject.get("openid").toString());
        accessToken.setScope(jsonObject.get("scope").toString());
        return accessToken;
    }

    /**
     * 微信OAuth2.0授权-检验授权凭证（access_token）是否有效
     */
    public synchronized boolean isAccessTokenValid(String accessToken, String openId) throws Exception {
        String isOAuthAccessToken = useValue.isOAuthAccessToken.replace("ACCESS_TOKEN", accessToken).replace("OPENID", openId);
       // log.info("isOAuthAccessToken Value:" + isOAuthAccessToken);
        HttpGet get = HttpClientConnectionManager.getGetMethod(isOAuthAccessToken);
        HttpResponse response = httpclient.execute(get);
        String jsonStr = EntityUtils.toString(response.getEntity(), "utf-8");
        Map<String, Integer> jsonObject = new ObjectMapper().configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true).readValue(jsonStr, TreeMap.class);
        int returnNum = jsonObject.get("errcode");
        if (returnNum == 0) {
            return true;
        }
        return false;
    }

    /**
     * 微信OAuth2.0授权-获取用户信息（网页授权作用域为snsapi_userinfo，则此时开发者可以通过access_token和openid拉取用户信息）
     */
    public  synchronized UserEntity acceptOAuthUserNews(String accessToken, String openId) throws Exception {
        String getOAuthUserNews = useValue.getOAuthUserNews.replace("ACCESS_TOKEN", accessToken).replace("OPENID", openId);
       // log.info("getOAuthUserNews Value:" + getOAuthUserNews);
        HttpGet get = HttpClientConnectionManager.getGetMethod(getOAuthUserNews);
        HttpResponse response = httpclient.execute(get);
        String jsonStr = EntityUtils.toString(response.getEntity(), "utf-8");
        Map<String, Object> jsonObject = new ObjectMapper().configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true).readValue(jsonStr, TreeMap.class);
        UserEntity entity = new UserEntity();
        entity.setOpenid(jsonObject.get("openid").toString());
        entity.setNickname(jsonObject.get("nickname").toString());
        entity.setSex(Integer.valueOf(jsonObject.get("sex").toString()));
        entity.setProvince(jsonObject.get("province").toString());
        entity.setCity(jsonObject.get("city").toString());
        entity.setCountry(jsonObject.get("country").toString());
        entity.setHeadimgurl(jsonObject.get("headimgurl").toString());
        return entity;
    }

}
