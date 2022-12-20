package com.illyasr.mydempviews.base;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.widget.Toast;


import androidx.lifecycle.ViewModel;

import com.bigkoo.svprogresshud.SVProgressHUD;
import com.illyasr.mydempviews.MyApplication;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public class BasePresenter extends ViewModel {
    @SuppressLint("StaticFieldLeak")
    public Context mContext;
    @SuppressLint("StaticFieldLeak")
    public View mView;
    /**
     //这是示例数据
     public MutableLiveData<Boolean> its = new MutableLiveData<>();
     public ObservableField<String> psw = new ObservableField<>();
     // 如果需要在xml里面双向绑定, 以前写法是 @{vm.XXX},改成 @={vm.XXX} 就可以双向绑定了,不仅这个,其他的ObservableField<> 也是这种写法
     */




    public void onDestroy() {
        mView=null;
    }
    //将所有正在处理的Subscription都添加到CompositeSubscription中。统一退出的时候注销观察
    private CompositeDisposable mCompositeDisposable;

    /**
     * 将Disposable添加
     *
     * @param subscription
     */
    public void addDisposable(Disposable subscription) {
        //csb 如果解绑了的话添加 sb 需要新的实例否则绑定时无效的
        if (mCompositeDisposable == null || mCompositeDisposable.isDisposed()) {
            mCompositeDisposable = new CompositeDisposable();
        }
        mCompositeDisposable.add(subscription);
    }

    /**
     * 在界面退出等需要解绑观察者的情况下调用此方法统一解绑，防止Rx造成的内存泄漏
     */
    public void unDisposable() {
        if (mCompositeDisposable != null) {
            mCompositeDisposable.dispose();
        }
    }
    /**
     * 显示Toast信息
     */
    public void showToast(String text) {
        Toast.makeText(MyApplication.getInstance(), text, Toast.LENGTH_SHORT).show();
    }
    /**
     * 弹出提示框
     *
     * @param msg
     */
    public SVProgressHUD mSVProgressHUD;
    public void showDialog(String msg) {
        if (mSVProgressHUD == null) {
            mSVProgressHUD = new SVProgressHUD(mContext);
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
        //------
        if (this.mSVProgressHUD != null) {
            if (this.mSVProgressHUD.isShowing()) {
                this.mSVProgressHUD.dismiss();
            }
        }

    }
}