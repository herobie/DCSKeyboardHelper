package com.example.dcskeyboardhelper.ui;

import com.example.dcskeyboardhelper.model.bean.ActionModule;

public interface OnModuleChangeListener {
    void onStarChange(ActionModule module, boolean isStarred);

    void onStepChange(int currentStep);
}
