package com.illyasr.mydempviews.ui.activity.qq;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.widget.ImageView;

import com.illyasr.mydempviews.MainPresent;
import com.illyasr.mydempviews.R;
import com.illyasr.mydempviews.base.BaseActivity;
import com.illyasr.mydempviews.databinding.ActivityTensentBinding;
import com.illyasr.mydempviews.http.BaseResponse;
import com.illyasr.mydempviews.http.RetrofitUtils;
import com.illyasr.mydempviews.http.UserApi;
import com.illyasr.mydempviews.util.GlideUtil;
import com.illyasr.mydempviews.util.LocationUtil;
import com.illyasr.mydempviews.util.RxTimerUtil;
import com.illyasr.mydempviews.util.StatusBarUtil;
import com.illyasr.mydempviews.util.StatusUtils;
import com.jeremyliao.liveeventbus.LiveEventBus;

import java.util.HashMap;
import java.util.List;
import java.util.Observable;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;

public class TensentActivity extends BaseActivity<ActivityTensentBinding, MainPresent> {

    @Override
    protected int setLayoutId() {
        return R.layout.activity_tensent;
    }

    @Override
    protected void initData() {

        //  沉浸式状态栏
        StatusUtils.transparentStatusBar(this, true);
        StatusBarUtil.setDarkMode(this, true);

        ImageView main = new ImageView(this);
        ImageView draw = new ImageView(this);

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.add(R.id.frame_main,new TensentFragment());
        ft.commit();

        GlideUtil.putHttpImg(R.mipmap.drawable,draw);
        GlideUtil.putHttpImg(R.mipmap.mainmenu,main);
//        mBindingView.frameDraw.addView(draw);
//        mBindingView.frameMain.addView(main);
//        mBindingView.frameMain



        LiveEventBus.get().with("tensent",Integer.class).observe(this, integer -> {
            Log.e("TAG", "接收到了2");
            mBindingView.rlPe.openDrawer(Gravity.LEFT);
        });


        RxTimerUtil.test(1000, aLong -> new OptionalObj(new Location("浙江")), it -> {
            OptionalObj obj = (OptionalObj) it;
        });


        UserApi.getProtocolList(new HashMap<>(), new Observer<BaseResponse<List<String>>>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }

            @Override
            public void onNext(@NonNull BaseResponse<List<String>> it) {
                //  it就是返回的网络请求,里面有我需要传递下去的数据
               String oc =  it.getData().get(7);
            }

            @Override
            public void onError(@NonNull Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });
    }
}