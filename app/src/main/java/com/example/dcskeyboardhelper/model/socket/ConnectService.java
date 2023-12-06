package com.example.dcskeyboardhelper.model.socket;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.dcskeyboardhelper.model.Constant;

public class ConnectService extends Service {
    private Client client;
    private final ConnectBinder binder = new ConnectBinder();
    @Override
    public void onCreate() {
        Log.w("MainActivity", "onCreate: Service");
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Client getClient() {
        return client;
    }

    @Override
    public void onDestroy() {
        Log.w("MainActivity", "onDestroy: Service");
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    public class ConnectBinder extends Binder {
        // 声明一个方法，getService。（提供给客户端调用）
        public ConnectService getService() {
            // 返回当前对象ConnectService,这样就可在客户端端调用Service的公共方法了
            return ConnectService.this;
        }
    }
}
