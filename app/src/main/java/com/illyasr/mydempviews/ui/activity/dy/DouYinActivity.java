package com.illyasr.mydempviews.ui.activity.dy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.google.gson.Gson;
import com.illyasr.mydempviews.MainPresent;
import com.illyasr.mydempviews.R;
import com.illyasr.mydempviews.base.BaseActivity;
import com.illyasr.mydempviews.bean.VideoBean;
import com.illyasr.mydempviews.databinding.ActivityDouYinBinding;
import com.illyasr.mydempviews.ui.activity.GetVideoActivity;
import com.illyasr.mydempviews.ui.activity.bilibili.BiliBiliActivity;
import com.illyasr.mydempviews.util.DonwloadSaveImg;
import com.illyasr.mydempviews.util.FileUtils;
import com.illyasr.mydempviews.view.ActionSheetDialog;
import com.illyasr.mydempviews.view.MyAlertDialog;
import com.zhouyou.http.EasyHttp;
import com.zhouyou.http.cache.model.CacheMode;
import com.zhouyou.http.callback.CallBack;
import com.zhouyou.http.callback.DownloadProgressCallBack;
import com.zhouyou.http.exception.ApiException;

import java.util.Date;

public class DouYinActivity extends BaseActivity<ActivityDouYinBinding, MainPresent> {

    public static String DOU_YIN_BASE_URL = "https://www.iesdouyin.com/web/api/v2/aweme/iteminfo/?item_ids=";
    private MyAlertDialog dialog;

    @Override
    protected void initData() {
        // 6.46 vfO:/ 爆笑名字梗 你get到了吗？%搞笑 %沙雕 %影视 @抖音小助手   https://v.douyin.com/8H13ycL/ 緮制Ci链接，咑幵Dou音搜索，直接觀看视频！
        mBindingView.stv2.setOnClickListener(v -> {
            String s1 = mBindingView.etInput.getText().toString();
            if(TextUtils.isEmpty(s1)){
                showToast("请先输入链接");
                return;
            }
            EasyHttpP(s1.substring(s1.indexOf("http"),s1.lastIndexOf("/"))+"");
        });

        mBindingView.tvClean.setOnClickListener(v -> {
            mBindingView.etInput.setText("");
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getPasteString();
    }


    // 从黏贴板获取数据
    private void getPasteString(){
        // 获取并保存粘贴板里的内容
        try {
            runOnUiThread(() -> {
                ClipboardManager clipboard = (ClipboardManager)getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clipData = clipboard.getPrimaryClip();
                if (clipData != null && clipData.getItemCount() > 0) {
                    CharSequence text = clipData.getItemAt(0).getText();
                   String pasteString = text.toString();
                    Log.d("TAG", "getFromClipboard text=" + pasteString);
                    if (!TextUtils.isEmpty(pasteString)) {
                        mBindingView.etInput.setText(getClipboardContent(this));
                    }

                }
            });
        } catch (Exception e) {
            Log.e("TAG", "getFromClipboard error");
            e.printStackTrace();
        }
    }


    /**
     * 获取剪切板上的内容
     */
    public static String getClipboardContent(Context context) {
        ClipboardManager cm = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        if (cm != null) {
            ClipData data = cm.getPrimaryClip();
            if (data != null && data.getItemCount() > 0) {
                ClipData.Item item = data.getItemAt(0);
                if (item != null) {
                    CharSequence sequence = item.coerceToText(context);
                    if (sequence != null) {
                        return sequence.toString();
                    }
                }
            }
        }
        return null;
    }

    private Gson gson = new Gson();
    private void EasyHttpP(String redirect) {
        Log.e("TAG", "it is = " + redirect);
        if (TextUtils.isEmpty(redirect)){
            showToast("链接获取不正确,请联系管理员之后重试");
            return;
        }
//       String redirectUrl = CommonUtils.getLocation(redirect+"");//这个有点问题,舍弃了
//        String itemId = CommonUtils.matchNo(redirectUrl);
        CommonUtils.redirectUrl(redirect, s -> {
            Log.e("TAG", "重定向之后的长链接为"+s);
            String itemId = CommonUtils.matchNo(s);
            showDialog("解析中...",1);
            EasyHttp.get("api/v2/aweme/iteminfo/")
                    .cacheTime(300)//缓存300s 单位s
                    .baseUrl("https://www.iesdouyin.com/web/")
                    .cacheKey("cachekey")//缓存key
                    .cacheMode(CacheMode.CACHEANDREMOTE)//设置请求缓存模式
//                .params("item_ids","7073030464168070437")
                    .params("item_ids",""+itemId)
                    .execute(new CallBack<String>() {
                        @Override
                        public void onStart() {}
                        @Override
                        public void onCompleted() {}
                        @Override
                        public void onError(ApiException e) {
                            dismissDialog();
                            showToast(e.toString());
                        }
                        @Override
                        public void onSuccess(String it) {
                            dismissDialog();
                            DYDto dyDto = new DYDto();
                            DYResult dyResult = gson.fromJson(it, DYResult.class);
                            if (dyResult.getStatus_code() != 0) {
                                showToast("参数不合法,请检查链接之后重试!若还是不行请联系管理员!");
                                return;
                            }
                            //4、无水印视频 url
                            String  videoUrl = dyResult.getItem_list().get(0)
                                    .getVideo().getPlay_addr().getUrl_list().get(0)
                                    .replace("playwm", "play");
//                            String videoRedirectUrl = CommonUtils.getLocation(videoUrl);
                            CommonUtils.redirectUrl(videoUrl, s1 -> dyDto.setVideoUrl(s1));
                            //5、音频 url
//                       String musicUrl = dyResult.getItem_list().get(0).getMusic().getPlay_url().getUri();
//                        dyDto.setMusicUrl(musicUrl);
                            //6、封面
                       String videoPic = dyResult.getItem_list().get(0).getVideo().getDynamic_cover().getUrl_list().get(0);
                        dyDto.setVideoPic(videoPic);
                            // 7、视频文案
//                        String desc = dyResult.getItem_list().get(0).getDesc();
//                        dyDto.setDesc(desc);
                            if (!TextUtils.isEmpty(videoUrl)) {
                                dialog = new MyAlertDialog(DouYinActivity.this).builder()
                                        .setTitle("提示")
                                        .setMsg("解析完成,是否下载?")
                                        .setPositiveButton("下载", v13 -> {
                                            FileUtils.downMp4(DouYinActivity.this, dyDto.getVideoUrl(), new FileUtils.OnDownloadListener() {
                                                @Override
                                                public void onStart() {

                                                }

                                                @Override
                                                public void onEnd() {
                                                    handler.sendEmptyMessage(200);
//                                                    showToast("下载完成");
                                                }

                                                @Override
                                                public void onFailured() {
                                                    handler.sendEmptyMessage(400);
//                                                    showToast("下载失败");
                                                }
                                            });
                                        })
                                        .setNegativeButton("不下载,去播放", v14 -> {
                                            //https://v.douyin.com/F18xkQH/
                                            BiliBiliActivity.getToPlayVideo(DouYinActivity.this, dyDto.getVideoUrl(), false);
                                        });
                                if (!dialog.isShowing()) {
                                    dialog.show();
                                }

                            }


                        }
                    });
        });


    }

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                default:
                    break;
                case 200:
                    showToast("下载成功!视频已经保存在相册中");
                    break;
                case 400:
                    showToast("下载失败!请检查网络或者内存之后重试");
                    break;
            }
        }
    };

    @Override
    protected int setLayoutId() {
        return R.layout.activity_dou_yin;
    }


}