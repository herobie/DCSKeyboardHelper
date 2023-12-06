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
import com.example.dcskeyboardhelper.model.bean.SupportItemData;
import com.example.dcskeyboardhelper.ui.debug.OperatePageDebugFragment;
import com.example.dcskeyboardhelper.ui.debug.SupportDebugFragment;

import java.util.ArrayList;
import java.util.List;

public class ModuleDebugViewModel extends AndroidViewModel {
    private long profileId;
    private final OperatePageModel pageModel;
    private final ActionModuleModel actionModuleModel;
    private FragmentManager fragmentManager;
    private FragmentsAdapter<OperatePageDebugFragment> operatePageAdapter;
    private OperatePage currentPage;
    private final MutableLiveData<List<SupportItemData>> statusDisplayed;//保存左侧support页面的文字
    public ModuleDebugViewModel(@NonNull Application application) {
        super(application);
        pageModel = new OperatePageModel(application);
        actionModuleModel = new ActionModuleModel(application);
        statusDisplayed = new MutableLiveData<>();
        statusDisplayed.postValue(getSupportData());
    }

    public long insertModule(ActionModule module){
        return actionModuleModel.insertModule(module);
    }

    public LiveData<List<OperatePage>> getAllOperatePageLiveData(long profileId){
        return pageModel.getOperatePageLiveData(profileId);
    }

    public long insertPage(OperatePage page){
        return pageModel.insertPage(page);
    }

    public void deletePage(long id){
        pageModel.deletePage(id);//先执行删除页面
        actionModuleModel.deleteModuleInPage(id);//再把页面下所有的模块也删除了
    }

    public FragmentManager getFragmentManager() {
        return fragmentManager;
    }

    public void setFragmentManager(FragmentManager fragmentManager) {
        this.fragmentManager = fragmentManager;
    }

    public FragmentsAdapter<OperatePageDebugFragment> getOperatePageAdapter() {
        return operatePageAdapter;
    }

    public void setOperatePageAdapter(FragmentsAdapter<OperatePageDebugFragment> operatePageAdapter) {
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

    public List<SupportItemData> getSupportData(){
        return actionModuleModel.getSupportData();
    }

    public MutableLiveData<List<SupportItemData>> getStatusDisplayed() {
        return statusDisplayed;
    }
}
