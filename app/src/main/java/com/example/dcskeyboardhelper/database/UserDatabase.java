package com.example.dcskeyboardhelper.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.dcskeyboardhelper.model.bean.ActionModule;
import com.example.dcskeyboardhelper.model.bean.OperatePage;
import com.example.dcskeyboardhelper.model.bean.Profile;

@Database(entities = {Profile.class, ActionModule.class, OperatePage.class}, version = 1, exportSchema = false)
public abstract class UserDatabase extends RoomDatabase {
    private static UserDatabase INSTANCE;
    public static synchronized UserDatabase getInstance(Context context){
        if (INSTANCE == null){
            INSTANCE = Room.databaseBuilder(context.getApplicationContext() , UserDatabase.class , "UserData")
                    .build();
        }
        return INSTANCE;
    }

    public abstract ProfileDao getProfileDao();

    public abstract ActionModuleDao getActionModuleDao();

    public abstract OperatePageDao getOperatePageDao();
}
