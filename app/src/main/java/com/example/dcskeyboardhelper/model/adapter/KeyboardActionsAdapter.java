package com.example.dcskeyboardhelper.model.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dcskeyboardhelper.R;
import com.example.dcskeyboardhelper.base.SuperBaseAdapter;
import com.example.dcskeyboardhelper.databinding.ItemInsertActionBinding;
import com.example.dcskeyboardhelper.model.bean.Key;
import com.example.dcskeyboardhelper.ui.dialog.SetKeyboardActionDialog;

import java.util.ArrayList;
import java.util.List;

//创建/更新界面下显示已经添加的操作模块的Adapter
public class KeyboardActionsAdapter extends SuperBaseAdapter<ItemInsertActionBinding> {
    private List<String> stepsDesc;//各步骤的描述，如（自动驾驶：开）中的“开”“关”
    private List<Integer> keyboardActions;//与该按钮绑定的按键
    private final Context context;

    public KeyboardActionsAdapter(Context context) {
        this.context = context;
        this.keyboardActions = new ArrayList<>();
        this.stepsDesc = new ArrayList<>();
    }

    public KeyboardActionsAdapter(List<String> stepsDesc, List<Integer> keyboardActions, Context context) {
        this.stepsDesc = stepsDesc;
        this.keyboardActions = keyboardActions;
        this.context = context;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        super.onBindViewHolder(holder, position);
        binding.ibInsertAb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SetKeyboardActionDialog keyboardActionDialog = new SetKeyboardActionDialog(context);
                keyboardActionDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        //获取步骤名及所要模拟的按键Code
                        Key key = keyboardActionDialog.getKey();
                        if (key == null){//判空，如果为空则表明不是按确认键退出的，直接return
                            return;
                        }else {
                            keyboardActions.add(key.getCode());
                            binding.tvAction.setText(key.getName());
                        }
                        String desc = keyboardActionDialog.getStepDesc();
                        if (desc != null && stepsDesc != null){
                            binding.tvActionDesc.setText(desc);
                        }
                        stepsDesc.add(desc);
                        notifyItemChanged(keyboardActions.size() - 1);
                    }
                });
                keyboardActionDialog.show();
            }
        });

        binding.ibActionDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (keyboardActions != null){
                    keyboardActions.remove(position);
                }
                if (stepsDesc != null){
                    stepsDesc.remove(position);
                }
                notifyItemRemoved(position);
                notifyItemChanged(position, keyboardActions.size());
            }
        });

        if (keyboardActions.isEmpty() || position >= keyboardActions.size()){
            binding.clKeyboardSet.setVisibility(View.GONE);
        }else {
            binding.clKeyboardSet.setVisibility(View.VISIBLE);
        }

        boolean isKeyboardSet = (binding.clKeyboardSet.getVisibility() == View.VISIBLE);

        if (isKeyboardSet){
            binding.tvInsertStep.setText(context.getString(R.string.action_step_placeholder_no_number) + (1 + position));
        }
    }

    @Override
    protected int getItemLayoutRes() {
        return R.layout.item_insert_action;
    }

    @Override
    public int getItemCount() {
        return keyboardActions == null? 1: keyboardActions.size() + 1;
    }

    public List<String> getStepsDesc() {
        return stepsDesc;
    }

    public void setStepsDesc(List<String> stepsDesc) {
        this.stepsDesc = stepsDesc;
    }

    public List<Integer> getKeyboardActions() {
        return keyboardActions;
    }

    public void setKeyboardActions(List<Integer> keyboardActions) {
        this.keyboardActions = keyboardActions;
    }
}
