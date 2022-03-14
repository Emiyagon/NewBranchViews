package com.illyasr.mydempviews.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.text.TextUtils;
import android.widget.Toast;
 
import com.bumptech.glide.Glide;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXImageObject;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXTextObject;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import java.io.ByteArrayOutputStream;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * FileName: WXAPI
 * Author: xiongxiang
 * Date: 2020/12/10
 * Description:
 * History:
 * <author> <time> <version> <desc>
 * 作者姓名 修改时间 版本号 描述
 */
public class WXAPI {
    public static IWXAPI api;
    private static String APP_ID = "你申请的appid";
    private static Context mContext;
    private static final int IMAGE_SIZE = 32768;

    public  WXAPI init(Context context) {
        mContext = context;
        api = WXAPIFactory.createWXAPI(context, APP_ID, false);
        return this;
    }

    @SuppressLint("CheckResult")
    public static void webWx(String link, String title, String describe, String path) {
        toByte(new Consumer<byte[]>() {
            @Override
            public void accept(byte[] bytes) throws Exception {
                webWx(link, title, describe, bytes);
            }
        }, path);

    }

    public static void webWx(String link, String title, String describe, int path) {
        toByte(new Consumer<byte[]>() {
            @Override
            public void accept(byte[] bytes) throws Exception {
                webWx(link, title, describe, bytes);
            }
        }, path);

    }

    public static void webTimeline(String link, String title, String describe, String path) {
        toByte(new Consumer<byte[]>() {
            @Override
            public void accept(byte[] bytes) throws Exception {
                webTimeline(link, title, describe, bytes);
            }
        }, path);
    }

    public static void webTimeline(String link, String title, String describe, int path) {
        toByte(new Consumer<byte[]>() {
            @Override
            public void accept(byte[] bytes) throws Exception {
                webTimeline(link, title, describe, bytes);
            }
        }, path);
    }

    public static void webWx(String link, String title, String describe, byte[] imgs) {
        web(link, title, describe, imgs, SendMessageToWX.Req.WXSceneSession);
    }

    public static void webImage(Bitmap mbitmap) {
        image(mbitmap);
    }

    public static void webText(String mshareurl) {
        text(mshareurl);
    }

    public static void webTimeline(String link, String title, String describe, byte[] imgs) {
        web(link, title, describe, imgs, SendMessageToWX.Req.WXSceneTimeline);
    }

    public static void webImagepyq(Bitmap mbitmap) {
        imagepyq(mbitmap);
    }

    public static void webTextPyq(String mshareurl) {
        textpyq(mshareurl);
    }

    private static void web(String link, String title, String describe, byte[] imgs, int scene) {
        WXWebpageObject webpage = new WXWebpageObject();
        webpage.webpageUrl = link;
        WXMediaMessage msg = new WXMediaMessage(webpage);
        msg.title = title;
        msg.description = describe;
        if (imgs != null && imgs.length != 0) {
            msg.thumbData = imgs;
        }
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = "webpage" + System.currentTimeMillis();
        req.message = msg;
        req.scene = scene;
        boolean sendReq = api.sendReq(req);
        if (!sendReq) {
            Toast.makeText(mContext, "打开微信失败", Toast.LENGTH_SHORT).show();
        }
    }

    private static void image(Bitmap mbitmap) {
        Bitmap bmp = mbitmap;

        //初始化 WXImageObject 和 WXMediaMessage 对象
        WXImageObject imgObj = new WXImageObject(bmp);
        WXMediaMessage msg = new WXMediaMessage();
        msg.mediaObject = imgObj;

        //设置缩略图
        Bitmap thumbBmp = Bitmap.createScaledBitmap(bmp, 20, 20, true);
//        bmp.recycle();
        msg.thumbData = bitmapTobytearray(thumbBmp);
        ;

        //构造一个Req
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = "img" + System.currentTimeMillis();
        req.message = msg;
        req.scene = SendMessageToWX.Req.WXSceneSession;
        req.userOpenId = "openid";
        //调用api接口，发送数据到微信
        boolean sendReq = api.sendReq(req);
        if (!sendReq) {
            Toast.makeText(mContext, "打开微信失败", Toast.LENGTH_SHORT).show();
        }
    }

    public static void shareText(String text) {
        //初始化一个 WXTextObject 对象，填写分享的文本内容
        WXTextObject textObj = new WXTextObject();
        textObj.text = text;

//用 WXTextObject 对象初始化一个 WXMediaMessage 对象
        WXMediaMessage msg = new WXMediaMessage();
        msg.mediaObject = textObj;
        msg.description = text;

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = text;
        req.message = msg;
        req.scene = SendMessageToWX.Req.WXSceneSession;
//调用api接口，发送数据到微信
        boolean sendReq = api.sendReq(req);
        if (!sendReq) {
            Toast.makeText(mContext, "打开微信失败", Toast.LENGTH_SHORT).show();
        }
    }

    private static void text(String mshareurl) {
        //初始化 WXImageObject 和 WXMediaMessage 对象
        WXTextObject textObj = new WXTextObject();
        textObj.text = mshareurl;

        //用 WXTextObject 对象初始化一个 WXMediaMessage 对象
        WXMediaMessage msg = new WXMediaMessage();
        msg.mediaObject = textObj;
        msg.description = "海报";

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = "海报";
        req.message = msg;
        req.scene = SendMessageToWX.Req.WXSceneSession;
        //调用api接口，发送数据到微信
        boolean sendReq = api.sendReq(req);
        if (!sendReq) {
            Toast.makeText(mContext, "打开微信失败", Toast.LENGTH_SHORT).show();
        }
    }

