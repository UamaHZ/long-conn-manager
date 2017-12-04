package cn.com.uama.longconnmanager;

import java.util.Locale;

/**
 * Created by liwei on 2017/11/28 15:03
 * Email: liwei@uama.com.cn
 * Description: 消息码实体类
 */
public class WSMessageCode {
    // 业务类型：1 位系统类型
    private int businessType;
    // 消息类型：1 表示客户端发送；2 表示服务端返回
    private int messageType;
    // 业务相关码值
    private int businessCode;

    public int getBusinessType() {
        return businessType;
    }

    private void setBusinessType(int businessType) {
        this.businessType = businessType;
    }

    public int getMessageType() {
        return messageType;
    }

    private void setMessageType(int messageType) {
        this.messageType = messageType;
    }

    public int getBusinessCode() {
        return businessCode;
    }

    private void setBusinessCode(int businessCode) {
        this.businessCode = businessCode;
    }

    /**
     * 创建消息实体
     * @param businessType 业务类型
     * @param messageType 消息类型
     * @param businessCode 业务码值
     * @return 创建好的消息实体
     */
    public static WSMessageCode create(int businessType, int messageType, int businessCode) {
        checkMessageType(messageType);

        WSMessageCode code = new WSMessageCode();
        code.setBusinessType(businessType);
        code.setMessageType(messageType);
        code.setBusinessCode(businessCode);
        return code;
    }

    /**
     * 检查消息类型的有消息，只能是 WSMessageType.CLIENT(1) 或 WSMessageType.SERVER(2)
     * 如果消息类型不符合逾期，抛出 IllegalArgumentException 异常
     * @param messageType 消息类型
     */
    private static void checkMessageType(int messageType) {
        if (messageType != WSMessageType.CLIENT && messageType != WSMessageType.SERVER) {
            throw new IllegalArgumentException("Message type must be either WSMessageType.CLIENT or WSMessageType.SERVER!");
        }
    }

    /**
     * 将字符串格式的 code 值转换为实体
     * @param text 字符串格式的 code 值
     * @return 消息码实体，如果 text 为 null 或中间转换发生异常返回 null
     */
    public static WSMessageCode parse(String text) {
        if (text == null) return null;
        WSMessageCode code = new WSMessageCode();
        try {
            int businessType = Integer.parseInt(text.substring(0, 1));
            code.setBusinessType(businessType);

            int messageType = Integer.parseInt(text.substring(1, 2));
            code.setMessageType(messageType);

            int businessCode = Integer.parseInt(text.substring(2));
            code.setBusinessCode(businessCode);
        } catch (IndexOutOfBoundsException | NumberFormatException e) {
            e.printStackTrace();
            return null;
        }
        return code;
    }

    /**
     * 是否是客户端消息
     * @return 是的话返回 true，否则返回 false
     */
    public boolean isClient() {
        return WSMessageType.CLIENT == messageType;
    }

    /**
     * 是否服务端消息
     * @return 是的话返回 true，否则返回 false
     */
    public boolean isServer() {
        return WSMessageType.SERVER == messageType;
    }

    /**
     * 转换为字符串类型的码值
     * @return 转换过后的码值
     */
    public String toText() {
        return String.format(Locale.CHINA, "%d%d%06d", businessType, messageType, businessCode);
    }
}
