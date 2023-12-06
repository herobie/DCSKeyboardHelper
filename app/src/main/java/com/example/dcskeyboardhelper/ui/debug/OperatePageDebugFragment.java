package com.example.dcskeyboardhelper.ui.debug;

import android.annotation.SuppressLint;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dcskeyboardhelper.R;
import com.example.dcskeyboardhelper.base.BaseFragment;
import com.example.dcskeyboardhelper.databinding.FragmentOperateBinding;
import com.example.dcskeyboardhelper.model.adapter.DebugButtonAdapter;
import com.example.dcskeyboardhelper.model.bean.ActionModule;
import com.example.dcskeyboardhelper.model.bean.OperatePage;
import com.example.dcskeyboardhelper.model.bean.SupportItemData;
import com.example.dcskeyboardhelper.ui.listeners.OnModuleChangeListener;
import com.example.dcskeyboardhelper.viewModel.ModuleDebugViewModel;
import com.example.dcskeyboardhelper.viewModel.OperatePageViewModel;

import java.util.Collections;
import java.util.List;

public class OperatePageDebugFragment extends BaseFragment<FragmentOperateBinding, OperatePageViewModel>
        implements OnModuleChangeListener{
    private OperatePage operatePage;
    private DebugButtonAdapter adapter;
    private ModuleDebugViewModel moduleDebugViewModel;
    public OperatePageDebugFragment(ModuleDebugViewModel moduleDebugViewMode, OperatePage operatePage) {
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
        adapter.setOnModuleChangeListener(this);
        binding.rvOperate.setAdapter(adapter);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.Callback() {
            @Override
            public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
                int swipeFlags =ItemTouchHelper.ACTION_STATE_IDLE;
                int dragFlags = ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT | ItemTouchHelper.DOWN |  ItemTouchHelper.UP;
                return makeMovementFlags(dragFlags, swipeFlags);
            }

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder,
                                  @NonNull RecyclerView.ViewHolder target) {
                int fromPosition = viewHolder.getLayoutPosition();
                int targetPosition = target.getLayoutPosition();
                if (adapter != null){
                    onModulesSwap(fromPosition, targetPosition);
                    return true;
                }
                return true;
            }

            @SuppressLint("NewApi")
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

            }

            @Override
            public float getMoveThreshold(@NonNull RecyclerView.ViewHolder viewHolder) {
                return 0.8f;
            }
        });
        itemTouchHelper.attachToRecyclerView(binding.rvOperate);

        List<ActionModule> modules = viewModel.getAllModules(operatePage.getPageId());
        adapter.setModules(modules);
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

    public OperatePage getOperatePage() {
        return operatePage;
    }

    public List<ActionModule> getAdapterModules() {
        return adapter.getModules();
    }

    @Override
    public void onInitModules() {
        Log.d("MainActivity", "onInitModules: ");
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onStarChange(ActionModule module, boolean isStarred) {
        Log.d("MainActivity", "onStarChange: ");
        List<SupportItemData> list = moduleDebugViewModel.getStatusDisplayed().getValue();
        if (list != null){
            if (isStarred){
                SupportItemData supportItemData = new SupportItemData(module.getId(), module.getDesc(),
                        module.getActions(), module.getCurrentStep());
                list.add(supportItemData);
            }else {
                int index = 0;
                for (SupportItemData supportItemData : list){
                    if (supportItemData.getModuleId() == module.getId()){
                        list.remove(index);
                        break;
                    }
                    index++;
                }
            }
            moduleDebugViewModel.getStatusDisplayed().postValue(list);
        }
    }

    @Override
    public void onStepChange(ActionModule module, int currentStep) {

    }

    @Override
    public void onModuleInserted(ActionModule module, int position) {
        if (module.isStarred()){//检查一下是不是星标
            onStarChange(module, true);
        }
        if (position == -1){
            adapter.getModules().add(module);
            module.setGridPosition(adapter.getModules().size());
            adapter.notifyItemChanged(adapter.getModules().size());
            viewModel.insertModule(module);
            return;
        }
        module.setGridPosition(position);
        adapter.getModules().add(position, module);
        adapter.notifyItemInserted(position);
        viewModel.insertModule(module);
    }

    @Override
    public void onModuleRemoved(int position) {
        viewModel.deleteModule(adapter.getModules().get(position).getId());
        if (adapter.getModules() != null){
            adapter.getModules().remove(position);
            adapter.notifyItemRemoved(position);
        }
    }

    @Override
    public void onModuleUpdate(int position, ActionModule module) {
        Log.d("MainActivity", "onModuleUpdate: ");
        viewModel.updateModule(module);
        adapter.getModules().set(position, module);
        adapter.notifyItemChanged(position);
    }

    @Override
    public void onModulesSwap(int from, int to) {
        Log.d("MainActivity", "onSwap: " + from + " and " + to);
        //刷新adapter
        Collections.swap(adapter.getModules(), from, to);
        adapter.notifyItemMoved(from, to);
        //数据库更新位置坐标
        for (int i = 0; i < adapter.getModules().size(); i++){
            adapter.getModules().get(i).setGridPosition(i);
        }
        viewModel.updateModule(adapter.getModules().toArray(new ActionModule[0]));
    }
}
