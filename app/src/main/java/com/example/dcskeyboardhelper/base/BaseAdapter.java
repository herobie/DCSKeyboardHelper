package com.example.dcskeyboardhelper.base;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.lifecycle.ViewModel;
import androidx.recyclerview.widget.RecyclerView;

public abstract class BaseAdapter<T extends ViewDataBinding, VM extends ViewModel>
        extends SuperBaseAdapter<T>{
    protected VM viewModel;

    public BaseAdapter(VM viewModel) {
        super();
        this.viewModel = viewModel;
    }

    public BaseAdapter() {
        super();
    }
}
