package com.example.jinphy.pictureblur.utils;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.support.v4.content.FileProvider;
import android.util.Log;

import com.example.jinphy.pictureblur.base.App;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import static android.support.constraint.Constraints.TAG;

/**
 * DESC:
 * Created by jinphy on 2018/6/11.
 */

public class ImageUtils {

    /**
     * DESC: 从相机中拍照获取图片，并且需要获取原图的方式
     *
     *      注意：这种方式可以获取原图，但是比较复杂，你需要进行一些配置
     *      步骤如下：
     *          1、在 AndroidManifest.xml 文件中加入如下代码<p>{@code
     *              </application>
     *                  <provider
     *                      android:name="android.support.v4.content.FileProvider"
     *                      android:authorities="{替换成自己的包名}.fileprovider"
     *                      android:exported="false"
     *                      android:grantUriPermissions="true">
     *                  <meta-data
     *                      android:name="android.support.FILE_PROVIDER_PATHS"
     *                      android:resource="@xml/file_paths">
     *                  </meta-data>
     *                  </provider>
     *              </application>
     *          }</p>
     *          2、在xml目录下新建一个file_paths.xml 文件（文件名可以自定义，但是必须与上面{@code android:resource="@xml/file_paths"}）
     *              相同，内容如下：<p>{@code
     *              <paths xmlns:android="http://schemas.android.com/apk/res/android">
     *                  <external-path name="my_files" path="Android/data/{替换成自己的包名}/files/Pictures" />
     *              </paths>
     *          }</p>
     *
     *          3、然后执行该函数
     *
     *
     *
     * @param activity      Activity 对象
     * @param authority     authority值，必须与上面说明的provider标签下的 authorities的值一致
     * @param filePrefix    文件名的前缀, 例如 image
     * @param fileSuffix    文件名的后缀  例如 .png
     * @param requestCode   请求码
     * @return              返回目标文件的绝对路径
     * Created by Jinphy, on 2017/12/27, at 17:44
     */

    public static String takePhotoFullSize(
            Activity activity,
            String authority,
            String filePrefix,
            String fileSuffix,
            int requestCode) {
        try {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            // 确保有拍照的应用程序可以接受该intent
            if (intent.resolveActivity(activity.getPackageManager()) != null) {
                // 获取存储目录
                File dir = activity.getExternalFilesDir(Environment.DIRECTORY_PICTURES);

                // 创建保存图片的文件
                File image = File.createTempFile(filePrefix, fileSuffix, dir);

                // 如果文件为null则抛出异常
                if (image == null) {
                    throw new Exception();
                }

                // 获取文件的url
                Uri uri = FileProvider.getUriForFile(activity, authority, image);

                intent.putExtra(MediaStore.Images.Media.ORIENTATION, 0);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                activity.startActivityForResult(intent, requestCode);
                return image.getAbsolutePath();
            } else {
                App.toast("您的相机没有相机程序！");
                return null;
            }
        } catch (Exception e) {
            App.toast("没有找到储存目录");
            return null;
        }
    }