    private static void imagepyq(Bitmap mbitmap) {
        Bitmap bmp = mbitmap;

        //初始化 WXImageObject 和 WXMediaMessage 对象
        WXImageObject imgObj = new WXImageObject(bmp);
        WXMediaMessage msg = new WXMediaMessage();
        msg.mediaObject = imgObj;

        //设置缩略图
        Bitmap thumbBmp = Bitmap.createScaledBitmap(bmp, 20, 20, true);
//        bmp.recycle();
        msg.thumbData = bitmapTobytearray(thumbBmp);
        ;

        //构造一个Req
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = "img" + System.currentTimeMillis();
        req.message = msg;
        req.scene = SendMessageToWX.Req.WXSceneTimeline;
        req.userOpenId = "openid";
        //调用api接口，发送数据到微信
        boolean sendReq = api.sendReq(req);
        if (!sendReq) {
            Toast.makeText(mContext, "打开微信失败", Toast.LENGTH_SHORT).show();
        }
    }

    private static void textpyq(String mshareurl) {
        //初始化 WXImageObject 和 WXMediaMessage 对象
        WXTextObject textObj = new WXTextObject();
        textObj.text = mshareurl;

        //用 WXTextObject 对象初始化一个 WXMediaMessage 对象
        WXMediaMessage msg = new WXMediaMessage();
        msg.mediaObject = textObj;
        msg.description = "海报";

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = "海报";
        req.message = msg;
        req.scene = SendMessageToWX.Req.WXSceneTimeline;
        //调用api接口，发送数据到微信
        boolean sendReq = api.sendReq(req);
        if (!sendReq) {
            Toast.makeText(mContext, "打开微信失败", Toast.LENGTH_SHORT).show();
        }
    }

    @SuppressLint("CheckResult")
    private static void toByte(Consumer<byte[]> consumer, String path) {
        Observable.create(new ObservableOnSubscribe<byte[]>() {
            @Override
            public void subscribe(ObservableEmitter<byte[]> e) throws Exception {
                byte[] bytes = toByte(mContext, path);
                e.onNext(bytes);
                e.onComplete();
            }
        }).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(consumer);
    }

    @SuppressLint("CheckResult")
    private static void toByte(Consumer<byte[]> consumer, int path) {
        Observable.create(new ObservableOnSubscribe<byte[]>() {
            @Override
            public void subscribe(ObservableEmitter<byte[]> e) throws Exception {
                byte[] bytes = toByte(mContext, path);
                e.onNext(bytes);
                e.onComplete();
            }
        }).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(consumer);
    }

    private static byte[] toByte(Context context, String path) {
        if (TextUtils.isEmpty(path)) {
            return new byte[]{};
        }
        if (!path.contains("ceping-oss.wxbig.cn")) {   //测评
//            path = AppConstant.getCdnUrl(path);
            path = "你自己的网址";
        }
        try {
            Bitmap bitmap = Glide.with(context)
                    .asBitmap()
                    .load(path)
                    .into(80, 80)
                    .get();

            int options = 100;
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, options, outputStream);
//            LLog.d("图片大小" + outputStream.toByteArray().length);
            while (outputStream.toByteArray().length > IMAGE_SIZE && options != 1) {
                outputStream.reset();
                bitmap.compress(Bitmap.CompressFormat.JPEG, options, outputStream);
                options -= 1;
//                LLog.d("图片大小" + outputStream.toByteArray().length);
            }
            bitmap.recycle();
            outputStream.close();
            return outputStream.toByteArray();


        } catch (Exception e) {
            e.printStackTrace();
        }
        return new byte[]{};
    }

    /**
     * bitmap中的透明色用白色替换
     *
     * @param bitmap
     * @return
     */
    public static Bitmap changeColor(Bitmap bitmap) {
        if (null == bitmap) {
            return null;
        }
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        int[] colorArray = new int[w * h];
        int n = 0;
        for (int i = 0; i < h; i++) {
            for (int j = 0; j < w; j++) {
                int color = getMixtureWhite(bitmap.getPixel(j, i));
                colorArray[n++] = color;
            }
        }
        return Bitmap.createBitmap(colorArray, w, h, Bitmap.Config.ARGB_8888);
    }

    /**
     * 获取和白色混合颜色
     *
     * @return
     */
    private static int getMixtureWhite(int color) {
        int alpha = Color.alpha(color);
        int red = Color.red(color);
        int green = Color.green(color);
        int blue = Color.blue(color);
        return Color.rgb(getSingleMixtureWhite(red, alpha), getSingleMixtureWhite

                        (green, alpha),
                getSingleMixtureWhite(blue, alpha));
    }

    /**
     * 获取单色的混合值
     *
     * @param color
     * @param alpha
     * @return
     */
    private static int getSingleMixtureWhite(int color, int alpha) {
        int newColor = color * alpha / 255 + 255 - alpha;
        return newColor > 255 ? 255 : newColor;
    }

    private static byte[] toByte(Context context, int path) {

        try {
            Bitmap bitmap = Glide.with(context)
                    .asBitmap()
                    .load(path)
                    .into(50, 50)
                    .get();
            if (null != bitmap) {
                bitmap = changeColor(bitmap);
            }
            int options = 100;
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, options, outputStream);
//            LLog.d("图片大小" + outputStream.toByteArray().length);
            while (outputStream.toByteArray().length > IMAGE_SIZE && options != 1) {
                outputStream.reset();
                bitmap.compress(Bitmap.CompressFormat.JPEG, options, outputStream);
                options -= 1;
//                LLog.d("图片大小" + outputStream.toByteArray().length);
            }
            bitmap.recycle();
            outputStream.close();
            return outputStream.toByteArray();


        } catch (Exception e) {
            e.printStackTrace();
        }
        return new byte[]{};
    }

    private static byte[] bitmapTobytearray(Bitmap mRBmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        mRBmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();
        return data;
    }


}
