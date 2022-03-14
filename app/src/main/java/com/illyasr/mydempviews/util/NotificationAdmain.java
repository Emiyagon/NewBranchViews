package com.illyasr.mydempviews.util;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.SystemClock;
import android.widget.RemoteViews;

import com.illyasr.mydempviews.service.DeleteService;

import androidx.core.app.NotificationCompat;

public class NotificationAdmain {

    private static int NOTIFICATION_ID;
    private NotificationManager nm;
    private Notification notification;
    private NotificationCompat.Builder cBuilder;
    private Notification.Builder nBuilder;
    private Context mContext;
    int requestCode = (int) SystemClock.uptimeMillis();
    private static final int FLAG = Notification.FLAG_INSISTENT;

    public NotificationAdmain(Context context, int ID) {
        this.NOTIFICATION_ID = ID;
        mContext = context;
        // 获取系统服务来初始化对象
        nm = (NotificationManager) mContext
                .getSystemService(Activity.NOTIFICATION_SERVICE);
        cBuilder = new NotificationCompat.Builder(mContext);
    }

    /**
     * 设置在顶部通知栏中的各种信息
     *
     * @param intent
     * @param smallIcon
     * @param ticker
     */
    private void setCompatBuilder(Intent intent, int smallIcon, String ticker,
                                  String title, String msg) {
        // 如果当前Activity启动在前台，则不开启新的Activity。
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        // 当设置下面PendingIntent.FLAG_UPDATE_CURRENT这个参数的时候，常常使得点击通知栏没效果，你需要给notification设置一个独一无二的requestCode
        // 将Intent封装进PendingIntent中，点击通知的消息后，就会启动对应的程序
        @SuppressLint("WrongConstant") PendingIntent pIntent = PendingIntent.getActivity(mContext,
                requestCode, intent, FLAG);

        cBuilder.setContentIntent(pIntent);// 该通知要启动的Intent

        cBuilder.setSmallIcon(smallIcon);// 设置顶部状态栏的小图标
        cBuilder.setTicker(ticker);// 在顶部状态栏中的提示信息

        cBuilder.setContentTitle(title);// 设置通知中心的标题
        cBuilder.setContentText(msg);// 设置通知中心中的内容
        cBuilder.setWhen(System.currentTimeMillis());

        /*
         * 将AutoCancel设为true后，当你点击通知栏的notification后，它会自动被取消消失,
         * 不设置的话点击消息后也不清除，但可以滑动删除
         */
        cBuilder.setAutoCancel(true);
        // 将Ongoing设为true 那么notification将不能滑动删除
        // notifyBuilder.setOngoing(true);
        /*
         * 从Android4.1开始，可以通过以下方法，设置notification的优先级，
         * 优先级越高的，通知排的越靠前，优先级低的，不会在手机最顶部的状态栏显示图标
         */
        cBuilder.setPriority(NotificationCompat.PRIORITY_MAX);
        /*
         * Notification.DEFAULT_ALL：铃声、闪光、震动均系统默认。
         * Notification.DEFAULT_SOUND：系统默认铃声。
         * Notification.DEFAULT_VIBRATE：系统默认震动。
         * Notification.DEFAULT_LIGHTS：系统默认闪光。
         * notifyBuilder.setDefaults(Notification.DEFAULT_ALL);
         */
        cBuilder.setDefaults(Notification.DEFAULT_ALL);
    }

    /**
     * 设置builder的信息，在用大文本时会用到这个
     *
     * @param intent
     * @param smallIcon
     * @param ticker
     */
    @SuppressLint("WrongConstant")
    private void setBuilder(Intent intent, int smallIcon, String ticker) {
        nBuilder = new Notification.Builder(mContext);
        // 如果当前Activity启动在前台，则不开启新的Activity。
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        @SuppressLint("WrongConstant") PendingIntent pIntent = PendingIntent.getActivity(mContext,
                requestCode, intent, FLAG);
        nBuilder.setContentIntent(pIntent);
        nBuilder.setSmallIcon(smallIcon);
        nBuilder.setTicker(ticker);
        nBuilder.setWhen(System.currentTimeMillis());
        nBuilder.setPriority(NotificationCompat.PRIORITY_MAX);
        nBuilder.setDefaults(Notification.DEFAULT_ALL);
    }

    /**
     * 普通的通知
     *
     * @param intent
     * @param smallIcon
     * @param ticker
     * @param title
     * @param msg
     */
    public void normal_notification(Intent intent, int smallIcon,
                                    String ticker, String title, String msg) {

        setCompatBuilder(intent, smallIcon, ticker, title, msg);
        sent();
    }

