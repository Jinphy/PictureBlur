package com.example.jinphy.pictureblur.base;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * DESC:
 * Created by jinphy on 2018/6/11.
 */

public class App extends Application {

    private static Context app;
    private static Toast toast;

    private static Map<String,Object> extra;

    @Override
    public void onCreate() {
        super.onCreate();
        App.app = this;
        App.toast = Toast.makeText(this, "",Toast.LENGTH_SHORT);
        extra = new ConcurrentHashMap<>();
    }

    /**
     * DESC: 获取全局Context
     * Created by jinphy, on 2018/6/11, at 16:52
     */
    public static Context app() {
        return App.app;
    }

    /**
     * DESC: 显示提示信息
     * Created by jinphy, on 2018/6/11, at 16:52
     */
    public static void toast(Object text) {
        if (text == null) {
            return;
        }
        if (text instanceof CharSequence) {
            App.toast.setText((CharSequence) text);
        } else {
            App.toast.setText(text.toString());
        }
        App.toast.show();
    }

    /**
     * DESC: 显示提示信息
     * Created by jinphy, on 2018/6/11, at 16:52
     */

    public static void toast(int resId) {
        App.toast.setText(resId);
        App.toast.show();
    }

    /**
     * DESC: 发送广播
     * Created by jinphy, on 2018/6/12, at 13:25
     */
    public static void broadcast(Intent intent) {
        App.app.sendBroadcast(intent);
    }

    public static void setFullScreen(Activity activity) {
        if (activity == null) {
            return;
        }
        activity.requestWindowFeature(Window.FEATURE_NO_TITLE);
        activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager
                .LayoutParams.FLAG_FULLSCREEN);
    }

    /**
     * DESC: 设置竖屏
     * Created by jinphy, on 2018/6/11, at 21:24
     */
    public static void setVertical(Activity activity) {
        activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    /**
     * DESC: 设置横屏
     * Created by jinphy, on 2018/6/11, at 21:24
     */
    public static void setHorizontal(Activity activity) {
        activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
    }

    public static void put(String key, Object value) {
        App.extra.put(key, value);
    }

    public static Object get(String key) {
        return extra.get(key);
    }

    public static Bitmap getBitmap(String key) {
        return ((Bitmap) extra.get(key));
    }

    public static void remove(String key) {
        extra.remove(key);
    }
}
