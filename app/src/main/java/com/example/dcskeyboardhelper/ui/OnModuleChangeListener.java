package com.example.dcskeyboardhelper.ui;

import com.example.dcskeyboardhelper.model.bean.ActionModule;

public interface OnModuleChangeListener {
    void onInitModules();//初始化数据时调用，多配合notifyDataSetChange等粗暴刷新机制

    void onStarChange(ActionModule module, boolean isStarred);//星标状态改变时调用

    void onStepChange(int currentStep);//执行步骤(步骤指针）发生改变时调用

    void onModuleInserted(ActionModule module, int position);//模块被添加时调用

    void onModuleRemoved(int position);//模块被删除时调用

    void onModuleUpdate(int position, ActionModule module);//模块被更新时调用

    void onModulesSwap(int from, int to);//执行交换位置时调用
}
