package com.illyasr.mydempviews.base;

/**
 *    fragment监听返回键
 *
 *    必须在fragment依附的activity里面的 onKeyDown  方法里面写入
 *
 *             if ( Fragment!= null && isShow()) //base里面封装了一个是否可见的方法
 *                 ((OnBackListener) Fragment).onBackPressed();
 */
public interface  OnBackListener {
  void onBackPressed();
}
