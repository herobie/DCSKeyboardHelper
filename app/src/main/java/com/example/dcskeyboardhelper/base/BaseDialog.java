package com.example.dcskeyboardhelper.base;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

import com.example.dcskeyboardhelper.R;

public abstract class BaseDialog<T extends ViewDataBinding> extends Dialog {
    protected T binding;
    public BaseDialog(@NonNull Context context) {
        super(context, R.style.DialogBaseStyle);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), getLayoutRes(), null, false);
        setContentView(binding.getRoot());
    }

    protected abstract int getLayoutRes();
}
