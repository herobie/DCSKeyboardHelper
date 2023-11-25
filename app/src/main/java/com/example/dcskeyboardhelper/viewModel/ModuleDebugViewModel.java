package com.example.dcskeyboardhelper.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.dcskeyboardhelper.model.ActionModuleModel;
import com.example.dcskeyboardhelper.model.OperatePageModel;
import com.example.dcskeyboardhelper.model.adapter.FragmentsAdapter;
import com.example.dcskeyboardhelper.model.bean.ActionModule;
import com.example.dcskeyboardhelper.model.bean.OperatePage;
import com.example.dcskeyboardhelper.ui.debug.OperatePageFragment;

import java.util.ArrayList;
import java.util.List;

public class ModuleDebugViewModel extends AndroidViewModel {
    private long profileId;
    private final OperatePageModel pageModel;
    private ActionModuleModel actionModuleModel;
    private FragmentManager fragmentManager;
    private FragmentsAdapter<OperatePageFragment> operatePageAdapter;
    private OperatePage currentPage;
    private MutableLiveData<List<String>> statusDisplayed;//保存左侧support页面的文字
    public ModuleDebugViewModel(@NonNull Application application) {
        super(application);
        pageModel = new OperatePageModel(application);
        actionModuleModel = new ActionModuleModel(application);
        statusDisplayed = new MutableLiveData<>();
        statusDisplayed.postValue(new ArrayList<>());
    }

    public long insertModule(ActionModule module){
        return actionModuleModel.insertModule(module);
    }

    public LiveData<List<OperatePage>> getAllOperatePageLiveData(){
        return pageModel.getOperatePageLiveData();
    }

    public long insertPage(OperatePage page){
        return pageModel.insertPage(page);
    }

    public void deletePage(long id){
        pageModel.deletePage(id);
    }

    public FragmentManager getFragmentManager() {
        return fragmentManager;
    }

    public void setFragmentManager(FragmentManager fragmentManager) {
        this.fragmentManager = fragmentManager;
    }

    public FragmentsAdapter<OperatePageFragment> getOperatePageAdapter() {
        return operatePageAdapter;
    }

    public void setOperatePageAdapter(FragmentsAdapter<OperatePageFragment> operatePageAdapter) {
        this.operatePageAdapter = operatePageAdapter;
    }

    public OperatePage getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(OperatePage currentPage) {
        this.currentPage = currentPage;
    }

    public void setProfileId(long profileId) {
        this.profileId = profileId;
    }

    public long getProfileId() {
        return profileId;
    }

    public MutableLiveData<List<String>> getStatusDisplayed() {
        return statusDisplayed;
    }
}
