package com.illyasr.mydempviews.ui.activity;

import androidx.annotation.NonNull;
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
    private void checkPermission() {
            //???????????????NEED_PERMISSION?????????????????? PackageManager.PERMISSION_GRANTED??????????????????
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
                //??????????????????????????????????????????????????????????????????????????????????????????
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission
                    .WRITE_EXTERNAL_STORAGE)) {
                Toast.makeText(this, "????????????????????????????????????????????????????????????", Toast.LENGTH_SHORT).show();
            }
                //????????????
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_PERMISSION_CODE);
        } else {
            Toast.makeText(this, "???????????????", Toast.LENGTH_SHORT).show();
            Log.e("aaaaa", "checkPermission: ???????????????");
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

        mBindingView.etHine.setText("https://www.bilibili.com/video/BV1KV411x7y1");
        mBindingView.tvClean.setOnClickListener(v -> mBindingView.etHine.setText(""));
        mBindingView.stv0.setOnLongClickListener(v -> {
            new MyAlertDialog(this).builder()
                    .setTitle("tips")
                    .setMsg("?????????????????????????????????B??????\t" +
                            "????????????\t" +
                            "????????????\t" +
                            "????????????\t" +
                            "????????????\t" +
                            "??????????????????\t" +
                            "??????????????????\t" +
                            "????????????\t" +
                            "????????????,???????????????????????????????????????,?????????????????????\n(ps:?????????????????????????????????????????????,????????????????????????????????????!)")
                    .setPositiveButton("????????????", v13 -> {

                    }).setNegativeButton("????????????????????????", v14 -> {
                //
                String oldPath = "https://parse.bqrdh.com/#/";
                //???????????????????????????
                ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                // ?????????????????????ClipData
                ClipData mClipData = ClipData.newPlainText("Label", oldPath);
                // ???ClipData?????????????????????????????????
                cm.setPrimaryClip(mClipData);
            }).show();
            return false;
        });
        mBindingView.imgFmt.setOnClickListener(v -> {
            if(bean==null){
                return;
            }
            new MyAlertDialog(this).builder()
                    .setTitle("??????")
                    .setMsg("?????????????????????")
                    .setPositiveButton("??????", v13 -> {
                        DonwloadSaveImg.donwloadImg(GetVideoActivity.this, bean.getCoverPic());
                    }).setNegativeButton("??????", v14 -> {
                //
            }) .setNegativeButton("??????????????????", v1 -> {
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
//            if ( ! mBindingView.etHine.getText().toString().replace(" ","").startsWith("http")){
//                Toast.makeText(MyApplication.getInstance(),"????????????????????????!",Toast.LENGTH_SHORT).show();
//                return;
//            }


            if (comClickDialog != null && bean != null) {
                if (!comClickDialog.isShowing()) {
                    comClickDialog.show();
                }
                return;
            }

            //  ???????????????????????????????????????????????????????????????-??????????????? https://b23.tv/98FKWNl
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

            @Override
            public void onNext(@NonNull VideoBean it) {
                dismissDialog();
//                    bean = gson.fromJson(it.getData(), VideoBean.class);
                bean = it;
                if (bean.isSuccess() || bean.getCode() == 0) {
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

                showToast("????????????,??????="+e.getMessage());
            }

            @Override
            public void onComplete() {

            }
        });

        /*    EasyHttp.post("api/video/parser?_t="+new Date().getTime())
                    .cacheTime(300)//??????300s ??????s
                    .baseUrl("https://parse.bqrdh.com/")
                    .cacheKey("cachekey")//??????key
                    .cacheMode(CacheMode.CACHEANDREMOTE)//????????????????????????
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
    //   ??????????????????????????????
    private void OnList(VideoBean bean ) {

        ActionSheetDialog actionSheetDialog = new ActionSheetDialog(GetVideoActivity.this)
                .builder()
                .setTitle("??????????????????")
                .setCancelable(false)
                .setCanceledOnTouchOutside(false);
        for (int i = 0; i < bean.getResources().size(); i++) {
            int finalI = i;
            actionSheetDialog.addSheetItem(bean.getResources().get(finalI).getRatio(), ActionSheetDialog.SheetItemColor.Blue, which -> {
                showToast("????????????");
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
                        .saveName(new Date().getTimezoneOffset()+"video.mp4")//??????????????????????????????????????????
                        .execute(new DownloadProgressCallBack<String>() {
                            @Override
                            public void update(long bytesRead, long contentLength, boolean done) {
                                int progress = (int) (bytesRead * 100 / contentLength);

                                dialog.setProgress(progress);
                                if (done) {//????????????
                                }
                            }
                            @Override
                            public void onStart() {
                                //????????????
                            }
                            @Override
                            public void onComplete(String path) {
                                //???????????????path????????????????????????????????????
                                showToast("????????????,???????????????"+path+"???");
                                dialog.dismiss();
                            }
                            @Override
                            public void onError(ApiException e) {
                                //????????????
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
                    showToast("????????????!??????????????????????????????");
                    break;
                case 400:
                    showToast("????????????!???????????????????????????????????????");
                    break;
            }
        }
    };








}