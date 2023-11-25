package com.example.dcskeyboardhelper.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.dcskeyboardhelper.model.bean.Profile;

import java.util.List;

@Dao
public interface ProfileDao {
    @Query("SELECT * FROM Profile ORDER BY id DESC")
    LiveData<List<Profile>> queryAll();//获取全部

    @Insert
    long insertProfile(Profile profile);//增加配置

    @Query("DELETE FROM Profile WHERE id=:id")
    void deleteProfile(long id);//删除指定配置

    @Query("SELECT * FROM Profile WHERE id =:id")
    long getProfileById(long id);//根据id获取指定配置
}
