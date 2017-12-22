package com.yudan.thread;

import com.yudan.core.App;

public class KeepAlive implements Runnable {
    private App app;

    public KeepAlive(App app) {
        this.app = app;
    }

    @Override
    public void run() {

    }
}
