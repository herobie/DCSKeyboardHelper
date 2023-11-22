package com.example.dcskeyboardhelper.model.bean;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Profile {
    @PrimaryKey(autoGenerate = true)
    private long id;
    @ColumnInfo
    private String title;
    @ColumnInfo
    private String desc;
    @ColumnInfo
    private long createdDate;

    public Profile(String title, String desc, long createdDate) {
        this.title = title;
        this.desc = desc;
        this.createdDate = createdDate;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public long getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(long createdDate) {
        this.createdDate = createdDate;
    }
}
