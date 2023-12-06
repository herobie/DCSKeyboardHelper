package com.example.dcskeyboardhelper.ui.listeners;

import com.example.dcskeyboardhelper.model.bean.ActionModule;

public interface OnModuleChangeBasicListener {
    void onInitModules();//初始化数据时调用，多配合notifyDataSetChange等粗暴刷新机制

    void onStarChange(ActionModule module, boolean isStarred);//星标状态改变时调用

    void onStepChange(ActionModule module, int currentStep);//执行步骤(步骤指针）发生改变时调用
}
