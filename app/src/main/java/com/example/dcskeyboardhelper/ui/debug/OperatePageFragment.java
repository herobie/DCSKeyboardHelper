package com.example.dcskeyboardhelper.ui.debug;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;

import com.example.dcskeyboardhelper.R;
import com.example.dcskeyboardhelper.base.BaseFragment;
import com.example.dcskeyboardhelper.databinding.FragmentOperateBinding;
import com.example.dcskeyboardhelper.model.adapter.DebugButtonAdapter;
import com.example.dcskeyboardhelper.model.bean.ActionModule;
import com.example.dcskeyboardhelper.model.bean.OperatePage;
import com.example.dcskeyboardhelper.ui.OnModuleStarChangeListener;
import com.example.dcskeyboardhelper.viewModel.ModuleDebugViewModel;
import com.example.dcskeyboardhelper.viewModel.OperatePageViewModel;

import java.util.List;
import java.util.Objects;

public class OperatePageFragment extends BaseFragment<FragmentOperateBinding, OperatePageViewModel>
        implements OnModuleStarChangeListener {
    private OperatePage operatePage;
    private DebugButtonAdapter adapter;
    private ModuleDebugViewModel moduleDebugViewModel;
    public OperatePageFragment(ModuleDebugViewModel moduleDebugViewMode, OperatePage operatePage) {
        this.operatePage = operatePage;
        this.moduleDebugViewModel = moduleDebugViewMode;
    }

    @Override
    protected void initParams() {
        // TODO: 2023/11/22 rv
        binding.tvPagePlaceholder.setText(operatePage.getPageName());

        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 5);
        binding.rvOperate.setLayoutManager(layoutManager);
        adapter = new DebugButtonAdapter(viewModel, getContext());
        adapter.setOnModuleStarChangeListener(this);
        binding.rvOperate.setAdapter(adapter);

        viewModel.getAllModules(operatePage.getPageId()).observe(this, new Observer<List<ActionModule>>() {
            @Override
            public void onChanged(List<ActionModule> actionModules) {
                adapter.setModules(actionModules);
            }
        });
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
    protected OperatePageViewModel getViewModel() {
        return new ViewModelProvider(this).get(OperatePageViewModel.class);
    }

    @Override
    public int getLayoutRes() {
        return R.layout.fragment_operate;
    }

    public OperatePage getOperatePage() {
        return operatePage;
    }

    @Override
    public void onStarChange(String moduleDesc, boolean isStarred) {
        if (isStarred){
            Objects.requireNonNull(moduleDebugViewModel.getStatusDisplayed().getValue()).add(moduleDesc);
        }else {
            Objects.requireNonNull(moduleDebugViewModel.getStatusDisplayed().getValue()).remove(moduleDesc);
        }
    }
}
