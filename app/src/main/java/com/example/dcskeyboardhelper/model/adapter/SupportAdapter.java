package com.example.dcskeyboardhelper.model.adapter;

import android.annotation.SuppressLint;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dcskeyboardhelper.R;
import com.example.dcskeyboardhelper.base.BaseAdapter;
import com.example.dcskeyboardhelper.databinding.ItemSupportBinding;
import com.example.dcskeyboardhelper.model.bean.ActionModule;
import com.example.dcskeyboardhelper.viewModel.SimulateViewModel;

import java.util.List;

public class SupportAdapter extends BaseAdapter<ItemSupportBinding, SimulateViewModel> {
    private List<ActionModule> modules;
    public SupportAdapter(SimulateViewModel viewModel) {
        super(viewModel);
    }

    public void setModules(List<ActionModule> modules) {
        this.modules = modules;
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
        return modules == null? 0 : modules.size();
    }

    public List<ActionModule> getModules() {
        return modules;
    }
}
