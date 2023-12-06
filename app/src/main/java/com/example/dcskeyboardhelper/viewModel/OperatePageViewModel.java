package com.example.dcskeyboardhelper.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.example.dcskeyboardhelper.base.BaseViewModel;
import com.example.dcskeyboardhelper.model.ActionModuleModel;
import com.example.dcskeyboardhelper.model.bean.ActionModule;
import com.example.dcskeyboardhelper.model.bean.OperatePage;

import java.util.List;

public class OperatePageViewModel extends BaseViewModel<ActionModuleModel> {
    public OperatePageViewModel(@NonNull Application application) {
        super(application);
        model = new ActionModuleModel(application);
    }

    public List<ActionModule> getAllModules(long pageId){
        return model.queryAllModules(pageId);
    }

    public long insertModule(ActionModule module){
        return model.insertModule(module);
    }

    public void updateModule(ActionModule...module){
        model.updateModule(module);
    }

    public void deleteModule(long id){
        model.deleteModule(id);
    }

    public void swapModule(List<ActionModule> modules){
        model.swapModule(modules);
    }
}
