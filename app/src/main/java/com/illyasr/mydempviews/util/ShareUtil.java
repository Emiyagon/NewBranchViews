package com.illyasr.mydempviews.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;


import okhttp3.internal.platform.Platform;

import static com.alipay.sdk.app.statistic.c.R;

/**
 *   分享工具类
 */
public class ShareUtil {
/*

    */
/**
     * 一键分享
     * urlType 1:分享使用本地图片   2:分享使用网络图片
     *//*

    public static void OnOneKeyshare(Context context, int urlType, String url,String imgUrl,
                                     String title, String text) {
        OnekeyShare oks = new OnekeyShare();
        //imagepath和imageurl二选一
        if (urlType == 1) {
            oks.setImagePath(imgUrl);
        } else {
            oks.setImageUrl(imgUrl);
        }
        if (imgUrl!=null){
            oks.setUrl(url);
        }
        oks.setTitle(title);//设置标题头
        oks.setText(text);//设置内容
        oks.show(context);
        //关闭sso授权
        oks.disableSSOWhenAuthorize();
    }

    */
/**
     *  带返回值的一键分享
     *//*

    public static void OnOneKeyshare(Context context, int urlType, String url, String imgUrl,
                                     String title, String text, PlatformActionListener listener){
        OnekeyShare oks = new OnekeyShare();
        //imagepath和imageurl二选一
        if (urlType == 1) {
            oks.setImagePath(imgUrl);
        } else {
            oks.setImageUrl(imgUrl);
        }
        if (imgUrl!=null){
            oks.setUrl(url);
        }
        oks.setTitle(title);//设置标题头
        oks.setText(text);//设置内容
        oks.show(context);
        oks.setCallback(listener);

        //关闭sso授权
        oks.disableSSOWhenAuthorize();
    }

    */
/**
     * 分享到指定平台(不带返回值)
     * shareUrl  :  分享的是网页
     * 如果是朋友圈，那么指定分享需要将Wechat.NAME修改为WechatMoments.NAME
     *//*

    public static void ShareToArrorPT(Context context,String shareUrl,String logoUrl,
                                      String text,String title,String shareImgurl,int form,
                                      PlatformActionListener listener) {
        Platform.ShareParams params = new Platform.ShareParams();
        // imagedata,imagepath,和 imageurl三选一
        Bitmap logo = BitmapFactory.decodeResource(context.getResources(), R.mipmap.logo);
        if (logoUrl==null ){
            params.setImageData(logo);
        }else {
            if (logoUrl.startsWith("http://")
                    || logoUrl.startsWith("https://")
                    || logoUrl.startsWith("www.")
                    || logoUrl.startsWith("widevine://")) {
                params.setImageUrl(logoUrl);
            } else {
                params.setImagePath(logoUrl);
            }
        }
        if (!TextUtils.isEmpty(shareUrl)) {
            //分享网页一定要写这个
            params.setShareType(Platform.SHARE_WEBPAGE);
            // 如果是分享图片,那就不要设置url
            params.setUrl(shareUrl);
        } else if (!TextUtils.isEmpty(shareImgurl)){
            */
/**
             *  还有一个分享图片,估计用不到,
             *  params.setShareType(Platform.SHARE_IMAGE);
             *//*

            params.setShareType(Platform.SHARE_IMAGE);
            //  shareImgurl
//            params.set(shareImgurl);
        }

        params.setText(text);
        params.setTitle(title);
        Platform mob = null;
        if (form == 0) {
            mob = ShareSDK.getPlatform(Wechat.NAME);
        } else {
            mob = ShareSDK.getPlatform(WechatMoments.NAME);
        }
        mob.setPlatformActionListener(listener);
        mob.share(params);
    }

    */
/**
     * 分享到qq 好友
     *//*

    public static void ShareToQQ(Context context) {
        OnekeyShare oks = new OnekeyShare();
        oks.setImageUrl("http://firicon.fir.im/baa18a6d779c597888d685f1159070df5b4f2912");
        oks.setTitleUrl("http://www.baidu.com");
        oks.setText("text");
        oks.setTitle("标题");
        oks.setPlatform(QQ.NAME);
//        oks.setPlatform(QZone.NAME);
        oks.show(context);
    }
*/

}
