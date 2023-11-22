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
    @Query("SELECT * FROM OperatePage ORDER BY pageId ASC")
    LiveData<List<OperatePage>> getOperatePageLiveData();//获取全部page的livedata

    @Query("SELECT * FROM OperatePage ORDER BY pageId ASC")
    List<OperatePage> getOperatePage();//获取全部

    @Insert
    long insertPage(OperatePage page);


}
