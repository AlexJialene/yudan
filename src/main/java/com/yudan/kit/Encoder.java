package com.yudan.kit;

public class Encoder {
    private StringBuffer stringBuffer = new StringBuffer();

    /**
     * 返回弹幕协议格式化后的结果
     * @return
     */
    public String getResult()
    {
        //数据包末尾必须以'\0'结尾
        stringBuffer.append('\0');
        return stringBuffer.toString();
    }

    /**
     * 添加协议参数项
     * @param key
     * @param value
     */
    public void addItem(String key, Object value)
    {
        //根据斗鱼弹幕协议进行相应的编码处理
        stringBuffer.append(key.replaceAll("/", "@S").replaceAll("@", "@A"));
        stringBuffer.append("@=");
        if(value instanceof String){
            stringBuffer.append(((String)value).replaceAll("/", "@S").replaceAll("@", "@A"));
        }else if(value instanceof Integer){
            stringBuffer.append(value);
        }
        stringBuffer.append("/");
    }
}
