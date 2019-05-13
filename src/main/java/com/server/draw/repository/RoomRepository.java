package com.server.draw.repository;

import com.server.draw.Util.RoomNoUtil;
import com.server.draw.model.PlayerEntity;
import com.server.draw.model.RoomEntity;

import java.util.Hashtable;
import java.util.Iterator;

public class RoomRepository {

    private static Hashtable<String, RoomEntity> rooms = new Hashtable<>();

    // 判断房间是否存在
    public static boolean hasRoom(String roomId) {
        return rooms.get(roomId) != null;
    }

    // 获取房间
    public static RoomEntity getRoom(String roomId) {

        return rooms.get(roomId);
    }

    // 判断玩家是否在房间中
    public static boolean inRoom(String userId, String roomId) {

        RoomEntity room = rooms.get(roomId);
        if(room != null) {
            return room.hasPlayer(userId);
        }
        return false;
    }

    // 判断玩家是不是房间主
    public static boolean isOwner(String userId, String roomId) {

        RoomEntity room = rooms.get(roomId);
        if(room != null) {
            //String之间的比较，使用equals()，不用==
            return room.getUserId().equals(userId);
        }
        return false;
    }

    // 原子操作
    // 添加房间，同时将房间主添加到玩家记录
    synchronized public static void addRoom(String roomId, RoomEntity room) {

        // 如果该房间不存在，可以添加
        if(!hasRoom(roomId)) {
            // 将房间主同时加入玩家记录中
            PlayerEntity newPlayer = new PlayerEntity(room.getUserId(), roomId);
            rooms.put(roomId, room);
            PlayerRepository.addPlayer(room.getUserId(), newPlayer);
        }
    }

    // 原子操作
    // 删除房间，同时将房间主和其它所有玩家从玩家记录中删除
    synchronized public static void removeRoom(String roomId) {

        RoomEntity room = rooms.get(roomId);
        // 如果房间存在，可以删除
        if(room != null) {
            // 将房主和房间内的其他所有玩家同时从玩家记录中删除，删除房间
            PlayerRepository.removePlayer(room.getUserId());
            for (PlayerEntity player : room.getPlayers()) {
                PlayerRepository.removePlayer(player.getUserId());
            }
            rooms.remove(roomId);
            RoomNoUtil.returnRoomNo(roomId);
        }
    }

    // 原子操作
    // 将用户添加到房间中，
    synchronized public static void addUserToRoom(String userId, String roomId) {

        RoomEntity room = rooms.get(roomId);
        // 如果房间存在，房间没满，玩家并没有进行游戏，可以加
        if(room != null && room.getSize() < room.getMaxSize() && !PlayerRepository.isPlaying(userId)) {
            PlayerEntity newPlayer = new PlayerEntity(userId, roomId);
            // 将newPlayer同时加入房间和玩家记录中
            room.addPlayer(newPlayer);
            PlayerRepository.addPlayer(userId, newPlayer);
        }
    }

    // 原子操作
    // 将用户从房间中删除，
    synchronized public static void removePlayerFromRoom(String userId, String roomId) {

        RoomEntity room = rooms.get(roomId);
        // 如果房间存在，玩家是在该房间中
        if(room != null && inRoom(userId, roomId)) {
            // 将player同时从房间和玩家记录中删除
            room.removePlayer(userId);
            PlayerRepository.removePlayer(userId);
        }
    }


    // 将用户随机分配到某一房间
    public static String dispatchUser(String userId) {

        String roomId = null;
        int maxSize = 0;
        Iterator<RoomEntity> roomItor = rooms.values().iterator();
        while(roomItor.hasNext()) {
            RoomEntity room = roomItor.next();
            if(room.getSize() < room.getMaxSize() && room.getSize() > maxSize
                    && (!room.isStarted()) && (!room.isPrivate())) {
                //RoomRepository.addUserToRoom(userId, roomId);
                roomId = room.getRoomId();
                maxSize = room.getSize();
            }
        }

        if(roomId!=null){//&&RoomRepository.inRoom(userId, roomId)) {
            // TODO 可能出现竞争情况导致用户未加入
            RoomRepository.addUserToRoom(userId, roomId);
            if(!RoomRepository.inRoom(userId, roomId))
            {
                return null;

            }
            return roomId;
        } else {
            //返回一个新的房间
            //roomId = ControllerUtil.genRoomId();
            roomId = RoomNoUtil.getRoomNo();
            RoomRepository.addRoom(roomId, new RoomEntity(roomId,userId,userId+"'s room"));
            return roomId;
        }
    }


}
