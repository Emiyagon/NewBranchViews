package com.illyasr.mydempviews.ui.activity;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.provider.SyncStateContract;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;
import com.illyasr.mydempviews.MainPresent;
import com.illyasr.mydempviews.MyApplication;
import com.illyasr.mydempviews.R;
import com.illyasr.mydempviews.base.BaseActivity;
import com.illyasr.mydempviews.bean.VideoBean;
import com.illyasr.mydempviews.databinding.ActivityGetVideoBinding;
import com.illyasr.mydempviews.databinding.DialogShowBinding;
import com.illyasr.mydempviews.http.BaseResponse;
import com.illyasr.mydempviews.http.UserApi;
import com.illyasr.mydempviews.manager.AndroidDownloadManager;
import com.illyasr.mydempviews.ui.activity.bilibili.BiliBiliActivity;
import com.illyasr.mydempviews.ui.activity.dy.CommonUtils;
import com.illyasr.mydempviews.util.DonwloadSaveImg;
import com.illyasr.mydempviews.util.FileUtils;
import com.illyasr.mydempviews.util.GlideUtil;
import com.illyasr.mydempviews.util.ImageUtil;
import com.illyasr.mydempviews.util.ImgDonwload;
import com.illyasr.mydempviews.util.UrlUtil;
import com.illyasr.mydempviews.util.aesencryption.MyAESUtil;
import com.illyasr.mydempviews.view.ActionSheetDialog;
import com.illyasr.mydempviews.view.ComClickDialog;
import com.illyasr.mydempviews.view.MyAlertDialog;
import com.zhouyou.http.EasyHttp;
import com.zhouyou.http.cache.model.CacheMode;
import com.zhouyou.http.callback.CallBack;
import com.zhouyou.http.callback.DownloadProgressCallBack;
import com.zhouyou.http.exception.ApiException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class GetVideoActivity extends BaseActivity<ActivityGetVideoBinding, MainPresent> {

    private Gson gson = new Gson();
    private long time = 0l;
    private VideoBean bean;
    private Dialog comClickDialog;
    private static int REQUEST_PERMISSION_CODE = 1;
    public static final String oldPath = "https://parse.bqrdh.com/#/";

    public static final String test = "https://www.bilibili.com/video/BV1KV411x7y1";
//    public static final String test = "https://v.douyin.com/MU9r2tV/";

    private void checkPermission() {
            //检查权限（NEED_PERMISSION）是否被授权 PackageManager.PERMISSION_GRANTED表示同意授权
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
                //用户已经拒绝过一次，再次弹出权限申请对话框需要给用户一个解释
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission
                    .WRITE_EXTERNAL_STORAGE)) {
                Toast.makeText(this, "请开通相关权限，否则无法正常使用本应用！", Toast.LENGTH_SHORT).show();
            }
                //申请权限
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_PERMISSION_CODE);
        } else {
            Toast.makeText(this, "授权成功！", Toast.LENGTH_SHORT).show();
            Log.e("aaaaa", "checkPermission: 已经授权！");
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_get_video);
//        binding = DataBindingUtil.setContentView(this, R.layout.activity_get_video);
//        binding.setLifecycleOwner(this);



    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_get_video;
    }

    @Override
    protected void initData() {
        dialog = new ProgressDialog(this);
        dialog.setMax(100);
        checkPermission();


        mBindingView.imgFmt.setOnLongClickListener(v -> false);

        mBindingView.etHine.setText(test);
        mBindingView.tvClean.setOnClickListener(v -> mBindingView.etHine.setText(""));
        mBindingView.stv0.setOnLongClickListener(v -> {
            new MyAlertDialog(this).builder()
                    .setTitle("tips(可以点击外部取消)")
                    .setMsg("现在支持的解析链接有【B站】\t" +
                            "【微博】\t" +
                            "【知乎】\t" +
                            "【秒拍】\t" +
                            "【抖音】\t" +
                            "【西瓜视频】\t" +
                            "【今日头条】\t" +
                            "【美拍】\t" +
                            "【微视】,后续链接升级的话可能会增加,请关注官方通知\n(ps:所有链接最好都是网页上的视频哦,单独分享的链接是打不开的!)" +
                            "\n目前发现手机端的都没办法,尝试过去解析也找不到逻辑,等我重新找找他到底怎么加密之后再去尝试吧,现在就先直接去电脑端下载比较安全\n" +
                            "(而且现在...叔叔也实在是恶心了...首页清一色手机视频,真恶心...)")
                    .setPositiveButton("我知道了", v13 -> {
//                            WebActivity.GoTo(GetVideoActivity.this,oldPath);

//                        startActivity(new Intent(Intent.ACTION_VIEW,Uri.parse(oldPath)));
                    }).setNegativeButton("复制原本网站链接", v14 -> {
                //

                //获取剪贴板管理器：
                ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                // 创建普通字符型ClipData
                ClipData mClipData = ClipData.newPlainText("Label", oldPath);
                // 将ClipData内容放到系统剪贴板里。
                cm.setPrimaryClip(mClipData);
            }).show();
            return false;
        });
        mBindingView.imgFmt.setOnClickListener(v -> {
            if(bean==null){
                return;
            }
            new MyAlertDialog(this).builder()
                    .setTitle("提示")
                    .setMsg("是否保存本图片")
                    .setPositiveButton("保存", v13 -> {
                        DonwloadSaveImg.donwloadImg(GetVideoActivity.this, bean.getCoverPic());
                    }).setNegativeButton("取消", v14 -> {
                //
            }) .setNegativeButton("直接观看视频", v1 -> {
                BiliBiliActivity.getToPlayVideo(GetVideoActivity.this,
                        bean.getResources().get(0).getOriginalUrl(), false);

            })
                    .show();
        });
        mBindingView.stv0.setOnClickListener(v -> {
            if (TextUtils.isEmpty(mBindingView.etHine.getText().toString())){
                bean = null;
                return;
            }


            if (comClickDialog != null && bean != null) {
                if (!comClickDialog.isShowing()) {
                    comClickDialog.show();
                }
                return;
            }

            //  【八大胡同旧主，是男妓，撑起了妓院半边天！-哔哩哔哩】 https://b23.tv/98FKWNl
            String s = mBindingView.etHine.getText().toString().trim().replace(" ","");
            String url = s.substring(s.indexOf("http"));
            if (UrlUtil.isLongUrl(url)){
                OnHttp(url);
            }else {
                CommonUtils.redirectUrl(url, x -> {
                    OnHttp(x);
                });
            }



        });
    }


    private void OnHttp(String url){
        showDialog("");
        //  https://parse.bqrdh.com/api/video/parser?t=1625293799417
        Map<String, Object> map = new HashMap<>();
        map.put("url", url);
        UserApi.getVideos(map, new Observer<VideoBean>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }

            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onNext(@NonNull VideoBean it) {
                dismissDialog();
                if (it.isSuccess() || it.getCode() == 0) {
                    bean = it;
                    GlideUtil.putHttpImg(bean.getCoverPic(), mBindingView.imgFmt);
                    comClickDialog = new ComClickDialog(GetVideoActivity.this, R.layout.dialog_show) {
                        DialogShowBinding showBinding;

                        @Override
                        public void initView() {
                            showBinding = DataBindingUtil.bind(getContentView());
                        }

                        @Override
                        public void initEvent() {
                            showBinding.dismiss.setOnClickListener(v1 -> dismiss());
                            showBinding.download.setOnClickListener(v12 -> {
                                dismiss();
                                OnList(bean);
                            });
                        }
                    };
                    if (!comClickDialog.isShowing()) {
                        comClickDialog.show();
                    }
                }else if (it.getCode() == 20003){//新版加密之后
                    String res = it.getData();
                    String newJson = MyAESUtil.OnRes64(res);
                    //这是逼站解析
                    bean = gson.fromJson(newJson, VideoBean.class);
                    GlideUtil.putHttpImg(bean.getCoverPic(), mBindingView.imgFmt);
                    comClickDialog = new ComClickDialog(GetVideoActivity.this, R.layout.dialog_show) {
                        DialogShowBinding showBinding;

                        @Override
                        public void initView() {
                            showBinding = DataBindingUtil.bind(getContentView());
                        }

                        @Override
                        public void initEvent() {
                            showBinding.dismiss.setOnClickListener(v1 -> dismiss());
                            showBinding.download.setOnClickListener(v12 -> {
                                dismiss();
                                OnList(bean);
                            });
                        }
                    };
                    if (!comClickDialog.isShowing()) {
                        comClickDialog.show();
                    }
                }
            }

            @Override
            public void onError(@NonNull Throwable e) {
                dismissDialog();
                showToast("出现错误,信息="+e.getMessage());
                Log.e("TAG","出现错误,信息="+e.getMessage());
            }

            @Override
            public void onComplete() {

            }
        });

        /*    EasyHttp.post("api/video/parser?_t="+new Date().getTime())
                    .cacheTime(300)//缓存300s 单位s
                    .baseUrl("https://parse.bqrdh.com/")
                    .cacheKey("cachekey")//缓存key
                    .cacheMode(CacheMode.CACHEANDREMOTE)//设置请求缓存模式
//                    .params("url",)
                    .upJson(gson.toJson(map))
                    .execute(new CallBack<String>() {
                        @Override
                        public void onStart() { }

                        @Override
                        public void onCompleted() {}

                        @Override
                        public void onError(ApiException e) {
                            dismissDialog();
                            Log.e("TAG", "" + e.getMessage());
                         }
                        @Override
                        public void onSuccess(String str) {
                            Log.e("TAG", "" + str);
                            dismissDialog();
                            bean = gson.fromJson(str, VideoBean.class);
                            if (bean.isSuccess() || bean.getCode() == 0) {
                                GlideUtil.putHttpImg(bean.getCoverPic(),mBindingView.imgFmt);
                                comClickDialog = new ComClickDialog(GetVideoActivity.this, R.layout.dialog_show) {
                                    DialogShowBinding showBinding ;
                                    @Override
                                    public void initView() {
                                        showBinding = DataBindingUtil.bind(getContentView());
                                    }

                                    @Override
                                    public void initEvent() {
                                        showBinding.dismiss.setOnClickListener(v1 -> dismiss());
                                        showBinding.download.setOnClickListener(v12 -> {
                                            dismiss();
                                            OnList(bean);
                                        });
                                    }
                                };
                                if (!comClickDialog.isShowing()) {
                                    comClickDialog.show();
                                }
                            }
                        }
                    });*/
    }

    private ProgressDialog dialog;
    //   下方列表并且展示下载
    private void OnList(VideoBean bean ) {

        ActionSheetDialog actionSheetDialog = new ActionSheetDialog(GetVideoActivity.this)
                .builder()
                .setTitle("请选择分辨率")
                .setCancelable(false)
                .setCanceledOnTouchOutside(false);
        for (int i = 0; i < bean.getResources().size(); i++) {
            int finalI = i;
            actionSheetDialog.addSheetItem(bean.getResources().get(finalI).getRatio(), ActionSheetDialog.SheetItemColor.Blue, which -> {
                showToast("开始下载");
//                dialog.show();

                if (bean != null) {
                    FileUtils.downMp4(GetVideoActivity.this, bean.getResources().get(finalI).getOriginalUrl(), new FileUtils.OnDownloadListener() {
                        @Override
                        public void onStart() {

                        }

                        @Override
                        public void onEnd() {
                            handler.sendEmptyMessage(200);
                        }

                        @Override
                        public void onFailured() {
                            handler.sendEmptyMessage(400);
                        }
                    });
                    return;
                }


               /* EasyHttp.downLoad(bean.getResources().get(finalI).getOriginalUrl())
//                        .savePath("/sdcard/test/QQ")
                        .saveName(new Date().getTimezoneOffset()+"video.mp4")//不设置默认名字是时间戳生成的
                        .execute(new DownloadProgressCallBack<String>() {
                            @Override
                            public void update(long bytesRead, long contentLength, boolean done) {
                                int progress = (int) (bytesRead * 100 / contentLength);

                                dialog.setProgress(progress);
                                if (done) {//下载完成
                                }
                            }
                            @Override
                            public void onStart() {
                                //开始下载
                            }
                            @Override
                            public void onComplete(String path) {
                                //下载完成，path：下载文件保存的完整路径
                                showToast("下载完成,文件保存在"+path+"下");
                                dialog.dismiss();
                            }
                            @Override
                            public void onError(ApiException e) {
                                //下载失败
                                dialog.dismiss();
                            }
                        });*/



            });
        }
        if (actionSheetDialog!=null && !actionSheetDialog.isShowDialogIng()){
            actionSheetDialog.show();
        }

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








}