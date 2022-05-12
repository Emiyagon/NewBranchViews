package com.example.videolibrary.videolibrary.base;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Build;
import androidx.annotation.IdRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RawRes;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.Window;
import android.widget.FrameLayout;


import com.example.videolibrary.videolibrary.listener.OnFullscreenChangedListener;
import com.example.videolibrary.videolibrary.listener.OnStateChangedListener;
import com.example.videolibrary.videolibrary.listener.OnVideoSizeChangedListener;
import com.example.videolibrary.videolibrary.util.VideoUtils;

import java.util.Map;

/**
 * Created by zhouteng on 2019-09-18
 * <p>
 * 播放器布局基类，用于添加播放器画面视图
 */
public abstract class BaseVideoView extends FrameLayout implements IVideoView {

    protected ViewGroup surfaceContainer;
    protected OrientationHelper orientationHelper;

    //是否支持重力感应自动横竖屏，默认支持
    private boolean supportSensorRotate = true;

    //是否跟随系统的方向锁定，默认跟随
    private boolean rotateWithSystem = true;

    //播放器配置
    private PlayerConfig playerConfig;

    //播放器播放画面视图
    protected RenderContainerView renderContainerView;

    protected OnVideoSizeChangedListener onVideoSizeChangedListener;
    protected OnFullscreenChangedListener onFullScreenChangeListener;
    protected OnStateChangedListener onStateChangedListener;

    private boolean isFullScreen = false;

    private int mSystemUiVisibility;

    //全屏之前，正常状态下控件的宽高
    private int originWidth;
    private int originHeight;

    //父视图
    private ViewParent viewParent;
    //当前view在父视图中的布局参数
    private ViewGroup.LayoutParams viewLayoutParams;
    //当前view在父视图中的位置
    private int positionInParent;

    //actionbar可见状态记录
    private boolean actionBarVisible;

    //当前播放器状态
    private int currentState;

    public BaseVideoView(@NonNull Context context) {
        this(context, null);
    }

    public BaseVideoView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BaseVideoView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public BaseVideoView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    public void setVideoUrlPath(String url) {
        renderContainerView.setVideoUrlPath(url);
    }

    public void setVideoRawPath(@RawRes int rawId) {
        renderContainerView.setVideoRawPath(rawId);
    }

    public void setVideoAssetPath(String assetFileName) {
        renderContainerView.setVideoAssetPath(assetFileName);
    }

    public void setVideoHeaders(Map<String, String> headers) {
        renderContainerView.setVideoHeaders(headers);
    }

    @Override
    public RenderContainerView getRenderContainerView() {
        return renderContainerView;
    }

    /**
     * 从父控件中剥离出来，将变量置空后，返回，防止内存泄漏
     * <p>
     * 用于将播放器画面剥离出来，添加到另外的控制层界面上，实现窗口画面的转移，比如小窗口播放
     *
     * @return
     */
    @Override
    public RenderContainerView getRenderContainerViewOffParent() {
        ViewParent parent = renderContainerView.getParent();
        if (parent instanceof ViewGroup) {
            ((ViewGroup) parent).removeView(renderContainerView);
        }
        RenderContainerView result = renderContainerView;

        //renderContainerView和playConfig会被剥离出去的renderContainerView实例持有引用，因此置空，防止内存泄漏
        renderContainerView = null;
        playerConfig = null;

        return result;
    }

    protected void init(Context context) {
        LayoutInflater.from(context).inflate(getLayoutId(), this);
        surfaceContainer = findViewById(getSurfaceContainerId());
        playerConfig = new PlayerConfig.Builder().build();
        orientationHelper = new OrientationHelper(this);

        renderContainerView = newRenderContainerView();
        addRenderContainer(renderContainerView);
    }

