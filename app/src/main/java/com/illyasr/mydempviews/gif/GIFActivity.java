package com.illyasr.mydempviews.gif;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.solver.state.State;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.illyasr.mydempviews.MainPresent;
import com.illyasr.mydempviews.R;
import com.illyasr.mydempviews.base.BaseActivity;
import com.illyasr.mydempviews.databinding.ActivityGifactivityBinding;
import com.illyasr.mydempviews.util.BitmapUtil;
import com.illyasr.mydempviews.util.RxTimerUtil;
import com.illyasr.mydempviews.view.ActionSheetDialog;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.listener.OnResultCallbackListener;
import com.luck.picture.lib.tools.GlideEngine;

import java.util.List;

public class GIFActivity extends BaseActivity<ActivityGifactivityBinding, MainPresent> {
    private static final int REQUEST_SELECT_VIDEO = 1;
    private String filePath;
    private enum State {INIT, READY, BUILDING, COMPLETE}
    private State state = State.INIT;
    @SuppressLint("StaticFieldLeak")
    @Override
    protected void initData() {

        mBindingView.tip.setOnClickListener(v -> {

            new AsyncTask<Void, Void, Void>() {
                @Override
                protected void onPreExecute() {
                    state = GIFActivity.State.BUILDING;
                    mBindingView.tip.setText("正在生成中,请勿点击!");
                }

                @Override
                protected Void doInBackground(Void... params) {
                    BitmapExtractor extractor = new BitmapExtractor();
                    extractor.setFPS(4);
                    extractor.setScope(0, 5);
                    extractor.setSize(540, 960);
                    filePath = "https://vd2.bdstatic.com/mda-ngwz9j8ip7rp2jut/sc/cae_h264/1659310381713284483/mda-ngwz9j8ip7rp2jut.mp4?v_from_s=bdapp-resbox-nanjing";
                    List<Bitmap> bitmaps = extractor.createBitmaps(filePath);

                    String fileName = String.valueOf(System.currentTimeMillis()) + ".gif";
                    String filePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + fileName;
                    GIFEncoder encoder = new GIFEncoder();
                    encoder.init(bitmaps.get(0));
                    encoder.start(filePath);
                    for (int i = 1; i <bitmaps.size(); i++) {
                        encoder.addFrame(bitmaps.get(i));
                    }
                    encoder.finish();
                    BitmapUtil.saveGif(encoder.indexedPixels);
                    return null;
                }

                @Override
                protected void onPostExecute(Void aVoid) {
                    state = GIFActivity.State.COMPLETE;
                    mBindingView.tip.setText("gif生成完成");
                    mBindingView.tip.setVisibility(View.GONE);
//                    selectVideo.setText(R.string.select_video);
//                    Toast.makeText(getApplicationContext(), "存储路径" + filePath, Toast.LENGTH_LONG).show();
                    showToast("生成成功,存储路径是" + filePath);

                }
            }.execute();

        });

        mBindingView.stv12.setOnClickListener(v -> {
            new ActionSheetDialog(this)
                    .builder()
                    .setTitle("请选择")
                    .setCancelable(true)
                    .setCanceledOnTouchOutside(true)
                    .addSheetItem("图片", ActionSheetDialog.SheetItemColor.Blue, which -> {
                      //图片就做放大缩小处理
                        PictureSelector.create(this)
                                .openGallery(PictureMimeType.ofImage())
                                .theme(R.style.picture_WeChat_style)
                                .maxSelectNum(1)
                                .minSelectNum(1)
                                .selectionMode(PictureConfig.SINGLE)
                                .isMaxSelectEnabledMask(true)// 选择数到了最大阀值列表是否启用蒙层效果
                                .isCamera(false)//是否显示拍照
                                .isCompress(true)//打开压缩.synOrAsy(false)//同步true或异步false 压缩 默认同步
                                .minimumCompressSize(1*1024)// 小于100kb的图片不压缩
//                                .imageEngine(GlideEngine.createGlideEngine())// 外部传入图片加载引擎，必传项,但是这块有点不友好,所以我就设置了默认引擎,不传也没事
                                .isGif(true)
                                .forResult(new OnResultCallbackListener<LocalMedia>() {
                                    @Override
                                    public void onResult(List<LocalMedia> result) {
                                        String fileRealPath = result.get(0).getRealPath();
                                        showToast("抱歉,这一步我暂时还没做好,用视频试试吧~");
                                    }

                                    @Override
                                    public void onCancel() {

                                    }
                                });

                    })
                    .addSheetItem("视频", ActionSheetDialog.SheetItemColor.Blue, which -> {
                        //视频按照本来的来
                        PictureSelector.create(this)
                                .openGallery(PictureMimeType.ofVideo())
                                .theme(R.style.picture_WeChat_style)
                                .maxSelectNum(1)
                                .minSelectNum(1)
                                .selectionMode(PictureConfig.SINGLE)
                                .isMaxSelectEnabledMask(true)// 选择数到了最大阀值列表是否启用蒙层效果
                                .isCamera(false)//是否显示拍照
                                .isCompress(true)//打开压缩.synOrAsy(false)//同步true或异步false 压缩 默认同步
                                .minimumCompressSize(1*1024)// 小于100kb的图片不压缩
//                                .imageEngine(GlideEngine.createGlideEngine())// 外部传入图片加载引擎，必传项,但是这块有点不友好,所以我就设置了默认引擎,不传也没事
                                .isGif(true)
                                .forResult(new OnResultCallbackListener<LocalMedia>() {
                                    @Override
                                    public void onResult(List<LocalMedia> result) {
                                        String fileRealPath = result.get(0).getRealPath();
                                        Log.e("TAG", result.get(0).toString());

                                        Uri videoUri = Uri.parse(TextUtils.isEmpty(result.get(0).getAndroidQToPath())?
                                                result.get(0).getPath():result.get(0).getAndroidQToPath());
                                        filePath = mPresenter.getRealFilePath(videoUri);
                                        state = GIFActivity.State.READY;
//                                        mBindingView.selectVideo.setText("生成 GIF");
                                        mBindingView.tip.setVisibility(View.VISIBLE);
                                        mBindingView.tip.setText("点击按钮, 生成 GIF");
                                    }

                                    @Override
                                    public void onCancel() {

                                    }
                                });
                    })
                    .show();
        });
    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_gifactivity;
    }
}