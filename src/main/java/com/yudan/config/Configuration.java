package com.yudan.config;

import com.yudan.kit.Encoder;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.nio.ByteBuffer;

public class Configuration {
    private static final Logger LOGGER = LoggerFactory.getLogger(Configuration.class);
    private static volatile Configuration Configuration;
    private final String hostName = "openbarrage.douyutv.com";
    private final int port = 8601;
    private final int maxBufferLen = 4096;
    private final short messageClient = 689;


    public static Configuration newInstance() {
        if (null == Configuration) {
            synchronized (Configuration.class) {
                if (null == Configuration) {
                    Configuration = new Configuration();
                }
            }
        }
        return Configuration;
    }

    public byte[] int2LittleByte(int i) {
        byte[] b = new byte[4];
        b[0] = (byte) (i & 0xff);
        b[1] = (byte) (i >> 8 & 0xff);
        b[2] = (byte) (i >> 16 & 0xff);
        b[3] = (byte) (i >> 24 & 0xff);
        return b;
    }

    public byte[] short2littleByte(short i) {
        byte[] b = new byte[2];
        b[0] = (byte) (i & 0xff);
        b[1] = (byte) (i >> 8 & 0xff);
        return b;
    }

    public byte[] int2BigByte(int i) {
        byte[] b = new byte[4];
        b[3] = (byte) (i & 0xff);
        b[2] = (byte) (i >> 8 & 0xff);
        b[1] = (byte) (i >> 16 & 0xff);
        b[0] = (byte) (i >> 24 & 0xff);
        return b;
    }

    public byte[] short2BigByte(short i) {
        byte[] b = new byte[2];
        b[1] = (byte) (i & 0xff);
        b[0] = (byte) (i >> 8 & 0xff);
        return b;
    }

    public byte[] joinRoomData(int roomId) {
        Encoder enc = new Encoder();
        enc.addItem("type", "loginreq");
        enc.addItem("roomid", roomId);
        return getByte(enc.getResult());
    }

    public boolean parseLoginResult(byte[] respond) {
        boolean flag = false;
        if (respond.length <= 12) {
            return flag;
        }
        String dataStr = new String(respond, 12, respond.length - 12);
        if (StringUtils.contains(dataStr, "type@=loginres")) {
            flag = true;
        }
        return flag;
    }

    public byte[] getByte(String data) {
        byte[] b = data.getBytes();
        byte i = 0;
        int dataLen = b.length+12;
        ByteBuffer buff = ByteBuffer.allocate(dataLen);
        buff.put(int2LittleByte(dataLen));
        buff.put(int2LittleByte(dataLen));
        buff.put(short2BigByte(this.messageClient));
        buff.put(i);
        buff.put(i);
        buff.put(b);
        buff.flip();
        return buff.array();
    }

    public byte[] getJoinGroupRequest(int roomId, int groupId){
        Encoder enc = new Encoder();
        enc.addItem("type", "joingroup");
        enc.addItem("rid", roomId);
        enc.addItem("gid", groupId);
        return this.getByte(enc.getResult());
    }

    public byte[] getKeepAliveData(int timeStamp){
        Encoder enc = new Encoder();
        enc.addItem("type", "keeplive");
        enc.addItem("tick", timeStamp);
        return this.getByte(enc.getResult());
    }

    public byte[] long2Byte() {
        return null;
    }

    public String getHostName() {
        return hostName;
    }

    public int getPort() {
        return port;
    }

    public int getMaxBufferLen() {
        return maxBufferLen;
    }
}
