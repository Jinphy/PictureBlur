package com.example.jinphy.pictureblur.utils;

import android.content.Context;
import android.content.Intent;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Environment;

import com.example.jinphy.pictureblur.base.App;

import java.io.File;
import java.io.IOException;

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
    public static void shareFile(Context context, String filePath) {
        File file = new File(filePath);
        if (file.exists()) {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
            intent.setType(getMimeType(file.getAbsolutePath()));//此处可发送多种文件
            if (intent.resolveActivity(context.getPackageManager()) != null) {
                context.startActivity(Intent.createChooser(intent, "分享文件"));
            } else {
                App.toast("没有应用程序可以打开该文件！");
            }
        } else {
            App.toast("文件不存在！");
        }
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



    public static String getMimeType(String filePath) {
        MediaMetadataRetriever mmr = new MediaMetadataRetriever();
        String mime = "*/*";
        if (filePath != null) {
            try {
                mmr.setDataSource(filePath);
                mime = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_MIMETYPE);
            } catch (IllegalStateException e) {
                return mime;
            } catch (IllegalArgumentException e) {
                return mime;
            } catch (RuntimeException e) {
                return mime;
            }

        }
        return mime;
    }
}
