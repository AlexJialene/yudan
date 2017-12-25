package com.yudan.core;

import com.yudan.core.inj.Event;
import com.yudan.core.inj.MessageHandle;
import com.yudan.kit.ReceiveMsg;
import org.apache.commons.lang3.StringUtils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

public class Processing implements Event {
    private Socket socket;
    private BufferedOutputStream bufferedOutputStream;
    private BufferedInputStream bufferedInputStream;
    private int maxBufferLen;
    private MessageHandle handle;

    public Processing(String hostName, int port, int maxBufferLen) {
        try {
            String host = InetAddress.getByName(hostName).getHostAddress();
            this.socket = new Socket(host, port);
            this.bufferedInputStream = new BufferedInputStream(this.socket.getInputStream());
            this.bufferedOutputStream = new BufferedOutputStream(this.socket.getOutputStream());
            this.maxBufferLen = maxBufferLen;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void receive() {
        byte[] recvByte = new byte[maxBufferLen];
        //定义服务器返回信息的字符串
        String dataStr;
        try {
            //读取服务器返回信息，并获取返回信息的整体字节长度
            int recvLen = bufferedInputStream.read(recvByte, 0, recvByte.length);

            //根据实际获取的字节数初始化返回信息内容长度
            byte[] realBuf = new byte[recvLen];
            //按照实际获取的字节长度读取返回信息
            //System.arraycopy(recvByte, 0, realBuf, 0, recvLen);
            //根据TCP协议获取返回信息中的字符串信息
            dataStr = new String(recvByte, 12, recvLen-12);
            //循环处理socekt黏包情况
            while(dataStr.lastIndexOf("type@=") > 5){
                //对黏包中最后一个数据包进行解析
                ReceiveMsg msgView = new ReceiveMsg(StringUtils.substring(dataStr, dataStr.lastIndexOf("type@=")));
                //分析该包的数据类型，以及根据需要进行业务操作
                callback(msgView);
                //处理黏包中的剩余部分
                dataStr = StringUtils.substring(dataStr, 0, dataStr.lastIndexOf("type@=") - 12);
            }
            //对单一数据包进行解析
            ReceiveMsg msgView = new ReceiveMsg(dataStr);
            //分析该包的数据类型，以及根据需要进行业务操作
            callback(msgView);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void send(byte[] reqData) {
        try {
            bufferedOutputStream.write(reqData, 0, reqData.length);
            bufferedOutputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void setCallback(MessageHandle handle) {
        this.handle = handle;
    }

    @Override
    public byte[] join(byte[] reqData) {
        try {
            bufferedOutputStream.write(reqData, 0, reqData.length);
            bufferedOutputStream.flush();
            byte[] recvByte = new byte[maxBufferLen];
            bufferedInputStream.read(recvByte, 0, recvByte.length);
            return recvByte;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    protected void callback(ReceiveMsg msg) {
        if (null == this.handle)
            return;
        this.handle.handle(msg.getMessageList());
    }

}
