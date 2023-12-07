package com.example.dcskeyboardhelper.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.dcskeyboardhelper.model.ActionModuleModel;
import com.example.dcskeyboardhelper.model.Constant;
import com.example.dcskeyboardhelper.model.OperatePageModel;
import com.example.dcskeyboardhelper.model.adapter.FragmentsAdapter;
import com.example.dcskeyboardhelper.model.adapter.SupportDebugAdapter;
import com.example.dcskeyboardhelper.model.bean.ActionModule;
import com.example.dcskeyboardhelper.model.bean.OperatePage;
import com.example.dcskeyboardhelper.ui.debug.OperatePageDebugFragment;

import java.util.ArrayList;
import java.util.List;

public class ModuleDebugViewModel extends AndroidViewModel {
    private long profileId;
    private final OperatePageModel pageModel;
    private final ActionModuleModel actionModuleModel;
    private FragmentManager fragmentManager;
    private FragmentsAdapter<OperatePageDebugFragment> operatePageAdapter;
    private List<OperatePage> pages;
    private OperatePage currentPage;
    private final List<ActionModule> starredModules = new ArrayList<>();
    private SupportDebugAdapter supportDebugAdapter;
    private final MutableLiveData<Boolean> isStarredModulesReady = new MutableLiveData<>();
    public ModuleDebugViewModel(@NonNull Application application) {
        super(application);
        pageModel = new OperatePageModel(application);
        actionModuleModel = new ActionModuleModel(application);
        isStarredModulesReady.setValue(false);
    }

    public void updateModule(ActionModule...module){
        actionModuleModel.updateModule(module);
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

    public void updatePage(OperatePage...pages){
        pageModel.updatePage(pages);
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

    public List<ActionModule> getStarredModules() {
        return starredModules;
    }

    public SupportDebugAdapter getSupportDebugAdapter() {
        return supportDebugAdapter;
    }

    public void setSupportDebugAdapter(SupportDebugAdapter supportDebugAdapter) {
        this.supportDebugAdapter = supportDebugAdapter;
    }

    public MutableLiveData<Boolean> getIsStarredModulesReady() {
        return isStarredModulesReady;
    }

    public List<OperatePage> getPages() {
        if(pages == null){
            pages = pageModel.getOperatePage(Constant.CURRENT_PROFILE_ID);
        }
        return pages;
    }
}
