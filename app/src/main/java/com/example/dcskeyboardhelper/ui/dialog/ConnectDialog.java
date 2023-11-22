package com.example.dcskeyboardhelper.ui.dialog;

import android.app.Dialog;
import android.view.View;
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
                int port = Integer.parseInt(Objects.requireNonNull(binding.edPort.getEditText()).getText().toString());
                viewModel.setServerIP(serverIp);
                viewModel.setPort(port);
                viewModel.createClient();
                Toast.makeText(getContext(), getString(R.string.operate_success), Toast.LENGTH_SHORT).show();
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
