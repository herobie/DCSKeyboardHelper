package com.example.dcskeyboardhelper.model.bean;

import java.util.List;

//每条support的数据
public class SupportItemData {
    private long moduleId;
    private String title;//support名
    private List<String> stepDesc;//各个步骤的名称
    private int currentStep = 0;//当前步骤指针位置

    public SupportItemData(long moduleId, String title, List<String> stepDesc, int currentStep) {
        this.title = title;
        this.stepDesc = stepDesc;
        this.currentStep = currentStep;
        this.moduleId = moduleId;
    }

    public SupportItemData(long moduleId, String title, List<String> stepDesc) {
        this.title = title;
        this.stepDesc = stepDesc;
        this.moduleId = moduleId;
    }

    public String getCurrentStepName(){
        if (stepDesc != null){
            return stepDesc.get(currentStep);
        }
        return null;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<String> getStepDesc() {
        return stepDesc;
    }

    public void setStepDesc(List<String> stepDesc) {
        this.stepDesc = stepDesc;
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
}
