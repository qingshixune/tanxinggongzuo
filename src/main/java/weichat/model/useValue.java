package weichat.model;



public class useValue {

    /**
     *
     *
     * ****/
    public final static String AppId = "wx3a6168d5668b917e";
    public final static String AppSecret = "e0f585488a8ed2dd1b7547272645e247";
    
   /* public final static String AppId = "wx102d7d6ca2678974";
    public final static String AppSecret = "76aca6d7852cae175bba45110d3fbebb";nanfang*/
    /*public final static String AppId = "wxb70b39080e26ae2f";
    public final static String AppSecret = "2f551d9ac5a2cb6c36147faeba053880";*/
    public final static String getTokenUrl = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET";
    public final static String getCodeUrl = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=APPID&redirect_uri=REDIRECT_URI&response_type=code&scope=SCOPE&state=STATE#wechat_redirect";
    public final static String getOAuthAccessToken = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code";
    public final static String getreferAccessUrl = "https://api.weixin.qq.com/sns/oauth2/refresh_token?appid=APPID&grant_type=refresh_token&refresh_token=REFRESH_TOKEN";
    public final static String getOAuthUserNews = "https://api.weixin.qq.com/sns/userinfo?access_token=ACCESS_TOKEN&openid=OPENID&lang=zh_CN";
    public final static String isOAuthAccessToken = "https://api.weixin.qq.com/sns/auth?access_token=ACCESS_TOKEN&openid=OPENID";


}
