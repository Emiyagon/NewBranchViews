package com.illyasr.mydempviews;

import android.Manifest;
import android.content.ComponentName;
import android.content.Intent;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.illyasr.mydempviews.adapter.MainAdapter;
import com.illyasr.mydempviews.base.BaseActivity;
import com.illyasr.mydempviews.bean.TabBean;
import com.illyasr.mydempviews.databinding.ActivityMainBinding;
import com.illyasr.mydempviews.phone.PhoneActivity;
import com.illyasr.mydempviews.ui.activity.WebActivity;
import com.illyasr.mydempviews.ui.activity.canender.CalenderActivity;
import com.illyasr.mydempviews.ui.activity.GetVideoActivity;
import com.illyasr.mydempviews.ui.activity.HealthyActivity;
import com.illyasr.mydempviews.ui.activity.MainActivityText;
import com.illyasr.mydempviews.ui.activity.MyLocationActivity;
import com.illyasr.mydempviews.ui.activity.PlayActivity;
import com.illyasr.mydempviews.ui.activity.QrCodeActivity;
import com.illyasr.mydempviews.ui.activity.dy.DouYinActivity;
import com.illyasr.mydempviews.ui.activity.guaxiang.DivinationActivity;
import com.illyasr.mydempviews.ui.activity.notify.NotifyActivity;
import com.illyasr.mydempviews.ui.activity.vr.VRSActivity;
import com.illyasr.mydempviews.util.GlideUtil;
import com.illyasr.mydempviews.view.dialog.CityDialog;
import com.illyasr.mydempviews.view.dialog.MyPasswordDialog;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.listener.OnResultCallbackListener;
import com.luck.picture.lib.tools.GlideEngine;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.interfaces.OnConfirmListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import pub.devrel.easypermissions.EasyPermissions;

//广告业
public class MainActivity extends BaseActivity<ActivityMainBinding,MainPresent> {

    private MainAdapter adapter;
    private List<TabBean> list = new ArrayList<>();
    private int tag;