    /**
     * 进行多项设置的通知(在小米上似乎不能设置大图标，系统默认大图标为应用图标)
     *
     * @param intent
     * @param smallIcon
     * @param ticker
     * @param LargeIcon
     * @param title
     * @param msg
     */
    public void special_notification(Intent intent, int smallIcon,
                                     String ticker, int LargeIcon, String title, String msg) {

        setCompatBuilder(intent, smallIcon, ticker, title, msg);

        // 如果不设置LargeIcon，那么系统会默认将上面的SmallIcon作为主要图标，显示在通知选项的最左侧，右下角的小图标将不再显示
        Bitmap bitmap = BitmapFactory.decodeResource(mContext.getResources(),
                LargeIcon);
        cBuilder.setLargeIcon(bitmap);

        // 将Ongoing设为true 那么notification将不能滑动删除
        cBuilder.setOngoing(true);
        // 删除时
        Intent deleteIntent = new Intent(mContext, DeleteService.class);
        int deleteCode = (int) SystemClock.uptimeMillis();
        // 删除时开启一个服务
        PendingIntent deletePendingIntent = PendingIntent.getService(mContext,
                deleteCode, deleteIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        cBuilder.setDeleteIntent(deletePendingIntent);

        cBuilder.setDefaults(Notification.DEFAULT_SOUND | // 设置使用默认的声音
                Notification.DEFAULT_LIGHTS);// 设置使用默认的LED
        cBuilder.setVibrate(new long[] { 0, 100, 200, 300 });// 设置自定义的振动
        cBuilder.setAutoCancel(true);
        // builder.setSound(Uri.parse("file:///sdcard/click.mp3"));

        // 设置通知样式为收件箱样式,在通知中心中两指往外拉动，就能出线更多内容，但是很少见
        cBuilder.setNumber(3);
        cBuilder.setStyle(new NotificationCompat.InboxStyle()
                .addLine("M.Lynn 你好，我是kale").addLine("M.Lynn 已收到，保证完成任务")
                .addLine("M.Lynn 哈哈，明白了~").setSummaryText("+3 more")); // 设置在细节区域底端添加一行文本
        sent();
    }

    /**
     * 自定义视图的通知
     *
     * @param remoteViews
     * @param intent
     * @param smallIcon
     * @param ticker
     */
    public void view_notification(RemoteViews remoteViews, Intent intent,
                                  int smallIcon, String ticker) {

        setCompatBuilder(intent, smallIcon, ticker, null, null);

        notification = cBuilder.build();
        notification.contentView = remoteViews;
        // 发送该通知
        nm.notify(NOTIFICATION_ID, notification);
    }

    /**
     * 可以容纳多行提示文本的通知信息 (因为在高版本的系统中才支持，所以要进行判断)
     *
     * @param intent
     * @param smallIcon
     * @param ticker
     * @param title
     * @param msg
     */
    public void big_notification(Intent intent, int smallIcon, String ticker,
                                 String title, String msg) {

        final int sdk = android.os.Build.VERSION.SDK_INT;
        if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
            normal_notification(intent, smallIcon, ticker, title, msg);
        } else {
            setBuilder(intent, smallIcon, ticker);
            nBuilder.setContentTitle(title);
            nBuilder.setPriority(Notification.PRIORITY_HIGH);
            notification = new Notification.BigTextStyle(nBuilder).bigText(msg)
                    .build();
            // 发送该通知
            nm.notify(NOTIFICATION_ID, notification);
        }
    }

    /**
     * 有进度条的通知，可以设置为模糊进度或者精确进度
     *
     * @param intent
     * @param smallIcon
     * @param ticker
     * @param title
     * @param msg
     */
    public void progress_notification(Intent intent, int smallIcon,
                                      String ticker, String title, String msg) {

        setCompatBuilder(intent, smallIcon, ticker, title, msg);
        /*
         * 因为进度条要实时更新通知栏也就说要不断的发送新的提示，所以这里不建议开启通知声音。
         * 这里是作为范例，给大家讲解下原理。所以发送通知后会听到多次的通知声音。
         */

        new Thread(new Runnable() {
            @Override
            public void run() {
                int incr;
                for (incr = 0; incr <= 100; incr += 10) {
                    // 参数：1.最大进度， 2.当前进度， 3.是否有准确的进度显示
                    cBuilder.setProgress(100, incr, false);
                    // cBuilder.setProgress(0, 0, true);
                    sent();
                    try {
                        Thread.sleep(1 * 500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                // 进度满了后，设置提示信息
                cBuilder.setContentText("下载完成~").setProgress(0, 0, false);
                sent();
            }
        }).start();
    }

    /**
     * 容纳大图片的通知
     *
     * @param intent
     * @param smallIcon
     * @param ticker
     * @param title
     * @param bigPic
     */
    public void pic_notification(Intent intent, int smallIcon, String ticker,
                                 String title, int bigPic) {

        setCompatBuilder(intent, smallIcon, ticker, title, null);
        NotificationCompat.BigPictureStyle picStyle = new NotificationCompat.BigPictureStyle();
        Bitmap bitmap = BitmapFactory.decodeResource(mContext.getResources(),
                bigPic);
        picStyle.bigPicture(bitmap);
        cBuilder.setStyle(picStyle);
        sent();
    }

    /**
     * 里面有两个按钮的通知
     *
     * @param intent
     * @param smallIcon
     * @param ticker
     * @param title
     * @param msg
     */
 /*   public void btn_notification(Intent intent, int smallIcon, String ticker,
                                 String title, String msg) {

        Intent notifyIntent = new Intent(mContext, OtherActivity.class);
        int requestCode = (int) SystemClock.uptimeMillis();
        PendingIntent pendIntent = PendingIntent.getActivity(mContext,
                requestCode, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        setCompatBuilder(intent, smallIcon, ticker, title, msg);
        cBuilder.addAction(android.R.drawable.ic_media_previous,
                mContext.getString(R.string.previous), pendIntent);
        cBuilder.addAction(android.R.drawable.ic_media_next,
                mContext.getString(R.string.next), pendIntent);
        sent();
    }*/

    /**
     * 发送通知
     */
    private void sent() {
        notification = cBuilder.build();
        // 发送该通知
        nm.notify(NOTIFICATION_ID, notification);
    }

    /**
     * 根据id清除通知
     */
    public void clear() {
        // 取消通知
        nm.cancelAll();

    }
}
