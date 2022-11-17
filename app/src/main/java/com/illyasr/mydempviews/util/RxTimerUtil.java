package com.illyasr.mydempviews.util;


import android.app.Activity;
import android.content.Context;

import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import cn.nekocode.rxlifecycle.RxLifecycle;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;

/**
 * Instruction:Rxjava2.x实现定时器
 * <p>
 * Author:pei
 * Date: 2017/6/29
 * Description:
 */

public class RxTimerUtil {


    private static Disposable mDisposable;

    /** milliseconds毫秒后执行next操作
     *
     * @param milliseconds
     * @param next
     */
    public static void timer( long milliseconds, final IRxNext next) {
        Observable.timer(milliseconds, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
//                .compose(context.<Long>bindUntilEvent(ActivityEvent.PAUSE))
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable disposable) {
                        mDisposable=disposable;
                    }

                    @Override
                    public void onNext(@NonNull Long number) {
                        if(next!=null){
                            next.doNext(number);
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        //取消订阅
                        cancel();
                    }

                    @Override
                    public void onComplete() {
                        //取消订阅
                        cancel();
                    }
                });
    }


    public interface  IRxNextObj{
        void doNext(Object number);
    }
    public static  <T> void test(long million, Function<Long, T> function , IRxNextObj next) {
        Observable.interval(0,million, TimeUnit.MILLISECONDS)
        .subscribeOn(AndroidSchedulers.mainThread())
        .map(function)
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Observer<T>() {
            @Override
            public void onSubscribe(@NonNull Disposable disposable) {
                mDisposable=disposable;
            }

            @Override
            public void onNext(@NonNull T t) {
                if (next != null) {
                    next.doNext(t);
                }
            }

            @Override
            public void onError(@NonNull Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        })
        ;
    }

    /** 每隔milliseconds毫秒后执行next操作
     *
     * @param milliseconds
     * @param next
     */
    public static void interval(long milliseconds,final IRxNext next){
        Observable.interval(milliseconds, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable disposable) {
                        mDisposable=disposable;
                    }

                    @Override
                    public void onNext(@NonNull Long number) {
                        if(next!=null){
                            next.doNext(number);//  这里返回的是秒,不是毫秒
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }


    /**
     * 取消订阅
     */
    public static void cancel(){
        if(mDisposable!=null&&!mDisposable.isDisposed()){
            mDisposable.dispose();
            LogUtil.e("====定时器取消======");
        }
    }

    public interface IRxNext{
        void doNext(long number);
    }
}