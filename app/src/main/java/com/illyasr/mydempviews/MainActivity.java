package com.illyasr.mydempviews;

import android.Manifest;
import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.illyasr.mydempviews.adapter.MainAdapter;
import com.illyasr.mydempviews.base.ActivityCollector;
import com.illyasr.mydempviews.base.BaseActivity;
import com.illyasr.mydempviews.bean.TabBean;
import com.illyasr.mydempviews.databinding.ActivityMainBinding;
import com.illyasr.mydempviews.databinding.DialogCityBinding;
import com.illyasr.mydempviews.phone.PhoneActivity;
import com.illyasr.mydempviews.ui.activity.GetVideoActivity;
import com.illyasr.mydempviews.ui.activity.HealthyActivity;
import com.illyasr.mydempviews.ui.activity.HeartActivity;
import com.illyasr.mydempviews.ui.activity.MainActivityText;
import com.illyasr.mydempviews.ui.activity.MyLocationActivity;
import com.illyasr.mydempviews.ui.activity.QrCodeActivity;
import com.illyasr.mydempviews.ui.activity.qq.TensentActivity;
import com.illyasr.mydempviews.util.AppUtils;
import com.illyasr.mydempviews.util.Utils;
import com.illyasr.mydempviews.view.ComPopupDialog;
import com.illyasr.mydempviews.view.dialog.CityDialog;

import java.util.ArrayList;
import java.util.List;

import pub.devrel.easypermissions.EasyPermissions;

//广告业
public class MainActivity extends BaseActivity<ActivityMainBinding,MainPresent> {

    private MainAdapter adapter;
    private List<TabBean> list = new ArrayList<>();
    @Override
    protected int setLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initData() {

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


        list.add(new TabBean("获取定位",0));
        list.add(new TabBean("撩妹神器",1));
        list.add(new TabBean("SpView",2));
        list.add(new TabBean("获取视频并且下载",3));
        list.add(new TabBean("获取通讯录列表",4));
        list.add(new TabBean("心电折线图(堆叠)",5));
        list.add(new TabBean("模拟qq侧滑菜单一",6));
        list.add(new TabBean("城市三级联动选择器",7));
        list.add(new TabBean("zxing二维码",8));
        list.add(new TabBean("健康",9));


//        rvAlbums.setLayoutManager(new GridLayoutManager(this,3));
        adapter = new MainAdapter(this,list);
        mBindingView.rvAlbums.setAdapter(adapter);
        adapter.setOnRem((pos, type) -> {
            switch (pos) {
                case 0:// 获取定位
                    startActivity(new Intent(MainActivity.this, MyLocationActivity.class));
                    break;
                case 1:// 高能弹幕
                    startActivity(new Intent(MainActivity.this, MainActivityText.class));
                    break;
                case 2:// 高能弹幕
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
                case 3:

                    // 从API11开始android推荐使用android.content.ClipboardManager
                    // 为了兼容低版本我们这里使用旧版的android.text.ClipboardManager，虽然提示deprecated，但不影响使用。
                    ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                    // 将文本内容放到系统剪贴板里。
//                    cm.setText(videoUrl2);
                    startActivity(new Intent(MainActivity.this, GetVideoActivity.class));
                    break;
                case 4:
                    startActivity(new Intent(MainActivity.this, PhoneActivity.class));
                    break;
                case 5:
                    break;
                case 6:
                    startActivity(new Intent(MainActivity.this, TensentActivity.class));
                    break;
                case 7:
                    new CityDialog(this)
                            .setGetFinger(false)
                            .show();
                    break;
                case 8:
                    startActivity(new Intent(MainActivity.this, QrCodeActivity.class));
                    break;
                case 9:
                    startActivity(new Intent(MainActivity.this, HealthyActivity.class));
                    break;
                default:
                    break;
            }

        });
    }

    private long time = 0;
/*    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (System.currentTimeMillis() - time > 2000) {
            time = System.currentTimeMillis();
            showToast("再点击一次退出程序");
        } else {
//                Intent intent = new Intent(BASE_ADDRESS);
            String application = AppUtils.getPackageName(MyApplication.getInstance())+".base.BaseActivity";
            Intent intent = new Intent(application);
            intent.putExtra(CLOSE_ALL, 1);
            //发送广播 ActivityCollector.java
            sendBroadcast(intent);
        }
    }*/

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
