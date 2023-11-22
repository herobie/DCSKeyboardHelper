package com.example.dcskeyboardhelper.base;

import android.os.Handler;
import android.os.Looper;

import androidx.databinding.ViewDataBinding;
import androidx.lifecycle.ViewModel;

public abstract class BaseDialogFragment<T extends ViewDataBinding, VM extends ViewModel> extends SuperBaseDialogFragment<T> {
    protected VM viewModel;
    protected Handler handler = new Handler(Looper.getMainLooper());//内存泄漏啥的修一下

    public BaseDialogFragment(VM viewModel) {
        this.viewModel = viewModel;
    }

    public BaseDialogFragment() {
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        handler = null;
    }
}
