package com.example.dcskeyboardhelper.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.dcskeyboardhelper.model.ActionModuleModel;
import com.example.dcskeyboardhelper.model.OperatePageModel;
import com.example.dcskeyboardhelper.model.adapter.FragmentsAdapter;
import com.example.dcskeyboardhelper.model.adapter.SupportAdapter;
import com.example.dcskeyboardhelper.model.bean.ActionModule;
import com.example.dcskeyboardhelper.model.bean.OperatePage;
import com.example.dcskeyboardhelper.model.socket.Client;
import com.example.dcskeyboardhelper.ui.simulate.OperatePageFragment;

import java.util.ArrayList;
import java.util.List;

public class SimulateViewModel extends AndroidViewModel {
    private long profileId;
    private OperatePage currentPage;
    private final OperatePageModel operatePageModel;
    private FragmentManager fragmentManager;
    private FragmentsAdapter<OperatePageFragment> operatePageAdapter;
    private SupportAdapter supportAdapter;
    private List<ActionModule> starredModules = new ArrayList<>();
    private final MutableLiveData<Boolean> isStarredModulesReady = new MutableLiveData<>();
    private Client client;//传入ConnectService中Client的引用,方便在子页面进行调用

    private boolean isCorrectMode = false;//判断是否处于调试模式

    public SimulateViewModel(@NonNull Application application) {
        super(application);
        operatePageModel = new OperatePageModel(application);
        isStarredModulesReady.setValue(false);
    }

    //添加星标模组
    public void addStarredModules(List<ActionModule> modules){
        for (ActionModule module : modules){
            if (module.isStarred()){
                starredModules.add(module);
            }
        }
    }

    public List<ActionModule> getStarredModules(){
        return starredModules;
    }

    public MutableLiveData<Boolean> getIsStarredModulesReady() {
        return isStarredModulesReady;
    }

    public List<OperatePage> getOperatePage(){
        return operatePageModel.getOperatePage(profileId);
    }

    public long getProfileId() {
        return profileId;
    }

    public void setProfileId(long profileId) {
        this.profileId = profileId;
    }

    public OperatePage getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(OperatePage currentPage) {
        this.currentPage = currentPage;
    }

    public FragmentManager getFragmentManager() {
        return fragmentManager;
    }

    public void setFragmentManager(FragmentManager fragmentManager) {
        this.fragmentManager = fragmentManager;
    }

    public void setOperatePageAdapter(FragmentsAdapter<OperatePageFragment> operatePageAdapter) {
        this.operatePageAdapter = operatePageAdapter;
    }

    public FragmentsAdapter<OperatePageFragment> getOperatePageAdapter() {
        return operatePageAdapter;
    }

    public SupportAdapter getSupportAdapter() {
        return supportAdapter;
    }

    public void setSupportAdapter(SupportAdapter supportAdapter) {
        this.supportAdapter = supportAdapter;
    }

    public boolean isCorrectMode() {
        return isCorrectMode;
    }

    public void setCorrectMode(boolean correctMode) {
        isCorrectMode = correctMode;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }
}
