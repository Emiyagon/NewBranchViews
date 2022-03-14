package com.illyasr.mydempviews.http;

import android.content.Context;

import com.bigkoo.svprogresshud.SVProgressHUD;
import com.illyasr.mydempviews.R;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public abstract class BaseObserver<T> implements Observer<T> {

    private SVProgressHUD mProgressDialog;//进度窗体

    public BaseObserver() {
    }

    /**
     * 传入Context则显示进度条
     */
    public BaseObserver(Context mContext) {
        showInfoProgressDialog(mContext, mContext.getResources().getString(R.string.network_load));
    }

    @Override
    public void onComplete() {
        onFinish();
        onBaseComplete();
    }

    @Override
    public void onError(Throwable e) {
        onFinish();
        onBaseError(e);
    }

    @Override
    public void onNext(T t) {
        onFinish();
        onSuccess(t);

    }

    public abstract void onSuccess(T t);

    public abstract void onBaseComplete();

    public abstract void onBaseError(Throwable e);

    public abstract void onBaseSubscribe(Disposable d);

    @Override
    public void onSubscribe(Disposable d) {

        onBaseSubscribe(d);
    }

    /**
     * 隐藏等待条
     */
    private void hideInfoProgressDialog() {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
            mProgressDialog = null;
        }
    }

    /**
     * 显示等待条
     */
    private void showInfoProgressDialog(Context context, final String str) {
        if (mProgressDialog == null) {
            mProgressDialog = new SVProgressHUD(context) ;
//            mProgressDialog.setCancellable(false);
        }

        mProgressDialog.showWithStatus(str);

        if (!mProgressDialog.isShowing()) {
            mProgressDialog.show();
        }
    }

    /**
     * 当请求完成时调用（无论成功或失败）
     */
    public void onFinish() {
        //如果没有加入进度条操作可以不调用super
        hideInfoProgressDialog();
    }
}
