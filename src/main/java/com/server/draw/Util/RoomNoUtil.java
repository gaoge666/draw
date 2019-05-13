package com.server.draw.Util;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class RoomNoUtil {

    private static int total = 10000;
    private static Set<Integer> unusedRoomNo = new HashSet<>();
    private static Set<Integer> usedRoomNo = new HashSet<>();

    static {
        for(int i = 0; i < total; i++) {
            unusedRoomNo.add(i);
        }
    }

    public synchronized static String getRoomNo() {
        if(unusedRoomNo.size() == 0) {
            return null;
        }
        Iterator<Integer> itor = unusedRoomNo.iterator();
        Integer roomNo = itor.next();
        itor.remove();
        usedRoomNo.add(roomNo);
        return roomNo.toString();
    }

    public synchronized static void returnRoomNo(String roomNoStr) {

        try {
            Integer roomNo = Integer.valueOf(roomNoStr);
            usedRoomNo.remove(roomNo);
            unusedRoomNo.add(roomNo);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}
