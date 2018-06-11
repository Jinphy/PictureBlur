package com.example.jinphy.pictureblur.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.NonNull;
import android.view.Window;
import android.view.WindowManager;

/**
 * DESC:
 * Created by jinphy on 2018/6/11.
 */

public class DeviceUtils {


    /**
     * @param context 上下文
     * @param dpValue dp数值
     * @return dp to  px
     */
    public static int dp2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public static int dp2px(int dpValue, float density) {
        return (int) (dpValue*density+0.5f);
    }

    /**
     * 把像素单位转换为像素无关单位dp
     * @param context    上下文
     * @param pxValue  px的数值
     * @return  px to dp
     */
    public static int px2dp(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);

    }


    /**
     * @param context 上下文
     * @param spValue dp数值
     * @return dp to  px
     */
    public static int sp2px(Context context, float spValue) {
        final float scale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * scale + 0.5f);
    }


    /**
     * 获取状态来的高度，单位为像素
     * @param context 上下文
     * @return 返回状态栏像素单位的高度
     * */
    public static int getStatusBarHeight(Context context){
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }
    /**
     * 把状态栏设置为指定的颜色
     * @param activity activity 对象
     * @param color 颜色值
     * */
    public static void setStatusBarColor(@NonNull Activity activity, int color){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity.getWindow().setStatusBarColor(color);
        }
    }

    /**
     * 获取toolbar的高度
     * */
    public static int getToolbarHeight(Context context) {
        return (int) (56 * getDensity(context));
    }

    /**
     *
     * 获取对应activity对象下的状态栏颜色
     * @param activity activity对象
     *
     * */
    public static int getStatusBarColor(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return activity.getWindow().getStatusBarColor();
        }
        return Color.BLACK;
    }

    /**
     * 把对应activity对象下的屏幕设置成全屏，该方法必须在
     * activity.setContentView()之前调用
     * @param activity activity对象
     * */
    public static void setFullScreen(Activity activity) {
        activity.requestWindowFeature(Window.FEATURE_NO_TITLE);

        activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    /**
     * 获取屏幕宽度
     * @param context 上下文
     * @return 屏幕宽度，单位为像素
     * */
    public static int getScreenWidth(Context context){
        return context.getResources().getDisplayMetrics().widthPixels;
    }

    /**
     * 获取屏幕高度
     * @param context 上下文
     * @return 屏幕高度，单位为像素
     * */
    public static int getScreenHeight(Context context) {
        return context.getResources().getDisplayMetrics().heightPixels;
    }

    /**
     * 获取屏幕密度比
     * @param context 上下文
     * @return 屏幕密度比，xml文件中的dp单位的大小乘上该值即为像素大小
     * */
    public static float getDensity(Context context) {
        return context.getResources().getDisplayMetrics().density;
    }

    /**
     * 获取字体的密度比
     * @param context 上下文
     * @return 字体的密度比，xml文件中的sp单位的字体大小乘上该值，即为字体的像素大小
     * */
    public static float getScaleDensity(Context context) {
        return context.getResources().getDisplayMetrics().scaledDensity;
    }

}
