package com.example.dcskeyboardhelper.ui.listeners;

import com.example.dcskeyboardhelper.model.bean.OperatePage;

public interface OperatePageChangeListener {
    void onPageInit();

    void onPageInsert(OperatePage operatePage);

    void onPageRemove(int position);

    void onPageUpdate(OperatePage...operatePages);
}
