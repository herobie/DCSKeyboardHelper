package com.example.dcskeyboardhelper.model;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.example.dcskeyboardhelper.base.BaseModel;
import com.example.dcskeyboardhelper.database.ActionModuleDao;
import com.example.dcskeyboardhelper.database.UserDatabase;
import com.example.dcskeyboardhelper.model.bean.ActionModule;

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

    public LiveData<List<ActionModule>> queryAllModules(){
        Future<LiveData<List<ActionModule>>> future = executorService.submit(new Callable<LiveData<List<ActionModule>>>() {
            @Override
            public LiveData<List<ActionModule>> call() throws Exception {
                return actionModuleDao.queryAll();
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
}
