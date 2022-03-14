package com.illyasr.mydempviews.ui.activity;



import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.app.ActivityOptionsCompat;
import androidx.databinding.DataBindingUtil;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.illyasr.mydempviews.MainPresent;
import com.illyasr.mydempviews.R;
import com.illyasr.mydempviews.base.BaseActivity;
import com.illyasr.mydempviews.databinding.ActivityQrCodeBinding;
import com.illyasr.mydempviews.databinding.DialogColorsBinding;
import com.illyasr.mydempviews.util.BitmapUtil;
import com.illyasr.mydempviews.util.GlideUtil;
import com.illyasr.mydempviews.view.ActionSheetDialog;
import com.illyasr.mydempviews.view.ComClickDialog;
import com.illyasr.mydempviews.view.ComPopupDialog;
import com.illyasr.mydempviews.view.MyAlertDialog;
import com.zhuosen.bilibili.biliplayer.zxing.CameraScan;
import com.zhuosen.bilibili.biliplayer.zxing.CaptureActivity;
import com.zhuosen.bilibili.biliplayer.zxing.util.CodeUtils;
import com.zhuosen.bilibili.biliplayer.zxing.util.LogUtils;

import java.util.List;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import pub.devrel.easypermissions.EasyPermissions;

public class QrCodeActivity extends BaseActivity<ActivityQrCodeBinding, MainPresent> {

    private static final int IMAGE_LOGO = 1929;
    private static final int IMAGE_PICKER = 1999;
    public static final int REQUEST_CODE = 1;
    public static final int RC_READ_PHOTO = 0X02;
    private ClipboardManager cm;
    private ClipData mClipData;
    public static final int REQUEST_CODE_PHOTO = 0X02;
    public static final String KEY_TITLE = "key_title";
    public static final String KEY_IS_QR_CODE = "key_code";
    public static final String KEY_IS_CONTINUOUS = "key_continuous_scan";
    private boolean isContinuousScan=false;

    public static final String IMAGE_URL = "tele/photos";
    private int codeColor = 0xff333333;
    private Bitmap logo;

