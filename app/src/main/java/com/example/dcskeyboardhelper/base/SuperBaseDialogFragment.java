package com.example.dcskeyboardhelper.base;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.DialogFragment;

import java.util.Objects;

public abstract class SuperBaseDialogFragment<T extends ViewDataBinding> extends DialogFragment {
    protected T binding;
    protected DialogInterface.OnDismissListener onDismissListener;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        if (setCustomDialogStyle() == null){
            Dialog dialog = super.onCreateDialog(savedInstanceState);
            LayoutInflater inflater = requireActivity().getLayoutInflater();
            if (binding != null){
                dialog.setContentView(binding.getRoot());
            }else {
                dialog.setContentView(inflater.inflate(getLayoutRes(), null));
            }
            dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
            return dialog;
        }else {
            return setCustomDialogStyle();
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, getLayoutRes(), container, false);
        return binding == null?
                super.onCreateView(inflater, container, savedInstanceState) : binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.setLifecycleOwner(getViewLifecycleOwner());
        initParams();
        setDismissCountdown();
    }

    @Override
    public void onResume() {
        super.onResume();
        //必须onResume的时候设置dialogFragment的宽高
        Objects.requireNonNull(getDialog())
                .getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
    }

    abstract protected void initParams();//加载dialog的一些参数，例如文本，图片等

    abstract protected void setDismissCountdown();//设置自动消失读秒时间

    abstract protected int getLayoutRes();

    abstract protected Dialog setCustomDialogStyle();//自定义dialog的样式，如设置背景透明度之类的

    public interface onDismissListener{
        void onDismiss();
    }

    public void setOnDismissListener(DialogInterface.OnDismissListener onDismissListener) {
        this.onDismissListener = onDismissListener;
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        if (onDismissListener != null){
            onDismissListener.onDismiss(dialog);//退出事件
        }
    }
}
