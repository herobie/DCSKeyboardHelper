package com.example.dcskeyboardhelper.model.adapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dcskeyboardhelper.R;
import com.example.dcskeyboardhelper.base.BaseAdapter;
import com.example.dcskeyboardhelper.databinding.ItemActionButtonBinding;
import com.example.dcskeyboardhelper.model.Constant;
import com.example.dcskeyboardhelper.model.bean.ActionModule;
import com.example.dcskeyboardhelper.model.bean.SupportItemData;
import com.example.dcskeyboardhelper.ui.OnModuleChangeListener;
import com.example.dcskeyboardhelper.ui.debug.ItemMoveListener;
import com.example.dcskeyboardhelper.ui.dialog.ModuleUpdateDialog;
import com.example.dcskeyboardhelper.viewModel.OperatePageViewModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DebugButtonAdapter extends BaseAdapter<ItemActionButtonBinding, OperatePageViewModel>
        implements ItemMoveListener {
    private List<ActionModule> modules;
    private final Context context;
    private OnModuleChangeListener onModuleChangeListener;
    private final ItemMoveListener itemMoveListener = this;

    public DebugButtonAdapter(OperatePageViewModel viewModel, Context context) {
        super(viewModel);
        this.context = context;
    }

    public void setModules(List<ActionModule> modules) {
        this.modules = modules;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder holder = super.onCreateViewHolder(parent, viewType);
        holder.setIsRecyclable(false);//省事，避免VH复用带来的显示错乱问题
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        super.onBindViewHolder(holder, position);
        binding.tvActionName.setText(modules.get(holder.getAdapterPosition()).getTitle());
        binding.cbStar.setChecked(modules.get(holder.getAdapterPosition()).isStarred());

        binding.cbStar.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                ActionModule actionModule = modules.get(holder.getAdapterPosition());
                actionModule.setStarred(isChecked);
                actionModule.setCurrentStep(0);//这里把步骤指针重置为0.避免后面显示错乱了
                onModuleChangeListener.onModuleUpdate(holder.getAdapterPosition());
                viewModel.updateModule(actionModule);
                onModuleChangeListener.onStarChange(modules.get(holder.getAdapterPosition()), isChecked);
            }
        });

        binding.ibSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ModuleUpdateDialog updateDialog = new ModuleUpdateDialog(context, viewModel,
                        modules.get(holder.getAdapterPosition()));
                updateDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        if (updateDialog.isUpdate()){
                            onModuleChangeListener.onModuleUpdate(holder.getAdapterPosition());
                        }
                    }
                });
                updateDialog.show();
            }
        });

        binding.ibRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context)
                        .setTitle(context.getString(R.string.warning))
                        .setMessage(context.getString(R.string.confirm_operate));
                alertBuilder.setPositiveButton(context.getString(R.string.confirm), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        viewModel.deleteModule(modules.get(position).getId());
                    }
                });
                alertBuilder.setNegativeButton(context.getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                alertBuilder.show();
            }
        });

        binding.clActionBackground.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActionModule module = modules.get(holder.getAdapterPosition());
                int switchMode = module.getSwitchMode();
                //step和loop的container参数不能直接传一个binding.stepIndicatorContainer，怀疑是和dataBinding绑定的viewHolder错乱有关
                LinearLayout container = holder.itemView.findViewById(R.id.step_indicator_container);
                if (switchMode == Constant.STEP){
                    step(module, container);
                }else if (switchMode == Constant.LOOP){
                    loop(module, container);
                }
            }
        });
    }

    private void loop(ActionModule module, LinearLayout container){
        if (container.getChildCount() < module.getStepsNum()){
            //如果指示器里的view数量小于步骤总数，那么就增加view，同时module里的currentStep加1
            View view = new View(container.getContext());
            view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    measureIndicatorWeight(container, module.getStepsNum())));
            view.setBackgroundResource(R.color.grey_light);
            view.setAlpha(0.5f);
            container.addView(view);
            int currentStep = module.getCurrentStep();
            module.setCurrentStep(currentStep + 1);
        }else {
            //如果大于等于步骤总数，那么则会移除所有view，实现“循环”效果
            container.removeAllViews();
            module.setCurrentStep(0);
        }
    }

    private void step(ActionModule module, LinearLayout container){
        if (module.isReverse()){
            if (module.getCurrentStep() > 0){
                //开始执行步减操作
                container.removeViewAt(module.getCurrentStep() - 1);
                module.setCurrentStep(module.getCurrentStep() - 1);
            }
            if (module.getCurrentStep() == 0){
                module.setReverse(false);//如果步减到了最小步骤，则开始步增
            }
            return;
        }

        if (container.getChildCount() < module.getStepsNum()){
            //如果指示器里的view数量小于步骤总数，那么就增加view，同时module里的currentStep加1
            View view = new View(context);
            view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    measureIndicatorWeight(container, module.getStepsNum())));
            view.setBackgroundResource(R.color.grey_light);
            view.setAlpha(0.5f);
            container.addView(view);
            int currentStep = module.getCurrentStep();//注意这里，先获取本次的步骤位置
            module.setCurrentStep(currentStep + 1);//再给module更新步骤位置
            //步进模式下，如果达到了最大步骤，则开始步减
            if (module.getCurrentStep() >= module.getStepsNum()){
                module.setReverse(true);
            }
        }else {
            //先移除掉所有位置大于步骤上限的view
            int index = container.getChildCount();
            while (container.getChildCount() > module.getStepsNum()){
                try {
                    container.removeViewAt(index);
                }catch (IndexOutOfBoundsException e){
                    continue;
                }
                index--;
            }
            module.setCurrentStep(module.getStepsNum());//移除掉所有有问题的超额view后，按理说指针应该处于最大步骤处
            //开始执行步减操作
            container.removeViewAt(module.getCurrentStep() - 1);
            module.setCurrentStep(module.getCurrentStep() - 1);
        }
    }

    private int measureIndicatorWeight(LinearLayout container, int stepSum){
        return container.getMeasuredHeight() / stepSum;
    }

    public List<ActionModule> getModules() {
        return modules;
    }

    @Override
    protected int getItemLayoutRes() {
        return R.layout.item_action_button;
    }

    @Override
    public int getItemCount() {
        return modules == null? 0: modules.size();
    }

    public void setOnModuleChangeListener(OnModuleChangeListener onModuleChangeListener) {
        this.onModuleChangeListener = onModuleChangeListener;
    }

    public ItemMoveListener getItemMoveListener() {
        return itemMoveListener;
    }

    @Override
    public boolean onMove(int from, int to) {
        //adapter里交换元素
        Collections.swap(modules, from, to);
        notifyItemMoved(from, to);
        //获取交换双方的对象
        ActionModule fromModule = modules.get(from);
        ActionModule toModule = modules.get(to);
        //将二者坐标对调
        fromModule.setGridPosition(to);
        toModule.setGridPosition(from);
        //保存坐标
//        viewModel.updateModule(fromModule);
//        viewModel.updateModule(toModule);
        return true;
    }
}
