package com.example.dcskeyboardhelper.model;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.example.dcskeyboardhelper.base.BaseModel;
import com.example.dcskeyboardhelper.database.OperatePageDao;
import com.example.dcskeyboardhelper.database.UserDatabase;
import com.example.dcskeyboardhelper.model.bean.OperatePage;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class OperatePageModel extends BaseModel {
    private OperatePageDao operatePageDao;
    private Context context;

    public OperatePageModel(Context context) {
        this.context = context;
        UserDatabase userDataBase = UserDatabase.getInstance(context.getApplicationContext());
        operatePageDao = userDataBase.getOperatePageDao();
    }

    public LiveData<List<OperatePage>> getOperatePageLiveData(long profileId){
        Future<LiveData<List<OperatePage>>> future = executorService.submit(new Callable<LiveData<List<OperatePage>>>() {
            @Override
            public LiveData<List<OperatePage>> call() throws Exception {
                return operatePageDao.getOperatePageLiveData(profileId);
            }
        });

        try {
            return future.get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<OperatePage> getOperatePage(long profileId){
        Future<List<OperatePage>> future = executorService.submit(new Callable<List<OperatePage>>() {
            @Override
            public List<OperatePage> call() throws Exception {
                return operatePageDao.getOperatePage(profileId);
            }
        });

        try {
            return future.get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            return null;
        }
    }

    public long insertPage(OperatePage page){
        Future<Long> future = executorService.submit(new Callable<Long>() {
            @Override
            public Long call() throws Exception {
                return operatePageDao.insertPage(page);
            }
        });

        try {
            return future.get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            return -1;
        }
    }

    public void deletePage(long id){
        executorService.submit(new Runnable() {
            @Override
            public void run() {
                operatePageDao.deletePage(id);
            }
        });
    }
}
