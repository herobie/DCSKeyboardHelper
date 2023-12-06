package com.example.dcskeyboardhelper.ui.debug;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.dcskeyboardhelper.R;
import com.example.dcskeyboardhelper.base.BaseFragment;
import com.example.dcskeyboardhelper.databinding.FragmentSupportBinding;
import com.example.dcskeyboardhelper.model.adapter.SupportDebugAdapter;
import com.example.dcskeyboardhelper.model.bean.SupportItemData;
import com.example.dcskeyboardhelper.viewModel.ModuleDebugViewModel;

import java.util.List;

public class SupportDebugFragment extends BaseFragment<FragmentSupportBinding, ModuleDebugViewModel> {
    public SupportDebugFragment(ModuleDebugViewModel viewModel) {
        super(viewModel);
    }

    @Override
    protected void initParams() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        binding.rvSupport.setLayoutManager(layoutManager);
        SupportDebugAdapter adapter = new SupportDebugAdapter(getContext());
        binding.rvSupport.setAdapter(adapter);

        viewModel.getStatusDisplayed().observe(this, new Observer<List<SupportItemData>>() {
            @Override
            public void onChanged(List<SupportItemData> supportItemData) {
                adapter.setStatus(supportItemData);
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
