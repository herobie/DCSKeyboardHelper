package com.example.dcskeyboardhelper.ui.dialog;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dcskeyboardhelper.R;
import com.example.dcskeyboardhelper.base.BaseDialog;
import com.example.dcskeyboardhelper.databinding.DialogInsertActionModuleBinding;
import com.example.dcskeyboardhelper.model.Constant;
import com.example.dcskeyboardhelper.model.adapter.KeyboardActionsAdapter;
import com.example.dcskeyboardhelper.model.bean.ActionModule;
import com.example.dcskeyboardhelper.viewModel.OperatePageViewModel;

import java.util.Objects;

public class ModuleUpdateDialog extends BaseDialog<DialogInsertActionModuleBinding> implements View.OnClickListener {
    private final OperatePageViewModel viewModel;
    private final ActionModule module;
    private KeyboardActionsAdapter adapter;
    public ModuleUpdateDialog(@NonNull Context context, OperatePageViewModel viewModel, ActionModule module) {
        super(context);
        this.viewModel = viewModel;
        this.module = module;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //初始化rv
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(RecyclerView.HORIZONTAL);
        binding.rvInsertAction.setLayoutManager(layoutManager);
        adapter = new KeyboardActionsAdapter(module.getStepsDesc(),
                module.getKeyboardActions(), getContext());
        binding.rvInsertAction.setAdapter(adapter);

        //初始化module的信息至相应的输入框
        Objects.requireNonNull(binding.edButtonTitle.getEditText()).setText(module.getTitle());
        Objects.requireNonNull(binding.edButtonDesc.getEditText()).setText(module.getDesc());
        Objects.requireNonNull(binding.edButtonStepSum.getEditText()).setText(String.valueOf(module.getStepsNum()));
        Objects.requireNonNull(binding.edButtonDefaultStep.getEditText()).setText(String.valueOf(module.getDefaultStep()));
        binding.cbAddInfo.setChecked(module.isStarred());
        if (Constant.STEP == module.getSwitchMode()){
            binding.rbStep.setChecked(true);
        }else if (Constant.LOOP == module.getSwitchMode()){
            binding.rbLoop.setChecked(true);
        }

        binding.btnInsertCancel.setOnClickListener(this);
        binding.btnInsertConfirm.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_insert_cancel:
                dismiss();
                break;
            case R.id.btn_insert_confirm:
                String title = Objects.requireNonNull(binding.edButtonTitle.getEditText()).getText().toString();
                String desc = Objects.requireNonNull(binding.edButtonDesc.getEditText()).getText().toString();
                int stepSum = Integer.parseInt(Objects.requireNonNull(binding.edButtonStepSum
                        .getEditText()).getText().toString());
                int defaultStep = Integer.parseInt(Objects.requireNonNull(binding.edButtonDefaultStep
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
                 module.updateModule(title, desc, stepSum, defaultStep, switchMode, adapter.getStepsDesc(),
                         adapter.getKeyboardActions(), isStarred, pageId, profileId, 0);
                viewModel.updateModule(module);
                Toast.makeText(getContext(), getContext().getString(R.string.operate_success), Toast.LENGTH_SHORT).show();
                dismiss();
        }
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.dialog_insert_action_module;
    }
}
