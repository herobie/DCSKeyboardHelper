package com.example.dcskeyboardhelper.ui.debug;

import androidx.lifecycle.LifecycleOwner;

import com.example.dcskeyboardhelper.R;
import com.example.dcskeyboardhelper.base.BaseFragment;
import com.example.dcskeyboardhelper.databinding.FragmentOperateBinding;
import com.example.dcskeyboardhelper.model.bean.OperatePage;
import com.example.dcskeyboardhelper.viewModel.ModuleDebugViewModel;

public class OperatePageFragment extends BaseFragment<FragmentOperateBinding, ModuleDebugViewModel> {
    private OperatePage operatePage;
    public OperatePageFragment(ModuleDebugViewModel viewModel, OperatePage operatePage) {
        super(viewModel);
        this.operatePage = operatePage;
    }

    @Override
    protected void initParams() {
        // TODO: 2023/11/22 rv
        binding.tvPagePlaceholder.setText(operatePage.getPageName());
    }

    @Override
    protected LifecycleOwner getLifeCycleOwner() {
        return this;
    }

    @Override
    protected int getBindingVariable() {
        return 0;
    }

    @Override
    protected ModuleDebugViewModel getViewModel() {
        return null;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.fragment_operate;
    }
}
