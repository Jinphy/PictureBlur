package com.example.jinphy.pictureblur.utils;

import android.content.Context;
import android.content.Intent;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import com.example.jinphy.pictureblur.base.App;

import java.io.File;
import java.io.IOException;

import static android.support.constraint.Constraints.TAG;
import static android.text.TextUtils.isEmpty;

/**
 * DESC:
 * Created by jinphy on 2018/6/12.
 */

public class FileUtils {

    public static final String TEMP_DIR = Environment.getExternalStorageDirectory()
            .getAbsolutePath() + "/PictureBlur/tmp";
    public static final String PHOTO_DIR = Environment.getExternalStorageDirectory()
            .getAbsolutePath() + "/PictureBlur/photo";


    /**
     * DESC: 第三方应用打开文件
     * Created by jinphy, on 2018/4/2, at 22:17
     */
    public static void shareFile(Context context, String filePath, MimeType mimeType) {
        File file = new File(filePath);
        if (!file.exists()) {
            App.toast("文件不存在！");
            return;
        }

        Intent intent = new Intent(Intent.ACTION_SEND)
                .setType(isEmpty(mimeType.get()) ? MimeType.Any.get() : mimeType.get())
                .putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));

        if (intent.resolveActivity(context.getPackageManager()) == null) {
            App.toast("没有程序可以处理该文件！");
            return;
        }

        context.startActivity(Intent.createChooser(intent, "分享文件"));
    }

    /**
     * DESC: 分享图片
     * Created by jinphy, on 2018/6/12, at 23:13
     */
    public static void shareImage(Context context, String filePath) {
        shareFile(context, filePath, MimeType.Image);
    }

    /**
     * DESC: 创建临时文件
     *
     * @param suffix 文件后缀，例如“.jpg”
     * Created by jinphy, on 2018/6/12, at 0:36
     */
    public static File createTempFile(String suffix) {
        return createFile(TEMP_DIR + "/temp" + suffix);
    }

    /**
     * DESC: 创建图片文件
     *
     * @param suffix 文件后缀，例如“.png"、“.jpg”
     * Created by jinphy, on 2018/6/12, at 0:37
     */
    public static File createPhotoFile(String suffix) {
        String fileName = "IMG_" + System.currentTimeMillis();
        return createFile(PHOTO_DIR + "/" + fileName + suffix);
    }

    public static File createFile(String filePath) {
        File file = new File(filePath);
        File dir = file.getParentFile();
        if (!dir.exists()) {
            dir.mkdirs();
        }
        try {
            file.createNewFile();
            return file;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * DESC: 通知系统更新文件，例如更新图库
     * Created by jinphy, on 2018/6/12, at 13:23
     */
    public static void notifySystem(File file) {
        Uri uri = Uri.fromFile(file);
        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri);
//        intent.setData(uri);
        App.broadcast(intent);
    }

    /**
     * DESC: 通知系统更新文件，例如更新图库
     * Created by jinphy, on 2018/6/12, at 13:23
     */
    public static void notifySystem(String filePath) {
        notifySystem(new File(filePath));
    }


    public enum MimeType{

        Any("*/*"),

        Image("image/*"),

        Image_JPG("image/jpeg");

        String value;

        MimeType(String value) {
            this.value = value;
        }

        public String get() {
            return this.value;
        }


    }
}