    @SuppressLint("CheckResult")
    @Override
    protected void initData() {

        // 先默认logo,后面改成可选logo
        logo = BitmapFactory.decodeResource(getResources(), R.mipmap.applogo);



        mBindingView.stvLogo.setOnClickListener(v -> {
            new ActionSheetDialog(this)
                    .builder()
                    .setTitle("请选择样式")
                    .setCancelable(true)
                    .setCanceledOnTouchOutside(true)
                    .addSheetItem("原版logo", ActionSheetDialog.SheetItemColor.Blue, which -> {
                        logo = BitmapFactory.decodeResource(getResources(), R.mipmap.applogo);
                        showToast("已设置为默认logo!");
                    })
                    .addSheetItem("自定义logo", ActionSheetDialog.SheetItemColor.Blue, which -> {
                        startLogoPhoto();
                    })
                    .show();


        });

        //跳转的默认扫码界面
        mBindingView.stv1.setOnClickListener(v -> {
//            startActivityForResult(new Intent(this,CaptureActivity.class),REQUEST_CODE);
            ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeCustomAnimation(this,R.anim.in,R.anim.out);
            Intent intent = new Intent(this, CaptureActivity.class);
            intent.putExtra(KEY_TITLE,"扫码");
            intent.putExtra(KEY_IS_CONTINUOUS,isContinuousScan);
            ActivityCompat.startActivityForResult(this,intent,REQUEST_CODE,optionsCompat.toBundle());
        });
        //打开相册选择
        mBindingView.stv2.setOnClickListener(v -> {
            String[] perms = {Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE};
            if (EasyPermissions.hasPermissions(this, perms)) {//有权限
//                startPhotoCode();
                openAlbum();
            }else{
                EasyPermissions.requestPermissions(this,"App需要用到读写权限", RC_READ_PHOTO, perms);
            }
        });
        //生成  不带logo二维码
        mBindingView.stv3.setOnClickListener(v -> {
            if (TextUtils.isEmpty(mBindingView.et.getText().toString())){
                showToast("请先设置内容之后再尝试");
                return;
            }
            GlideUtil.putHttpImg(CodeUtils.createQRCode(mBindingView.et.getText().toString(),
                    mBindingView.imgQr.getWidth(),null,codeColor),mBindingView.imgQr);
        });
        //带logo二维码
        mBindingView.stv4.setOnClickListener(v -> {
            if (TextUtils.isEmpty(mBindingView.et.getText().toString())){
                showToast("请先设置内容之后再尝试");
                return;
            }
            GlideUtil.putHttpImg(CodeUtils.createQRCode(mBindingView.et.getText().toString(),
                    mBindingView.imgQr.getWidth(), logo,codeColor),mBindingView.imgQr);
        });
        mBindingView.stv13.setOnClickListener(v -> {// 扫描内容复制
            //获取剪贴板管理器：
            cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            // 创建普通字符型ClipData
            mClipData = ClipData.newPlainText("Label", mBindingView.stv13.getText().toString().replace("结果=",""));
            // 将ClipData内容放到系统剪贴板里。
            cm.setPrimaryClip(mClipData);
            showToast("复制成功");
        });

        mBindingView.imgQr.setOnLongClickListener(v -> {
            new MyAlertDialog(QrCodeActivity.this).builder()
                    .setTitle("提示")
                    .setMsg("保存到相册?")
                    .setPositiveButton("取消", v13 -> {

                    }).setNegativeButton("确定", v14 -> {
                Flowable.create((FlowableOnSubscribe<Bitmap>) e -> {
                    Bitmap bitmap =  BitmapUtil.drawable2Bitmap(mBindingView.imgQr.getDrawable());
                    e.onNext(bitmap);
                    e.onComplete();
                }, BackpressureStrategy.BUFFER)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
//                            .compose(bindToLifecycle())
                        .subscribe(bitmap -> {
                            BitmapUtil.saveImageToGallery(getApplicationContext(), bitmap, () -> {
//                                    Toasty.success(getApplicationContext(), "保存成功").show();
                                showToast("保存成功");
                            });
                        });

            }).show();

            return false;
        });

        mBindingView.stvColor.setOnClickListener(v -> {
            new ComClickDialog(QrCodeActivity.this, R.layout.dialog_colors) {
                DialogColorsBinding binding;
                @Override
                public void initView() {
                    binding = DataBindingUtil.bind(getContentView());
                    binding.colorPicker.addSVBar(binding.svBar);
                    binding.colorPicker.addOpacityBar(binding.opacityBar);
                    binding.colorPicker.addSaturationBar(binding.saturBar);
                    binding.colorPicker.addValueBar(binding.valueBar);
                }

                @Override
                public void initEvent() {
                    binding.colorPicker.setOnColorChangedListener(color -> {
                        Log.e("TAG", "color = " + color);
                    });

                    binding.dismiss.setOnClickListener(v12 -> dismiss());
                    binding.enter.setOnClickListener(v1 -> {
                        dismiss();
                        Log.e("TAG", "color = " + binding.colorPicker.getColor());
                        mBindingView.stvColor.setTextColor(binding.colorPicker.getColor());
                        codeColor = binding.colorPicker.getColor();
                    });
                }
            }.show();
        });

    }
    @Override
    protected int setLayoutId() {
        return R.layout.activity_qr_code;
    }
    /**
     *  打开相册获取图片,然后渲染成logo
     */
    private void startLogoPhoto(){
        Intent pickIntent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        startActivityForResult(pickIntent, IMAGE_LOGO);
    }




    //打开相册
    protected void openAlbum() {
        Intent intent = new Intent(Intent.ACTION_PICK, null);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        startActivityForResult(intent, IMAGE_PICKER);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK ){
            switch (requestCode){
                case REQUEST_CODE://   二维码扫描结果
//                    Bundle bundle = data.getExtras();;
//                    String resultStr = bundle.getString("result");
                    String resultStr = data.getStringExtra(CameraScan.SCAN_RESULT); //parseScanResult(data);
                    mBindingView.stv13.setText(resultStr);
                    showToast(resultStr+"");
                    mBindingView.stv13.setText("结果="+resultStr);
                    break;
                case IMAGE_PICKER:
                case REQUEST_CODE_PHOTO:
//                    parsePhoto(data);
//                    getRealPathFromUri_AboveApi19()
                    Log.e("TAG", data.getData() + "");
                   String s= CodeUtils.parseQRCode(BitmapUtil.ImageSizeCompress(data.getData()));
                   if (TextUtils.isEmpty(s)){
                       showToast("您选择的不是一维码/二维码图片,系统识别错误,请重新选择!");
                       return;
                   }
                   showToast(s);
                   mBindingView.stv13.setText(s);
                    break;

                case IMAGE_LOGO:
                    logo = BitmapUtil.ImageSizeCompress(data.getData());
                    showToast("已设置为自定义logo!");
                    break;
            }
        }
    }


}