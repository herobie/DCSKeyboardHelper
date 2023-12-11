package com.example.dcskeyboardhelper.base;

import androidx.databinding.ViewDataBinding;
import androidx.lifecycle.ViewModel;

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
