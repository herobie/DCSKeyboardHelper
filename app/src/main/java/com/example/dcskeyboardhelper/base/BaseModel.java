package com.example.dcskeyboardhelper.base;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public abstract class BaseModel{
    protected Handler handler = new Handler(Looper.getMainLooper());
    protected ExecutorService executorService = Executors.newFixedThreadPool(2);

    protected void onLoadFinished(){

    }

    protected void onLoadFailed(){

    }

    protected void shutdown(){
        if (!executorService.isShutdown()){
            executorService.shutdown();
        }
    }
}
