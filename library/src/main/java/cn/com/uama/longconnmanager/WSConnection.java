package cn.com.uama.longconnmanager;

import android.os.Handler;
import android.os.Looper;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import cn.com.uama.longconnmanager.internal.LoginBody;
import cn.com.uama.longconnmanager.internal.WSMessageParameterizedType;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;

/**
 * Created by liwei on 2017/11/28 13:13
 * Email: liwei@uama.com.cn
 * Description: WebSocket 连接类
 */
public class WSConnection {
    private String url;
    private String token;
    private WSListener listener;

    private WebSocket webSocket;
    private Gson gson;

    // 是否已登录
    private boolean isLoggedIn;

    // 断线重连次数
    private int retryCount = 0;

    private Handler handler;

    // 最大断线重连次数
    private static final int MAX_RETRY_COUNT = 5;
    // 正常关闭连接的 code
    private static final int NORMAL_CLOSE_CODE = 1000;

    /**
     * 登录消息 code
     */
    static class LoginCode {
        /**
         * 登录请求
         */
        static final String REQUEST = "1100000000";
        /**
         * 登录返回
         */
        static final String RESPONSE = "1200000000";
    }

    /**
     * 通用的状态值
     */
    static class MessageStatus {
        /**
         * 成功
         */
        static final String SUCCESS = "100";
    }

    public interface WSListener {
        /**
         * 登录成功
         */
        void onLoginSuccess();

        /**
         * 收到新的消息
         * @param connection 连接对象
         * @param text 消息字符串
         */
        void onMessage(WSConnection connection, String text);

        /**
         * 正常关闭
         */
        void onClosed();

        /**
         * 连接失败
         */
        void onConnectionFailed();

        /**
         * 失去连接
         */
        void onDisconnected();
    }

    public static class SimpleWSListener implements WSListener {

        @Override
        public void onLoginSuccess() {

        }

        @Override
        public void onMessage(WSConnection connection, String text) {

        }

        @Override
        public void onClosed() {

        }

        @Override
        public void onConnectionFailed() {

        }

        @Override
        public void onDisconnected() {

        }
    }

    public WSConnection(String url, String token, SimpleWSListener listener) {
        if (url == null) {
            throw new IllegalArgumentException("url must not be null!");
        }
        this.url = url;
        this.token = token;
        this.listener = listener;
        isLoggedIn = false;
        gson = new Gson();
        handler = new Handler(Looper.getMainLooper());

        connect();
    }


    /**
     * 建立连接
     */
    public void connect() {
        if (isLoggedIn) return;

        Request request = new Request.Builder()
                .url(url)
                .build();

        webSocket = LMLongConnManager.getClient().newWebSocket(request, new WebSocketListener() {
            @Override
            public void onOpen(WebSocket webSocket, Response response) {
                // 连接建立成功之后马上发一条登录消息
                webSocket.send(loginMessage());
            }

            @Override
            public void onMessage(WebSocket webSocket, final String text) {
                if (isLoggedIn) {
                    // 如果已经登录，直接回调收到的消息
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (listener != null) {
                                listener.onMessage(WSConnection.this, text);
                            }
                        }
                    });
                } else {
                    WSMessage loginMessage = parseMessage(text);
                    if (loginMessage != null) {
                        String code = loginMessage.getCode();
                        if (LoginCode.RESPONSE.equals(code)) {
                            String status = loginMessage.getStatus();
                            if (MessageStatus.SUCCESS.equals(status)) {
                                // 登录成功
                                isLoggedIn = true;
                                retryCount = 0;
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (listener != null) {
                                            listener.onLoginSuccess();
                                        }
                                    }
                                });
                            }
                        }
                    }
                }
            }

            @Override
            public void onClosed(WebSocket webSocket, int code, String reason) {
                // 如果是正常关闭，进行回调，否则需要重连
                if (code == NORMAL_CLOSE_CODE) {
                    isLoggedIn = false;
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (listener != null) {
                                listener.onClosed();
                            }
                        }
                    });
                } else {
                    onDisconnected();
                }
            }

            @Override
            public void onFailure(WebSocket webSocket, Throwable t, Response response) {
                onDisconnected();
            }
        });
    }

    /**
     * 断线
     */
    private void onDisconnected() {
        if (isLoggedIn) {
            // 如果登录状态下断线后回调
            isLoggedIn = false;
            handler.post(new Runnable() {
                @Override
                public void run() {
                    if (listener != null) {
                        listener.onDisconnected();
                    }
                }
            });
        }
        reConnect();
    }

    /**
     * 重连
     */
    private void reConnect() {
        retryCount ++;
        if (retryCount > MAX_RETRY_COUNT) {
            // 如果已经达到重连最大次数，回调连接失败方法
            handler.post(new Runnable() {
                @Override
                public void run() {
                    if (listener != null) {
                        listener.onConnectionFailed();
                    }
                }
            });
        } else {
            // 延迟 1s 进行重连
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    connect();
                }
            }, 1000L);
        }
    }

    /**
     * 解析收到的消息，从而可以拿到消息中的 code/status/msg
     * @param text 要解析的字符串格式的消息
     * @return 消息实体
     */
    private WSMessage parseMessage(String text) {
        try {
            return gson.fromJson(text, WSMessage.class);
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 将接收到的字符串格式的消息转换为实体类
     * @param text 字符串格式的消息
     * @return 消息实体
     */
    public <T> WSMessage<T> parseMessage(String text, Class<T> clazz) {
        try {
            return gson.fromJson(text, new WSMessageParameterizedType<>(clazz));
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 登录请求消息
     * @return JSON 格式的请求消息
     */
    private String loginMessage() {
        WSMessage<LoginBody> message = new WSMessage<>();
        message.setCode(LoginCode.REQUEST);
        message.setBody(new LoginBody(token));
        return gson.toJson(message);
    }

    /**
     * 发送消息
     * @param message 消息实体
     * @return 如果消息被插入消息队列中返回 true，如果未登录或出现其他异常情况返回 false
     */
    public boolean send(WSMessage message) {
        if (isLoggedIn) {
            try {
                String text = gson.toJson(message);
                return webSocket.send(text);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * 关闭连接
     */
    public void close() {
        webSocket.close(NORMAL_CLOSE_CODE, null);
    }

    /**
     * 是否已登录
     * @return 是否已登录
     */
    public boolean isLoggedIn() {
        return isLoggedIn;
    }

    /**
     * @return 当前连接的 url 地址
     */
    String getUrl() {
        return url;
    }
}
