package com.yudan.thread;

import com.yudan.core.inj.KeepAliveInf;

public class KeepAlive extends Thread {
    private KeepAliveInf app;

    public KeepAlive(KeepAliveInf app) {
        this.app = app;
    }

    @Override
    public void run() {
        while (this.app.isInit()){
            app.keepAlive();

            try {
                Thread.sleep(45000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
}
