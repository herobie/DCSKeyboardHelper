package com.example.dcskeyboardhelper.ui.dialog;

import static android.content.Context.MODE_PRIVATE;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.example.dcskeyboardhelper.R;
import com.example.dcskeyboardhelper.base.BaseDialogFragment;
import com.example.dcskeyboardhelper.databinding.DialogConnectionBinding;
import com.example.dcskeyboardhelper.viewModel.MainViewModel;

import java.util.Objects;

public class ConnectDialog extends BaseDialogFragment<DialogConnectionBinding, MainViewModel> {

    public ConnectDialog(MainViewModel viewModel) {
        super(viewModel);
    }

    @Override
    protected void initParams() {
        binding.cbDefaultConfig.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (buttonView.isPressed() && isChecked){
                    SharedPreferences sharedPreferences= requireContext().getSharedPreferences("ipConfig", MODE_PRIVATE);
                    String ip =sharedPreferences.getString("ip","");
                    int port = sharedPreferences.getInt("port",0);
                    if (ip != null){
                        Objects.requireNonNull(binding.edServerIp.getEditText()).setText(ip);
                    }
                    Objects.requireNonNull(binding.edPort.getEditText()).setText(String.valueOf(port));
                }
            }
        });

        binding.btnConnectCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        binding.btnConnectConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String serverIp = Objects.requireNonNull(binding.edServerIp.getEditText()).getText().toString();
                int port;
                if (!Objects.requireNonNull(binding.edPort.getEditText()).getText().toString().isEmpty()){
                    port = Integer.parseInt(Objects.requireNonNull(binding.edPort.getEditText()).getText().toString());
                }else {
                    Toast.makeText(getContext(), R.string.require_port, Toast.LENGTH_SHORT).show();
                    return;
                }
                viewModel.setServerIP(serverIp);
                viewModel.setPort(port);
                //如果勾选了设置为默认ip设置，则使用SharedPreference进行保存
                if (binding.cbSetConfig.isChecked()){
                    //获取SharedPreferences对象
                    SharedPreferences sharedPreferences = requireContext().getSharedPreferences("ipConfig",MODE_PRIVATE);
                    //获取Editor对象的引用
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    //将获取过来的值放入文件
                    editor.putString("ip", serverIp);
                    editor.putInt("port", port);
                    // 提交数据
                    editor.apply();
                }
                viewModel.createConnection();
                Toast.makeText(getContext(), R.string.operate_success, Toast.LENGTH_SHORT).show();
                dismiss();
            }
        });
    }

    @Override
    protected void setDismissCountdown() {

    }

    @Override
    protected int getLayoutRes() {
        return R.layout.dialog_connection;
    }

    @Override
    protected Dialog setCustomDialogStyle() {
        return null;
    }
}
