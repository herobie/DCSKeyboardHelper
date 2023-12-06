package com.example.dcskeyboardhelper.ui.debug;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListPopupWindow;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import com.example.dcskeyboardhelper.R;
import com.example.dcskeyboardhelper.base.BaseActivity;
import com.example.dcskeyboardhelper.databinding.ActivitySimulateBinding;
import com.example.dcskeyboardhelper.model.Constant;
import com.example.dcskeyboardhelper.model.adapter.FragmentsAdapter;
import com.example.dcskeyboardhelper.model.bean.ActionModule;
import com.example.dcskeyboardhelper.model.bean.OperatePage;
import com.example.dcskeyboardhelper.ui.dialog.ModuleInsertDialog;
import com.example.dcskeyboardhelper.ui.dialog.PageDialog;
import com.example.dcskeyboardhelper.util.PopupWindowUtil;
import com.example.dcskeyboardhelper.viewModel.ModuleDebugViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ModuleDebugActivity extends BaseActivity<ActivitySimulateBinding, ModuleDebugViewModel>
        implements View.OnClickListener {
    @SuppressLint("NotifyDataSetChanged")
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
        viewModel.setOperatePageAdapter(new FragmentsAdapter<>(viewModel.getFragmentManager(),
                getLifecycle(), null));

        //监控OperatePage类
        viewModel.getAllOperatePageLiveData(Constant.CURRENT_PROFILE_ID).observe(this, operatePages -> {
            if (binding.vpTac.getAdapter() == null){
                binding.vpTac.setAdapter(viewModel.getOperatePageAdapter());
            }
            //如果operatePage有数据，那么new对应数量的fragment出来，并给adapter设置数据源
            if (operatePages != null){
                List<OperatePageDebugFragment> fragments = new ArrayList<>();
                for (OperatePage page : operatePages){
                    fragments.add(new OperatePageDebugFragment(viewModel, page));
                }
                viewModel.getOperatePageAdapter().setFragments(fragments);
                binding.vpTac.setAdapter(viewModel.getOperatePageAdapter());
            }
            binding.vpTac.getAdapter().notifyDataSetChanged();
        });

        binding.vpTac.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);
        binding.vpTac.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                OperatePageDebugFragment operatePageDebugFragment = viewModel.getOperatePageAdapter()
                        .getFragments().get(position);
                if (!operatePageDebugFragment.isHidden()){//这里怕出问题，必须是当前显示的fragment才行
                    Constant.CURRENT_PAGE_ID = operatePageDebugFragment.getOperatePage().getPageId();
                    viewModel.setCurrentPage(operatePageDebugFragment.getOperatePage());
                    binding.tbSim.setTitle(operatePageDebugFragment.getOperatePage().getPageName());
                }
                if (Objects.requireNonNull(binding.vpTac.getAdapter()).getItemCount() <= 0){//如果没有任何页面则toolbar显示默认标题
                    binding.tbSim.setTitle(R.string.operate_panel);
                    Constant.CURRENT_PAGE_ID = -1;//将当前pageId设置为-1，避免出现指针错误的情况
                }
            }
        });

        List<Fragment> supportFragments = new ArrayList<>();
        supportFragments.add(new SupportDebugFragment(viewModel));
        binding.vpSupport.setAdapter(new FragmentsAdapter<>(viewModel.getFragmentManager(), getLifecycle(), supportFragments));
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
                            ModuleInsertDialog moduleInsertDialog
                                    = new ModuleInsertDialog(ModuleDebugActivity.this, viewModel);
                            moduleInsertDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                                @Override
                                public void onDismiss(DialogInterface dialog) {
                                    ActionModule module = moduleInsertDialog.getModule();
                                    if (module != null){
                                        int position = binding.vpTac.getCurrentItem();
                                        viewModel.getOperatePageAdapter().getFragments()
                                                .get(position).onModuleInserted(module, -1);
                                    }
                                }
                            });
                            moduleInsertDialog.show();
                        }else if (position == 1){
                            PageDialog pageDialog = new PageDialog(ModuleDebugActivity.this, viewModel);
                            pageDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                                @Override
                                public void onDismiss(DialogInterface dialog) {
                                    Objects.requireNonNull(binding.vpTac.getAdapter())
                                            .notifyItemInserted(binding.vpTac.getChildCount());
                                }
                            });
                            pageDialog.show();
                        }
                    }
                });
                insertWindow.show();
                break;
            case R.id.delete_page://删除页面
                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this)
                        .setTitle(getString(R.string.warning))
                        .setMessage(getString(R.string.confirm_operate));
                alertBuilder.setPositiveButton(getString(R.string.confirm), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (Constant.CURRENT_PAGE_ID != -1){//检查一下页面指针出错没有
                            Toast.makeText(ModuleDebugActivity.this,
                                    getString(R.string.page_index_error), Toast.LENGTH_SHORT).show();
                        }
                        viewModel.deletePage(Constant.CURRENT_PAGE_ID);
                    }
                });
                alertBuilder.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                alertBuilder.show();
                break;
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_debug, menu);
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ib_prev:
                int currentPosition = binding.vpTac.getCurrentItem();
                if (currentPosition > 0){
                    binding.vpTac.setCurrentItem(currentPosition - 1);
                }
                break;
            case R.id.ib_next:
                currentPosition = binding.vpTac.getCurrentItem();
                if (currentPosition < Objects.requireNonNull(binding.vpTac.getAdapter()).getItemCount() - 1){
                    binding.vpTac.setCurrentItem(currentPosition + 1);
                }
                break;
        }
    }
}
