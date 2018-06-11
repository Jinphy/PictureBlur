package com.example.jinphy.pictureblur.views.dialog;

import android.view.View;

import com.example.jinphy.pictureblur.base.BaseDialog;

/**
 * DESC:
 * Created by jinphy on 2018/3/14.
 */

public interface Dialog {

    Dialog width(int valueDp);

    Dialog height(int valueDp);

    Dialog alpha(float alpha);

    Dialog x(int valueDp);

    Dialog y(int valueDp);

    Dialog gravity(int gravity);

    Dialog view(View view);

    Dialog view(int resourceId);

    Dialog cancelable(boolean cancelable);

    Dialog hasFocus(boolean hasFocus);

    Dialog onDismiss(BaseDialog.Callback onDismiss);

    Dialog onDisplay(BaseDialog.Callback onDisplay);

    BaseDialog.Holder display();


    void dismiss();



}
