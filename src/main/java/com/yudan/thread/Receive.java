package com.yudan.thread;

import com.yudan.core.inj.ReceiveInf;

public class Receive extends Thread {
    private ReceiveInf app;

    public Receive(ReceiveInf app) {
        this.app = app;
    }

    @Override
    public void run() {
        while (app.isInit()) {
            app.receive();
        }
    }
}
