package com.server.draw.model;


public class RInfo {

    private String status;
    private Object info;

    public RInfo() {

    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setInfo(Object info) {
        this.info = info;
    }

    public String getStatus() {
        return status;
    }

    public Object getInfo() {
        return info;
    }
}
