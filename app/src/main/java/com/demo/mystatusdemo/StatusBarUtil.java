package com.demo.mystatusdemo;

import android.annotation.TargetApi;
import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Created by wushange on 2017/9/15.
 */

public class StatusBarUtil {

    /**
     * 设置状态栏全透明
     *
     * @param activity 需要设置的activity
     */
    public static void setTransparentPadding(Activity activity) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            return;
        }
        transparentStatusBar(activity);
        setRootView(activity);
    }
    @TargetApi(Build.VERSION_CODES.KITKAT)
    private static void transparentStatusBar(@NonNull final Activity activity) {
        Window window = activity.getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            window.getDecorView().setSystemUiVisibility(option);
            window.setStatusBarColor(Color.TRANSPARENT);
        } else {
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }
    /**
     * 设置根布局参数
     */
    public static void setRootView(Activity activity) {
        ViewGroup rootView =
                (ViewGroup) ((ViewGroup) activity.findViewById(android.R.id.content)).getChildAt(0);
        rootView.setFitsSystemWindows(true);
        rootView.setClipToPadding(true);
    }
    /**
     * 设置状态栏字体颜色是否加深
     *
     * @param activity
     * @param isDark   true为加深
     */
    public static void setStatusBarTextColor(Activity activity, boolean isDark) {
        if (isDark == true) {
            if (OsInfoUtil.isMIUI()) {
                setMiuiStatusBarDarkMode(activity, true);
            } else if (OsInfoUtil.isFlyme()) {
                setMeizuStatusBarDarkIcon(activity, true);
            } else {
                setStatusBarLightMode(activity, true);
            }
        } else {
            if (OsInfoUtil.isMIUI()) {
                setMiuiStatusBarDarkMode(activity, false);
            } else if (OsInfoUtil.isFlyme()) {
                setMeizuStatusBarDarkIcon(activity, false);
            } else {
                setStatusBarLightMode(activity, false);
            }
        }
    }

    /**
     * 设置6.0+状态栏字体颜色
     *
     * @param activity
     * @param isFontColorDark
     */
    private static void setStatusBarLightMode(Activity activity, boolean isFontColorDark) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (isFontColorDark) {
                // 沉浸式
                //                activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                //非沉浸式
                activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            } else {
                //非沉浸式
                activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
            }
        }
    }
    /**
     * MIUI 状态栏字体颜色
     *
     * @param activity
     * @param darkmode
     * @return
     */
    private static boolean setMiuiStatusBarDarkMode(Activity activity,
                                                    boolean darkmode) {
        Class<? extends Window> clazz = activity.getWindow().getClass();
        try {
            int darkModeFlag = 0;
            Class<?> layoutParams = Class
                    .forName("android.view.MiuiWindowManager$LayoutParams");
            Field field = layoutParams
                    .getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
            darkModeFlag = field.getInt(layoutParams);
            Method extraFlagField = clazz.getMethod("setExtraFlags", int.class,
                    int.class);
            extraFlagField.invoke(activity.getWindow(), darkmode ? darkModeFlag
                    : 0, darkModeFlag);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * flyme状态栏字体颜色
     *
     * @param activity
     * @param dark
     * @return
     */
    private static boolean setMeizuStatusBarDarkIcon(Activity activity,
                                                     boolean dark) {
        boolean result = false;
        if (activity != null) {
            try {
                WindowManager.LayoutParams lp = activity.getWindow()
                        .getAttributes();
                Field darkFlag = WindowManager.LayoutParams.class
                        .getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON");
                Field meizuFlags = WindowManager.LayoutParams.class
                        .getDeclaredField("meizuFlags");
                darkFlag.setAccessible(true);
                meizuFlags.setAccessible(true);
                int bit = darkFlag.getInt(null);
                int value = meizuFlags.getInt(lp);
                if (dark) {
                    value |= bit;
                } else {
                    value &= ~bit;
                }
                meizuFlags.setInt(lp, value);
                activity.getWindow().setAttributes(lp);
                result = true;
            } catch (Exception e) {
            }
        }
        return result;
    }
}
