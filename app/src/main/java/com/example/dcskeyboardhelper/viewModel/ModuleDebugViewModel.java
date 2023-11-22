package com.example.dcskeyboardhelper.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.dcskeyboardhelper.model.ActionModuleModel;
import com.example.dcskeyboardhelper.model.OperatePageModel;
import com.example.dcskeyboardhelper.model.bean.ActionModule;
import com.example.dcskeyboardhelper.model.bean.OperatePage;

import java.util.List;

public class ModuleDebugViewModel extends AndroidViewModel {
    private OperatePageModel pageModel;
    private ActionModuleModel actionModuleModel;
    private List<Fragment> pages, modules;
    private FragmentManager fragmentManager;
    private List<OperatePage> operatePages;
    public ModuleDebugViewModel(@NonNull Application application) {
        super(application);
        pageModel = new OperatePageModel(application);
        actionModuleModel = new ActionModuleModel(application);
    }

    public LiveData<List<OperatePage>> getAllOperatePageLiveData(){
        return pageModel.getOperatePageLiveData();
    }

    public List<OperatePage> getOperatePages() {
        if (operatePages == null){
            operatePages = pageModel.getOperatePage();
        }
        return operatePages;
    }

    public long insertPage(OperatePage page){
        return pageModel.insertPage(page);
    }

    public void deletePage(long id){
        pageModel.deletePage(id);
    }

    public LiveData<List<ActionModule>> getAllModules(){
        return actionModuleModel.queryAllModules();
    }

    public long insertModule(ActionModule module){
        return actionModuleModel.insertModule(module);
    }

    public void deleteModule(long id){
        actionModuleModel.deleteModule(id);
    }

    public List<Fragment> getPages() {
        return pages;
    }

    public void setPages(List<Fragment> pages) {
        this.pages = pages;
    }

    public List<Fragment> getModules() {
        return modules;
    }

    public void setModules(List<Fragment> modules) {
        this.modules = modules;
    }

    public FragmentManager getFragmentManager() {
        return fragmentManager;
    }

    public void setFragmentManager(FragmentManager fragmentManager) {
        this.fragmentManager = fragmentManager;
    }

    public void setOperatePages(List<OperatePage> operatePages) {
        this.operatePages = operatePages;
    }
}
