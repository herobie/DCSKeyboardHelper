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
    private final MutableLiveData<Integer> connectionStatus = new MutableLiveData<>();
    private FragmentManager fragmentManager;
    public MainViewModel(@NonNull Application application) {
        super(application);
        connectionStatus.setValue(0);//设置为未连接状态
        client = Client.getInstance(connectionStatus);
        client.useDefaultConfig();
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
    public void createConnection() {
        if (client == null){
            client = Client.getInstance(connectionStatus);
            client.setNetworkAttr(serverIP, port);
        }
        client.init();
    }

    public MutableLiveData<Integer> getConnectionStatus() {
        return connectionStatus;
    }
}
