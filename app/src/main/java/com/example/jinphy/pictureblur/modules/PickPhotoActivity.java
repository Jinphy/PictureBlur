package com.example.jinphy.pictureblur.modules;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.jinphy.pictureblur.R;
import com.example.jinphy.pictureblur.base.App;
import com.example.jinphy.pictureblur.utils.ImageUtils;
import com.example.jinphy.pictureblur.utils.StringUtils;
import com.theartofdev.edmodo.cropper.CropImageView;

public class PickPhotoActivity extends AppCompatActivity  implements View.OnClickListener{

    public static final String KEY_OPTION = "KEY_OPTION";

    public static final String AUTHORITIES = "com.example.jinphy.pictureblur.fileprovider";
    public static final String DATA_PICTURE = "DATA_PICTURE";
    private final String IMG_FILE_NAME_PREFIX = "tmp";

    private final String IMG_FILE_NAME_SUFFIX = ".png";


    public static final int TAKE_PHOTO = 1;
    public static final int CHOOSE_PHOTO = 2;
    public static final int PICK_PHOTO = 3;


    private int option;
    private String filePath;

    private Bitmap bitmap;
    private int rotatedDegree = 0;


    public static void startForResult(Activity activity, Option option) {
        if (activity==null || option == null) {
            App.toast("启动异常！");
            return;
        }
        Intent intent = new Intent(activity, PickPhotoActivity.class);
        intent.putExtra(KEY_OPTION, option.get());
        activity.startActivityForResult(intent, PICK_PHOTO);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(KEY_OPTION, option);
    }

    private TextView btnRecapture;
    private TextView btnFinish;
    private FloatingActionButton btnRotate;
    private TextView degreeView;
    private CropImageView pictureView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App.setFullScreen(this);
        App.setVertical(this);
        setContentView(R.layout.activity_pick_photo);
        btnRecapture = findViewById(R.id.btn_recapture);
        btnFinish = findViewById(R.id.btn_finish);
        btnRotate = findViewById(R.id.btn_rotate);
        degreeView = findViewById(R.id.rotate_degree_view);
        pictureView = findViewById(R.id.crop_image_view);
        btnRecapture.setOnClickListener(this);
        btnFinish.setOnClickListener(this);
        btnRotate.setOnClickListener(this);

        if (savedInstanceState == null) {
            option = getIntent().getIntExtra(KEY_OPTION, 0);
        } else {
            option = savedInstanceState.getInt(KEY_OPTION);
        }
        initView();

        getPicture();
    }

    private void initView() {
        if (option == Option.TAKE_PHOTO.get()) {
            btnRecapture.setText("重拍");
        } else {
            btnRecapture.setText("重选");
        }
        degreeView.setText(StringUtils.formatDegree(rotatedDegree));
    }

    private static final String TAG = "PickPhotoActivity";
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_recapture:
                getPicture();
                break;
            case R.id.btn_rotate:
                rotatedDegree = (rotatedDegree + 90) % 360;
                degreeView.setText(StringUtils.formatDegree(rotatedDegree));
                pictureView.setRotatedDegrees(rotatedDegree);
                break;
            case R.id.btn_finish:
                Bitmap croppedImage = pictureView.getCroppedImage();
                if (croppedImage != null) {
                    App.put(DATA_PICTURE, croppedImage);
                    setResult(RESULT_OK);
                } else {
                    setResult(RESULT_CANCELED);
                }
                finish();
                break;
            default:
                break;
        }
    }

    /**
     * DESC: 捕获图片
     * Created by jinphy, on 2018/6/11, at 18:33
     */
    private void getPicture() {
        if (option == Option.TAKE_PHOTO.get()) {
            // 相机
            filePath = ImageUtils.takePhotoFullSize(
                    this,
                    AUTHORITIES,
                    IMG_FILE_NAME_PREFIX,
                    IMG_FILE_NAME_SUFFIX,
                    TAKE_PHOTO);
        } else {
            // 相册
            ImageUtils.choosePhoto(this, CHOOSE_PHOTO);
        }
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            if (bitmap == null) {
                finish();
            }
            return;
        }
        if (requestCode == TAKE_PHOTO) {
            this.bitmap = ImageUtils.getBitmap(
                    filePath,
                    500,
                    500);
        } else if (requestCode == CHOOSE_PHOTO) {
            Uri uri = data.getData();
            if (uri == null) {
                this.bitmap = data.getParcelableExtra("data");
            } else {
                this.bitmap = ImageUtils.getBitmap(this, uri, 500, 500);
            }
        }
        pictureView.setImageBitmap(this.bitmap);
    }














    public enum Option{

        /**
         * DESC: 拍照获取图片
         * Created by jinphy, on 2018/6/11, at 18:00
         */
        TAKE_PHOTO(0),

        /**
         * DESC: 从相册中选取图片
         * Created by jinphy, on 2018/6/11, at 18:01
         */
        CHOOSE_PHOTO(1);

        private int value;

        Option(int value){
            this.value = value;
        }

        public int get() {
            return value;
        }
    }
}
