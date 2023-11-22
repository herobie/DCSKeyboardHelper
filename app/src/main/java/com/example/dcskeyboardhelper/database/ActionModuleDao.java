package com.example.dcskeyboardhelper.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.dcskeyboardhelper.model.bean.ActionModule;

import java.util.List;

@Dao
public interface ActionModuleDao {
    @Query("SELECT * FROM ActionModule ORDER BY id ASC")
    LiveData<List<ActionModule>> queryAll();//获取全部

    @Insert
    long insertModule(ActionModule module);

    @Update
    void updateModule(ActionModule module);

    @Query("DELETE FROM ActionModule WHERE id=:id")
    void deleteModule(long id);
}