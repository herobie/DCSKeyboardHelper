package com.example.dcskeyboardhelper.base;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModel;

public abstract class BaseFragment<T extends ViewDataBinding, VM extends ViewModel> extends Fragment {
    protected T binding;
    protected VM viewModel;

    public BaseFragment(VM viewModel) {
        this.viewModel = viewModel;
    }

    public BaseFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, getLayoutRes(), container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (viewModel == null){//如果选择的是无参构造函数，这里就要获取一下新创建的vm，否则将带参构造函数的vm作为fragment的vm
            viewModel = getViewModel();
        }
        Log.w("MainActivity", "onViewCreated: ");
        if (getBindingVariable() > 0) {
            binding.setVariable(getBindingVariable(), viewModel);
            binding.executePendingBindings();
        }
        binding.setLifecycleOwner(getLifeCycleOwner());
        initParams();
    }

    protected abstract void initParams();

    protected abstract LifecycleOwner getLifeCycleOwner();

    protected abstract int getBindingVariable();

    protected abstract VM getViewModel();
    @LayoutRes
    public abstract int getLayoutRes();
}
