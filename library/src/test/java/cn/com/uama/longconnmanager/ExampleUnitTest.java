package cn.com.uama.longconnmanager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.junit.Test;

import java.lang.reflect.Type;

import cn.com.uama.longconnmanager.internal.LoginBody;
import cn.com.uama.longconnmanager.internal.WSMessageStringBodyDeserializer;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);

        assertEquals(Integer.parseInt("000022"), 22);
    }

    @Test
    public void testWSMessageCode() throws Exception {
        WSMessageCode code = WSMessageCode.create(1, 1, 1);
        assertEquals(code.toText(), "1100000001");
    }

    @Test
    public void testGsonDeserializer() throws Exception {
        Type type = new TypeToken<WSMessage<String>>() {
        }.getType();
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(type, new WSMessageStringBodyDeserializer())
                .create();

        // Test case 1
        String jsonStr = "{\"code\":1200000001,\"status\":100,\"msg\":null,\"body\":{\"test\":\"双手合十\"}}";
        WSMessage message = gson.fromJson(jsonStr, type);

        assertNotNull(message);
        assertEquals(message.getCode(), "1200000001");
        assertEquals(message.getStatus(), "100");
        assertNull(message.getMsg());
        assertEquals(message.getBody(), "{\"test\":\"双手合十\"}");

        // Test case 2
        jsonStr = "{\"code\":1100000001,\"body\":{\"test\":\"双手合十\"}}";
        message = gson.fromJson(jsonStr, type);

        assertNotNull(message);
        assertEquals(message.getCode(), "1100000001");
        assertNull(message.getStatus());
        assertNull(message.getMsg());
        assertEquals(message.getBody(), "{\"test\":\"双手合十\"}");

        // Test case 3
        jsonStr = "{\"code\":1100000001,\"body\":\"双手合十\"}";
        message = gson.fromJson(jsonStr, type);

        assertNotNull(message);
        assertEquals(message.getCode(), "1100000001");
        assertNull(message.getStatus());
        assertNull(message.getMsg());
        assertEquals(message.getBody(), "双手合十");

        // Test case 4
        jsonStr = "{\"code\":1100000001,\"body\":[{\"test\":\"双手合十\"}]}";
        message = gson.fromJson(jsonStr, type);

        assertNotNull(message);
        assertEquals(message.getBody(), "[{\"test\":\"双手合十\"}]");

        // Test case 5
        message = new WSMessage<LoginBody>();
        message.setCode("888");
        message.setBody(new LoginBody("kkk"));
        jsonStr = gson.toJson(message);

        assertEquals(jsonStr, "{\"code\":\"888\",\"body\":{\"token\":\"kkk\"}}");
    }
}