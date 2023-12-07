package com.example.dcskeyboardhelper.model.adapter;

import android.annotation.SuppressLint;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dcskeyboardhelper.R;
import com.example.dcskeyboardhelper.base.SuperBaseAdapter;
import com.example.dcskeyboardhelper.databinding.ItemSupportBinding;
import com.example.dcskeyboardhelper.model.bean.ActionModule;

import java.util.List;

public class SupportDebugAdapter extends SuperBaseAdapter<ItemSupportBinding> {
    private List<ActionModule> modules;

    public SupportDebugAdapter() {

    }

    @SuppressLint("NotifyDataSetChanged")
    public void setModules(List<ActionModule> modules) {
        this.modules = modules;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        ActionModule module = modules.get(position);
        int currentStep = module.getCurrentStep();
        binding.tvSupportTitle.setText(module.getDesc());
        try {
            binding.tvSupportStatus.setText(module.getActions().get(currentStep).getName());//如果没有设置步骤则这里可能有指针越界问题
        }catch (IndexOutOfBoundsException ignored){
        }

    }

    @Override
    protected int getItemLayoutRes() {
        return R.layout.item_support;
    }

    @Override
    public int getItemCount() {
        return modules == null? 0:modules.size();
    }

    public List<ActionModule> getModules() {
        return modules;
    }
}