    //添加播放器画面视图，到播放器界面上
    @Override
    public void addRenderContainer(RenderContainerView renderContainerView) {
        this.renderContainerView = renderContainerView;
        surfaceContainer.removeAllViews();
        surfaceContainer.addView(renderContainerView, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        renderContainerView.setVideoView(this);
        renderContainerView.setPlayerConfig(playerConfig);
    }

    protected RenderContainerView newRenderContainerView() {
        return new RenderContainerView(getContext());
    }

    @Override
    public BaseVideoView getPlayView() {
        return this;
    }

    @Override
    public void onVideoSizeChanged(int width, int height) {
        if (onVideoSizeChangedListener != null) {
            onVideoSizeChangedListener.onVideoSizeChanged(width, height);
        }
        resizeTextureView(width, height);
    }

    public int getCurrentState() {
        return currentState;
    }

    //重新设置播放器状态
    public void setPlayerStatus(int status) {
        currentState = status;
        onStateChange(status);
    }

    @Override
    public void onStateChange(int state) {
        currentState = state;
        if (onStateChangedListener != null) {
            onStateChangedListener.onStateChange(state);
        }
        Context context = getContext();
        switch (state) {
            case BasePlayer.STATE_PLAYING:
            case BasePlayer.STATE_BUFFERING_START:
                VideoUtils.keepScreenOn(context);
                break;
            default:
                VideoUtils.removeScreenOn(context);
                break;
        }
        updatePlayIcon();
    }

    protected void updatePlayIcon() {
        if (isPlaying()) {
            setPlayingIcon();
        } else {
            setPausedIcon();
        }
    }

    //设置播放时，播放按钮图标
    protected abstract void setPlayingIcon();

    //设置暂停时，播放按钮图标
    protected abstract void setPausedIcon();

    public void setPlayerConfig(PlayerConfig playerConfig) {
        if (playerConfig != null) {
            this.playerConfig = playerConfig;
            renderContainerView.setPlayerConfig(playerConfig);
        }
    }

    protected abstract @IdRes
    int getSurfaceContainerId();

    protected abstract @LayoutRes
    int getLayoutId();

    public abstract boolean onBackKeyPressed();

    public abstract void setTitle(String title);

    /**
     * @return 控制是否支持重力感旋转屏幕来全屏等操作，竖向全屏模式和智能全屏模式下不开启重力感应旋转屏幕，避免造成奇怪的交互。
     */
    @Override
    public boolean supportSensorRotate() {
        return supportSensorRotate && playerConfig.screenMode == PlayerConfig.LANDSCAPE_FULLSCREEN_MODE;
    }

    public void setSupportSensorRotate(boolean supportSensorRotate) {
        this.supportSensorRotate = supportSensorRotate;
    }

    @Override
    public boolean rotateWithSystem() {
        return rotateWithSystem;
    }

    public void setRotateWithSystem(boolean rotateWithSystem) {
        this.rotateWithSystem = rotateWithSystem;
    }

    public void setOnStateChangedListener(OnStateChangedListener onStateChangedListener) {
        this.onStateChangedListener = onStateChangedListener;
    }

    public void setOnFullscreenChangeListener(OnFullscreenChangedListener onFullScreenChangeListener) {
        this.onFullScreenChangeListener = onFullScreenChangeListener;
    }

    public void setOnVideoSizeChangedListener(OnVideoSizeChangedListener onVideoSizeChangedListener) {
        this.onVideoSizeChangedListener = onVideoSizeChangedListener;
    }

    //region 播放器相关

    public void replay() {
        BasePlayer player = getPlayer();
        if (player != null) {
            player.seekTo(0);
            start();
        }
    }

    public void start() {
        orientationHelper.start();
        renderContainerView.start();
    }

    public void startVideo() {
        renderContainerView.startVideo();
    }

    public void pause() {
        BasePlayer player = getPlayer();
        if (player != null) {
            player.pause();
        }
    }

    public boolean isPlaying() {
        BasePlayer player = getPlayer();
        return player != null && player.isInPlaybackState() && player.isPlaying();
    }

    public void release() {
        BasePlayer player = getPlayer();
        if (player != null) {
            player.release();
        }
    }

    public void destroy() {
        BasePlayer player = getPlayer();
        if (player != null) {
            player.destroy();
        }
        orientationHelper.setOrientationEnable(false);
    }

    public long getDuration() {
        BasePlayer player = getPlayer();
        return player == null ? 0 : player.getDuration();
    }

    public void seekTo(long position) {
        BasePlayer player = getPlayer();
        if (player != null) {
            player.seekTo(position);
        }
    }

    public long getCurrentPosition() {
        BasePlayer player = getPlayer();
        return player == null ? 0 : player.getCurrentPosition();
    }

    public int getStreamMaxVolume() {
        BasePlayer player = getPlayer();
        return player == null ? 0 : player.getStreamMaxVolume();
    }

    public boolean isInPlaybackState() {
        BasePlayer player = getPlayer();
        return player != null && player.isInPlaybackState();
    }

    public int getStreamVolume() {
        BasePlayer player = getPlayer();
        return player != null ? player.getStreamVolume() : 0;
    }

    public void setStreamVolume(int value) {
        BasePlayer player = getPlayer();
        if (player != null) {
            player.setStreamVolume(value);
        }
    }

    public BasePlayer getPlayer() {
        return renderContainerView == null ? null : renderContainerView.getPlayer();
    }
    //endregion

    /**
     * 数据网络下，默认情况下直接播放
     */
    @Override
    public void handleMobileData() {
        startVideo();
    }

    //region 全屏处理

    /**
     * 正常情况下，通过点击全屏按钮来全屏
     */
    public void startFullscreen() {
        startFullscreenWithOrientation(getFullScreenOrientation());
    }

    /**
     * 通过重力感应，旋转屏幕来全屏
     *
     * @param orientation
     */
    @Override
    public void startFullscreenWithOrientation(int orientation) {

        isFullScreen = true;

        Activity activity = VideoUtils.getActivity(getContext());

        actionBarVisible = VideoUtils.isActionBarVisible(activity);

        mSystemUiVisibility = activity.getWindow().getDecorView().getSystemUiVisibility();

        activity.setRequestedOrientation(orientation);

        VideoUtils.hideSupportActionBar(activity, true);
        VideoUtils.addFullScreenFlag(activity);
        VideoUtils.hideNavKey(activity);

        changeToFullScreen();

        postRunnableToResizeTexture();

        if (onFullScreenChangeListener != null) {
            onFullScreenChangeListener.onFullscreenChange(true);
        }
    }

    private void postRunnableToResizeTexture() {
        post(new Runnable() {
            @Override
            public void run() {
                resizeTextureView(getVideoWidth(), getVideoHeight());
            }
        });
    }

    private int getVideoWidth() {
        BasePlayer player = getPlayer();
        return player == null ? 0 : player.getVideoWidth();
    }

    private int getVideoHeight() {
        BasePlayer player = getPlayer();
        return player == null ? 0 : player.getVideoHeight();
    }

    //根据视频内容重新调整视频渲染区域大小
    private void resizeTextureView(int width, int height) {
        IRenderView renderView = renderContainerView.getRenderView();
        if (renderView == null || renderView.getRenderView() == null || height == 0 || width == 0) {
            return;
        }

        float aspectRation = playerConfig.aspectRatio == 0 ? (float) height / width : playerConfig.aspectRatio;

        int parentWidth = getWidth();
        int parentHeight = getHeight();

        float parentAspectRation = (float) parentHeight / parentWidth;

        int w, h;

        if (aspectRation < parentAspectRation) {
            w = parentWidth;
            h = (int) (w * aspectRation);
        } else {
            h = parentHeight;
            w = (int) (h / aspectRation);
        }
        renderView.setVideoSize(w, h);
    }

    /**
     * 通过获取到Activity的ID_ANDROID_CONTENT根布局，来添加视频控件，并全屏
     */
    protected void changeToFullScreen() {

        originWidth = getWidth();
        originHeight = getHeight();

        viewParent = getParent();
        viewLayoutParams = getLayoutParams();
        positionInParent = ((ViewGroup) viewParent).indexOfChild(this);

        ViewGroup vp = getRootViewGroup();

        removePlayerFromParent();

        LayoutParams lpParent = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        FrameLayout frameLayout = new FrameLayout(getContext());
        frameLayout.setBackgroundColor(Color.BLACK);

        LayoutParams lp = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        frameLayout.addView(this, lp);
        vp.addView(frameLayout, lpParent);
    }

    /**
     * 获取到Activity的ID_ANDROID_CONTENT根布局
     *
     * @return
     */
    private ViewGroup getRootViewGroup() {
        Activity activity = (Activity) getContext();
        if (activity != null) {
            return (ViewGroup) activity.findViewById(Window.ID_ANDROID_CONTENT);
        }
        return null;
    }

    private void removePlayerFromParent() {
        ViewParent parent = getParent();
        if (parent != null) {
            ((ViewGroup) parent).removeView(this);
        }
    }

    public void exitFullscreen() {
        exitFullscreenWithOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    @Override
    public void exitFullscreenWithOrientation(int orientation) {

        isFullScreen = false;

        Activity activity = VideoUtils.getActivity(getContext());

        activity.setRequestedOrientation(orientation);

        VideoUtils.showSupportActionBar(activity, actionBarVisible);
        VideoUtils.clearFullScreenFlag(activity);

        activity.getWindow().getDecorView().setSystemUiVisibility(mSystemUiVisibility);

        changeToNormalScreen();

        postRunnableToResizeTexture();

        if (onFullScreenChangeListener != null) {
            onFullScreenChangeListener.onFullscreenChange(false);
        }
    }

    /**
     * 对应上面的全屏模式，来恢复到全屏之前的样式
     */
    protected void changeToNormalScreen() {
        ViewGroup vp = getRootViewGroup();
        vp.removeView((View) getParent());
        removePlayerFromParent();

        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(originWidth, originHeight);
        setLayoutParams(layoutParams);

        if (viewParent != null) {
            ((ViewGroup) viewParent).addView(this, positionInParent, viewLayoutParams);
        }
    }

    @Override
    public boolean isFullScreen() {
        return isFullScreen;
    }

    /**
     * 视频全屏策略，竖向全屏，横向全屏，还是根据宽高比来智能选择
     */
    protected int getFullScreenOrientation() {
        if (playerConfig.screenMode == PlayerConfig.PORTRAIT_FULLSCREEN_MODE) {
            return ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
        }
        BasePlayer player = getPlayer();
        if (playerConfig.screenMode == PlayerConfig.AUTO_FULLSCREEN_MODE && player != null) {
            return player.getAspectRation() < 1 ? ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE : ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
        }
        return ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
    }


    //endregion
}
