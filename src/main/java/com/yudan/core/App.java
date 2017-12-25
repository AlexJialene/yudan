package com.yudan.core;

import com.yudan.config.Configuration;
import com.yudan.core.inj.Event;
import com.yudan.core.inj.KeepAliveInf;
import com.yudan.core.inj.MessageHandle;
import com.yudan.core.inj.ReceiveInf;
import com.yudan.thread.KeepAlive;
import com.yudan.thread.Receive;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class App implements KeepAliveInf , ReceiveInf , MessageHandle{
    private static final Logger LOGGER = LoggerFactory.getLogger(App.class);
    private Configuration configuration = Configuration.newInstance();
    private Receive receive;
    private KeepAlive keepAlive;
    private Event event;
    private int roomId;
    private int groupId;
    private boolean init = false;

    protected App(int roomId, int groupId) {
        this.roomId = roomId;
        this.groupId = groupId;
    }


    public void start() {
        this.connectServer();
        this.joinRoom();
        this.joinGroup();
        this.init = true;
        this.receive = new Receive(this);
        this.keepAlive = new KeepAlive(this);
        this.receive.start();
        this.keepAlive.start();

    }

    protected void joinGroup() {
        byte[] reqData = this.configuration.getJoinGroupRequest(this.roomId, this.groupId);
        this.event.send(reqData);
    }

    protected void joinRoom() {
        byte[] reqData = this.configuration.joinRoomData(this.roomId);
        byte[] result = this.event.join(reqData);
        if(this.configuration.parseLoginResult(result)){
            LOGGER.info("join room success!");
        }else{
            LOGGER.info("join room fail!");
        }
    }

    protected void connectServer() {
        LOGGER.info("connect to barrage Server of Douyu ...");
        event = new Processing(configuration.getHostName() , configuration.getPort() , configuration.getMaxBufferLen());
        event.setCallback(this);
    }

    public boolean isInit() {
        return init;
    }

    @Override
    public void receive() {
        this.event.receive();
    }

    public void keepAlive() {
        byte[] keepAliveRequest = configuration.getKeepAliveData((int)(System.currentTimeMillis() / 1000));
        this.event.send(keepAliveRequest);
    }

    @Override
    public void handle(Map<String , Object> msg) {
        if(msg.get("type") != null){
            if(msg.get("type").equals("error")){
                LOGGER.debug(msg.toString());
                this.init = false;
            }
            //判断消息类型
            if(msg.get("type").equals("chatmsg")){//弹幕消息
                LOGGER.info("弹幕消息===>" + msg.toString());
            } else if(msg.get("type").equals("dgb")){//赠送礼物信息
                LOGGER.info("礼物消息===>" + msg.toString());
            } else {
                LOGGER.info("其他消息===>" + msg.toString());
            }
        }
    }
}
