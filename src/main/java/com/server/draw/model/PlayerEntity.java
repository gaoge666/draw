package com.server.draw.model;

public class PlayerEntity {

    private String userId;
    private String roomId;

    public PlayerEntity(String userId, String roomId){
        this.userId = userId;
        this.roomId = roomId;
    }

    public PlayerEntity() {

    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomid(String roomId) {
        this.roomId = roomId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}

