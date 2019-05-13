package com.server.draw.model;

public class PaintingEntity {

    private String userId;
    private String roomId;
    private String targetName;

    public PaintingEntity(String userId, String roomId, String targetName) {
        this.userId = userId;
        this.roomId = roomId;
        this.targetName = targetName;
    }

    public PaintingEntity() {

    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getTargetName() {
        return targetName;
    }

    public void setTargetName(String targetName) {
        this.targetName = targetName;
    }
}
