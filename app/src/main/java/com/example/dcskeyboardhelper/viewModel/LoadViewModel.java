package com.example.dcskeyboardhelper.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.example.dcskeyboardhelper.base.BaseViewModel;
import com.example.dcskeyboardhelper.model.ProfileModel;
import com.example.dcskeyboardhelper.model.bean.Profile;

import java.util.List;

public class LoadViewModel extends BaseViewModel<ProfileModel> {
    private ProfileModel model;
    public LoadViewModel(@NonNull Application application) {
        super(application);
        model = new ProfileModel(application);
    }

    public LiveData<List<Profile>> queryAll(){
        return model.queryAll();
    }

    public long insert(Profile profile){
        return model.insert(profile);
    }

    public void delete(long id){
        model.delete(id);
    }

    public long getProfileById(long id){
        return model.getProfileById(id);
    }
}
