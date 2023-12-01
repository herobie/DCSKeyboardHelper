package com.example.dcskeyboardhelper.ui;

import com.example.dcskeyboardhelper.model.bean.ActionModule;

public interface OnModuleChangeListener {
    void onInitModules();

    void onStarChange(ActionModule module, boolean isStarred);

    void onStepChange(int currentStep);

    void onModuleInserted(int position);

    void onModuleRemoved(int position);

    void onModuleUpdate(int position);

    void onModulesSwap(int from, int to);
}
