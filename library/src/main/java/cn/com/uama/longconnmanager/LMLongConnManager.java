package cn.com.uama.longconnmanager;

import java.util.HashMap;
import java.util.Map;

import okhttp3.OkHttpClient;

/**
 * Created by liwei on 2017/11/28 13:11
 * Email: liwei@uama.com.cn
 * Description: 长连接管理类，负责长连接的建立和释放
 */
public class LMLongConnManager {
    private static OkHttpClient client;

    private static Map<String, WSConnection> connectionMap = new HashMap<>();

    private LMLongConnManager() {
    }

    /**
     * 获取 OkHttpClient
     * @return 唯一的 OkHttpClient
     */
    static OkHttpClient getClient() {
        if (client == null) {
            client = new OkHttpClient();
        }
        return client;
    }

    /**
     * 创建新的连接
     * @param url 连接地址
     * @param token token
     * @param listener 回调接口
     * @return WSConnection 对象
     */
    public static WSConnection newConnection(String url, String token, WSConnection.SimpleWSListener listener) {
        // 释放旧的连接
        WSConnection oldConnection = connectionMap.get(url);
        if (oldConnection != null) {
            releaseConnection(url);
        }

        // 创建新的连接
        WSConnection newConnection = new WSConnection(url, token, listener);
        connectionMap.put(url, newConnection);
        return newConnection;
    }

    /**
     * 释放连接
     * @param url 要释放的连接的 url
     */
    public static void releaseConnection(String url) {
        WSConnection connection = connectionMap.remove(url);
        if (connection != null) {
            connection.close();
        }
    }
}
