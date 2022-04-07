package com.illyasr.mydempviews.util;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.illyasr.mydempviews.MyApplication;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutionException;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class BitmapUtil {

    /**
     *  忘了图片转bitmap
     * @param src
     * @return
     */
    public static Bitmap returnBitMap(final String src){
        try {
            Log.d("FileUtil", src);
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            // Log exception
            return null;
        }

    }
    /**
     * bitmap转字节数组
     * @param bitmap
     * @param format
     * @return
     */
    public static byte[] bitmap2Bytes(final Bitmap bitmap, final Bitmap.CompressFormat format) {
        if (bitmap == null) return null;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(format, 100, baos);
        return baos.toByteArray();
    }

    /**
     * 字节数组转成bitmap
     * @param bytes
     * @return
     */
    public static Bitmap bytes2Bitmap(final byte[] bytes) {
        return (bytes == null || bytes.length == 0)
                ? null
                : BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }

    /**
     * drawable转成bitmap
     * @param drawable
     * @return
     */
    public static Bitmap drawable2Bitmap(final Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            if (bitmapDrawable.getBitmap() != null) {
                return bitmapDrawable.getBitmap();
            }
        }
        Bitmap bitmap;
        if (drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0) {
            bitmap = Bitmap.createBitmap(1, 1,
                    drawable.getOpacity() != PixelFormat.OPAQUE
                            ? Bitmap.Config.ARGB_8888
                            : Bitmap.Config.RGB_565);
        } else {
            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
                    drawable.getIntrinsicHeight(),
                    drawable.getOpacity() != PixelFormat.OPAQUE
                            ? Bitmap.Config.ARGB_8888
                            : Bitmap.Config.RGB_565);
        }
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    /**
     *  Uri 转 bitmap
     * @param uri
     * @return
     */
    public static Bitmap ImageSizeCompress(Uri uri){
        InputStream Stream = null;
        InputStream inputStream = null;
        try {
            //根据uri获取图片的流
            inputStream = MyApplication.getInstance().getContentResolver().openInputStream(uri);
            BitmapFactory.Options options = new BitmapFactory.Options();
            //options的in系列的设置了，injustdecodebouond只解析图片的大小，而不加载到内存中去
            options.inJustDecodeBounds = true;
            //1.如果通过options.outHeight获取图片的宽高，就必须通过decodestream解析同options赋值
            //否则options.outheight获取不到宽高
            BitmapFactory.decodeStream(inputStream,null,options);
            //2.通过 btm.getHeight()获取图片的宽高就不需要1的解析，我这里采取第一张方式
//            Bitmap btm = BitmapFactory.decodeStream(inputStream);
            //以屏幕的宽高进行压缩
            DisplayMetrics displayMetrics = MyApplication.getInstance().getResources().getDisplayMetrics();
            int heightPixels = displayMetrics.heightPixels;
            int widthPixels = displayMetrics.widthPixels;
            //获取图片的宽高
            int outHeight = options.outHeight;
            int outWidth = options.outWidth;
            //heightPixels就是要压缩后的图片高度，宽度也一样
            int a = (int) Math.ceil((outHeight/(float)heightPixels));
            int b = (int) Math.ceil(outWidth/(float)widthPixels);
            //比例计算,一般是图片比较大的情况下进行压缩
            int max = Math.max(a, b);
            if(max > 1){
                options.inSampleSize = max;
            }
            //解析到内存中去
            options.inJustDecodeBounds = false;
//            根据uri重新获取流，inputstream在解析中发生改变了
            Stream = MyApplication.getInstance().getContentResolver().openInputStream(uri);
            Bitmap bitmap = BitmapFactory.decodeStream(Stream, null, options);
            return bitmap;
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                if(inputStream != null) {
                    inputStream.close();
                }
                if(Stream != null){
                    Stream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        return  null;
    }


    /**
     * bitmap转成Drawable
     * @param bitmap
     * @return
     */
    public static Drawable bitmap2Drawable(final Bitmap bitmap) {
        return bitmap == null ? null : new BitmapDrawable(MyApplication.getInstance().getResources(), bitmap);
    }

    /**
     * drawable转字节数组
     *
     * @param drawable
     * @param format
     * @return
     */
    public static byte[] drawable2Bytes(final Drawable drawable, final Bitmap.CompressFormat format) {
        return drawable == null ? null : bitmap2Bytes(drawable2Bitmap(drawable), format);
    }

    /**
     * 字节数组转 drawable
     * @param bytes
     * @return
     */
    public static Drawable bytes2Drawable(final byte[] bytes) {
        return bitmap2Drawable(bytes2Bitmap(bytes));
    }

    /**
     *添加文字水印
     * @param src
     * @param content
     * @param textSize
     * @param color
     * @param x
     * @param y
     * @param recycle
     * @return
     */
    public static Bitmap addTextWatermark(final Bitmap src,
                                          final String content, final float textSize, @ColorInt final int color, final float x, final float y, final boolean recycle) {
        if (isEmptyBitmap(src) || content == null) return null;
        Bitmap ret = src.copy(src.getConfig(), true);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        Canvas canvas = new Canvas(ret);
        paint.setColor(color);
        paint.setTextSize(textSize);
        Rect bounds = new Rect();
        paint.getTextBounds(content, 0, content.length(), bounds);
        canvas.drawText(content, x, y + textSize, paint);
        if (recycle && !src.isRecycled()) src.recycle();
        return ret;
    }

    /**
     *添加图片水印
     * @param src
     * @param watermark
     * @param x
     * @param y
     * @param alpha
     * @param recycle
     * @return
     */
    public static Bitmap addImageWatermark(final Bitmap src,
                                           final Bitmap watermark,
                                           final int x,
                                           final int y,
                                           final int alpha,
                                           final boolean recycle) {
        if (isEmptyBitmap(src)) return null;
        Bitmap ret = src.copy(src.getConfig(), true);
        if (!isEmptyBitmap(watermark)) {
            Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
            Canvas canvas = new Canvas(ret);
            paint.setAlpha(alpha);
            canvas.drawBitmap(watermark, x, y, paint);
        }
        if (recycle && !src.isRecycled()) src.recycle();
        return ret;
    }

    /**
     * 透明bitmap
     * @param src
     * @param recycle
     * @return
     */
    public static Bitmap toAlpha(final Bitmap src, final Boolean recycle) {
        if (isEmptyBitmap(src)) return null;
        Bitmap ret = src.extractAlpha();
        if (recycle && !src.isRecycled()) src.recycle();
        return ret;
    }

    /**
     *  灰色bitmap
     * @param src
     * @param recycle
     * @return
     */
    public static Bitmap toGray(final Bitmap src, final boolean recycle) {
        if (isEmptyBitmap(src)) return null;
        Bitmap ret = Bitmap.createBitmap(src.getWidth(), src.getHeight(), src.getConfig());
        Canvas canvas = new Canvas(ret);
        Paint paint = new Paint();
        ColorMatrix colorMatrix = new ColorMatrix();
        colorMatrix.setSaturation(0);
        ColorMatrixColorFilter colorMatrixColorFilter = new ColorMatrixColorFilter(colorMatrix);
        paint.setColorFilter(colorMatrixColorFilter);
        canvas.drawBitmap(src, 0, 0, paint);
        if (recycle && !src.isRecycled()) src.recycle();
        return ret;
    }

    /**
     * 判断是否是图片
     * @param filePath
     * @return
     */

    public static boolean isImage(final String filePath) {
        String path = filePath.toUpperCase();
        return path.endsWith(".PNG") || path.endsWith(".JPG")
                || path.endsWith(".JPEG") || path.endsWith(".BMP")
                || path.endsWith(".GIF") || path.endsWith(".WEBP");
    }


    /**
     * if(bitmap != null && !bitmap.isRecycled()){
     * // 回收并且置为null
     */

    public static boolean isEmptyBitmap(Bitmap bitmap) {

        if (bitmap==null){
            return true;
        } else if (bitmap.isRecycled()) {
            return true;
        } else {
            return false;
        }

    }


    /**
     *  长按Imageview 并保存在本地
     * @param view
     * @param filePath
     *
     * Environment.getExternalStorageDirectory() + "/Yingyun/IMG/"
     */
    public static void saveBitmap(ImageView view, String filePath) {
        Drawable drawable = view.getDrawable();
        if (drawable == null) {
            return;
        }
        FileOutputStream outStream = null;
        File file = new File(Environment.getExternalStorageDirectory() + filePath);//Environment.getExternalStorageDirectory() +
//        File file = new File(filePath);//Environment.getExternalStorageDirectory() +
        if (!file.exists()) {
            file.mkdirs();
        }
     /*   if (file.isDirectory()) {// 如果是目录不允许保存
            return;
        }*/
        try {
            outStream = new FileOutputStream(file);
            Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outStream);
            outStream.flush();
            outStream.close();
            ToastUtils.showToast("图片已保存在" + filePath + "文件夹下");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

/**
 *
 */
public static void saveBitmapL(View view, String filePath){
// 创建对应大小的bitmap
    Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(),
            Bitmap.Config.RGB_565);
    Canvas canvas = new Canvas(bitmap);
    view.draw(canvas);
//存储
    FileOutputStream outStream = null;
    File file=new File(filePath);
   /* if(file.isDirectory()){//如果是目录不允许保存
        return;
    }*/
    if (!file.exists()) {
        file.mkdirs();
    }
    try {
        outStream = new FileOutputStream(file);
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outStream);
        outStream.flush();
    } catch (IOException e) {
        e.printStackTrace();
    }finally {
        try {
            bitmap.recycle();
            if(outStream!=null){
                outStream.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

/*
*   ImageView.buildDrawingCache(true);
        ImageView.buildDrawingCache();
        Bitmap bitmap = ImageView.getDrawingCache();
        saveBitmapFile(bitmap);
        ImageView.setDrawingCacheEnabled(false);
* */

    /**
     * create by lb
     */
    public static void saveBitmapFile(Bitmap bitmap) throws Exception{
        File temp = new File("/sdcard/cqjq/");//要保存文件先创建文件夹
        if (!temp.exists()) {
            temp.mkdir();
        }
        //  用时间戳起名
        File file=new File("/sdcard/cqjq/"+System.currentTimeMillis()+".jpg");//将要保存图片的路径和图片名称
        //    File file =  new File("/sdcard/1delete/1.png");/////延时较长
        try {
            BufferedOutputStream bos= new BufferedOutputStream(new FileOutputStream(file));
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            bos.flush();
            bos.close();
            galleryAddPic(file.getPath());
            ToastUtils.showToastCenter("保存成功,图片在/sdcard/cqjq下");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * @param path
     */
    public static void galleryAddPic(String path) {
        Intent mediaScanIntent = new Intent(
                Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(path);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
//        sendBroadcast(mediaScanIntent);
        MyApplication.getInstance().sendBroadcast(mediaScanIntent);


    }

    public Bitmap getImageViewBiamap(Object bmp) throws ExecutionException, InterruptedException {
        final Bitmap[] bitmap = {};

        Bitmap myBitmap = Glide.with(MyApplication.getInstance())
//                .load(bmp)
                .asBitmap() //必须
                .load(bmp)
//                .centerCrop()
                .into(500, 500)
                .get();

        Glide.with(MyApplication.getInstance())
                .asBitmap()

//                .placeholder(R.drawable.default)
//		        .dontAnimate()
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        bitmap[0] = resource;

                    }
                })



        ;

        return bitmap!=null&&bitmap.length>0? bitmap[0]:null;
    }

    /**
     * 保存图片到系统相册
     */
    public static void saveImageToGallery(Context context, Bitmap bmp, SaveGalleryListener saveGallery) {
        // 首先保存图片
        File appDir = new File(Environment.getExternalStorageDirectory(), "Boohee");
        if (!appDir.exists()) {
            appDir.mkdir();
        }
        String fileName = bmp.toString()+ ".jpg";
        File file = new File(appDir, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        // 其次把文件插入到系统图库
//            MediaStore.Images.Media.insertImage(context.getContentResolver(), file.getAbsolutePath(), fileName, null);//这个会导致生成两张图,原因是因为缩略图
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 其次把文件插入到系统图库
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.DATA, file.getAbsolutePath());
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
        Uri uri = context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

        // 最后通知图库更新
        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + file.getAbsolutePath())));
        saveGallery.saveComplete();
    }


    public interface SaveGalleryListener {
        void saveComplete();
    }


}
