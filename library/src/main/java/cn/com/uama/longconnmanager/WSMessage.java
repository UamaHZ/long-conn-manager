package cn.com.uama.longconnmanager;

/**
 * Created by liwei on 2017/11/28 13:14
 * Email: liwei@uama.com.cn
 * Description: WebSocket 发送和接收消息实体类
 */
public class WSMessage<T> {
    private String code;
    private String status;
    private String msg;
    private T body;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getBody() {
        return body;
    }

    public void setBody(T body) {
        this.body = body;
    }
}