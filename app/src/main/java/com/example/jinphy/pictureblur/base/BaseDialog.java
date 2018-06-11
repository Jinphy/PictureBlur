package com.example.jinphy.pictureblur.base;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.example.jinphy.pictureblur.utils.DeviceUtils;
import com.example.jinphy.pictureblur.views.dialog.Dialog;

import static android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN;
import static android.view.WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;

/**
 * DESC:
 * Created by jinphy on 2018/3/14.
 */

public class BaseDialog extends AlertDialog implements Dialog {

    private int width;

    private int height;

    private int resourceId;

    private View view;

    private boolean hasFocus = true;

    /**
     * DESC: x = 0 为默认值，对话框会在中间
     * Created by jinphy, on 2018/3/27, at 9:02
     */
    private int x;

    /**
     * DESC: y = 0 为默认值，对话框会在中间
     * Created by jinphy, on 2018/3/27, at 9:03
     */
    private int y;

    private int gravity = Gravity.CENTER;
    private Holder holder;
    private float alpha = 1;


    protected BaseDialog(Context context) {
        super(context);
    }

    public static Dialog create(Context context) {
        return new BaseDialog(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //        getWindow().setLayout(600,300);
        Window window = getWindow();

        window.setFlags(FLAG_FULLSCREEN,FLAG_FULLSCREEN);

        if (!hasFocus) {
            window.setFlags(FLAG_NOT_FOCUSABLE,FLAG_NOT_FOCUSABLE);
        }

        final WindowManager.LayoutParams attrs = window.getAttributes();
        // 设置该值可以自己设置的布局背景的形状才能够显示，比如cardView的圆角
        window.getDecorView().setBackgroundColor(Color.TRANSPARENT);

        if (width > 0) {
            attrs.width = DeviceUtils.dp2px(getContext(), width);
        }
        if (height > 0) {
            attrs.height = DeviceUtils.dp2px(getContext(), height);
        }
        attrs.x = DeviceUtils.dp2px(getContext(), x);
        attrs.y = DeviceUtils.dp2px(getContext(), y);
        attrs.gravity = gravity;
        attrs.alpha = alpha;
        window.setAttributes(attrs);
    }

    @Override
    public Dialog width(int valueDp) {
        this.width = valueDp;
        return this;
    }

    @Override
    public Dialog height(int valueDp) {
        this.height = valueDp;
        return this;
    }

    @Override
    public Dialog alpha(float alpha) {
        this.alpha = alpha;
        return this;
    }

    @Override
    public Dialog x(int valueDp) {
        this.x = valueDp;
        return this;
    }

    @Override
    public Dialog y(int valueDp) {
        this.y = valueDp;
        return this;
    }

    @Override
    public Dialog gravity(int gravity) {
        this.gravity = gravity;
        return this;
    }

    @Override
    public Dialog view(View view) {
        this.view = view;
        setView(view);
        holder = new Holder(view, this);
        return this;
    }

    @Override
    public Dialog view(int resourceId) {
        view =LayoutInflater.from(getContext())
                .inflate(resourceId, null, false);
        setView(view);
        holder = new Holder(view, this);
        return this;
    }

    @Override
    public Dialog cancelable(boolean cancelable) {
        setCancelable(cancelable);
        return this;
    }

    @Override
    public Dialog hasFocus(boolean hasFocus) {
        this.hasFocus = hasFocus;
        return this;
    }

    @Override
    public Dialog onDismiss(Callback onDismiss) {
        this.setOnDismissListener(dialog -> onDismiss.call(holder));
        return this;
    }

    @Override
    public Dialog onDisplay(Callback onDisplay) {
        this.setOnShowListener(dialog -> onDisplay.call(holder));
        return this;
    }

    @Override
    public Holder display() {
        show();
        return holder;
    }


    @Override
    public void dismiss() {
        super.dismiss();
    }

    public static class Holder{
        public final View view;

        public final Dialog dialog;

        public Holder(View view, Dialog dialog) {
            this.view = view;
            this.dialog = dialog;
        }
    }

    public interface Callback {
        void call(Holder holder);
    }
}
