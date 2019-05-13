package com.server.draw.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="target")
public class TargetEntity {

    @Id
    @Column(name = "name")
    private String name;

    @Column(name = "level")
    private int level;

    @Column(name = "hint")
    private String hint;

    public TargetEntity() {

    }

    public TargetEntity(String name, int level, String hint) {
        this.name = name;
        this.level = level;
        this.hint = hint;
    }

    public String getName() {
        return name;
    }

    public int getLevel() {
        return level;
    }

    public String getHint() {
        return hint;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void setHint(String hint) {
        this.hint = hint;
    }
}
