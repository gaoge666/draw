package com.server.draw.model;

public class PaintingMsg {
    private String roomId;
    private String drawContent;
    public PaintingMsg(){

    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getDrawContent() {
        return drawContent;
    }

    public void setDrawContent(String drawContent) {
        this.drawContent = drawContent;
    }
}
