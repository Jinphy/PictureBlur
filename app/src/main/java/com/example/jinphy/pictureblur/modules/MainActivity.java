package com.example.jinphy.pictureblur.modules;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar;
import com.example.jinphy.pictureblur.R;
import com.example.jinphy.pictureblur.base.App;
import com.example.jinphy.pictureblur.custom.RuntimePermission;
import com.example.jinphy.pictureblur.utils.FileUtils;
import com.example.jinphy.pictureblur.utils.ImageUtils;
import com.example.jinphy.pictureblur.views.dialog.MenuDialog;

import java.io.File;

import static com.example.jinphy.pictureblur.modules.PickPhotoActivity.DATA_PICTURE;
import static com.example.jinphy.pictureblur.modules.PickPhotoActivity.Option.CHOOSE_PHOTO;
import static com.example.jinphy.pictureblur.modules.PickPhotoActivity.Option.TAKE_PHOTO;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private View btnChoosePicture;
    private View btnSave;
    private ImageView pictureView;
    private TextView btnLeftPercent;
    private TextView btnRightPercent;
    private RoundCornerProgressBar progressBar;

    public static final int STEP = 10;


    private Bitmap croppedBitmap;
    private Bitmap blurBitmap;
    private int blurRadius = 20;

    private boolean hasGranted = false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App.setFullScreen(this);
        App.setVertical(this);
        setContentView(R.layout.activity_main);

        btnChoosePicture = findViewById(R.id.btn_picture);
        btnSave = findViewById(R.id.btn_save);
        pictureView = findViewById(R.id.img_picture);
        btnLeftPercent = findViewById(R.id.left_percent);
        btnRightPercent = findViewById(R.id.right_percent);
        progressBar = findViewById(R.id.progress_bar);

        btnChoosePicture.setOnClickListener(this);
        btnSave.setOnClickListener(this);
        btnLeftPercent.setOnClickListener(this);
        btnRightPercent.setOnClickListener(this);

        RuntimePermission.newInstance(this)
                .permission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .permission(Manifest.permission.CAMERA)
                .onReject(() -> App.toast("您已拒绝相关权限！"))
                .onDialog(() -> App.toast("您已拒绝相关权限！"))
                .onGranted(() -> hasGranted = true)
                .execute();
        initView();
    }

    private void initView() {
        progressBar.setProgress(0);
    }


    @Override
    public void onClick(View v) {
        if (!hasGranted) {
            App.toast("您已拒绝该功能的相关权限，请到系统设置中授权！");
            return;
        }
        switch (v.getId()) {
            case R.id.btn_picture:
                MenuDialog.create(this)
                        .width(200)
                        .item("拍照", (menu, item) -> takePhoto())
                        .item("相册", (menu, item) -> choosePhoto())
                        .item(blurBitmap == null ? null : "分享", (menu, item) -> {
                            File tempFile = FileUtils.createTempFile(".jpg");
                            String filePath = ImageUtils.saveToFile(blurBitmap, tempFile);
                            FileUtils.shareFile(this, filePath);
                        })
                        .display();
                break;
            case R.id.btn_save:
                if (blurBitmap == null) {
                    App.toast("图片未获取！");
                    return;
                }
                // 保存图片并更新图库
                ImageUtils.saveToFile(blurBitmap, FileUtils.createPhotoFile(".jpg"));
                App.toast("图片保存成功！");
                break;
            case R.id.left_percent:
                if (blurBitmap == null) {
                    App.toast("图片未获取！");
                    return;
                }
                if (blurRadius < 0) {
                    blurRadius = 0;
                } else if (blurRadius > 0) {
                    blurRadius -= STEP;
                } else {
                    return;
                }

                blurBitmap();
                break;
            case R.id.right_percent:
                if (blurBitmap == null) {
                    App.toast("图片未获取！");
                    return;
                }
                if (blurRadius > 200) {
                    blurRadius = 200;
                } else if (blurRadius < 200) {
                    blurRadius += STEP;
                } else {
                    return;
                }
                blurBitmap();
                break;
            default:
                break;
        }
    }

    /**
     * DESC: 拍照获取图片
     * Created by jinphy, on 2018/6/11, at 17:50
     */
    private void takePhoto() {
        PickPhotoActivity.startForResult(this, TAKE_PHOTO);
    }

    /**
     * DESC: 从相册中选取图片
     * Created by jinphy, on 2018/6/11, at 17:51
     */
    private void choosePhoto() {
        PickPhotoActivity.startForResult(this, CHOOSE_PHOTO);
    }


    private static final String TAG = "MainActivity";

    /**
     * DESC: 拍照或者从相册中选择图片后返回结果
     * Created by jinphy, on 2018/6/12, at 0:08
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK || requestCode != PickPhotoActivity.PICK_PHOTO) {
            return;
        }
        croppedBitmap = App.getBitmap(DATA_PICTURE);
        blurRadius = 20;
        blurBitmap();
    }

    private void blurBitmap() {
        if (blurRadius <= 200) {
            blurBitmap = ImageUtils.blurBitmap(croppedBitmap, blurRadius);
        }
        pictureView.setImageBitmap(blurBitmap);
        int percent = blurRadius >> 1;
        btnRightPercent.setText(percent + "%");
        progressBar.setProgress(blurRadius);
    }
}
