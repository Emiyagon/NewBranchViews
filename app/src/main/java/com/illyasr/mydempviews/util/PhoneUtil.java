package com.illyasr.mydempviews.util;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.os.Build;
import android.telephony.TelephonyManager;

import java.util.HashMap;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import androidx.core.app.ActivityCompat;
import pub.devrel.easypermissions.EasyPermissions;

public class PhoneUtil {


    /**
     *   验证是否是手机号
     */
    public static boolean isPhone(String phone) {
        String regex = "^((13[0-9])|(14[5,7,9])|(15([0-3]|[5-9]))|(166)|(17[0,1,3,5,6,7,8])|(18[0-9])|(19[8|9]))\\d{8}$";
        if (phone.length() != 11) {
//            MToast.showToast("手机号应为11位数");
            ToastUtils.showToastCenter("请输入11位的手机号");
            return false;
        } else {
            Pattern p = Pattern.compile(regex);
            Matcher m = p.matcher(phone);
            boolean isMatch = m.matches();
            LogUtil.e(isMatch+"");
            if (!isMatch) {
                ToastUtils.showToastCenter("请填入正确的手机号");
            }
            return isMatch;
        }
    }


    /**
     * 获取手机IMEI号
     * 需要动态权限: android.permission.READ_PHONE_STATE
     */
    public static String getIMEI(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(context.TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return "0000";
        }
        @SuppressLint("HardwareIds") String imei = telephonyManager.getDeviceId();

        return imei;
    }
    public static boolean checkInitPermission(Activity activity) {
        // SDK 小于23默认已经授权
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }

        String[] perms = {Manifest.permission.READ_PHONE_STATE, Manifest.permission.WRITE_EXTERNAL_STORAGE};

        // 手机状态和写SDCARD的权限是必须的
        if (EasyPermissions.hasPermissions(activity, perms)) {
            return true;
        } else {
            //    EasyPermissions.requestPermissions(activity, "需要存储数据到设备！", PERMISSION_REQUEST_CODE_INIT, perms);
            return false;
        }
    }

    //获取当前的时间戳
    public static int getTime() {
        return Integer.parseInt(System.currentTimeMillis() / 1000 + "");
    }
    /**
     * 服务器返回url，通过url去获取视频的第一帧
     * Android 原生给我们提供了一个MediaMetadataRetriever类
     * 提供了获取url视频第一帧的方法,返回Bitmap对象
     *
     * @param videoPath
     * @return
     */
    public static Bitmap getNetVideoBitmap(String type, String videoPath) {
        Bitmap bitmap = null;
        if (type.equals("Local")) {
            MediaMetadataRetriever media = new MediaMetadataRetriever();
            media.setDataSource(videoPath);
// 视频封面
            bitmap = media.getFrameAtTime();
            return bitmap;
        } else if (type.equals("Net")) {
            MediaMetadataRetriever retriever = new MediaMetadataRetriever();
            try {
                //根据url获取缩略图
                retriever.setDataSource(videoPath, new HashMap());
                //获得第一帧图片
                bitmap = retriever.getFrameAtTime();
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } finally {
                retriever.release();
            }
            return bitmap;
        }
        return bitmap;
    }

    /**
     * 获取当前手机系统语言。
     *
     * @return 返回当前系统语言。例如：当前设置的是“中文-中国”，则返回“zh-CN”
     */
    public static String getSystemLanguage() {
        return Locale.getDefault().getLanguage();
    }

    /**
     * 获取当前系统上的语言列表(Locale列表)
     *
     * @return  语言列表
     */
    public static Locale[] getSystemLanguageList() {
        return Locale.getAvailableLocales();
    }

    /**
     * 获取当前手机系统版本号
     *
     * @return  系统版本号
     */
    public static String getSystemVersion() {
        return Build.VERSION.RELEASE;
    }
    /**
     * 获取手机型号
     *
     * @return  手机型号
     */
    public static String getSystemModel() {
        return Build.MODEL;
    }


    /**
     * 获取手机厂商
     *
     * @return  手机厂商
     */
    public static String getDeviceBrand() {
        return Build.BRAND;
    }

    /**
     *  获取UA
     * @return
     */
//    public String getUserAgent(){
//        String user_agent = ProductProperties.get(ProductProperties.USER_AGENT_KEY, null);
//        return user_agent;
//    }

}
