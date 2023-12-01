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
    private final ModuleDebugViewModel viewModel;
    private KeyboardActionsAdapter adapter;
    private ActionModule module;
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

            int defaultStep = 0;//修好了指示器再改回去
            if (defaultStep >= stepSum){//如果defaultStep设太大了，会自动调整至最后一步
                defaultStep = stepSum - 1;
            }else if (defaultStep < 0){//同理，如果设置了一个负数会调整为0
                defaultStep = 0;
            }

            int switchMode = Constant.LOOP;//默认为循环
            if (binding.rbStep.isChecked()){//检查是否切换模式选中的步进
                switchMode = Constant.STEP;
            }

            boolean isStarred = binding.cbAddInfo.isChecked();//是否星标
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
            //在这里为module赋值，后续会在ModuleDebugActivity中获取这个module，如果获取的不为Null说明是点击了确认而不是取消
            //添加操作见OperatePageDebugFragment的onModuleInserted重写方法
            module = new ActionModule(title, desc,  stepSum, defaultStep, switchMode,
                    adapter.getActions(), isStarred, pageId, profileId, 0);//gridPosition在上面提到的重写方法里设置
            Toast.makeText(getContext(), getContext().getString(R.string.operate_success), Toast.LENGTH_SHORT).show();
            dismiss();
        }
    }
    //如果是添加成功这里会返回module，否则null
    public ActionModule getModule() {
        return module;
    }
}
