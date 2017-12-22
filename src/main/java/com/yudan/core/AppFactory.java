package com.yudan.core;

public final class AppFactory {
    private static volatile App APP;

    public static App createDefaultApp(int roomId, int groupId) {
        //
        //
        //
        return new App(roomId, groupId);

    }

    /**
     * return a single App class
     * @param roomId
     * @param groupId
     * @return
     */
    public static App createSingleApp(int roomId, int groupId) {
        if (null == APP) {
            synchronized (AppFactory.class) {
                if (null == APP) {
                    APP = createDefaultApp(roomId, groupId);
                }
            }
        }
        return APP;
    }
}
