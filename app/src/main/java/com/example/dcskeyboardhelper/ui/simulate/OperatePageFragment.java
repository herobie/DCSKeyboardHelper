package com.example.dcskeyboardhelper.ui.simulate;

import android.annotation.SuppressLint;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;

import com.example.dcskeyboardhelper.R;
import com.example.dcskeyboardhelper.base.BaseFragment;
import com.example.dcskeyboardhelper.databinding.FragmentOperateBinding;
import com.example.dcskeyboardhelper.model.adapter.SimulateButtonAdapter;
import com.example.dcskeyboardhelper.model.bean.Action;
import com.example.dcskeyboardhelper.model.bean.ActionModule;
import com.example.dcskeyboardhelper.model.bean.OperatePage;
import com.example.dcskeyboardhelper.model.socket.Client;
import com.example.dcskeyboardhelper.model.socket.json.BaseJson;
import com.example.dcskeyboardhelper.ui.listeners.OnModuleChangeBasicListener;
import com.example.dcskeyboardhelper.ui.listeners.OnSupportItemChangeListener;
import com.example.dcskeyboardhelper.viewModel.OperatePageViewModel;
import com.example.dcskeyboardhelper.viewModel.SimulateViewModel;

import java.io.IOException;
import java.util.List;

public class OperatePageFragment extends BaseFragment<FragmentOperateBinding, OperatePageViewModel>
        implements OnModuleChangeBasicListener, OnSupportItemChangeListener {
    private final SimulateViewModel simulateViewModel;
    private SimulateButtonAdapter adapter;
    private OperatePage operatePage;
    public OperatePageFragment(OperatePage operatePage, SimulateViewModel simulateViewMode) {
        this.operatePage = operatePage;
        this.simulateViewModel = simulateViewMode;
    }

    @Override
    protected void initParams() {
        binding.tvPagePlaceholder.setText(operatePage.getPageName());

        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 5);
        binding.rvOperate.setLayoutManager(layoutManager);
        adapter = new SimulateButtonAdapter(viewModel, getContext());
        adapter.setOnModuleChangeBasicListener(this);
        binding.rvOperate.setAdapter(adapter);

        List<ActionModule> modules = viewModel.getAllModules(operatePage.getPageId());
        adapter.setModules(modules);
        simulateViewModel.addStarredModules(modules);
        // TODO: 2023/12/3 保证Support在这个类之后加载 
        simulateViewModel.getIsStarredModulesReady().setValue(true);
        onInitModules();
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

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onInitModules() {
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onStarChange(ActionModule module, boolean isStarred) {
        if (isStarred){
            onSupportItemInsert(module);
        }else {
            //找到要移除support界面的module的位置进行移除
            int index = 0;
            for (ActionModule m : simulateViewModel.getSupportAdapter().getModules()){
                if (m.getId() == module.getId()){
                    break;
                }
                index++;
            }
            onSupportItemRemove(index);
        }
        module.setStarred(isStarred);//更新module的数据库状态
        viewModel.updateModule(module);
    }

    @Override
    public void onStepChange(ActionModule module, int currentStep) {
        //判断是否是星标module，是的话进行更新左侧Support的ui操作
        if (module.isStarred()){
            //在support的显示item集合里找到module
            int index = 0;
            for (ActionModule m : simulateViewModel.getStarredModules()){
                if (m.getId() == module.getId()){
                    break;
                }
                index++;
            }
            onSupportItemStep(module, index);
        }
        //判断是否已经连接到服务端，如果没有则不会进行接下来的操作
        if (simulateViewModel.getClient() == null || !simulateViewModel.getClient().isConnected()){
            return;
        }
        Action actions = module.getActions().get(currentStep);
        BaseJson<Action> message = new BaseJson<>(Client.TYPE_ACTION, actions);
        try {
            simulateViewModel.getClient().sendMessage(message);
        } catch (IOException e) {
            // TODO: 2023/12/4 检测消息无法发送的原因
            e.printStackTrace();
        }
    }

    @Override
    public void onSupportItemInsert(ActionModule module) {
        List<ActionModule> starredModules = simulateViewModel.getStarredModules();
        starredModules.add(module);
        simulateViewModel.getSupportAdapter().notifyItemInserted(starredModules.size());
    }

    @Override
    public void onSupportItemRemove(int position) {
        List<ActionModule> starredModules = simulateViewModel.getStarredModules();
        starredModules.remove(position);
        simulateViewModel.getSupportAdapter().notifyItemRemoved(position);
    }

    @Override
    public void onSupportItemStep(ActionModule module, int position) {
        simulateViewModel.getSupportAdapter().notifyItemChanged(position);
    }

    public OperatePage getOperatePage(){
        return operatePage;
    }
}
