package com.example.dcskeyboardhelper.ui.listeners;

import com.example.dcskeyboardhelper.model.bean.ActionModule;

public interface OnSupportItemChangeListener {
    void onSupportItemInsert(ActionModule module);//添加

    void onSupportItemRemove(int position);//移除

    void onSupportItemStep(ActionModule module, int position);//步骤更新时调用
}
