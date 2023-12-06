package com.example.dcskeyboardhelper.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.dcskeyboardhelper.model.bean.OperatePage;

import java.util.List;

@Dao
public interface OperatePageDao {
    @Query("SELECT * FROM OperatePage WHERE parentId=:parentId ORDER BY pageId ASC")
    LiveData<List<OperatePage>> getOperatePageLiveData(long parentId);//获取全部page的livedata

    @Query("SELECT * FROM OperatePage WHERE parentId=:parentId  ORDER BY pageId ASC")
    List<OperatePage> getOperatePage(long parentId);//获取全部

    @Insert
    long insertPage(OperatePage page);

    @Query("DELETE FROM OperatePage WHERE pageId=:id")
    void deletePage(long id);
}