    private String wechatUrl = "https://mmbiz.qpic.cn/mmbiz_gif/Ljib4So7yuWjl1icpf1AEqjZBoBicMPk0N8ZYlSxh9NuBctGpGRsBTcWVHouxLvMg3IRRCby99mNMHa7O6SeHcqTA/640?wx_fmt=gif";
    @Override
    protected int setLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initData() {
        GlideUtil.putHttpImg(wechatUrl,mBindingView.img);

        String[] messions = new String[]{
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.READ_PHONE_STATE,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CALL_PHONE,
                Manifest.permission.CAMERA,
        };
        if (EasyPermissions.hasPermissions(this, messions)) {
//            GetLoc();
        } else {
            EasyPermissions.requestPermissions(this, getResources().getString(R.string.toast_1), 100, messions);
        }

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction =fragmentManager.beginTransaction();
//        fragmentTransaction.setCustomAnimations(R.anim.push_right_in,R.anim.push_right_out, R.anim.push_left_in,R.anim.push_left_out);//设置进入，退出动画
        fragmentTransaction.addToBackStack(null);//加入回退栈
        fragmentTransaction.add(R.id.frameLayout,  TestFragment.instantiate(MainActivity.this,TestFragment.class.getName()));


//        list.add(new TabBean("原初",-1));
        list.add(new TabBean("获取定位",0));
        list.add(new TabBean("撩妹神器",1));
        list.add(new TabBean("SpView",2));
        list.add(new TabBean("获取Bilibili视频并且下载",3));
        list.add(new TabBean("获取抖音无水印视频",5));
        list.add(new TabBean("获取通讯录列表",4));
        list.add(new TabBean("聚会神器",6));
        list.add(new TabBean("城市三级联动选择器",7));
        list.add(new TabBean("zxing二维码",8));
        list.add(new TabBean("健康",9));
        list.add(new TabBean("VR/图片选择器(相册+拍照+视频)",10));
        list.add(new TabBean("各种日历",11));
        list.add(new TabBean("倒计时",12));
        list.add(new TabBean("卜卦",13));
        list.add(new TabBean("视频源",14));
//        rvAlbums.setLayoutManager(new GridLayoutManager(this,3));
        adapter = new MainAdapter(this,list);
        mBindingView.rvAlbums.setAdapter(adapter);
        adapter.setOnRem((pos, type) -> {
            switch (type) {
                case 0:// 获取定位
                    startActivity(new Intent(MainActivity.this, MyLocationActivity.class));
                    break;
                case 1:// 高能弹幕
                    startActivity(new Intent(MainActivity.this, MainActivityText.class));
                    break;
                case 2:// sptv
                    try {
//                        startActivity(new Intent(MainActivity.this,Class.forName("com.coorchice.supertextview.MainActivity")));//
                        Intent intent=new Intent(Intent.ACTION_MAIN);
                        intent.addCategory(Intent.CATEGORY_LAUNCHER);
                        //参数是包名，类全限定名，注意直接用类名不行
                        String lm = "com.coorchice.supertextview";
                        ComponentName cn=new ComponentName(lm,  String.format("%s.MainActivity",lm));
                        intent.setComponent(cn);
                        startActivity(intent);
                    } catch (Exception e) {
                        showToast("请安装STV之后重试");
                        e.printStackTrace();
                    }
                    break;
                case 3://获取bilibili视频
                    // 从API11开始android推荐使用android.content.ClipboardManager
                    // 为了兼容低版本我们这里使用旧版的android.text.ClipboardManager，虽然提示deprecated，但不影响使用。
//                    ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                    // 将文本内容放到系统剪贴板里。
//                    cm.setText(videoUrl2);
                    startActivity(new Intent(MainActivity.this, GetVideoActivity.class));
                    break;
                case 4://通讯录
                    startActivity(new Intent(MainActivity.this, PhoneActivity.class));
                    break;
                case 5://获取抖音无水印视频
                    startActivity(new Intent(MainActivity.this, DouYinActivity.class));
                    break;
                case 6://聚会神器
//                    startActivity(new Intent(MainActivity.this, TensentActivity.class));
                    startActivity(new Intent(MainActivity.this, PlayActivity.class));
                    break;
                case 7://
                    tag++;
                    if (tag%2==0){
                       new XPopup.Builder(getContext())
//                        .hasNavigationBar(false)
//                        .hasStatusBar(false)
//                                .isDestroyOnDismiss(true)
//                                .isTouchThrough(true)
//                        .dismissOnBackPressed(false)
//                        .isViewMode(true)
//                        .hasBlurBg(true)
//                         .autoDismiss(false)
//                        .popupAnimation(PopupAnimation.NoAnimation)
                                .asConfirm("哈哈", "床前明月光，疑是地上霜；举头望明月，低头思故乡。",
                                        "取消", "确定",
                                        new OnConfirmListener() {
                                            @Override
                                            public void onConfirm() {
                                            }
                                        }, null, false)
                           .show();
                        return;
                    }
                   /* new CityDialog(this)
                            .setGetFinger(false)
                            .show();*/
                    new MyPasswordDialog(this).show();
                    break;
                case 8://二维码相关
                    startActivity(new Intent(MainActivity.this, QrCodeActivity.class));
                    break;
                case 9://健康
                    startActivity(new Intent(MainActivity.this, HealthyActivity.class));
                    break;
                case 10://VR/图片选择器
//                    startActivity(new Intent(MainActivity.this, VRSActivity.class));
                    int maxSelecNum = 9;
                    PictureSelector
                            .create(this)
//                            .openGallery(PictureMimeType.ofImage())
                            .openGallery(PictureMimeType.ofAll())
                            .theme(R.style.picture_WeChat_style)
                            .maxSelectNum(maxSelecNum)
                            .minSelectNum(1)
                            .selectionMode(PictureConfig.MULTIPLE)
                            .isMaxSelectEnabledMask(true)// 选择数到了最大阀值列表是否启用蒙层效果
                            .isCamera(true)//是否显示拍照
                            .isCompress(true)//打开压缩
                            .isEnableCrop(maxSelecNum==1)// 是否裁剪,单独时需要裁减,其他时候不用
                            .circleDimmedLayer(true)// 是否圆形裁剪
                            .showCropFrame(false)// 是否显示裁剪矩形边框 圆形裁剪时建议设为false
                            .showCropGrid(false)// 是否显示裁剪矩形网格 圆形裁剪时建议设为false
                            .isOpenClickSound(false)// 是否开启点击声音
                            //.basicUCropConfig()//对外提供所有UCropOptions参数配制，但如果PictureSelector原本支持设置的还是会使用原有的设置
                            .compressQuality(50)// 图片压缩后输出质量 0~ 100
                            .synOrAsy(false)//同步true或异步false 压缩 默认同步
                            .minimumCompressSize(1*1024)// 小于100kb的图片不压缩
//                    .imageEngine(GlideEngine.createGlideEngine())// 外部传入图片加载引擎，必传项,但是这块有点不友好,所以我就设置了默认引擎,不传也没事
                            .isGif(true)
//                            .selectionData(list)
//              .forResult(PictureConfig.CHOOSE_REQUEST)
                            .forResult(new OnResultCallbackListener<LocalMedia>() {
                                @Override
                                public void onResult(List<LocalMedia> result) {

                                }

                                @Override
                                public void onCancel() {

                                }
                            });
                    break;
                case 11://日历
                    startActivity(new Intent(MainActivity.this, CalenderActivity.class));
                    break;
                case 12://通知
                    startActivity(new Intent(MainActivity.this, NotifyActivity.class));
                    break;
                case 13://卜卦
                    startActivity(new Intent(MainActivity.this, DivinationActivity.class));
                    break;
                case 14://web 视频
                    WebActivity.GoTo(MainActivity.this,"http://120.25.241.57/");//fdjknvjk
//                    WebActivity.GoTo(MainActivity.this,"http://10.1.7.193:8081/#/login");
                    break;
                default:

                    break;
            }

        });

        onPit();
    }

    private void onPit() {
        //数据源
        ArrayList<String> spinners = new ArrayList<>();
        spinners.add("今日");
        spinners.add("昨日");
        spinners.add("本周");
        spinners.add("上周");
        spinners.add("本月");
        spinners.add("上月");
        //设置ArrayAdapter内置的item样式-这里是单行显示样式
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, new ArrayList<>());
        //这里设置的是Spinner的样式 ， 输入 simple_之后会提示有4人，如果专属spinner的话应该是俩种，在特殊情况可自己定义样式
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        //如果元素改变可以先clear然后再addall()
        adapter.clear();
//        adapter.addAll(spinners);
        adapter.addAll(Arrays.asList("今天","明天","后天"));
        //设置Adapter了
        mBindingView.spinner.setAdapter(adapter);
//        adapter.notifyDataSetChanged();
        mBindingView.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                showToast(adapter.getItem(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private long time = 0;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (System.currentTimeMillis() - time > 2000) {
                time = System.currentTimeMillis();
                showToast("再点击一次退出程序");
            } else {

               /* String application = BASE_ADDRESS;
                Intent intent = new Intent(BASE_ADDRESS);
                intent.putExtra(CLOSE_ALL, 1);
                //发送广播
                sendBroadcast(intent);*/
                finishAffinity();
//                ActivityCollector.finishAll();
            }

            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
