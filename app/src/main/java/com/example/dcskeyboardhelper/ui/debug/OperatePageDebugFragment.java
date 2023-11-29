package com.example.dcskeyboardhelper.ui.debug;

import android.annotation.SuppressLint;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
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
import com.example.dcskeyboardhelper.ui.OnModuleChangeListener;
import com.example.dcskeyboardhelper.viewModel.ModuleDebugViewModel;
import com.example.dcskeyboardhelper.viewModel.OperatePageViewModel;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

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
                    return adapter.getItemMoveListener().onMove(fromPosition, targetPosition);
                }
                return true;
            }

            @SuppressLint("NewApi")
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

            }

            @Override
            public float getMoveThreshold(@NonNull RecyclerView.ViewHolder viewHolder) {
                return 0.5f;
            }
        });
        itemTouchHelper.attachToRecyclerView(binding.rvOperate);

        viewModel.getAllModules(operatePage.getPageId()).observe(this, new Observer<List<ActionModule>>() {
            @Override
            public void onChanged(List<ActionModule> actionModules) {
                //只会刷新显示中的fragment的adapter
                if (!OperatePageDebugFragment.this.isHidden()){
                    adapter.setModules(actionModules);
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

    @Override
    public void onStarChange(ActionModule module, boolean isStarred) {
        List<SupportItemData> list = moduleDebugViewModel.getStatusDisplayed().getValue();
        if (list != null){
            if (isStarred){
                SupportItemData supportItemData = new SupportItemData(module.getId(), module.getDesc(),
                        module.getStepsDesc(), module.getCurrentStep());
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
    public void onStepChange(int currentStep) {

    }
}