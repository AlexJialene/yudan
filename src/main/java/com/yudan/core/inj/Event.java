package com.yudan.core.inj;

public interface Event {

    void receive();

    void send(byte[] reqData);

    void setCallback(MessageHandle handle);

    byte[] join(byte[] reqData);
}
