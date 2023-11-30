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
import com.example.dcskeyboardhelper.viewModel.ModuleDebugViewModel;

import java.util.Objects;

//添加按键的dialog
public class ModuleInsertDialog extends BaseDialog<DialogInsertActionModuleBinding> implements View.OnClickListener{
    private ModuleDebugViewModel viewModel;
    private KeyboardActionsAdapter adapter;
    public ModuleInsertDialog(@NonNull Context context, ModuleDebugViewModel viewModel) {
        super(context);
        this.viewModel = viewModel;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding.btnInsertCancel.setOnClickListener(this);
        binding.btnInsertConfirm.setOnClickListener(this);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(RecyclerView.HORIZONTAL);
        binding.rvInsertAction.setLayoutManager(layoutManager);
        adapter = new KeyboardActionsAdapter(getContext());
        binding.rvInsertAction.setAdapter(adapter);
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
            int stepSum = adapter.getActions().size();
            int defaultStep = Integer.parseInt(Objects.requireNonNull(binding.edButtonDefaultStep
                    .getEditText()).getText().toString());
            int switchMode = Constant.LOOP;
            if (binding.rbStep.isChecked()){//切换模式选中的步进
                switchMode = Constant.STEP;
            }
            boolean isStarred = binding.cbAddInfo.isChecked();
            //检查pageId和profileId有无错误
            long pageId = Constant.CURRENT_PAGE_ID;
            if (pageId == -1){
                Toast.makeText(getContext(), getContext().getString(R.string.page_index_error), Toast.LENGTH_SHORT).show();
                dismiss();
                return;
            }
            long profileId = Constant.CURRENT_PROFILE_ID;
            if (profileId == -1){
                Toast.makeText(getContext(), getContext().getString(R.string.profile_index_error), Toast.LENGTH_SHORT).show();
                dismiss();
                return;
            }
            ActionModule module = new ActionModule(title, desc,  stepSum, defaultStep, switchMode,
                    adapter.getActions(),
                    isStarred, pageId, profileId, 0);
            viewModel.insertModule(module);
            Toast.makeText(getContext(), getContext().getString(R.string.operate_success), Toast.LENGTH_SHORT).show();
            dismiss();
        }
    }
}
