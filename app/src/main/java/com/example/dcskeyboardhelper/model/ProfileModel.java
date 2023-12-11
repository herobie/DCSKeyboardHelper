package com.example.dcskeyboardhelper.model;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.example.dcskeyboardhelper.base.BaseModel;
import com.example.dcskeyboardhelper.database.ProfileDao;
import com.example.dcskeyboardhelper.database.UserDatabase;
import com.example.dcskeyboardhelper.model.bean.Profile;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class ProfileModel extends BaseModel {
    private final ProfileDao profileDao;

    public ProfileModel(Context context) {
        UserDatabase userDataBase = UserDatabase.getInstance(context.getApplicationContext());
        profileDao = userDataBase.getProfileDao();
    }

    public LiveData<List<Profile>> queryAll(){
        Future<LiveData<List<Profile>>> future = executorService.submit(profileDao::queryAll);

        try {
            return future.get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            return null;
        }
    }

    public long insert(Profile profile){
        Future<Long> future = executorService.submit(() -> profileDao.insertProfile(profile));

        try {
            return future.get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            return -1;
        }
    }

    public void delete(long id){
        executorService.submit(() -> profileDao.deleteProfile(id));
    }

    public long getProfileById(long id){
        Future<Long> future = executorService.submit(() -> profileDao.getProfileById(id));

        try {
            return future.get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            return -1;
        }
    }
}