    /**
     * DESC: 从相册中选择图片
     * Created by Jinphy, on 2017/12/27, at 11:29
     */
    public static void choosePhoto(Activity activity,int requestCode) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        if (intent.resolveActivity(activity.getPackageManager()) != null) {
            activity.startActivityForResult(intent, requestCode);
        } else {
            App.toast("您的设备没有图库程序！");
        }
    }

    /**
     * DESC: 从文件路劲中获取bitmap
     *
     * @param filePath bitmap所在的文件绝对路径路径
     * @param reqWidth 指定输出bitmap的宽
     * @param reqHeight 指定输出bitmap的高
     * Created by jinphy, on 2018/1/10, at 17:26
     */
    public static Bitmap getBitmap(String filePath, int reqWidth, int reqHeight) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);

        if (reqWidth == 0 || reqHeight == 0) {
            options.inSampleSize = 4;
        } else {
            options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        }

        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(filePath, options);
    }

    /**
     * DESC: 从uri中获取bitmap
     *
     *
     * @param uri bitmap 所在的uri路径
     * @param reqWidth 指定输出bitmap的宽
     * @param reqHeight 指定输出bitmap的高
     * Created by jinphy, on 2018/1/10, at 17:23
     */
    public static Bitmap getBitmap(Activity activity, Uri uri, int reqWidth, int reqHeight) {
        if (activity == null) {
            return null;
        }
        try {
            ContentResolver resolver = activity.getContentResolver();
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(resolver.openInputStream(uri), null, options);

            if (reqWidth == 0 || reqHeight == 0) {
                options.inSampleSize = 4;
            } else {
                options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
            }

            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            options.inJustDecodeBounds = false;
            return BitmapFactory.decodeStream(resolver.openInputStream(uri), null, options);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * DESC: 高斯模糊bitmap
     *
     * @param source 源Bitmap，作用结果会影响该参数
     * @param radius 模糊半径，取值：0<radius<=25
     * Created by jinphy, on 2018/4/3, at 16:51
     */
    private static Bitmap blur(Bitmap source, int radius){
        if (source == null) {
            return null;
        }
        //(1)
        RenderScript renderScript =  RenderScript.create(App.app());

        // Allocate memory for Renderscript to work with
        //(2)
        final Allocation input = Allocation.createFromBitmap(renderScript, source);
        final Allocation output = Allocation.createTyped(renderScript,input.getType());
        //(3)
        // Load up an instance of the specific script that we want to use.
        ScriptIntrinsicBlur scriptIntrinsicBlur = ScriptIntrinsicBlur.create(renderScript, Element.U8_4(renderScript));
        //(4)
        scriptIntrinsicBlur.setInput(input);
        //(5)
        // Set the blur radius
        scriptIntrinsicBlur.setRadius(radius);
        //(6)
        // Start the ScriptIntrinisicBlur
        scriptIntrinsicBlur.forEach(output);
        //(7)
        // Copy the output to the blurred bitmap
        output.copyTo(source);
        //(8)
        renderScript.destroy();

        return source;
    }

    /**
     * DESC: 高斯模糊，外部调用
     * @param source 原始Bitmap，作用结果不会影响该参数
     * @param radius 模糊度 范围radius>0
     * @param useSource 标志模糊结果是否会影响原始Bitmap，useSource=true时；则影响，反之不影响
     * Created by jinphy, on 2018/6/11, at 23:32
     */
    public static Bitmap blurBitmap(Bitmap source, int radius,boolean useSource) {
        if (source == null) {
            return null;
        }
        if (radius <= 0) {
            return source;
        }
        if (radius < 26) {
            return blur(source, radius);
        }
        return blurBitmap(blur(useSource ? source : copyBitmap(source), 25), radius - 25, useSource);
    }

    /**
     * DESC: 高斯模糊，外部调用
     *
     * 该函数默认不影响source本身
     * Created by jinphy, on 2018/6/11, at 23:47
     */
    public static Bitmap blurBitmap(Bitmap source, int radius) {
        return blurBitmap(copyBitmap(source), radius, true);
    }

    /**
     * DESC: 复制bitmap
     * Created by jinphy, on 2018/4/3, at 18:08
     */
    public static Bitmap copyBitmap(Bitmap source) {
        if (source == null) {
            return null;
        }
        Bitmap out = Bitmap.createBitmap(
                source.getWidth(), source.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(out);

        Rect rect = new Rect(0, 0, source.getWidth(), source.getHeight());
        canvas.drawBitmap(source, rect, rect, null);
        return out;
    }

    /**
     * DESC: 把bitmap保存到文件中，并通知系统图库更新图片
     *
     * @return 返回文件路径
     * Created by jinphy, on 2018/6/12, at 0:16
     */
    public static String saveToFile(Bitmap source, File file) {
        if (source == null || file == null) {
            return null;
        }
        try {
            // 1、保存文件
            FileOutputStream out = new FileOutputStream(file);
            source.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();

            // 2、更新图库
            FileUtils.notifySystem(file);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return file.getAbsolutePath();
    }

    /**
     * DESC: 将Bitmap 添加到手机图库，添加到图库中的是source的缩略图
     *
     * @return 返回新生成的图片的url
     * Created by jinphy, on 2018/6/12, at 1:12
     */
    public static String addToAlbum(Bitmap source) {
        // 将图片添加到图库,这一步会向系统的图库中插入一张大小减半的缩略图，所以不需要这一步
        // 通过该方法保存，会自动更新到图库中
        String url = MediaStore.Images.Media.insertImage(
                App.app().getContentResolver(), source, null, null);
        return url;
    }


    private static int calculateInSampleSize(
            BitmapFactory.Options options,
            int reqWidth, int reqHeight) {
        int rawWidth = options.outWidth;
        int rawHeight = options.outHeight;
        return calculateInSampleSize(reqWidth, reqHeight, rawWidth, rawHeight);
    }


    private static int calculateInSampleSize(
            int reqWidth,
            int reqHeight,
            int rawWidth,
            int rawHeight) {
        int inSampleSize = 1;
        if (rawWidth > reqWidth || rawHeight > reqHeight) {
            final int halfWidth = rawWidth/2;
            final int halfHeight = rawHeight/2;
            while ( (halfWidth / inSampleSize > reqWidth)
                    && (halfHeight / inSampleSize > reqHeight)) {
                inSampleSize *=2;
            }
        }
        return inSampleSize;
    }

}
