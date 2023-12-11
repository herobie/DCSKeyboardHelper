package com.example.dcskeyboardhelper.model;

import android.content.Context;
import android.util.Log;

import com.example.dcskeyboardhelper.base.BaseModel;
import com.example.dcskeyboardhelper.database.ActionModuleDao;
import com.example.dcskeyboardhelper.database.UserDatabase;
import com.example.dcskeyboardhelper.model.bean.ActionModule;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class ActionModuleModel extends BaseModel {
    private final ActionModuleDao actionModuleDao;

    public ActionModuleModel(Context context) {
        UserDatabase userDataBase = UserDatabase.getInstance(context.getApplicationContext());
        actionModuleDao = userDataBase.getActionModuleDao();
    }

    public List<ActionModule> queryAllModules(long pageId){
        Future<List<ActionModule>> future = executorService.submit(() -> actionModuleDao.queryAll(pageId));

        try {
            return future.get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            return null;
        }
    }

    public long insertModule(ActionModule module){
        Future<Long> future = executorService.submit(() -> actionModuleDao.insertModule(module));

        try {
            return future.get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            return -1;
        }
    }

    public void deleteModule(long id){
        executorService.submit(() -> actionModuleDao.deleteModule(id));
    }

    public void deleteModuleInPage(long pageId){
        executorService.submit(() -> actionModuleDao.deleteModuleInPage(pageId));
    }

    public void updateModule(ActionModule...module){
        executorService.submit(() -> actionModuleDao.updateModule(module));
    }

    public List<ActionModule> getStarredModule(){
        Future<List<ActionModule>> future = executorService.submit(actionModuleDao::getStarredModule);

        try {
            return future.get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            return null;
        }
    }

    //当发生模块交换位置时调用，用于更新模块位置数据
    public void swapModule(List<ActionModule> modules){
        Log.d("MainActivity", "swapModule: ");
        executorService.submit(() -> {
            for (ActionModule module : modules){
                actionModuleDao.updateModule(module);
            }
        });
    }
}
