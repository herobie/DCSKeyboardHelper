package com.example.dcskeyboardhelper.ui.dialog;

import android.content.Context;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.dcskeyboardhelper.R;
import com.example.dcskeyboardhelper.base.BaseDialog;
import com.example.dcskeyboardhelper.databinding.DialogInsertPageBinding;
import com.example.dcskeyboardhelper.model.Constant;
import com.example.dcskeyboardhelper.model.bean.OperatePage;
import com.example.dcskeyboardhelper.viewModel.ModuleDebugViewModel;

import java.util.Objects;

public class PageDialog extends BaseDialog<DialogInsertPageBinding> {
    private final ModuleDebugViewModel viewModel;
    private OperatePage operatePage;
    private boolean isConfirm = false;
    public PageDialog(@NonNull Context context, ModuleDebugViewModel viewModel) {
        super(context);
        this.viewModel = viewModel;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding.btnPageConfirm.setOnClickListener(v -> {
            String pageName = Objects.requireNonNull(binding.edPageTitle.getEditText()).getText().toString();
            operatePage = new OperatePage(Constant.CURRENT_PROFILE_ID, pageName);
            long pageId = viewModel.insertPage(operatePage);
            operatePage.setPageId(pageId);
            isConfirm = true;
            Toast.makeText(getContext(), getContext().getString(R.string.operate_success), Toast.LENGTH_SHORT).show();
            dismiss();
        });

        binding.btnPageCancel.setOnClickListener(v -> dismiss());
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.dialog_insert_page;
    }

    public OperatePage getOperatePage() {
        return isConfirm? operatePage : null;
    }
}
