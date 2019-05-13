package com.server.draw.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class RoomEntity {
    public RoomEntity(){}
    public RoomEntity(String roomId,
                      String userId,
                      String roomName) {
        this.roomId = roomId;
        this.userId = userId;
        this.roomName = roomName;
    }

    //roomId
    private String roomId;
    //房主id
    private String userId;
    //房间名
    private String roomName;
    //可容纳人数
    private int maxSize = 6;
    //词库难度
    private int level = 1;
    //是否提供高级图形
    private boolean picProvided = false;
    //是否能够自己决定绘画图形
    private boolean diyEnable = false;
    //是否为私有房间(默认为私有房间)
    private boolean isPrivate = false;
    //房间内人物信息
    private List<PlayerEntity> players = new ArrayList<>();
    //已画过的目标图形
    private List<PaintingEntity> paintings = new ArrayList<>();
    //是否已经开始
    private boolean isStarted = false;

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public int getMaxSize() {
        return maxSize;
    }

    public void setMaxSize(int maxSize) {
        this.maxSize = maxSize;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public boolean isPicProvided() {
        return picProvided;
    }

    public void setPicProvided(boolean picProvided) {
        this.picProvided = picProvided;
    }

    public boolean isDiyEnable() {
        return diyEnable;
    }

    public void setDiyEnable(boolean diyEnable) {
        this.diyEnable = diyEnable;
    }

    public boolean isPrivate() {
        return isPrivate;
    }

    public void setPrivate(boolean aPrivate) {
        this.isPrivate = aPrivate;
    }

    public boolean isStarted() {
        return isStarted;
    }

    public void setStarted(boolean started) {
        isStarted = started;
    }

    public List<PlayerEntity> getPlayers() {
        return players;
    }

    public List<PaintingEntity> getPaintings() {
        return paintings;
    }

    // 包括玩家和房主
    public int getSize() {
        return players.size() + 1;
    }

    public boolean hasPlayer(String userId) {
        boolean found = false;
        for (PlayerEntity player: players) {
            if(player.getUserId().equals(userId)) {
                found = true;
            }
        }
        return found || this.userId.equals(userId);
    }

    // 原子操作
    synchronized public void addPlayer(PlayerEntity player)
    {
        players.add(player);
    }

    // 原子操作
    synchronized public void removePlayer(String userId)
    {
        Iterator<PlayerEntity> playersItor = players.iterator();
        while(playersItor.hasNext()) {
            if(playersItor.next().getUserId().equals(userId)) {
                playersItor.remove();
            }
        }
    }

    public void addPainting(PaintingEntity painting)
    {
        paintings.add(painting);
    }

}
