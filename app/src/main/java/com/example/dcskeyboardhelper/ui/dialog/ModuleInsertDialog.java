package com.example.dcskeyboardhelper.ui.dialog;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.dcskeyboardhelper.R;
import com.example.dcskeyboardhelper.base.BaseDialog;
import com.example.dcskeyboardhelper.databinding.DialogInsertActionModuleBinding;
import com.example.dcskeyboardhelper.model.Constant;
import com.example.dcskeyboardhelper.model.bean.ActionModule;
import com.example.dcskeyboardhelper.viewModel.ModuleDebugViewModel;

import java.util.Objects;

public class ModuleInsertDialog extends BaseDialog<DialogInsertActionModuleBinding> implements View.OnClickListener{
    private ModuleDebugViewModel viewModel;
    public ModuleInsertDialog(@NonNull Context context, ModuleDebugViewModel viewModel) {
        super(context);
        this.viewModel = viewModel;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding.btnInsertCancel.setOnClickListener(this);
        binding.btnInsertConfirm.setOnClickListener(this);

    }

    @Override
    protected int getLayoutRes() {
        return R.layout.dialog_insert_action_module;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_insert_cancel){
            dismiss();
        }else if (v.getId() == R.id.btn_insert_confirm){
            String title = Objects.requireNonNull(binding.edButtonTitle.getEditText()).getText().toString();
            String desc = Objects.requireNonNull(binding.edButtonDesc.getEditText()).getText().toString();
            int stepSum = Integer.parseInt(Objects.requireNonNull(binding.edButtonStepSum
                    .getEditText()).getText().toString());
            int defaultStep = Integer.parseInt(Objects.requireNonNull(binding.edButtonDefaultStep
                    .getEditText()).getText().toString());
            int width = Integer.parseInt(Objects.requireNonNull(binding.edButtonWidth
                    .getEditText()).getText().toString());
            int height = Integer.parseInt(Objects.requireNonNull(binding.edButtonHeight
                    .getEditText()).getText().toString());
            int switchMode = Constant.LOOP;
            if (binding.rbStep.isChecked()){//切换模式选中的步进
                switchMode = Constant.STEP;
            }
            boolean isStarred = binding.cbAddInfo.isChecked();
            //检查pageId和profileId有无错误
            long pageId = Constant.currentPageId;
            if (pageId == -1){
                Toast.makeText(getContext(), getContext().getString(R.string.page_index_error), Toast.LENGTH_SHORT).show();
                dismiss();
                return;
            }
            long profileId = Constant.currentProfileId;
            if (profileId == -1){
                Toast.makeText(getContext(), getContext().getString(R.string.profile_index_error), Toast.LENGTH_SHORT).show();
                dismiss();
                return;
            }
            ActionModule module = new ActionModule(title, desc, height, width, stepSum,
                    defaultStep, switchMode, null, null, isStarred, pageId, profileId, 0);
            viewModel.insertModule(module);
            Toast.makeText(getContext(), getContext().getString(R.string.operate_success), Toast.LENGTH_SHORT).show();
            dismiss();
        }
    }
}
