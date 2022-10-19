package com.illyasr.mydempviews.base;

import android.Manifest;
import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;


import com.bigkoo.svprogresshud.SVProgressHUD;
import com.illyasr.mydempviews.MyApplication;
import com.illyasr.mydempviews.R;
import com.illyasr.mydempviews.databinding.ActivityBaseFullscreenBinding;
import com.illyasr.mydempviews.receiver.NetWorkStateReceiver;
import com.illyasr.mydempviews.util.GlideCacheUtil;
import com.illyasr.mydempviews.util.GlideUtil;
import com.illyasr.mydempviews.util.PhoneUtil;
import com.illyasr.mydempviews.util.RxTimerUtil;
import com.illyasr.mydempviews.util.ScreenUtil;
import com.illyasr.mydempviews.util.SoftInputUtils;
import com.illyasr.mydempviews.util.StatusUtils;
import com.illyasr.mydempviews.util.TUtil;
import com.illyasr.mydempviews.view.MProgressDialog;
import com.jeremyliao.liveeventbus.LiveEventBus;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import pub.devrel.easypermissions.EasyPermissions;

/**
 * TODO
 *
 * @author Administrator
 * @version 1.0
 * @date 2022/4/7 11:07
 */
public abstract class BaseFullScreenActivity<SV extends ViewDataBinding,T extends BasePresenter> extends AppCompatActivity {

    MProgressDialog progressDialog;
    public static final int REQUEST_CODE = 2018;
    //    public static final String BASE_ADDRESS = AppUtils.getPackageName(MyApplication.getInstance()) + ".BaseActivity";
    public static final String BASE_ADDRESS =  "com.ahdz.oricalshelves.base.BaseActivity";

    public int statueHeight;

    protected SV mBindingView;
    protected T mPresenter;
    @NonNull
    protected ActivityBaseFullscreenBinding mBaseBinding;
    protected static String TAG = "TAG";
    @Override
    public void setContentView(int layoutResID) {
        StatusUtils.transparentStatusBar(this,true);
//        Immersion();
        super.setContentView(layoutResID);
        mBaseBinding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.activity_base_fullscreen, null, false);
        mBindingView = DataBindingUtil.inflate(getLayoutInflater(), layoutResID, null, false);
        mBindingView.setLifecycleOwner(this);
        // content
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mBindingView.getRoot().setLayoutParams(params);
        FrameLayout mContainer = mBaseBinding.container;
        mContainer.addView(mBindingView.getRoot());

//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);// 是否显示状态栏(全屏好看)

        getWindow().setContentView(mBaseBinding.getRoot());

