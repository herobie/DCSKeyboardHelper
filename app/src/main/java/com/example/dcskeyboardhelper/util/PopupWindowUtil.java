package com.example.dcskeyboardhelper.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListPopupWindow;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.util.List;

public class PopupWindowUtil {

    /**
     * 设置popupWindow参数
     * @param data 要展示的数据
     * @param view 目标view
     */
    @SuppressLint("UseCompatLoadingForDrawables")
    public static ListPopupWindow initPopupWindow(View view, Context context, String[] data, int width, int height, PopUpWindowListeners popUpWindowListeners) {
        final ListPopupWindow listPopupWindow;
        listPopupWindow = new ListPopupWindow(context);
        listPopupWindow.setAdapter(new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, data));//用android内置布局，或设计自己的样式
        listPopupWindow.setAnchorView(view);//设置目标View为锚点
        listPopupWindow.setModal(true);
        listPopupWindow.setWidth(width);
        listPopupWindow.setHeight(height);
        if (popUpWindowListeners != null){
            listPopupWindow.setOnItemClickListener(popUpWindowListeners.setItemClickListener());
            listPopupWindow.setOnDismissListener(popUpWindowListeners.setDismissListener());
        }
        return listPopupWindow;
    }

    /**
     * 设置popupWindow参数(传入集合）
     * @param data 要展示的数据
     * @param view 目标view
     */
    @SuppressLint("UseCompatLoadingForDrawables")
    public static ListPopupWindow initPopupWindow(View view, Context context, List<String> data, int width, int height, PopUpWindowListeners popUpWindowListeners) {
        final ListPopupWindow listPopupWindow;
        listPopupWindow = new ListPopupWindow(context);
        listPopupWindow.setAdapter(new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, data));//用android内置布局，或设计自己的样式
        listPopupWindow.setAnchorView(view);//设置目标View为锚点
        listPopupWindow.setModal(true);
        listPopupWindow.setWidth(width);
        listPopupWindow.setHeight(height);
        if (popUpWindowListeners != null){
            listPopupWindow.setOnItemClickListener(popUpWindowListeners.setItemClickListener());
            listPopupWindow.setOnDismissListener(popUpWindowListeners.setDismissListener());
        }
        return listPopupWindow;
    }

    public interface PopUpWindowListeners{
        AdapterView.OnItemClickListener setItemClickListener();

        PopupWindow.OnDismissListener setDismissListener();
    }

    //给EditText尾部的ICON设置点击事件
    public interface EndIconTouchListener extends View.OnTouchListener{
        @Override
        default boolean onTouch(View view, MotionEvent event) {
            final int DRAWABLE_RIGHT = 2;
            if (event.getAction() == MotionEvent.ACTION_UP) {
                if (view instanceof TextView && event.getX() >= (view.getWidth() - ((TextView)view)
                        .getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width() * 3)){
                    action();
                    return true;
                }
            }
            return false;
        }

        void action();
    }
}
