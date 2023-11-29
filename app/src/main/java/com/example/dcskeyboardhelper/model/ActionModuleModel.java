package com.example.dcskeyboardhelper.model;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.example.dcskeyboardhelper.base.BaseModel;
import com.example.dcskeyboardhelper.database.ActionModuleDao;
import com.example.dcskeyboardhelper.database.UserDatabase;
import com.example.dcskeyboardhelper.model.bean.ActionModule;
import com.example.dcskeyboardhelper.model.bean.SupportItemData;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class ActionModuleModel extends BaseModel {
    private ActionModuleDao actionModuleDao;
    private Context context;

    public ActionModuleModel(Context context) {
        this.context = context;
        UserDatabase userDataBase = UserDatabase.getInstance(context.getApplicationContext());
        actionModuleDao = userDataBase.getActionModuleDao();
    }

    public LiveData<List<ActionModule>> queryAllModules(long pageId){
        Future<LiveData<List<ActionModule>>> future = executorService.submit(new Callable<LiveData<List<ActionModule>>>() {
            @Override
            public LiveData<List<ActionModule>> call() throws Exception {
                return actionModuleDao.queryAll(pageId);
            }
        });

        try {
            return future.get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            return null;
        }
    }

    public long insertModule(ActionModule module){
        Future<Long> future = executorService.submit(new Callable<Long>() {
            @Override
            public Long call() throws Exception {
                return actionModuleDao.insertModule(module);
            }
        });

        try {
            return future.get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            return -1;
        }
    }

    public void deleteModule(long id){
        executorService.submit(new Runnable() {
            @Override
            public void run() {
                actionModuleDao.deleteModule(id);
            }
        });
    }

    public void updateModule(ActionModule...module){
        executorService.submit(new Runnable() {
            @Override
            public void run() {
                actionModuleDao.updateModule(module);
            }
        });
    }

    public List<ActionModule> getStarredModule(){
        Future<List<ActionModule>> future = executorService.submit(new Callable<List<ActionModule>>() {
            @Override
            public List<ActionModule> call() throws Exception {
                return actionModuleDao.getStarredModule();
            }
        });

        try {
            return future.get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<SupportItemData> getSupportData(){
        List<SupportItemData> list = new ArrayList<>();
        for (ActionModule module : getStarredModule()){
            SupportItemData supportItemData = new SupportItemData(module.getId(), module.getDesc(), module.getStepsDesc());
            list.add(supportItemData);
        }
        return list;
    }
}
