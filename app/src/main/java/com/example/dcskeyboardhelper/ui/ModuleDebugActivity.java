package com.example.dcskeyboardhelper.ui;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListPopupWindow;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.dcskeyboardhelper.R;
import com.example.dcskeyboardhelper.base.BaseActivity;
import com.example.dcskeyboardhelper.databinding.ActivitySimulateBinding;
import com.example.dcskeyboardhelper.model.adapter.FragmentsAdapter;
import com.example.dcskeyboardhelper.model.bean.ActionModule;
import com.example.dcskeyboardhelper.model.bean.OperatePage;
import com.example.dcskeyboardhelper.ui.debug.OperatePageFragment;
import com.example.dcskeyboardhelper.ui.dialog.ActionDialog;
import com.example.dcskeyboardhelper.util.PopupWindowUtil;
import com.example.dcskeyboardhelper.viewModel.ModuleDebugViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ModuleDebugActivity extends BaseActivity<ActivitySimulateBinding, ModuleDebugViewModel> {
    @Override
    protected void initParams() {
        setSupportActionBar(binding.tbSim);
        Objects.requireNonNull(getSupportActionBar()).setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        viewModel.setFragmentManager(getSupportFragmentManager());

        viewModel.getAllModules().observe(this, new Observer<List<ActionModule>>() {
            @Override
            public void onChanged(List<ActionModule> actionModules) {

            }
        });

        if (viewModel.getOperatePages() == null || viewModel.getOperatePages().isEmpty()){
            List<Fragment> fragments = new ArrayList<>();
            binding.vpTac.setAdapter(new FragmentsAdapter(viewModel.getFragmentManager(),
                    getLifecycle(), fragments));
        }else {
            List<Fragment> fragments = new ArrayList<>();
            for (OperatePage page : viewModel.getOperatePages()){
                fragments.add(new OperatePageFragment(viewModel, page));
            }
            binding.vpTac.setAdapter(new FragmentsAdapter(viewModel.getFragmentManager(),
                    getLifecycle(), fragments));
        }
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_simulate;
    }

    @Override
    protected ModuleDebugViewModel getViewModel() {
        return new ViewModelProvider(this).get(ModuleDebugViewModel.class);
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
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
            case R.id.add:
                View view = findViewById(item.getItemId());
                ListPopupWindow insertWindow = PopupWindowUtil.initPopupWindow(view, this,
                        new String[]{getString(R.string.insert_button), getString(R.string.insert_page)},
                        225, ListPopupWindow.WRAP_CONTENT, null);
                insertWindow.setHorizontalOffset(-100);
                insertWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        if (position == 0){
                            ActionDialog actionDialog = new ActionDialog(ModuleDebugActivity.this, viewModel);
                            actionDialog.show();
                        }else if (position == 1){

                        }
                    }
                });
                insertWindow.show();
                break;
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_debug, menu);
        return true;
    }
}
