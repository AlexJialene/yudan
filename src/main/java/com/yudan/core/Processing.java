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
import java.nio.ByteBuffer;

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
        String dataStr;
        try {
            int recvLen = bufferedInputStream.read(recvByte, 0, recvByte.length);
            ByteBuffer byteBuffer = ByteBuffer.allocate(recvLen-12);
            byteBuffer.put(recvByte , 12 , recvLen-12);
            byteBuffer.flip();
            dataStr = new String(byteBuffer.array());
            while (dataStr.lastIndexOf("type@=") > 5) {
                ReceiveMsg msgView = new ReceiveMsg(StringUtils.substring(dataStr, dataStr.lastIndexOf("type@=")));
                callback(msgView);
                dataStr = StringUtils.substring(dataStr, 0, dataStr.lastIndexOf("type@=") - 12);
            }
            ReceiveMsg msgView = new ReceiveMsg(dataStr);
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
