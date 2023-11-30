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
import com.example.dcskeyboardhelper.model.Constant;
import com.example.dcskeyboardhelper.model.bean.Action;
import com.example.dcskeyboardhelper.model.socket.KeyCodes;
import com.example.dcskeyboardhelper.ui.dialog.SetKeyboardActionDialog;

import java.util.ArrayList;
import java.util.List;

//创建/更新界面下显示已经添加的操作模块的Adapter
public class KeyboardActionsAdapter extends SuperBaseAdapter<ItemInsertActionBinding> {
    private List<Action> actions;//每个Key代表一个模拟动作的模块
    private final Context context;

    public KeyboardActionsAdapter(Context context) {
        this.context = context;
        actions = new ArrayList<>();
    }

    public KeyboardActionsAdapter(List<Action> actions, Context context) {
        this.actions = actions;
        this.context = context;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        super.onBindViewHolder(holder, position);
        binding.ibInsertAb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SetKeyboardActionDialog keyboardActionDialog;
                if (actions.isEmpty() || position >= actions.size()){
                    keyboardActionDialog = new SetKeyboardActionDialog(context);
                }else {
                    keyboardActionDialog = new SetKeyboardActionDialog(context, actions.get(position));
                }

                keyboardActionDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        //获取dialog的创建状态，是更新还是创建
                        String status = keyboardActionDialog.getDialog_status();
                        if (Constant.CREATE.equals(status)){
                            //获取步骤名及所要模拟的按键Code
                            Action action = keyboardActionDialog.getActions();
                            if (action == null){//判空，如果为空则表明不是按确认键退出的，直接return
                                return;
                            }else {
                                actions.add(action);//创建后添加至Key集合
                            }
                            notifyDataSetChanged();// TODO: 2023/11/30 更高效的刷新方式
                        }else if (Constant.UPDATE.equals(status)){
                            Action action = keyboardActionDialog.getActions();
                            if (action == null){
                                return;
                            }
                            actions.set(position, action);//更新该位置的key
                            notifyItemChanged(position);
                        }

                    }
                });
                keyboardActionDialog.show();
            }
        });

        binding.ibActionDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (actions != null){
                    actions.remove(position);
                }
                notifyItemRemoved(position);
                notifyItemChanged(position, actions.size());
            }
        });

        if (actions.isEmpty() || position >= actions.size()){
            binding.clKeyboardSet.setVisibility(View.GONE);
        }else {
            binding.clKeyboardSet.setVisibility(View.VISIBLE);
        }

        boolean isKeyboardSet = (binding.clKeyboardSet.getVisibility() == View.VISIBLE);

        if (isKeyboardSet){
            binding.tvInsertStep.setText(context.getString(R.string.action_step_placeholder_no_number) + (1 + position));
            binding.tvActionDesc.setText(actions.get(position).getName());
            binding.tvAction.setText(KeyCodes.getKeyNameByValue(actions.get(position).getCodes().get(0)));
        }
    }

    @Override
    protected int getItemLayoutRes() {
        return R.layout.item_insert_action;
    }

    @Override
    public int getItemCount() {
        return actions == null? 1: actions.size() + 1;
    }

    public List<Action> getActions() {
        return actions;
    }

    public void setActions(List<Action> actions) {
        this.actions = actions;
    }
}
