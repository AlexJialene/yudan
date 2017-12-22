package com.yudan.core;

import com.yudan.config.Config;
import com.yudan.thread.KeepAlive;
import com.yudan.thread.Receive;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.Socket;

public class App {
    private static final Logger LOGGER = LoggerFactory.getLogger(App.class);
    private Config config = Config.newInstance();
    private Receive receive;
    private KeepAlive keepAlive;
    private Socket socket;
    private int roomId;
    private int groupId;
    private boolean read;
    private BufferedOutputStream bufferedOutputStream;
    private BufferedInputStream bufferedInputStream;

    protected App(int roomId, int groupId) {
        this.roomId = roomId;
        this.groupId = groupId;
    }


    public void start(){
        //connect to Server
        this.connectServer();
        this.joinRoom();
        this.joinGroup();
    }

    protected void joinGroup() {

    }

    protected void joinRoom() {

    }

    protected void connectServer() {
        LOGGER.info("connect to barrage Server of Douyu ...");
        try {
            this.socket = new Socket(config.getHostName() , config.getPort());
            this.bufferedInputStream = new BufferedInputStream(this.socket.getInputStream());
            this.bufferedOutputStream = new BufferedOutputStream(this.socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


}
