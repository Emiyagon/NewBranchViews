package com.illyasr.mydempviews.ui.activity.qq;


import androidx.annotation.Nullable;

import java.util.NoSuchElementException;

/**
 * 描述:
 * 作者:LF
 * 创建日期: 2021/4/24 18:23
 * 备注:
 */
public class OptionalObj<M> {
    private final M optionalObj; // 接收到的返回结果

    public OptionalObj(@Nullable M optionalObj) {
        this.optionalObj = optionalObj;
    }
    // 判断返回结果是否为null
    public boolean isEmpty() {
        return this.optionalObj == null;
    }
    // 获取不能为null的返回结果，如果为null，直接抛异常，经过二次封装之后，这个异常最终可以在走向RxJava的onError()
    public M get() {
        if (optionalObj == null) {
            throw new NoSuchElementException("没有值");
        }
        return optionalObj;
    }
    // 获取可以为null的返回结果
    public M getIncludeNull() {
        return optionalObj;
    }
}
