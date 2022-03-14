package com.illyasr.mydempviews.base;

import static com.zhuosen.bilibili.biliplayer.utils.NetWorkUtils.isNetworkAvailable;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

import com.illyasr.mydempviews.MyApplication;
import com.illyasr.mydempviews.R;
import com.illyasr.mydempviews.databinding.ActivityBaseBinding;
import com.illyasr.mydempviews.util.AppUtils;
import com.illyasr.mydempviews.util.GlideCacheUtil;
import com.illyasr.mydempviews.util.GlideUtil;
import com.illyasr.mydempviews.util.SoftInputUtils;
import com.illyasr.mydempviews.util.StatusBarUtil;
import com.illyasr.mydempviews.util.StatusUtils;
import com.illyasr.mydempviews.util.TUtil;
import com.illyasr.mydempviews.view.MProgressDialog;

/**
 *  这套兼容了别人的框架,记得写返回按钮的点击事件 ,不然会直接屏蔽返回按钮的事件的
 * @param <SV>
 * @param <E>
 */
public abstract class BaseTitleActivity<SV extends ViewDataBinding,E extends BasePresenter> extends AppCompatActivity {
    private MyBaseActiviy_Broad oBaseActiviy_Broad;
    public MProgressDialog progressDialog;
    public static final int REQUEST_CODE = 2018;
    public static final String BASE_ADDRESS = AppUtils.getPackageName(MyApplication.getInstance()) + ".BaseTitleActivity";
    protected SV mBindingView;
    protected E mPresenter;
    private ActivityBaseBinding mBaseBinding;

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        //标题栏已经在activity_base 不用到每个布局里面添加
        mBaseBinding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.activity_base, null, false);
        mBindingView = DataBindingUtil.inflate(getLayoutInflater(), layoutResID, null, false);
        mBindingView.setLifecycleOwner(this);
        // content
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mBindingView.getRoot().setLayoutParams(params);
        FrameLayout mContainer = mBaseBinding.container;
        mContainer.addView(mBindingView.getRoot());
        getWindow().setContentView(mBaseBinding.getRoot());

        // 将返回按钮和返回键统一到一起
        mBaseBinding.commonTitle.llLiftBack.setOnClickListener(v -> BaseTitleActivity.this.onBackPressed());
    }



    /**
     * 隐藏标题栏
     */
    protected void hideTitleBar() {
        mBaseBinding.commonTitle.rlTitleBar.setVisibility(View.GONE);
    }


    /**
     * 隐藏返回箭头
     */
    protected void hideBackImg() {
        mBaseBinding.commonTitle.llLiftBack.setVisibility(View.GONE);
    }

    /**
     * 设置标题
     */
    protected void setTitle(String title) {
        mBaseBinding.commonTitle.tvTitle.setText(!TextUtils.isEmpty(title) ? title : "");
    }

    /**
     * 设置右侧文字
     */
    protected void setRightTitle(String rightTitle, View.OnClickListener listener) {
        mBaseBinding.commonTitle.tvRightText.setText(!TextUtils.isEmpty(rightTitle) ? rightTitle : "");
        mBaseBinding.commonTitle.tvRightText.setVisibility(View.VISIBLE);
        mBaseBinding.commonTitle.ivRightImg.setVisibility(View.GONE);
        if (listener != null) {
            mBaseBinding.commonTitle.tvRightText.setOnClickListener(listener);
        }
    }
    /**
     * 设置右侧图片
     */
    protected void setRightImg(Object img, View.OnClickListener listener) {
        mBaseBinding.commonTitle.tvRightText.setVisibility(View.GONE);
        mBaseBinding.commonTitle.ivRightImg.setVisibility(View.VISIBLE);
        GlideUtil.putHttpImg(img,mBaseBinding.commonTitle.ivRightImg);
        if (listener != null) {
            mBaseBinding.commonTitle.ivRightImg.setOnClickListener(listener);
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        Log.e("baseac", "onresume--"+getTopActivity(this));

    }
    //判断当前界面显示的是哪个Activity
    public static String getTopActivity(Context context){
        ActivityManager am = (ActivityManager) context.getSystemService(context.ACTIVITY_SERVICE);
        ComponentName cn = am.getRunningTasks(1).get(0).topActivity;
        Log.d("测试", "pkg:"+cn.getPackageName());//包名
        Log.d("测试", "cls:"+cn.getClassName());//包名加类名
        return cn.getClassName().replace(cn.getPackageName(),"");
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //动态注册广播
        oBaseActiviy_Broad = new MyBaseActiviy_Broad();
        //这里写baseactivity的地址
        IntentFilter intentFilter = new IntentFilter(BASE_ADDRESS);
        registerReceiver(oBaseActiviy_Broad, intentFilter);
        //  沉浸式状态栏
        StatusUtils.transparentStatusBar(this, true);
        StatusBarUtil.setDarkMode(this, true);


        mPresenter = TUtil.getT(this, 1);
        if (mPresenter != null) {
            mPresenter.mContext = this;
        }
        //view_title
        if (!isNetworkAvailable(this)) {
            showToast("网络连接不可用,请检查您的网络!");
        }

        initData();
    }
    /**
     * 控件注册和使用
     */
    protected abstract void initData();

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
     * 在销毁的方法里面注销广播
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        //注销广播
        unregisterReceiver(oBaseActiviy_Broad);
        //clearImageAllCache  清除glide缓存,防止oom
        GlideCacheUtil.getInstance().clearImageAllCache(BaseTitleActivity.this);
        // 这样一般是没用的,推荐按下返回键的时候启动这个方法
        SoftInputUtils.hideSoftInput(this);

        //  销毁的时候解绑present防止oom
        if (mPresenter != null) {
            mPresenter.unDisposable();
            mPresenter.onDestroy();
        }

    }
    /**
     * 显示Toast信息
     */
    public void showToast(String text) {
        Toast.makeText(MyApplication.getInstance(), text, Toast.LENGTH_SHORT).show();
    }
}
