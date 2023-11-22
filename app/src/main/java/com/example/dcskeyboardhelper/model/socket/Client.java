package com.example.dcskeyboardhelper.model.socket;

import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

// TODO: 2023/11/21 切换连接 
public class Client {
    private String serverIp;
    private int port;
    private Socket socket;
    private OutputStream os;
    private MutableLiveData<Integer> connectionStatus;

    public static final int NO_CONNECTED = 0;
    public static final int SERVER_CONNECTED = 1;
    public static final int SERVER_CONNECT_FAILED = -1;
    public static final int SERVER_CONNECTING = 2;

    public Client(String serverIp, int port, MutableLiveData<Integer> connectionStatus) {
        this.serverIp = serverIp;
        this.port = port;
        this.connectionStatus = connectionStatus;
        init();
    }

    public Client(String serverIp, int port, MutableLiveData<Integer> connectionStatus, boolean useDefaultConfig) {
        this.connectionStatus = connectionStatus;
        if (useDefaultConfig){
            this.serverIp = "10.158.205.24";
            this.port = 1688;
        }
        init();
    }

    //初始化socket和io
    public void init() {
        // TODO: 2023/11/21 线程管理
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Log.d("MainActivity", "init: 开始连接，ip" + serverIp + " 端口：" + port);
                    socket = new Socket(serverIp, port);
                    os = socket.getOutputStream();
                    //通知livedata连接状态
                    if (socket != null && os != null){
                        connectionStatus.postValue(SERVER_CONNECTED);
                    }else {
                        connectionStatus.postValue(SERVER_CONNECT_FAILED);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.w("MainActivity", "init: 连接超时");
                    connectionStatus.postValue(SERVER_CONNECT_FAILED);
                }
            }
        }).start();
        connectionStatus.postValue(SERVER_CONNECTING);
    }

    public void destroy(){
        try {
            os.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void debug() throws IOException {
        os.write("This is a debug message".getBytes());
    }
}
