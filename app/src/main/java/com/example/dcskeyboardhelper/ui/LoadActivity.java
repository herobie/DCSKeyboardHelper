package com.example.dcskeyboardhelper.ui;

import android.content.Intent;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.dcskeyboardhelper.R;
import com.example.dcskeyboardhelper.base.BaseActivity;
import com.example.dcskeyboardhelper.databinding.ActivityLoadBinding;
import com.example.dcskeyboardhelper.model.adapter.SaveAdapter;
import com.example.dcskeyboardhelper.viewModel.LoadViewModel;

import java.util.Objects;

public class LoadActivity extends BaseActivity<ActivityLoadBinding, LoadViewModel> {

    @Override
    protected void initParams() {
        Intent intent = getIntent();
        String launchMode = intent.getStringExtra("launchMode");//获取是从哪打开的加载页面
        if (launchMode == null){
            finish();
            return;
        }

        setSupportActionBar(binding.tbSave);
        Objects.requireNonNull(getSupportActionBar()).setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        binding.rvSave.setLayoutManager(layoutManager);
        SaveAdapter saveAdapter = new SaveAdapter(this, viewModel, launchMode);
        binding.rvSave.setAdapter(saveAdapter);

        viewModel.queryAll().observe(this, saveAdapter::setProfiles);
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_load;
    }

    @Override
    protected LoadViewModel getViewModel() {
        return new ViewModelProvider(this).get(LoadViewModel.class);
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
        //设置标题栏图标点击事件
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return true;
    }
}