        mBaseBinding.commonTitle.llBack.setOnClickListener(v -> {
//            BaseActivity.this.onBackPressed();
            onBackClick();
//            if (onClickIt != null) {
//                onClickIt.onBack();
//            }
        });


    }



    public interface OnClickIt {
        //        void onRightText();
        void onRightImg();
    }
    public interface OnClickText {
        void onRightText();
    }

    public void setParentBg(int color) {
        mBaseBinding.rlRootView.setBackgroundColor(color);
    }

    public void tohideTitleBar(boolean is) {
        mBaseBinding.commonTitle.viewTitle.setVisibility(!is? View.VISIBLE:View.GONE);
    }


    public void setTitle(String title) {
        mBaseBinding.commonTitle.tvTitle.setText(title+"");
    }

    public void setRightImg(Object img, OnClickIt onClick) {

        mBaseBinding.commonTitle.imgRight.setVisibility(img==null?View.GONE: View.VISIBLE);
        GlideUtil.putHttpImg(img,mBaseBinding.commonTitle.imgRight);
        mBaseBinding.commonTitle.imgRight.setOnClickListener(v -> {
            if (onClick != null) {
                onClick.onRightImg();
            }
        });
    }

    public void setRightText(String title, BaseActivity.OnClickText onClickIt) {
        mBaseBinding.commonTitle.tvRight.setVisibility(TextUtils.isEmpty(title)?View.GONE:View.VISIBLE);
        mBaseBinding.commonTitle.tvRight.setText(title+"");
        // OnClickText
        mBaseBinding.commonTitle.tvRight.setOnClickListener(v -> {
            if (onClickIt != null) {
                onClickIt.onRightText();
            }
        });
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //  沉浸式状态栏
        StatusUtils.transparentStatusBar(this, true);
//        Immersion();
        setContentView(setLayoutId());
        /**
         mBaseBinding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.activity_base, null, false);
         mBindingView = DataBindingUtil.inflate(getLayoutInflater(), layoutResID, null, false);
         */

//        mBindingView = DataBindingUtil.setContentView(this, setLayoutId());
//        mBindingView.setLifecycleOwner(this);

        mPresenter = TUtil.getT(this, 1);
        if (mPresenter != null) {
            mPresenter.mContext = this;
        }

        // 获取状态栏高度
        statueHeight = ScreenUtil.getStatueBarHeight(BaseFullScreenActivity.this);
        //view_title
        if (isNetworkAvailable(this) == false) {
            showToast("网络连接不可用,请检查您的网络!");
        }

        initData();

        StringBuffer sb = new StringBuffer();
        sb.append("手机型号 : "+ PhoneUtil.getSystemModel()+"\n");
        sb.append("手机系统语言 : "+ PhoneUtil.getSystemLanguage()+"\n");
        sb.append("手机系统版本号 : "+ PhoneUtil.getSystemVersion()+"\n");
        sb.append("手机厂商 : "+ PhoneUtil.getDeviceBrand()+"\n");
//        sb.append("手机网络状态 : "+ getNetworkTypeName(MyApplication.getInstance()) +"\n");
        Log.e(TAG, ""+sb.toString());

    }

    private void SetOffLine(final AppCompatActivity activity) {
        LiveEventBus.get().with("SetOffLine", Boolean.class).observe(activity, aBoolean -> {
            if (aBoolean) {
                Intent intent = new Intent(BASE_ADDRESS);
                intent.putExtra(CLOSE_ALL, 1);
                //发送广播
                sendBroadcast(intent);
//                startActivity(new Intent(activity,LoginActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                finish();
            }
        });
    }




    /**
     * Sets layout id.
     *
     * @return the layout id
     */
    protected abstract int setLayoutId();

    /**
     * 适配6.0沉浸式状态栏操作
     * 保险起见直接粘贴,或者直接提取一个方法也可以
     */
    public  void Immersion(){
        /*ImmersionBar.with(this)
                .transparentStatusBar()  //透明状态栏，不写默认透明色
                .transparentNavigationBar()  //透明导航栏，不写默认黑色(设置此方法，fullScreen()方法自动为true)
                .transparentBar()             //透明状态栏和导航栏，不写默认状态栏为透明色，导航栏为黑色（设置此方法，fullScreen()方法自动为true）
                .statusBarColor(R.color.transparent)     //状态栏颜色，不写默认透明色
                .navigationBarColor(R.color.black) //导航栏颜色，不写默认黑色
//                .barColor(R.color.colorPrimary)  //同时自定义状态栏和导航栏颜色，不写默认状态栏为透明色，导航栏为黑色
//                .statusBarAlpha(0.3f)  //状态栏透明度，不写默认0.0f
//                .navigationBarAlpha(0.1f)  //导航栏透明度，不写默认0.0F
                .barAlpha(0.5f)  //状态栏和导航栏透明度，不写默认0.0f
//                .statusBarDarkFont(true)   //状态栏字体是深色，不写默认为亮色
                .statusBarDarkFont(false)   //状态栏字体是深色，不写默认为亮色(false)
//                .fullScreen(true)      //有导航栏的情况下，activity全屏显示，也就是activity最下面被导航栏覆盖，不写默认非全屏
//                  FLAG_HIDE_BAR,  //隐藏状态栏和导航栏
//                FLAG_SHOW_BAR  //显示状态栏和导航栏
//                .hideBar(BarHide.FLAG_HIDE_BAR)  //隐藏状态栏或导航栏或两者，不写默认不隐藏
//                .addViewSupportTransformColor(toolbar)  //设置支持view变色，可以添加多个view，不指定颜色，默认和状态栏同色，还有两个重载方法
//                .titleBarMarginTop(toolbar)     //解决状态栏和布局重叠问题，任选其一,这个方法在我的手机上可以实现,但是明明设置了颜色却显示灰色
                .fitsSystemWindows(true)    //解决状态栏和布局重叠问题,但是statuebar颜色总是和colorpramirydark一致,这个无法解决
                .init();*/
//        ImmersionBar.with(this)
//                .statusBarDarkFont(true)   //状态栏字体是深色，不写默认为亮色
//                .init();
    }


    /**
     * 控件注册和使用
     */
    protected abstract void initData();

    /**
     * 返回按钮和左上角返回
     */
    protected abstract void onBackClick();


    BaseActivity.CheckPermissionsListener mListener;
    interface CheckPermissionsListener {
        void onGranted();
        void onDenied(List<String> permissions);
    }
    @Override
    /**
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_CODE:
                List<String> deniedPermissions = new ArrayList<>();
                int length = grantResults.length;
                for (int i = 0; i < length; i++) {
                    if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                        //该权限被拒绝了
                        deniedPermissions.add(permissions[i]);
                    }
                }
                if (deniedPermissions.size() > 0) {
                    mListener.onDenied(deniedPermissions);
                } else {
                    mListener.onGranted();
                }
                break;
            default:
                break;
        }
    }

    /**
     *   申请权限 (6.0专用)
     */
    public void newPromession() {
        ContentValues values;
        String[] messions = new String[]{
//                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.SYSTEM_ALERT_WINDOW,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CALL_PHONE};
        if (!EasyPermissions.hasPermissions(this, messions)) {
            EasyPermissions.requestPermissions(this, "请打开相关权限,以保证您可以正常使用app!", 100, messions);
        }
    }
    /**
     * 检查当前网络是否可用
     * @param
     * @return
     */
    public boolean isNetworkAvailable(Context activity){
        // 获取手机所有连接管理对象（包括对wi-fi,net等连接的管理）
        ConnectivityManager connectivityManager = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager == null){
            return false;
        }else{
            // 获取NetworkInfo对象
            NetworkInfo[] networkInfo = connectivityManager.getAllNetworkInfo();

            if (networkInfo != null && networkInfo.length > 0){
                for (int i = 0; i < networkInfo.length; i++){
                    Log.e("TAG",i + "===状态===" + networkInfo[i].getState());
                    Log.e("TAG", i + "===类型===" + networkInfo[i].getTypeName());
                    // 判断当前网络状态是否为连接状态
                    if (networkInfo[i].getState() == NetworkInfo.State.CONNECTED){
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * 判断网络状态
     */
    NetWorkStateReceiver netWorkStateReceiver;
    /**
     * 判断是否有网络连接
     */
    public boolean isNetworkConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }
        return false;
    }
    /**
     *  判断WIFI网络是否可用
     */
    public boolean isWifiConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mWiFiNetworkInfo = mConnectivityManager
                    .getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            if (mWiFiNetworkInfo != null) {
                return mWiFiNetworkInfo.isAvailable();
            }
        }
        return false;
    }
    /**
     * 判断MOBILE网络是否可用
     */
    public boolean isMobileConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mMobileNetworkInfo = mConnectivityManager
                    .getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            if (mMobileNetworkInfo != null) {
                return mMobileNetworkInfo.isAvailable();
            }
        }
        return false;
    }
    /**
     * 获取当前网络连接的类型信息
     */
    public static int getConnectedType(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null && mNetworkInfo.isAvailable()) {
                return mNetworkInfo.getType();
            }
        }
        return -1;
    }
    /**
     * 有时候我们连接上一个没有外网连接的WiFi或者有线就会出现这种极端的情况，
     * 目前Android SDK还不能识别这种情况，一般的解决办法就是ping一个外网。
     */
    /* @author suncat
     * @category 判断是否有外网连接（普通方法不能判断外网的网络是否连接，比如连接上局域网）
     * @return
     */
    public static final boolean ping() {
        String result = null;
        try {
            String ip = "www.baidu.com";// ping 的地址，可以换成任何一种可靠的外网
            Process p = Runtime.getRuntime().exec("ping -c 3 -w 100 " + ip);// ping网址3次
            // 读取ping的内容，可以不加
            InputStream input = p.getInputStream();
            BufferedReader in = new BufferedReader(new InputStreamReader(input));
            StringBuffer stringBuffer = new StringBuffer();
            String content = "";
            while ((content = in.readLine()) != null) {
                stringBuffer.append(content);
            }
            Log.d("------ping-----", "result content : " + stringBuffer.toString());
            // ping的状态
            int status = p.waitFor();
            if (status == 0) {
                result = "success";
                return true;
            } else {
                result = "failed";
            }
        } catch (IOException e) {
            result = "IOException";
        } catch (InterruptedException e) {
            result = "InterruptedException";
        } finally {
            Log.d("----result---", "result = " + result);
        }
        return false;
    }

    /**
     * 在onResume()方法注册
     */
    @Override
    protected void onResume() {
        if (netWorkStateReceiver == null) {
            netWorkStateReceiver = new NetWorkStateReceiver();
        }
        IntentFilter filter = new IntentFilter();
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        super.onResume();
        Log.e("baseac", "onresume--"+getTopActivity(this));
    }
    //判断当前界面显示的是哪个Activity
    public static String getTopActivity(Context context){

        ActivityManager am = (ActivityManager) context.getSystemService(context.ACTIVITY_SERVICE);
        //  //获取当前栈顶的activity
        //        ComponentName currentActivityName = activityManager.getRunningTasks(1).get(0).topActivity;
        ComponentName cn = am.getRunningTasks(1).get(0).topActivity;
//        Log.d("测试", "pkg:"+cn.getPackageName());//包名
        Log.e(TAG+"测试", "cls:"+cn.getClassName());//包名加类名
        return cn.getClassName().replace(cn.getPackageName(),"");
    }
    /**
     * onPause()方法注销
     */
    @Override
    protected void onPause() {
        super.onPause();
    }

    /**
     * 在销毁的方法里面注销广播
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        //clearImageAllCache  清除glide缓存,防止oom
        GlideCacheUtil.getInstance().clearImageAllCache(this);
        // 这样一般是没用的,推荐按下返回键的时候启动这个方法
        SoftInputUtils.hideSoftInput(this);
        RxTimerUtil.cancel();

        //  销毁的时候解绑present防止oom
   /*     if (mPresenter != null) {
            mPresenter.unDisposable();
            mPresenter.onDestroy();
        }*/

    }

    /**
     * 定义一个广播
     */
    public static final String CLOSE_ALL = "closeAll";
    public class MyBaseActiviy_Broad extends BroadcastReceiver {
        @Override
        public void onReceive(Context arg0, Intent intent) {
            //接收发送过来的广播内容
            int closeAll = intent.getIntExtra(CLOSE_ALL, 0);
            if (closeAll == 1) {
                finish();//销毁BaseActivity
            }
        }
    }
    /**
     * 弹出提示框
     */
    public MProgressDialog showDialog() {
        if (progressDialog == null) {
            progressDialog = new MProgressDialog(this)
                    .createLoadingDialog("加载中");
            progressDialog.show();
        } else if (!progressDialog.isShowing()) {
            progressDialog.show();
        }
        return progressDialog;
    }


    /**
     * 弹出提示框
     *
     * @param msg
     */
    public SVProgressHUD mSVProgressHUD;
    public void showDialog(String msg) {
        if (mSVProgressHUD == null) {
            mSVProgressHUD = new SVProgressHUD(this);
            mSVProgressHUD.showWithStatus(msg);
//            mSVProgressHUD.show();
        }  else if (!mSVProgressHUD.isShowing()) {
            mSVProgressHUD.showWithStatus(msg);
        }
    }

    /**
     * 关闭提示框
     */
    public void dismissDialog() {
        if (this.progressDialog != null) {
            if (this.progressDialog.isShowing()) {
                this.progressDialog.dismiss();
            }
            this.progressDialog = null;
        }

        //------
        if (this.mSVProgressHUD != null) {
            if (this.mSVProgressHUD.isShowing()) {
                this.mSVProgressHUD.dismiss();
            }
        }

    }

    /**
     * 显示Toast信息
     */
    private long toastTime = 0;
    public void showToast(String text) {
        if (System.currentTimeMillis()-toastTime >2000){
            Toast.makeText(MyApplication.getInstance(), text, Toast.LENGTH_SHORT).show();
        }
        toastTime = System.currentTimeMillis();
    }

    /**
     *    控件显示和隐藏
     */
    public void isViewShow(View view, int zes) {
        if (zes > 0) {
            view.setVisibility(View.VISIBLE);
        } else {
            view.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            onBackClick();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private long lastClickTime = 0;
    //设置拦截的时间间隔 500毫秒
    private final long RESTRICT_TIME = 1500;

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        /**
         * 拦截两次时间差小于RESTRICT_TIME
         */
      /*  if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            if (isFrequentlyClick()) {
                //可以在这里给点提示
                showToast("不要频繁点击！");
                lastClickTime = System.currentTimeMillis();
                return true;
            }
            lastClickTime = System.currentTimeMillis();
        }*/
        return super.dispatchTouchEvent(ev);
    }
    public boolean isFrequentlyClick() {
        long clickTime = System.currentTimeMillis();
        long value = clickTime - lastClickTime;
        lastClickTime = clickTime;
        return value <= RESTRICT_TIME;
    }
}
