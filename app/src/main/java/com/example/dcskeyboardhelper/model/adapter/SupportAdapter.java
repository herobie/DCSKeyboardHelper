package com.example.dcskeyboardhelper.model.adapter;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dcskeyboardhelper.R;
import com.example.dcskeyboardhelper.base.SuperBaseAdapter;
import com.example.dcskeyboardhelper.databinding.ItemSupportBinding;
import com.example.dcskeyboardhelper.model.bean.SupportItemData;

import java.util.List;

public class SupportAdapter extends SuperBaseAdapter<ItemSupportBinding> {
    private List<SupportItemData> status;
    private Context context;

    public SupportAdapter(Context context) {
        this.context = context;
    }

    public void setStatus(List<SupportItemData> status) {
        this.status = status;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        if (status != null){
            binding.tvSupportTitle.setText(status.get(position).getTitle());
            int currentStep = status.get(position).getCurrentStep();
            List<String> stepDesc = status.get(position).getStepDesc();
            if (stepDesc != null){
                binding.tvSupportStatus.setText(stepDesc.get(currentStep));
            }
        }

    }

    @Override
    protected int getItemLayoutRes() {
        return R.layout.item_support;
    }

    @Override
    public int getItemCount() {
        return status == null? 0:status.size();
    }
}
