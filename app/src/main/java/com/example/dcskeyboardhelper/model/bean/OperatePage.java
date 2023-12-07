package com.example.dcskeyboardhelper.model.bean;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class OperatePage {
    @PrimaryKey(autoGenerate = true)
    private long pageId;//页面在数据库中的id
    @ColumnInfo
    private long parentId;//页面所属存档配置的id
    @ColumnInfo
    private String pageName;//页面名称
    @ColumnInfo
    private int position;//页面排序

    public OperatePage(long parentId, String pageName) {
        this.parentId = parentId;
        this.pageName = pageName;
    }

    public long getPageId() {
        return pageId;
    }

    public void setPageId(long pageId) {
        this.pageId = pageId;
    }

    public long getParentId() {
        return parentId;
    }

    public void setParentId(long parentId) {
        this.parentId = parentId;
    }

    public String getPageName() {
        return pageName;
    }

    public void setPageName(String pageName) {
        this.pageName = pageName;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
