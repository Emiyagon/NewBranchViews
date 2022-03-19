package com.illyasr.mydempviews.util.xpopup;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.illyasr.mydempviews.R;
import com.illyasr.mydempviews.util.GlideUtil;
import com.lxj.xpopup.interfaces.XPopupImageLoader;
import com.zhouyou.http.EasyHttp;

import java.io.File;

/**
 * Create by 金KingMgg
 * on 4/12/21 2:17 PM
 * 程序人员写程序，又拿程序换酒钱
 * 请不要尝试优化这段代码,我自己都看不懂了
 */
public class LargeImgLoader implements XPopupImageLoader {
    @Override
    public void loadImage(int position, @NonNull Object uri, @NonNull ImageView imageView) {
        if (uri instanceof String) {
            String imgUrl = (String) uri;
            imgUrl = imgUrl.startsWith("http") ? imgUrl : EasyHttp.getBaseUrl() + imgUrl;
            if (imgUrl.endsWith("/132")) {
                imgUrl = imgUrl.substring(0, imgUrl.length() - 4);
                imgUrl += "/0";
            }
            if (imgUrl.endsWith("svg")) {
//                SvgUtils.getdownloadsvgstr(AppConstant.getCdnUrl(imgUrl), imageView);
            } else {
                Log.i("孙", "LargeImgLoader: " + imgUrl);
                if (imgUrl.endsWith(".gif")) {

//                    GlideUtils.load(imageView.getContext(),imgUrl).thumbnail(0.5f).error(R.drawable.ic_dynamic_pic_error).into(imageView);
                    GlideUtil.putHttpImg(imgUrl,imageView);
                } else if (imgUrl.endsWith(".bmp")) {

                    /*Glide.with(imageView.getContext())
                            .asBitmap()
                            .load(imgUrl)
                            .thumbnail(0.5f)
                            .error(R.drawable.ic_false)
                            .into(new SimpleTarget<Bitmap>() {
                                @Override
                                public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                    imageView.setImageBitmap(resource);
                                }
                            });*/
                    GlideUtil.putHttpImg(imgUrl,imageView);
                } else {
                    //下载图片保存到本地
//                    GlideUtils.load(imageView.getContext(),imgUrl).into(imageView);
                    GlideUtil.putHttpImg(imgUrl,imageView);
                }
            }
        }
    }

    @Override
    public File getImageFile(@NonNull Context context, @NonNull Object uri) {
        try {
            if (uri instanceof String) {
                String imgUrl = (String) uri;
                imgUrl = imgUrl.startsWith("http") ? imgUrl : EasyHttp.getBaseUrl() + imgUrl;
                if (imgUrl.endsWith("/132")) {
                    imgUrl = imgUrl.substring(0, imgUrl.length() - 4);
                    imgUrl += "/0";
                }
//                return (File) GlideUtils.load(context, imgUrl).downloadOnly(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL).get();
                return Glide.with(context).load(imgUrl).downloadOnly(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL).get();
            } else if (uri instanceof Uri) {
//                return (File) GlideUtils.load(context, (Uri) uri).downloadOnly(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL).get();
                return Glide.with(context).load(uri).downloadOnly(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL).get();
                 } else {
                return Glide.with(context).load(uri).downloadOnly(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL).get();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
