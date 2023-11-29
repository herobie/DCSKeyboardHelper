package com.example.dcskeyboardhelper.base;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;

public abstract class SuperBaseAdapter<T extends ViewDataBinding> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    protected T binding;

    public SuperBaseAdapter() {
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = DataBindingUtil
                .inflate(LayoutInflater.from(parent.getContext()) , getItemLayoutRes() , parent , false);
        return new BaseAdapter.BaseViewHolder(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        binding = DataBindingUtil.getBinding(holder.itemView);
    }

    abstract protected int getItemLayoutRes();

    public static class BaseViewHolder extends RecyclerView.ViewHolder{

        public BaseViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
