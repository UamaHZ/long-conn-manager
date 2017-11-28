package cn.com.uama.longconnmanager.internal;

/**
 * Created by liwei on 2017/11/28 13:12
 * Email: liwei@uama.com.cn
 * Description: 登录消息 body
 */
public class LoginBody {
    private String token;

    public LoginBody() {
    }

    public LoginBody(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
