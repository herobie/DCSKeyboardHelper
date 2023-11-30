package com.example.dcskeyboardhelper.model.bean;

import java.util.List;

//每条support的数据
public class SupportItemData {
    private long moduleId;
    private String title;//support名
    private int currentStep = 0;//当前步骤指针位置
    private final List<Action> actions;

    public SupportItemData(long moduleId, String title, List<Action> actions, int currentStep) {
        this.title = title;
        this.actions = actions;
        this.currentStep = currentStep;
        this.moduleId = moduleId;
    }

    public SupportItemData(long moduleId, String title, List<Action> actions) {
        this.moduleId = moduleId;
        this.title = title;
        this.actions = actions;
    }

    public String getCurrentStepName(){
        if (actions != null){
            return actions.get(currentStep).getName();
        }
        return null;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getCurrentStep() {
        return currentStep;
    }

    public void setCurrentStep(int currentStep) {
        this.currentStep = currentStep;
    }

    public long getModuleId() {
        return moduleId;
    }

    public List<Action> getActions() {
        return actions;
    }
}
