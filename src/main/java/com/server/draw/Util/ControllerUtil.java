package com.server.draw.Util;

public class ControllerUtil {

    public static String genRoomId()
    {
        long curMill = System.currentTimeMillis();
        return Long.toString(curMill % 2000000000000L, 36);
    }

}
