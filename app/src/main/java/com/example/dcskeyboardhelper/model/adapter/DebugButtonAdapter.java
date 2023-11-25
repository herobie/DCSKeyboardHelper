package com.example.dcskeyboardhelper.model.adapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dcskeyboardhelper.R;
import com.example.dcskeyboardhelper.base.BaseAdapter;
import com.example.dcskeyboardhelper.databinding.ItemActionButtonBinding;
import com.example.dcskeyboardhelper.model.Constant;
import com.example.dcskeyboardhelper.model.bean.ActionModule;
import com.example.dcskeyboardhelper.ui.OnModuleStarChangeListener;
import com.example.dcskeyboardhelper.viewModel.ModuleDebugViewModel;
import com.example.dcskeyboardhelper.viewModel.OperatePageViewModel;

import java.util.List;

public class DebugButtonAdapter extends BaseAdapter<ItemActionButtonBinding, OperatePageViewModel> {
    private List<ActionModule> modules;
    private Context context;
    private OnModuleStarChangeListener onModuleStarChangeListener;

    public DebugButtonAdapter(OperatePageViewModel viewModel, Context context) {
        super(viewModel);
        this.context = context;
    }

    public void setModules(List<ActionModule> modules) {
        this.modules = modules;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        super.onBindViewHolder(holder, position);
        binding.tvActionName.setText(modules.get(position).getTitle());
        //设置container里各个view的权重
        binding.stepIndicatorContainer.setWeightSum(measureIndicatorWeight(modules.get(position).getStepsNum()));

        binding.cbStar.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (onModuleStarChangeListener != null){
                    ActionModule actionModule = modules.get(position);
                    actionModule.setStarred(isChecked);
                    onModuleStarChangeListener.onStarChange(modules.get(position).getDesc(), isChecked);
                    viewModel.updateModule(actionModule);
                }
            }
        });

        binding.ibSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
                ActionModule module = modules.get(position);
                int switchMode = module.getSwitchMode();
                if (switchMode == Constant.STEP){
                    step(module);
                }else if (switchMode == Constant.LOOP){
                    loop(module);
                }
            }
        });
    }

    private void loop(ActionModule module){
        if (binding.stepIndicatorContainer.getChildCount() < module.getStepsNum()){
            //如果指示器里的view数量小于步骤总数，那么就增加view，同时module里的currentStep加1
            View view = new View(context);
            view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0));
            view.setBackgroundResource(R.color.grey_light);
            view.setAlpha(0.5f);
            binding.stepIndicatorContainer.addView(view);
            int currentStep = module.getCurrentStep();
            module.setCurrentStep(currentStep + 1);
        }else {
            //如果大于等于步骤总数，那么则会移除所有view，实现“循环”效果
            binding.stepIndicatorContainer.removeAllViews();
            module.setCurrentStep(0);
        }
    }

    private void step(ActionModule module){
        if (binding.stepIndicatorContainer.getChildCount() < module.getStepsNum()){
            //如果指示器里的view数量小于步骤总数，那么就增加view，同时module里的currentStep加1
            View view = new View(context);
            view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0));
            view.setBackgroundResource(R.color.grey_light);
            view.setAlpha(0.5f);
            binding.stepIndicatorContainer.addView(view);
            int currentStep = module.getCurrentStep();
            module.setCurrentStep(currentStep + 1);
        }else {
            //先移除掉所有位置大于步骤上限的view
            int index = binding.stepIndicatorContainer.getChildCount();
            while (binding.stepIndicatorContainer.getChildCount() > module.getStepsNum()){
                try {
                    binding.stepIndicatorContainer.removeViewAt(index);
                }catch (IndexOutOfBoundsException e){
                    continue;
                }
                index--;
            }
            module.setCurrentStep(module.getStepsNum());//移除掉所有有问题的超额view后，按理说指针应该处于最大步骤处
            //开始执行步减操作
            binding.stepIndicatorContainer.removeViewAt(module.getCurrentStep() - 1);
            module.setCurrentStep(module.getCurrentStep() - 1);
        }
    }

    private float measureIndicatorWeight(int stepSum){
        return stepSum -1;
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

    public void setOnModuleStarChangeListener(OnModuleStarChangeListener onModuleStarChangeListener) {
        this.onModuleStarChangeListener = onModuleStarChangeListener;
    }
}
