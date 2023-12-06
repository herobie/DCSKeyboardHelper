package com.example.dcskeyboardhelper.ui.simulate;

import android.annotation.SuppressLint;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.dcskeyboardhelper.R;
import com.example.dcskeyboardhelper.base.BaseFragment;
import com.example.dcskeyboardhelper.databinding.FragmentSupportBinding;
import com.example.dcskeyboardhelper.model.adapter.SupportAdapter;
import com.example.dcskeyboardhelper.model.bean.ActionModule;
import com.example.dcskeyboardhelper.viewModel.SimulateViewModel;

import java.util.List;

public class SupportFragment extends BaseFragment<FragmentSupportBinding, SimulateViewModel> {

    public SupportFragment(SimulateViewModel viewModel) {
        super(viewModel);
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void initParams() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        binding.rvSupport.setLayoutManager(layoutManager);
        SupportAdapter adapter = new SupportAdapter(viewModel);
        binding.rvSupport.setAdapter(adapter);
        viewModel.setSupportAdapter(adapter);

        List<ActionModule> starredModules = viewModel.getStarredModules();//获取星标item
        viewModel.getIsStarredModulesReady().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean){
                    adapter.setModules(starredModules);
                    adapter.notifyDataSetChanged();
                }
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
    protected SimulateViewModel getViewModel() {
        return null;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.fragment_support;
    }
}
