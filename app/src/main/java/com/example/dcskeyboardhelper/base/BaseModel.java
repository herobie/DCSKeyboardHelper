package com.example.dcskeyboardhelper.base;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public abstract class BaseModel{
    protected ExecutorService executorService = Executors.newFixedThreadPool(2);

    protected void shutdown(){
        if (!executorService.isShutdown()){
            executorService.shutdown();
        }
    }
}
