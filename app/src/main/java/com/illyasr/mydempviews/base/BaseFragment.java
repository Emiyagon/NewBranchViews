package com.illyasr.mydempviews.base;


import android.app.Activity;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;


import com.illyasr.mydempviews.MyApplication;
import com.illyasr.mydempviews.R;
import com.illyasr.mydempviews.databinding.FragmentBaseBinding;
import com.illyasr.mydempviews.util.ClickUtils;
import com.illyasr.mydempviews.util.GlideCacheUtil;
import com.illyasr.mydempviews.util.SpannableUtil;
import com.illyasr.mydempviews.util.TUtil;
import com.illyasr.mydempviews.view.MProgressDialog;

import java.util.Calendar;

import rx.subscriptions.CompositeSubscription;

/**
 * Created by dasu on 2016/9/27.
 * <p>
 * Fragment基类，封装了懒加载的实现
 * <p>
 * 1、Viewpager + Fragment情况下，fragment的生命周期因Viewpager的缓存机制而失去了具体意义
 * 该抽象类自定义新的回调方法，当fragment可见状态改变时会触发的回调方法，和 Fragment 第一次可见时会回调的方法
 *
 * @see #onFragmentVisibleChange(boolean)
 * @see #onFragmentFirstVisible()
 */
public abstract class BaseFragment<SV extends ViewDataBinding,T extends BasePresenter> extends Fragment {

  public static final String TAG = BaseFragment.class.getSimpleName();

  private boolean isFragmentVisible;
  private boolean isReuseView;
  private boolean isFirstVisible;
  private View rootView;

  protected View mRootView;
  protected Activity mActivity;
  public Calendar mCalendar = Calendar.getInstance();
  /**
   * 是否对用户可见
   */
  protected boolean mIsVisible;
  /**
   * 是否加载完成
   * 当执行完onViewCreated方法后即为true
   */
  protected boolean mIsPrepare;

  /**
   * 是否加载完成
   * 当执行完onViewCreated方法后即为true
   */
  protected boolean mIsImmersion;
  //setUserVisibleHint()在Fragment创建时会先被调用一次，传入isVisibleToUser = false
  //如果当前Fragment可见，那么setUserVisibleHint()会再次被调用一次，传入isVisibleToUser = true
  //如果Fragment从可见->不可见，那么setUserVisibleHint()也会被调用，传入isVisibleToUser = false
  //总结：setUserVisibleHint()除了Fragment的可见状态发生变化时会被回调外，在new Fragment()时也会被回调
  //如果我们需要在 Fragment 可见与不可见时干点事，用这个的话就会有多余的回调了，那么就需要重新封装一个
  protected boolean isInit = false;//视图是否已经初始化
  private boolean isLoad = false;//是否开始加载数据
  private boolean isRepeat = false;//是否允许重复加载当前frament页面数据
  //解决fragment奔溃时重叠问题
  private final String isFragmentHide = "STATE_SAVE_IS_HIDDEN";


