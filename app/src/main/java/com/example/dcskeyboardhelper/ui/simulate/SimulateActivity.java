package com.example.dcskeyboardhelper.ui.simulate;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListPopupWindow;
import android.widget.PopupWindow;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import com.example.dcskeyboardhelper.R;
import com.example.dcskeyboardhelper.base.BaseActivity;
import com.example.dcskeyboardhelper.databinding.ActivitySimulateBinding;
import com.example.dcskeyboardhelper.model.Constant;
import com.example.dcskeyboardhelper.model.adapter.FragmentsAdapter;
import com.example.dcskeyboardhelper.model.bean.OperatePage;
import com.example.dcskeyboardhelper.model.socket.Client;
import com.example.dcskeyboardhelper.model.socket.ConnectService;
import com.example.dcskeyboardhelper.ui.debug.OperatePageDebugFragment;
import com.example.dcskeyboardhelper.ui.debug.SupportDebugFragment;
import com.example.dcskeyboardhelper.util.PopupWindowUtil;
import com.example.dcskeyboardhelper.viewModel.SimulateViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SimulateActivity extends BaseActivity<ActivitySimulateBinding, SimulateViewModel>
        implements View.OnClickListener {
    private ConnectService connectService;
    private ServiceConnection conn;
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

        conn = new ServiceConnection() {

            @Override
            public void onServiceConnected(ComponentName name, IBinder binder) {
                //这里似乎不是在主线程执行的，如果把observeConnectStatus写在外面会出现空指针
                connectService = ((ConnectService.ConnectBinder) binder).getService();
                observeConnectStatus();
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                connectService = null;
            }
        };

        intent = new Intent(SimulateActivity.this, ConnectService.class);
        bindService(intent, conn, Service.BIND_AUTO_CREATE);

        setSupportActionBar(binding.tbSim);
        Objects.requireNonNull(getSupportActionBar()).setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        binding.ibNext.setOnClickListener(this);
        binding.ibPrev.setOnClickListener(this);

        //右侧操作面板的初始化
        viewModel.setFragmentManager(getSupportFragmentManager());
        List<OperatePageFragment> fragments = new ArrayList<>();
        viewModel.setOperatePageAdapter(new FragmentsAdapter<>(viewModel.getFragmentManager(),
                getLifecycle(), fragments));
        for (OperatePage page : viewModel.getOperatePage()){//初始化界面
            viewModel.getOperatePageAdapter().getFragments().add(new OperatePageFragment(page, viewModel));
        }
        binding.vpTac.setAdapter(viewModel.getOperatePageAdapter());
        binding.vpTac.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);

        binding.vpTac.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                OperatePageFragment operatePageFragment = viewModel.getOperatePageAdapter()
                        .getFragments().get(position);
                if (!operatePageFragment.isHidden()){//这里怕出问题，必须是当前显示的fragment才行
                    Constant.CURRENT_PAGE_ID = operatePageFragment.getOperatePage().getPageId();
                    viewModel.setCurrentPage(operatePageFragment.getOperatePage());
                    binding.tbSim.setTitle(operatePageFragment.getOperatePage().getPageName());
                }
                if (Objects.requireNonNull(binding.vpTac.getAdapter()).getItemCount() <= 0){//如果没有任何页面则toolbar显示默认标题
                    binding.tbSim.setTitle(R.string.operate_panel);
                    Constant.CURRENT_PAGE_ID = -1;//将当前pageId设置为-1，避免出现指针错误的情况
                }
            }
        });

        List<Fragment> supportFragments = new ArrayList<>();
        supportFragments.add(new SupportFragment(viewModel));
        binding.vpSupport.setAdapter(new FragmentsAdapter<>(viewModel.getFragmentManager(), getLifecycle(), supportFragments));
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
            case R.id.correction:
                if (connectService.getClient().isConnected()){
                    if (!viewModel.isCorrectMode()){//当不处于调试模式下时，调出模糊提示，并将调试模式设为开
                        binding.clBlur.setVisibility(View.VISIBLE);
                        binding.tvBlurTitle.setText(R.string.debug_mode);
                        binding.tvBlurWarning.setText(R.string.empty_placeholder);
                        viewModel.setCorrectMode(true);
                    }else {
                        binding.clBlur.setVisibility(View.GONE);//处于调试模式下则关闭模糊提示并把调试模式设为关
                        viewModel.setCorrectMode(false);
                    }
                }
                break;
            case R.id.pages_skip://页面快速跳转功能
                List<String> pageNames = new ArrayList<>();
                for (OperatePage page : viewModel.getOperatePage()){
                    pageNames.add(page.getPageName());
                }
                View view = findViewById(item.getItemId());
                ListPopupWindow pageWindow = PopupWindowUtil.initPopupWindow(view , this, pageNames, 250,
                        ListPopupWindow.WRAP_CONTENT, null);
                pageWindow.setHorizontalOffset(-75);
                pageWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        binding.vpTac.setCurrentItem(position);
                    }
                });
                pageWindow.show();
        }
        return true;
    }

    private void observeConnectStatus(){
        if (connectService == null){
            return;
        }
        //传入client的引用方便在operatePageFragment等子页面进行调用
        viewModel.setClient(connectService.getClient());
        //检查是否连接，此情况多出现在未选择连接服务端而直接点进此页面的情况
        // TODO: 2023/12/3 这里进去竟然是重新连接的状态？？
        if (!connectService.getClient().isConnected()){
            binding.clBlur.setVisibility(View.VISIBLE);
            binding.tvBlurTitle.setText(R.string.device_no_connect);
            binding.tvBlurWarning.setText(R.string.connect_notice_retry);
        }

        connectService.getClient().getConnectionStatus().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                switch (integer){
                    case Client.NO_CONNECTED:
                        binding.tvBlurTitle.setText(R.string.device_no_connect);
                        binding.tvBlurWarning.setText(R.string.reconnecting);
                        break;
                    case Client.SERVER_CONNECT_FAILED:
                        binding.tvBlurTitle.setText(R.string.device_no_connect);
                        binding.tvBlurWarning.setText(R.string.connect_notice_retry);
                        binding.clBlur.setVisibility(View.VISIBLE);
                        break;
                    case Client.SERVER_CONNECTED:
                        binding.clBlur.setVisibility(View.GONE);
                        break;
                    case Client.SERVER_CONNECTING:
                        binding.clBlur.setVisibility(View.VISIBLE);
                        binding.tvBlurTitle.setText(R.string.device_connecting);
                        binding.tvBlurWarning.setText(R.string.connect_notice_wait);
                        break;
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_simulate, menu);
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(conn);
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
