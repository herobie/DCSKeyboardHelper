package com.example.dcskeyboardhelper.ui.dialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.view.View;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dcskeyboardhelper.R;
import com.example.dcskeyboardhelper.base.BaseDialogFragment;
import com.example.dcskeyboardhelper.databinding.DialogInsertActionModuleBinding;
import com.example.dcskeyboardhelper.model.Constant;
import com.example.dcskeyboardhelper.model.adapter.KeyboardActionsAdapter;
import com.example.dcskeyboardhelper.model.bean.ActionModule;
import com.example.dcskeyboardhelper.viewModel.OperatePageViewModel;

import java.util.Objects;

public class ModuleUpdateDialog extends BaseDialogFragment<DialogInsertActionModuleBinding, OperatePageViewModel>
        implements View.OnClickListener {
    private final ActionModule module;
    private KeyboardActionsAdapter adapter;
    private boolean isUpdate = false;

    public ModuleUpdateDialog(OperatePageViewModel viewModel, ActionModule module) {
        super(viewModel);
        this.module = module;
    }

    @Override
    protected void initParams() {
        //初始化rv
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(RecyclerView.HORIZONTAL);
        binding.rvInsertAction.setLayoutManager(layoutManager);
        adapter = new KeyboardActionsAdapter(module.getActions(), getContext());
        binding.rvInsertAction.setAdapter(adapter);

        //初始化module的信息至相应的输入框
        Objects.requireNonNull(binding.edButtonTitle.getEditText()).setText(module.getTitle());
        Objects.requireNonNull(binding.edButtonDesc.getEditText()).setText(module.getDesc());
        Objects.requireNonNull(binding.edButtonDefaultStep.getEditText()).setText(String.valueOf(module.getDefaultStep()));
        binding.cbAddInfo.setChecked(module.isStarred());
        if (Constant.STEP == module.getSwitchMode()){
            binding.rbStep.setChecked(true);
        }else if (Constant.LOOP == module.getSwitchMode()){
            binding.rbLoop.setChecked(true);
        }else if (Constant.CLICK == module.getSwitchMode()){
            binding.rbClick.setChecked(true);
        }

        binding.btnInsertCancel.setOnClickListener(this);
        binding.btnInsertConfirm.setOnClickListener(this);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_insert_cancel:
                dismiss();
                break;
            case R.id.btn_insert_confirm:
                String title = Objects.requireNonNull(binding.edButtonTitle.getEditText()).getText().toString();
                String desc = Objects.requireNonNull(binding.edButtonDesc.getEditText()).getText().toString();

                int switchMode = Constant.LOOP;
                if (binding.rbStep.isChecked()){//切换模式选中的步进
                    switchMode = Constant.STEP;
                }else if (binding.rbClick.isChecked()){
                    switchMode = Constant.CLICK;
                }

                if (switchMode == Constant.CLICK){//单点模式只执行一个步骤，因此会把多余的步骤清除
                    if (adapter.getActions().size() >= 2) {
                        adapter.getActions().subList(2, adapter.getActions().size()).clear();
                    }
                }
                int stepSum = adapter.getActions().size();

                int defaultStep = Integer.parseInt(Objects.requireNonNull(binding.edButtonDefaultStep
                        .getEditText()).getText().toString());

                boolean isStarred = binding.cbAddInfo.isChecked();
                //检查pageId和profileId有无错误
                long pageId = Constant.CURRENT_PAGE_ID;
                if (pageId == -1){
                    Toast.makeText(getContext(), R.string.page_index_error, Toast.LENGTH_SHORT).show();
                    dismiss();
                    return;
                }
                long profileId = Constant.CURRENT_PROFILE_ID;
                if (profileId == -1){
                    Toast.makeText(getContext(), R.string.profile_index_error, Toast.LENGTH_SHORT).show();
                    dismiss();
                    return;
                }
                 module.updateModule(title, desc, stepSum, defaultStep, switchMode, adapter.getActions(),
                         isStarred, pageId, profileId, 0);
                viewModel.updateModule(module);
                Toast.makeText(getContext(), R.string.operate_success, Toast.LENGTH_SHORT).show();
                isUpdate = true;
                dismiss();
        }
    }

    public boolean isUpdate() {
        return isUpdate;
    }

    public ActionModule getModule() {
        return module;
    }

    @Override
    protected void setDismissCountdown() {

    }

    @Override
    protected int getLayoutRes() {
        return R.layout.dialog_insert_action_module;
    }

    @Override
    protected Dialog setCustomDialogStyle() {
        return null;
    }
}
