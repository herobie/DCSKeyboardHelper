package com.example.dcskeyboardhelper.ui.simulate;

import android.content.Intent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;

import com.example.dcskeyboardhelper.R;
import com.example.dcskeyboardhelper.base.BaseActivity;
import com.example.dcskeyboardhelper.databinding.ActivitySimulateBinding;
import com.example.dcskeyboardhelper.viewModel.SimulateViewModel;

import java.util.Objects;

public class SimulateActivity extends BaseActivity<ActivitySimulateBinding, SimulateViewModel>
        implements View.OnClickListener {
    @Override
    protected void initParams() {
        Intent intent = getIntent();
        long profileId = intent.getLongExtra("profileId", -1);//这里如果获取了id为-1代表着出问题了
        if (profileId == -1){
            Toast.makeText(this, getString(R.string.load_profile_error), Toast.LENGTH_SHORT).show();
            finish();//报错就finish这个页面回到上一级
            return;
        }else {
            viewModel.setProfileId(profileId);
        }

        setSupportActionBar(binding.tbSim);
        Objects.requireNonNull(getSupportActionBar()).setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        binding.ibNext.setOnClickListener(this);
        binding.ibPrev.setOnClickListener(this);

        viewModel.setFragmentManager(getSupportFragmentManager());
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_simulate;
    }

    @Override
    protected SimulateViewModel getViewModel() {
        return new ViewModelProvider(this).get(SimulateViewModel.class);
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
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }

    @Override
    public void onClick(View v) {

    }
}
