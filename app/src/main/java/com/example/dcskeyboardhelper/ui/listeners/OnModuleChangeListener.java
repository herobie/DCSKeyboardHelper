package com.example.dcskeyboardhelper.ui.listeners;

import com.example.dcskeyboardhelper.model.bean.ActionModule;

public interface OnModuleChangeListener extends OnModuleChangeBasicListener{
    void onModuleInserted(ActionModule module, int position);//模块被添加时调用

    void onModuleRemoved(int position);//模块被删除时调用

    void onModuleUpdate(int position, ActionModule module);//模块被更新时调用

    void onModulesSwap(int from, int to);//执行交换位置时调用
}
