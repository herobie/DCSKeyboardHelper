package com.example.dcskeyboardhelper.model.bean;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.example.dcskeyboardhelper.model.Constant;
import com.example.dcskeyboardhelper.util.KeyConverter;

import java.util.List;
@Entity
@TypeConverters({KeyConverter.class})
public class ActionModule {
    @PrimaryKey(autoGenerate = true)
    private long id;//按钮的数据库id
    @ColumnInfo
    private String title;//呈现在按钮上的标题
    @ColumnInfo
    private String desc;//信息栏上面的状态，如（自动驾驶：）
    @ColumnInfo
    private int stepsNum;//步骤总数
    @ColumnInfo
    private int defaultStep;//程序开始时按钮处于的步骤位置
    @ColumnInfo
    private int switchMode;//切换模式（循环或步增）
    @ColumnInfo
    private List<Action> actions;//模拟按键操作
    @ColumnInfo
    private boolean isStarred;//是否被选中加入左侧信息框
    @ColumnInfo
    private long pageId;//该按钮所处的信息界面的id
    @ColumnInfo
    private long profileId;//所属存档配置的id
    @ColumnInfo
    private int gridPosition;//在gridLayout中的位置
    @Ignore
    private int currentStep;//当前处于的步骤
    @Ignore
    private boolean isReverse;//步进操作时，判断是增加步骤还是减少步骤

    public ActionModule(String title, String desc, int stepsNum, int defaultStep, int switchMode, List<Action> actions,
                        boolean isStarred, long pageId, long profileId, int gridPosition) {
        this.title = title;
        this.desc = desc;
        this.stepsNum = stepsNum;
        this.defaultStep = defaultStep;
        this.switchMode = switchMode;
        this.actions = actions;
        this.isStarred = isStarred;
        this.pageId = pageId;
        this.profileId = profileId;
        this.gridPosition = gridPosition;
        currentStep = defaultStep;
        isReverse = false;
    }

    @Ignore
    //需要填写的最基本的参数
    public ActionModule(String title, String desc, int stepsNum, List<Action> actions, long pageId, long profileId) {
        this.title = title;
        this.desc = desc;
        this.stepsNum = stepsNum;
        this.actions = actions;
        this.pageId = pageId;
        this.profileId = profileId;
        useDefaultAttr();
    }

    //未填写的参数使用默认值
    private void useDefaultAttr(){
        isStarred = false;
        isReverse = false;
        switchMode = Constant.LOOP;
        defaultStep = 0;
        currentStep = defaultStep;
    }

    public void updateModule(String title, String desc, int stepsNum, int defaultStep, int switchMode, List<Action> actions,
                             boolean isStarred, long pageId, long profileId, int gridPosition) {
        this.title = title;
        this.desc = desc;
        this.stepsNum = stepsNum;
        this.defaultStep = defaultStep;
        this.switchMode = switchMode;
        this.actions = actions;
        this.isStarred = isStarred;
        this.pageId = pageId;
        this.profileId = profileId;
        this.gridPosition = gridPosition;
        currentStep = defaultStep;
        isReverse = false;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getStepsNum() {
        return stepsNum;
    }

    public void setStepsNum(int stepsNum) {
        this.stepsNum = stepsNum;
    }

    public int getDefaultStep() {
        return defaultStep;
    }

    public void setDefaultStep(int defaultStep) {
        this.defaultStep = defaultStep;
    }

    public int getSwitchMode() {
        return switchMode;
    }

    public void setSwitchMode(int switchMode) {
        this.switchMode = switchMode;
    }


    public List<Action> getActions() {
        return actions;
    }

    public void setActions(List<Action> actions) {
        this.actions = actions;
    }

    public boolean isStarred() {
        return isStarred;
    }

    public void setStarred(boolean starred) {
        isStarred = starred;
    }

    public long getPageId() {
        return pageId;
    }

    public void setPageId(long pageId) {
        this.pageId = pageId;
    }

    public long getProfileId() {
        return profileId;
    }

    public void setProfileId(long profileId) {
        this.profileId = profileId;
    }

    public int getGridPosition() {
        return gridPosition;
    }

    public void setGridPosition(int gridPosition) {
        this.gridPosition = gridPosition;
    }

    public int getCurrentStep() {
        return currentStep;
    }

    public void setCurrentStep(int currentStep) {
        this.currentStep = currentStep;
    }

    public boolean isReverse() {
        return isReverse;
    }

    public void setReverse(boolean reverse) {
        isReverse = reverse;
    }
}
