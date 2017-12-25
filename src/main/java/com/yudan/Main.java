package com.yudan;

import com.yudan.core.App;
import com.yudan.core.AppFactory;

public class Main {

    public static void main(String[] args) {
        App app = AppFactory.createDefaultApp(518512 , -9999);
        app.start();
    }
}
