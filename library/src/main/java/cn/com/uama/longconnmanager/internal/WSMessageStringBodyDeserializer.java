package cn.com.uama.longconnmanager.internal;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

import cn.com.uama.longconnmanager.WSMessage;

/**
 * Created by liwei on 2017/11/29 13:49
 * Email: liwei@uama.com.cn
 * Description: 将消息的 body 反序列化为 String 的 GSON 反序列化器
 */
public class WSMessageStringBodyDeserializer implements JsonDeserializer<WSMessage<String>> {

    @Override
    public WSMessage<String> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();

        String code = jsonElementAsString(jsonObject.get("code"));
        String status = jsonElementAsString(jsonObject.get("status"));
        String msg = jsonElementAsString(jsonObject.get("msg"));
        JsonElement bodyElement = jsonObject.get("body");
        String body;
        if (bodyElement == null || bodyElement.isJsonNull()) {
            body = null;
        } else {
            if (bodyElement.isJsonPrimitive()) {
                body = bodyElement.getAsString();
            } else {
                body = bodyElement.toString();
            }
        }

        WSMessage<String> message = new WSMessage<>();
        message.setCode(code);
        message.setStatus(status);
        message.setMsg(msg);
        message.setBody(body);

        return message;
    }

    private String jsonElementAsString(JsonElement jsonElement) {
        return jsonElement == null || jsonElement.isJsonNull() ? null : jsonElement.getAsString();
    }
}
