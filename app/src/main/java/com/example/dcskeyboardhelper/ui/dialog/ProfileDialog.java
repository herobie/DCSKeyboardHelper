package com.example.dcskeyboardhelper.ui.dialog;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.dcskeyboardhelper.R;
import com.example.dcskeyboardhelper.base.BaseDialog;
import com.example.dcskeyboardhelper.databinding.DialogInsertProfileBinding;
import com.example.dcskeyboardhelper.model.bean.Profile;
import com.example.dcskeyboardhelper.viewModel.LoadViewModel;

import java.util.Objects;

public class ProfileDialog extends BaseDialog<DialogInsertProfileBinding> {
    private LoadViewModel viewModel;
    private long newProfileId = -1;
    public ProfileDialog(@NonNull Context context, LoadViewModel viewModel) {
        super(context);
        this.viewModel = viewModel;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding.btnProfileCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        binding.btnProfileConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = Objects.requireNonNull(binding.edProfileTitle.getEditText()).getText().toString();
                String desc = Objects.requireNonNull(binding.edProfileDesc.getEditText()).getText().toString();
                long createdDate = System.currentTimeMillis();
                newProfileId = viewModel.insert(new Profile(title, desc, createdDate));
                // TODO: 2023/11/21 操作失败 
                Toast.makeText(getContext(), getContext().getString(R.string.operate_success), Toast.LENGTH_SHORT).show();
                dismiss();
            }
        });
    }

    public long getNewProfileId() {
        return newProfileId;
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.dialog_insert_profile;
    }
}
