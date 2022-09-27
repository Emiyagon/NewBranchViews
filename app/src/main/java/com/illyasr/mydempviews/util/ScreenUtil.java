package com.illyasr.mydempviews.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.view.View;
import android.view.WindowManager;

import com.illyasr.mydempviews.R;

/**
 *  屏幕数据工具类
 */
public class ScreenUtil {

    Context context;


    /**
     *  获取屏幕高度
     * @param context
     * @return
     */
    public static int getScreenHeight(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Point point=new Point();
        wm.getDefaultDisplay().getSize(point);
        int width = point.x;
        int height = point.y;

        return height;
    }

    /**
     *  获取屏幕宽度
     * @param context
     * @return
     */
    public static int getScreenWidth(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Point point=new Point();
        wm.getDefaultDisplay().getSize(point);
        int width = point.x;
        int height = point.y;
        return width;
    }


    /**
     *   获取状态栏高度
     * @param context
     * @return
     */
    public static int getStatueBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    /**
     *  获取当前截屏
     * @param activity
     * @return
     */
    public static Bitmap capture(Activity activity) {
        activity.getWindow().getDecorView().setDrawingCacheEnabled(true);
        Bitmap bmp = activity.getWindow().getDecorView().getDrawingCache();
        return bmp;
    }


    /**
     * 获取指定Activity的截屏，保存到png文件
     */
    public static Bitmap takeScreenShot(Activity activity) {

        // View是你需要截图的View
        View view = activity.getWindow().getDecorView();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap b1 = view.getDrawingCache();

        // 获取状态栏高度
//        Rect frame = new Rect();
//        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
//        int statusBarHeight = frame.top;
        int statusBarHeight = getStatusHeight(activity);

        // 获取屏幕长和高
        int width = activity.getWindowManager().getDefaultDisplay().getWidth();
        int height = activity.getWindowManager().getDefaultDisplay()
                .getHeight();

        //二维码图片
        Bitmap erweima = BitmapFactory.decodeResource(activity.getResources(), R.mipmap.ic_launcher);
        erweima = Bitmap.createScaledBitmap(erweima, width, width / 5, true);

        // 去掉标题栏
        // Bitmap b = Bitmap.createBitmap(b1, 0, 25, 320, 455);
        Bitmap b = Bitmap.createBitmap(b1, 0, statusBarHeight, width, height
                - statusBarHeight);
        view.destroyDrawingCache();

        Bitmap bitmap = Bitmap.createBitmap(width, height - statusBarHeight + width / 5, Bitmap.Config.ARGB_8888);
        final Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();
        Paint bgPaint = new Paint();
        bgPaint.setColor(Color.parseColor("#1e4873"));
        Rect rect = new Rect(0, 0, width, height - statusBarHeight + width / 5);
        canvas.drawRect(rect, bgPaint);

        int h = 0;
        canvas.drawBitmap(b, 0, h, paint);
        h += height - statusBarHeight;
        canvas.drawBitmap(erweima, 0, h, paint);


          BitmapUtil.saveImageToGallery(activity, bitmap, () -> {
                // 保存到相册
          });


        return bitmap;
    }


    /**
     * 获取状?栏高?
     *
     * @param context
     * @return
     */
    public static int getStatusHeight(Context context) {
        int statusHeight = 0;
        Class<?> localClass;
        try {
            localClass = Class.forName("com.android.internal.R$dimen");
            Object localObject = localClass.newInstance();
            int i5 = Integer.parseInt(localClass.getField("status_bar_height").get(localObject)
                    .toString());
            statusHeight = context.getResources().getDimensionPixelSize(i5);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (NumberFormatException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }

        return statusHeight;
    }
}
