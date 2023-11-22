package com.example.dcskeyboardhelper.util;

import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.view.View;
import android.view.WindowManager;

public class StatusBarUtil {
    //设置Activity全屏幕展示
    public static void setFullscreen(Activity activity, boolean isShowStatusBar, boolean isShowNavigationBar) {
        int uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;

        if (!isShowStatusBar) {
            uiOptions |= View.SYSTEM_UI_FLAG_FULLSCREEN;
        }
        if (!isShowNavigationBar) {
            uiOptions |= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        }
        activity.getWindow().getDecorView().setSystemUiVisibility(uiOptions);

        //隐藏标题栏
        // getSupportActionBar().hide();
        setNavigationStatusColor(activity, Color.TRANSPARENT);
    }

    public static void setNavigationStatusColor(Activity activity, int color) {
        //VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP
        activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        activity.getWindow().setNavigationBarColor(color);
        activity.getWindow().setStatusBarColor(color);
    }

    //根据activity的背景颜色深浅调整状态栏文字颜色
    public static void setAndroidNativeLightStatusBar(Activity activity, boolean dark) {
        View decor = activity.getWindow().getDecorView();
        if (dark) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            }
        } else {
            decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        }
    }

}
