package com.example.dcskeyboardhelper.ui;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.view.View;

import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;

import com.example.dcskeyboardhelper.R;
import com.example.dcskeyboardhelper.base.BaseActivity;
import com.example.dcskeyboardhelper.databinding.ActivityMainBinding;
import com.example.dcskeyboardhelper.model.Constant;
import com.example.dcskeyboardhelper.model.socket.Client;
import com.example.dcskeyboardhelper.model.socket.ConnectService;
import com.example.dcskeyboardhelper.ui.dialog.ConnectDialog;
import com.example.dcskeyboardhelper.viewModel.MainViewModel;

public class MainActivity extends BaseActivity<ActivityMainBinding, MainViewModel> implements View.OnClickListener {
    private ConnectService connectService;
    private ServiceConnection conn;
    @Override
    protected void initParams() {
        binding.btnConnectServer.setOnClickListener(this);
        binding.btnStartSimulation.setOnClickListener(this);
        binding.btnDebug.setOnClickListener(this);
        binding.btnDisconnect.setOnClickListener(this);
        FragmentManager fragmentManager = getSupportFragmentManager();
        viewModel.setFragmentManager(fragmentManager);

        conn = new ServiceConnection() {
            /**
             * 与服务器端交互的接口方法 绑定服务的时候被回调，在这个方法获取绑定Service传递过来的IBinder对象，
             * 通过这个IBinder对象，实现宿主和Service的交互。
             */
            @Override
            public void onServiceConnected(ComponentName name, IBinder binder) {
                // 获取Binder
                ConnectService.ConnectBinder myBinder = (ConnectService.ConnectBinder) binder;
                connectService = myBinder.getService();
                connectService.setClient(viewModel.getClient());
                observeConnectStatus();
            }
            /**
             * 当取消绑定的时候被回调。但正常情况下是不被调用的，它的调用时机是当Service服务被意外销毁时，
             * 例如内存的资源不足时这个方法才被自动调用。
             */
            @Override
            public void onServiceDisconnected(ComponentName name) {
                connectService = null;
            }
        };

        Intent intent = new Intent(MainActivity.this, ConnectService.class);
        bindService(intent, conn, Service.BIND_AUTO_CREATE);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_connect_server:
                ConnectDialog connectDialog = new ConnectDialog(viewModel);
                connectDialog.show(viewModel.getFragmentManager(), "ConnectDialog");
                break;
            case R.id.btn_start_simulation:
                Intent intent = new Intent(this, LoadActivity.class);
                intent.putExtra("launchMode", Constant.SIMULATION_MODE);
                startActivity(intent);
                break;
            case R.id.btn_debug:
                Intent i = new Intent(this, LoadActivity.class);
                i.putExtra("launchMode", Constant.DEBUG_MODE);
                startActivity(i);
                break;
            case R.id.btn_disconnect:
                if (viewModel.getClient() != null && viewModel.getClient().isConnected()){
                    viewModel.getClient().disconnect();
                    viewModel.getClient().destroy();
                }
                break;
        }
    }

    private void observeConnectStatus(){
        //观察服务端连接情况
        viewModel.getConnectionStatus().observe(this, integer -> {
            //连接情况发生改变时，会发送广播告知相应的接收者当前连接情况
            switch (integer){
                case Client.NO_CONNECTED:
                    binding.tvConnectionStatus.setText(R.string.device_no_connect);
                    binding.btnDisconnect.setVisibility(View.INVISIBLE);
                    break;
                case Client.SERVER_CONNECTED:
                    binding.tvConnectionStatus.setText(R.string.device_connected);
                    binding.btnDisconnect.setVisibility(View.VISIBLE);
                    break;
                case Client.SERVER_CONNECT_FAILED:
                    binding.tvConnectionStatus.setText(R.string.connect_failed);
                    binding.btnDisconnect.setVisibility(View.INVISIBLE);
                    break;
                case Client.SERVER_CONNECTING:
                    binding.tvConnectionStatus.setText(R.string.device_connecting);
                    binding.btnDisconnect.setVisibility(View.INVISIBLE);
                    break;
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(conn);
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_main;
    }

    @Override
    protected MainViewModel getViewModel() {
        return new ViewModelProvider(this).get(MainViewModel.class);
    }

    @Override
    protected LifecycleOwner getLifeCycleOwner() {
        return this;
    }

    @Override
    protected int getBindingVariable() {
        return 0;
    }
}