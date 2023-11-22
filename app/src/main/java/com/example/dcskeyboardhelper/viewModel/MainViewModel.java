package com.example.dcskeyboardhelper.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.MutableLiveData;

import com.example.dcskeyboardhelper.base.BaseViewModel;
import com.example.dcskeyboardhelper.model.socket.Client;

public class MainViewModel extends BaseViewModel {
    private String serverIP;
    private int port;
    private Client client;
    private MutableLiveData<Integer> connectionStatus = new MutableLiveData<>();
    private FragmentManager fragmentManager;
    public MainViewModel(@NonNull Application application) {
        super(application);
        connectionStatus.setValue(0);//设置为未连接状态
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getServerIP() {
        return serverIP;
    }

    public void setServerIP(String serverIP) {
        this.serverIP = serverIP;
    }

    public Client getClient() {
        return client;
    }

    public FragmentManager getFragmentManager() {
        return fragmentManager;
    }

    public void setFragmentManager(FragmentManager fragmentManager) {
        this.fragmentManager = fragmentManager;
    }

    //创建客户端
    public void createClient() {
        if (client == null){
            this.client = new Client(serverIP, port, connectionStatus, true);
        }else {
            client.init();//（一般发生在连接失败时）如果已经存在client，则重新加载一遍socket
        }
    }

    public MutableLiveData<Integer> getConnectionStatus() {
        return connectionStatus;
    }
}
