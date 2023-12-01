package com.example.dcskeyboardhelper.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.dcskeyboardhelper.model.OperatePageModel;
import com.example.dcskeyboardhelper.model.bean.OperatePage;

import java.util.List;

public class SimulateViewModel extends AndroidViewModel {
    private long profileId;
    private final OperatePageModel operatePageModel;
    private FragmentManager fragmentManager;
    public SimulateViewModel(@NonNull Application application) {
        super(application);
        operatePageModel = new OperatePageModel(application);
    }

    public LiveData<List<OperatePage>> getAllOperatePageLiveData(){
        return operatePageModel.getOperatePageLiveData();
    }

    public long getProfileId() {
        return profileId;
    }

    public void setProfileId(long profileId) {
        this.profileId = profileId;
    }

    public FragmentManager getFragmentManager() {
        return fragmentManager;
    }

    public void setFragmentManager(FragmentManager fragmentManager) {
        this.fragmentManager = fragmentManager;
    }
}