  /**
   * 新版设置view远离刘海屏的方法
   */
  public void setRHairBuffer(final View LLTop) {
    LLTop.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
      @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
      @Override
      public void onGlobalLayout() {
        LLTop.getViewTreeObserver().removeOnGlobalLayoutListener(this);
        Rect rect = new Rect();
        getView().getWindowVisibleDisplayFrame(rect);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LLTop.getLayoutParams());
        params.setMargins(0, rect.top, 0, 0);
        LLTop.setLayoutParams(params);
      }
    });
  }

  public void setLHairBuffer(final View LLTop) {
    LLTop.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
      @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
      @Override
      public void onGlobalLayout() {
        LLTop.getViewTreeObserver().removeOnGlobalLayoutListener(this);
        Rect rect = new Rect();
        getView().getWindowVisibleDisplayFrame(rect);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LLTop.getLayoutParams());
        params.setMargins(0, rect.top, 0, 0);
        LLTop.setLayoutParams(params);
      }
    });
  }
  @Override
  public void setUserVisibleHint(boolean isVisibleToUser) {
    super.setUserVisibleHint(isVisibleToUser);
    //setUserVisibleHint()有可能在fragment的生命周期外被调用
    if (rootView == null) {
      return;
    }
    if (isFirstVisible && isVisibleToUser) {
      onFragmentFirstVisible();
      isFirstVisible = false;
    }
    if (isVisibleToUser) {
      onFragmentVisibleChange(true);
      isFragmentVisible = true;
      return;
    }
    if (isFragmentVisible) {
      isFragmentVisible = false;
      onFragmentVisibleChange(false);
    }

    super.setUserVisibleHint(isVisibleToUser);

    if (getUserVisibleHint()) {
      mIsVisible = true;
      onVisible();
    } else {
      mIsVisible = false;
      onInvisible();
    }

  }

  /**
   * 用户不可见执行
   */
  protected void onInvisible() {

  }
  @Override
  public void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
    //当发生保存时，额外保存当前的fragment的显示状态
    outState.putBoolean(isFragmentHide, isHidden());
  }

  @Override
  public void onAttach(Activity context) {
    super.onAttach(context);
    mActivity = (Activity) context;
  }
  public T mPresenter;
  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    initVariable();
    //当恢复时，让app崩溃时处于隐藏状态的fragment恢复隐藏，处于显示状态的继续显示
    mPresenter = TUtil.getT(this, 1);
    if (savedInstanceState != null) {
      boolean isHidden = savedInstanceState.getBoolean(isFragmentHide);
      FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
      if (isHidden) {
        //如果已经隐藏,则就隐藏
        transaction.hide(this);
      } else {
        transaction.show(this);
      }
      transaction.commit();
    }
  }
  protected FragmentBaseBinding mBaseBinding;
  //布局view
  protected SV mBindingView;
  private CompositeSubscription mCompositeSubscription;//网络请求观察者
  // 内容布局
  protected FrameLayout mContainer;

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    mBaseBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_base, container, false);
    mBindingView = DataBindingUtil.inflate(getActivity().getLayoutInflater(), setContent(), null, false);
    mBindingView.setLifecycleOwner(this);
    mContainer = mBaseBinding.container;
    mContainer.addView(mBindingView.getRoot());
    return mBaseBinding.getRoot();
  }
  /**
   * 布局
   */
  public abstract int setContent();
  @Override
  public void onActivityCreated(@Nullable Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
    initView();


  }

  protected abstract void initView();


  @Override
  public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    //如果setUserVisibleHint()在rootView创建前调用时，那么
    //就等到rootView创建完后才回调onFragmentVisibleChange(true)
    //保证onFragmentVisibleChange()的回调发生在rootView创建完成之后，以便支持ui操作
    if (rootView == null) {
      rootView = view;
      if (getUserVisibleHint()) {
        if (isFirstVisible) {
          onFragmentFirstVisible();
          isFirstVisible = false;
        }
        onFragmentVisibleChange(true);
        isFragmentVisible = true;
      }
    }
    super.onViewCreated(isReuseView ? rootView : view, savedInstanceState);
    if (isLazyLoad()) {
      mIsPrepare = true;
      mIsImmersion = true;
      onLazyLoad();
    } else {
      initData();
      if (isImmersionBarEnabled()) {
        initImmersionBar();
      }
    }
  }

  /**
   * 是否在Fragment使用沉浸式
   *
   * @return the boolean
   */
  protected boolean isImmersionBarEnabled() {
    return true;
  }

  /**
   * 初始化沉浸式
   */
  protected void initImmersionBar() {
//        mImmersionBar = ImmersionBar.with(this);
//        mImmersionBar.keyboardEnable(true).navigationBarWithKitkatEnable(false).init();
  }

  /**
   * 初始化数据
   */
  protected void initData() {

  }

  /**
   * 是否懒加载
   *
   * @return the boolean
   */
  protected boolean isLazyLoad() {
    return true;
  }

  /**
   * 用户可见时执行的操作
   */
  protected void onVisible() {
    onLazyLoad();
  }

  private void onLazyLoad() {
    if (mIsVisible && mIsPrepare) {
      mIsPrepare = false;
      initData();
    }
    if (mIsVisible && mIsImmersion && isImmersionBarEnabled()) {
      initImmersionBar();
    }
  }


  @Override
  public void onDestroyView() {
    super.onDestroyView();
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    initVariable();
//        GlideCacheUtil.getInstance().clearImageAllCache(getContext());
    GlideCacheUtil.getInstance().clearImageAllCache(getActivity());
  }

  private void initVariable() {
    isFirstVisible = true;
    isFragmentVisible = false;
    rootView = null;
    isReuseView = true;
  }

  /**
   * 设置是否使用 view 的复用，默认开启
   * view 的复用是指，ViewPager 在销毁和重建 Fragment 时会不断调用 onCreateView() -> onDestroyView()
   * 之间的生命函数，这样可能会出现重复创建 view 的情况，导致界面上显示多个相同的 Fragment
   * view 的复用其实就是指保存第一次创建的 view，后面再 onCreateView() 时直接返回第一次创建的 view
   *
   * @param isReuse
   */
  protected void reuseView(boolean isReuse) {
    isReuseView = isReuse;
  }

  /**
   * 去除setUserVisibleHint()多余的回调场景，保证只有当fragment可见状态发生变化时才回调
   * 回调时机在view创建完后，所以支持ui操作，解决在setUserVisibleHint()里进行ui操作有可能报null异常的问题
   * <p>
   * 可在该回调方法里进行一些ui显示与隐藏，比如加载框的显示和隐藏
   *
   * @param isVisible true  不可见 -> 可见
   *                  false 可见  -> 不可见
   */
  protected void onFragmentVisibleChange(boolean isVisible) {


  }

  /**
   * 在fragment首次可见时回调，可在这里进行加载数据，保证只在第一次打开Fragment时才会加载数据，
   * 这样就可以防止每次进入都重复加载数据
   * 该方法会在 onFragmentVisibleChange() 之前调用，所以第一次打开时，可以用一个全局变量表示数据下载状态，
   * 然后在该方法内将状态设置为下载状态，接着去执行下载的任务
   * 最后在 onFragmentVisibleChange() 里根据数据下载状态来控制下载进度ui控件的显示与隐藏
   */
  protected void onFragmentFirstVisible() {

  }

  protected boolean isFragmentVisible() {
    return isFragmentVisible;
  }

  /**
   * 找到activity的控件
   *
   * @param <T> the type parameter
   * @param id  the id
   * @return the t
   */
  @SuppressWarnings("unchecked")
  protected <T extends View> T findActivityViewById(@IdRes int id) {
    return (T) mActivity.findViewById(id);
  }

  public void makeFrToast(String str) {
    Toast.makeText(getContext(), str, Toast.LENGTH_SHORT).show();
  }

  /**
   * 弹出提示框
   *
   * @param msg
   */
  MProgressDialog progressDialog;

  public MProgressDialog showDialog(String msg) {
    if (progressDialog == null) {
      progressDialog = new MProgressDialog(getActivity());
      progressDialog = progressDialog.createLoadingDialog(msg);
      progressDialog.show();
    } else if (!progressDialog.isShowing()) {
      progressDialog.show();
    }
    return progressDialog;
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

  }

  public void showToast(String s) {
    Toast.makeText(MyApplication.getInstance(),s, Toast.LENGTH_SHORT).show();
  }

  /**
   * 为通知提示类做的统一工具类
   *
   * @param tv
   * @param num 提示数字,默认不小于0
   */
  public void noticeBall(TextView tv, int num) {
    // TextColorChange
    if (num == 0) {
      tv.setVisibility(View.GONE);
    } else if (num < 10) {
      tv.setVisibility(View.VISIBLE);
      String notice = "0" + num;
      SpannableString span = SpannableUtil.TextColorChange(notice, "#00000000", 0, 1);
      tv.setText(span);
    } else if (num >= 10 && num < 100) {
      tv.setText(num+"");
    } else if (num > 99) {
      tv.setText("99+");
    }

  }

  /**
   *  是否快速点击
   * @return
   */
  public boolean isFastClick(){
    return ClickUtils.isFast(getActivity());
  }

}
