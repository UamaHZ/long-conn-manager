package cn.com.uama.longconnmanager.internal;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import cn.com.uama.longconnmanager.WSMessage;

/**
 * Created by liwei on 2017/11/28 13:15
 * Email: liwei@uama.com.cn
 * Description: 消息泛型类型
 */
public class WSMessageParameterizedType<X> implements ParameterizedType {

    private Class<X> wrapped;

    public WSMessageParameterizedType(Class<X> wrapped) {
        this.wrapped = wrapped;
    }

    @Override
    public Type[] getActualTypeArguments() {
        return new Type[] { wrapped };
    }

    @Override
    public Type getRawType() {
        return WSMessage.class;
    }

    @Override
    public Type getOwnerType() {
        return null;
    }
}
