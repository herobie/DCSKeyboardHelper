package com.example.dcskeyboardhelper.ui;

import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.view.View;

import com.example.dcskeyboardhelper.R;
import com.example.dcskeyboardhelper.base.BaseActivity;
import com.example.dcskeyboardhelper.databinding.ActivityMainBinding;
import com.example.dcskeyboardhelper.model.Constant;
import com.example.dcskeyboardhelper.model.socket.Client;
import com.example.dcskeyboardhelper.ui.dialog.ConnectDialog;
import com.example.dcskeyboardhelper.viewModel.MainViewModel;

public class MainActivity extends BaseActivity<ActivityMainBinding, MainViewModel> implements View.OnClickListener {

    @Override
    protected void initParams() {
        binding.btnConnectServer.setOnClickListener(this);
        binding.btnStartSimulation.setOnClickListener(this);
        binding.btnDebug.setOnClickListener(this);
        binding.btnDisconnect.setOnClickListener(this);
        FragmentManager fragmentManager = getSupportFragmentManager();
        viewModel.setFragmentManager(fragmentManager);

        //观察服务端连接情况
        viewModel.getConnectionStatus().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                switch (integer){
                    case Client.NO_CONNECTED:
                        binding.tvConnectionStatus.setText(getString(R.string.device_no_connect));
                        binding.btnDisconnect.setVisibility(View.INVISIBLE);
                        break;
                    case Client.SERVER_CONNECTED:
                        binding.tvConnectionStatus.setText(getString(R.string.device_connected));
                        binding.btnDisconnect.setVisibility(View.VISIBLE);
                        break;
                    case Client.SERVER_CONNECT_FAILED:
                        binding.tvConnectionStatus.setText(getString(R.string.connect_failed));
                        binding.btnDisconnect.setVisibility(View.INVISIBLE);
                        break;
                    case Client.SERVER_CONNECTING:
                        binding.tvConnectionStatus.setText(getString(R.string.device_connecting));
                        binding.btnDisconnect.setVisibility(View.INVISIBLE);
                        break;
                }
            }
        });
    }

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
        }
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