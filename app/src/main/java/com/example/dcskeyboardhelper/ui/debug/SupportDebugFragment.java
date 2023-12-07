package com.example.dcskeyboardhelper.ui.debug;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.dcskeyboardhelper.R;
import com.example.dcskeyboardhelper.base.BaseFragment;
import com.example.dcskeyboardhelper.databinding.FragmentSupportBinding;
import com.example.dcskeyboardhelper.model.adapter.SupportDebugAdapter;
import com.example.dcskeyboardhelper.viewModel.ModuleDebugViewModel;

public class SupportDebugFragment extends BaseFragment<FragmentSupportBinding, ModuleDebugViewModel> {
    public SupportDebugFragment(ModuleDebugViewModel viewModel) {
        super(viewModel);
    }

    @Override
    protected void initParams() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        binding.rvSupport.setLayoutManager(layoutManager);
        SupportDebugAdapter adapter = new SupportDebugAdapter();
        binding.rvSupport.setAdapter(adapter);
        viewModel.setSupportDebugAdapter(adapter);

        viewModel.getIsStarredModulesReady().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean) adapter.setModules(viewModel.getStarredModules());
            }
        });

    }

    @Override
    protected LifecycleOwner getLifeCycleOwner() {
        return null;
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
        return R.layout.fragment_support;
    }
}
