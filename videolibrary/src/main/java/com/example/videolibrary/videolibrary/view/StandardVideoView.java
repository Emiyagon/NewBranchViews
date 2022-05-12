package com.example.videolibrary.videolibrary.view;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;


import com.example.videolibrary.R;
import com.example.videolibrary.videolibrary.base.BaseVideoView;
import com.example.videolibrary.videolibrary.player.AndroidPlayer;
import com.example.videolibrary.videolibrary.util.VideoUtils;

import java.lang.ref.WeakReference;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 标准的视频播放控件
 */

public class StandardVideoView extends BaseVideoView implements View.OnClickListener, SeekBar.OnSeekBarChangeListener, View.OnTouchListener {

    protected ImageView thumbView;
    protected ViewGroup bottomLayout;
    protected ViewGroup topLayout;
    protected TextView currentTimeText;
    protected TextView totalTimeText;
    protected SeekBar seekBar;
    protected ImageView fullScreen;
    protected ImageView back;
    protected TextView title;
    protected ProgressBar loadingProgressBar;
    protected ImageView start;
    protected ViewGroup failedLayout;
    protected ViewGroup replayLayout;

    protected ViewGroup lockStatusLayout;
    protected ImageView lockStatus;

    protected Timer updateProgressTimer;
    protected ProgressTimerTask mProgressTimerTask;

    protected Timer controlViewTimer;
    protected ControlViewTimerTask controlViewTimerTask;

    private boolean isShowMobileDataDialog = false;

    public StandardVideoView(@NonNull Context context) {
        this(context, null);
    }

    public StandardVideoView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public StandardVideoView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    protected void initView() {
        surfaceContainer.setOnClickListener(this);
        surfaceContainer.setOnTouchListener(this);

        thumbView = findViewById(R.id.thumb);
        bottomLayout = findViewById(R.id.bottom_layout);
        topLayout = findViewById(R.id.top_layout);
        currentTimeText = findViewById(R.id.current);
        totalTimeText = findViewById(R.id.total);

        seekBar = findViewById(R.id.seekbar);
        seekBar.setOnSeekBarChangeListener(this);

        fullScreen = findViewById(R.id.fullscreen);
        fullScreen.setOnClickListener(this);

        back = findViewById(R.id.back);
        back.setOnClickListener(this);

        title = findViewById(R.id.title);
        loadingProgressBar = findViewById(R.id.loading);

        failedLayout = findViewById(R.id.failed_layout);
        replayLayout = findViewById(R.id.reply_layout);
        replayLayout.setOnClickListener(this);

        start = findViewById(R.id.start);
        start.setOnClickListener(this);

        lockStatus = findViewById(R.id.lock_status);
        lockStatusLayout = findViewById(R.id.lock_status_layout);
        lockStatusLayout.setOnClickListener(this);
    }

    @Override
    protected @IdRes
    int getSurfaceContainerId() {
        return R.id.surface_container;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.standard_video_layout;
    }

    //region 根据状态更新UI

    @Override
    public void onStateChange(int state) {
        super.onStateChange(state);
        switch (state) {
            case AndroidPlayer.STATE_IDLE:
                changeUIWithIdle();
                break;
            case AndroidPlayer.STATE_PREPARING:
                changeUIWithPreparing();
                break;
            case AndroidPlayer.STATE_PREPARED:
                changeUIWithPrepared();
                break;
            case AndroidPlayer.STATE_PLAYING:
                changeUIWithPlaying();
                break;
            case AndroidPlayer.STATE_PAUSED:
                changeUIWithPause();
                break;
            case AndroidPlayer.STATE_COMPLETED:
                changeUIWithComplete();
                break;
            case AndroidPlayer.STATE_ERROR:
                changeUIWithError();
                break;
            case AndroidPlayer.STATE_BUFFERING_START:
                changeUiWithBufferingStart();
                break;
            case AndroidPlayer.STATE_BUFFERING_END:
                changeUiWithBufferingEnd();
                break;
        }
        updateProgressStatus();
    }

    protected void updateProgressStatus() {
        if (isPlaying()) {
            startProgressTimer();
        } else {
            cancelProgressTimer();
        }
    }

    protected void changeUiWithBufferingStart() {
        setViewsVisible(View.VISIBLE, View.GONE, View.GONE, View.VISIBLE, View.GONE, View.GONE);
    }

    protected void changeUiWithBufferingEnd() {
        setViewsVisible(View.VISIBLE, View.VISIBLE, View.GONE, View.GONE, View.GONE, View.GONE);
    }

    protected void changeUIWithPlaying() {
        startProgressTimer();
        setViewsVisible(View.VISIBLE, View.VISIBLE, View.GONE, View.GONE, View.GONE, View.GONE);
    }

    protected void changeUIWithPause() {
        cancelControlViewTimer();
        setViewsVisible(View.VISIBLE, View.VISIBLE, View.GONE, View.GONE, View.GONE, View.GONE);
    }

    protected void changeUIWithError() {
        setViewsVisible(View.VISIBLE, View.GONE, View.VISIBLE, View.GONE, View.GONE, View.GONE);
    }

    protected void changeUIWithComplete() {
        setViewsVisible(View.VISIBLE, View.GONE, View.GONE, View.GONE, View.GONE, View.VISIBLE);
        seekBar.setProgress(100);
        currentTimeText.setText(totalTimeText.getText());
    }

    @Override
    protected void setPlayingIcon() {
        start.setImageResource(R.drawable.ic_pause);
    }

    @Override
    protected void setPausedIcon() {
        start.setImageResource(R.drawable.ic_play);
    }

    protected void resetProgressAndTime() {
        currentTimeText.setText(VideoUtils.stringForTime(0));
        totalTimeText.setText(VideoUtils.stringForTime(0));
    }

    protected void changeUIWithIdle() {
        setViewsVisible(View.VISIBLE, View.GONE, View.GONE, View.GONE, View.VISIBLE, View.GONE);
    }

    protected void changeUIWithPreparing() {
        resetProgressAndTime();
        setViewsVisible(View.VISIBLE, View.GONE, View.GONE, View.VISIBLE, View.GONE, View.GONE);
    }

    protected void changeUIWithPrepared() {

        int visible = View.VISIBLE;

        if (getDuration() <= 0) {

            //表示直播类的视频，没有进度条
            visible = View.INVISIBLE;

        }

        startControlViewTimer();

        currentTimeText.setVisibility(visible);
        totalTimeText.setVisibility(visible);
        seekBar.setVisibility(visible);

        setViewsVisible(View.VISIBLE, View.VISIBLE, View.GONE, View.GONE, View.GONE, View.GONE);
    }

    protected void setViewsVisible(int topLayoutVisi, int bottomLayoutVisi, int failedLayoutVisi, int loadingVisi, int thumbVisi, int replayLayoutVisi) {

        setTopVisi(topLayoutVisi);
        setBottomVisi(bottomLayoutVisi);

        failedLayout.setVisibility(failedLayoutVisi);
        loadingProgressBar.setVisibility(loadingVisi);
        thumbView.setVisibility(thumbVisi);
        replayLayout.setVisibility(replayLayoutVisi);
    }
    //endregion

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.start) {
            start();
        } else if (v.getId() == R.id.reply_layout) {
            replay();
        } else if (v.getId() == R.id.surface_container) {
            surfaceContainerClick();
        } else if (v.getId() == R.id.fullscreen) {
            handleFullScreen();
        } else if (v.getId() == R.id.back) {
            handleBack();
        } else if (v.getId() == R.id.lock_status_layout) {
            toggleVideoLockStatus();
        }
    }

    protected void handleBack() {
        if (isFullScreen()) {
            exitFullscreen();
        } else {
            VideoUtils.getActivity(getContext()).finish();
        }
    }

    protected void handleFullScreen() {
        if (isFullScreen()) {
            exitFullscreen();
        } else {
            startFullscreen();
        }
    }

    @Override
    public boolean onBackKeyPressed() {
        if (isLocked) {
            return true;
        }
        if (isFullScreen()) {
            exitFullscreen();
            return true;
        }

        return false;
    }

    @Override
    public void setTitle(String titleText) {
        title.setText(titleText);
    }

    @Override
    public void startFullscreenWithOrientation(int orientation) {
        super.startFullscreenWithOrientation(orientation);
        resetLockStatus();
    }

    @Override
    public void exitFullscreenWithOrientation(int orientation) {
        super.exitFullscreenWithOrientation(orientation);
        resetLockStatus();
    }

    @Override
    public void release() {
        super.release();
        seekBar.setProgress(0);
    }

    @Override
    public void destroy() {
        super.destroy();
        destroyPlayerController();
    }

    /**
     * 仅仅销毁播放器控制层逻辑，但是不去销毁RenderContainer层
     */
    public void destroyPlayerController() {
        cancelControlViewTimer();
        cancelProgressTimer();
        orientationHelper.setOrientationEnable(false);
    }

    @Override
    public void handleMobileData() {
        if (isShowMobileDataDialog) {
            return;
        }
        isShowMobileDataDialog = true;
        Context context = getContext();
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.Theme_AppCompat_Light_Dialog_Alert);
        builder.setMessage(context.getString(R.string.mobile_data_tips));
        builder.setPositiveButton(context.getString(R.string.continue_playing), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                startVideo();
            }
        });
        builder.setNegativeButton(context.getString(R.string.stop_play), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    //region 点击屏幕，显示隐藏控制栏
    protected void surfaceContainerClick() {
        if (!isInPlaybackState()) {
            return;
        }
        toggleControlView();
        startControlViewTimer();
    }

    private void toggleControlView() {
        setVideoLockLayoutVisi(lockStatusLayout.getVisibility() == View.VISIBLE ? View.INVISIBLE : View.VISIBLE);
        if (isLocked) {
            return;
        }
        int visi = bottomLayout.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE;
        setTopBottomVisi(visi);
    }

    private void setTopBottomVisi(int visi) {
        setBottomVisi(visi);
        setTopVisi(visi);
    }

    protected void setBottomVisi(int visi) {
        bottomLayout.setVisibility(isLocked ? View.GONE : visi);
    }

    protected void setTopVisi(int visi) {
        topLayout.setVisibility(isLocked ? View.GONE : visi);
    }

    //endregion

    //region top,bottom控制栏隐藏任务

    private void hideControlView() {
        if (isInPlaybackState()) {
            setTopBottomVisi(View.GONE);
            setVideoLockLayoutVisi(View.INVISIBLE);
        }
    }

    protected static class ControlViewTimerTask extends TimerTask {

        private WeakReference<StandardVideoView> weakReference;

        private ControlViewTimerTask(StandardVideoView videoView) {
            weakReference = new WeakReference<>(videoView);
        }

        @Override
        public void run() {
            StandardVideoView videoView = weakReference.get();
            if (videoView != null) {
                videoView.post(new ControlViewRunnable(videoView));
            }
        }
    }

    private static class ControlViewRunnable implements Runnable {

        private final WeakReference<StandardVideoView> weakReference;

        private ControlViewRunnable(StandardVideoView videoView) {
            weakReference = new WeakReference<>(videoView);
        }

        @Override
        public void run() {
            StandardVideoView videoView = weakReference.get();
            if (videoView != null) {
                videoView.hideControlView();
            }
        }
    }

    protected void startControlViewTimer() {
        cancelControlViewTimer();
        controlViewTimer = new Timer();
        controlViewTimerTask = new ControlViewTimerTask(this);
        controlViewTimer.schedule(controlViewTimerTask, 2500);
    }

    protected void cancelControlViewTimer() {
        if (controlViewTimer != null) {
            controlViewTimer.cancel();
        }
        if (controlViewTimerTask != null) {
            controlViewTimerTask.cancel();
        }
    }
    //endregion

    //region 进度条更新任务
    protected class ProgressTimerTask extends TimerTask {
        private WeakReference<StandardVideoView> weakReference;

        private ProgressTimerTask(StandardVideoView videoView) {
            weakReference = new WeakReference<>(videoView);
        }

        @Override
        public void run() {
            StandardVideoView videoView = weakReference.get();
            if (videoView == null || !videoView.isPlaying()) {
                return;
            }
            videoView.post(new ProgressRunnable(videoView));
        }
    }

    private static class ProgressRunnable implements Runnable {

        private WeakReference<StandardVideoView> weakReference;

        private ProgressRunnable(StandardVideoView videoView) {
            weakReference = new WeakReference<>(videoView);
        }

        @Override
        public void run() {
            StandardVideoView videoView = weakReference.get();
            if (videoView != null) {
                videoView.setProgressAndText();
            }
        }
    }

    //是否是直播类视频
    protected boolean isLive() {
        return getDuration() <= 0;
    }

    protected void startProgressTimer() {
        if (isLive()) {
            return;
        }
        cancelProgressTimer();
        updateProgressTimer = new Timer();
        mProgressTimerTask = new ProgressTimerTask(this);
        updateProgressTimer.schedule(mProgressTimerTask, 0, 300);
    }

    protected void cancelProgressTimer() {
        if (updateProgressTimer != null) {
            updateProgressTimer.cancel();
        }
        if (mProgressTimerTask != null) {
            mProgressTimerTask.cancel();
        }
    }

    protected void setProgressAndText() {
        int position = (int) getCurrentPosition();
        int duration = (int) getDuration();
        int progress = position * 100 / (duration == 0 ? 1 : duration);
        if (progress != 0 && !touchScreen) {
            seekBar.setProgress(progress);
        }
        currentTimeText.setText(VideoUtils.stringForTime(position));
        totalTimeText.setText(VideoUtils.stringForTime(duration));
    }
    //endregion

    //region 锁定屏幕

    protected boolean isSupportLock = true;

    protected boolean isLocked = false; //是否处于锁定屏幕状态

    protected boolean isSupportLock() {
        return isFullScreen() && isSupportLock;
    }

    //全屏情况下，是否支持锁定播放器
    public void setSupportLock(boolean supportLock) {
        isSupportLock = supportLock;
    }

    protected void toggleVideoLockStatus() {
        isLocked = !isLocked;
        if (isLocked) {
            setVideoLockedIcon();
        } else {
            setVideoUnlockedIcon();
        }
        orientationHelper.setOrientationEnable(!isLocked);
        setTopBottomVisi(isLocked ? View.GONE : View.VISIBLE);
    }

    protected void setVideoLockedIcon() {
        lockStatus.setImageResource(R.drawable.ic_locked);
    }

    protected void setVideoUnlockedIcon() {
        lockStatus.setImageResource(R.drawable.ic_unlocked);
    }

    protected void setVideoLockLayoutVisi(int visi) {
        if (isSupportLock()) {
            lockStatusLayout.setVisibility(visi);
        } else {
            lockStatusLayout.setVisibility(View.INVISIBLE);
        }
    }

    protected void resetLockStatus() {
        isLocked = false;
        setVideoLockLayoutVisi(bottomLayout.getVisibility());
    }

    //endregion

    //region 音量，亮度，进度调整

    protected boolean isSupportVolume = true;   //默认支持手势调节音量
    protected boolean isSupportBrightness = true;  //默认支持手势调节亮度
    protected boolean isSupportSeek = true;   //默认支持手势调节进度

    protected VolumeDialog volumeDialog;
    protected BrightnessDialog brightnessDialog;
    protected SeekDialog seekDialog;

    protected boolean touchScreen;

    private float downX;
    private float downY;

    private int downVolume;  //触摸屏幕时的当前音量
    private float downBrightness;  //触摸屏幕时的当前亮度

    private long downVideoPosition; //触摸屏幕时的当前播放进度
    private long newVideoPosition; //手势操作拖动后的新的进度

    private boolean isChangedProgress;  //是否手势操作拖动了进度条

    private boolean isSeekGesture = false; //是否触发了进度条拖拽的手势
    private boolean isVolumeGesture = false; //是否触发了音量调整的手势
    private boolean isBrightnessGesture = false; //是否触发了亮度调整的手势

    protected static final int MINI_GESTURE_DISTANCE = 60; // 手势的最小触发范围;

    //手动设置是否支持手势调节音量
    public void setSupportVolume(boolean supportVolume) {
        isSupportVolume = supportVolume;
    }

    //手动设置是否支持手势调节亮度
    public void setSupportBrightness(boolean supportBrightness) {
        isSupportBrightness = supportBrightness;
    }

    //手动设置是否支持手势拖动播放进度(不支持直播类视频流)
    public void setSupportSeek(boolean supportSeek) {
        isSupportSeek = supportSeek;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (!isInPlaybackState()) {
            return false;
        }
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                touchScreen = true;
                downX = event.getX();
                downY = event.getY();
                downVolume = getStreamVolume();
                downBrightness = VideoUtils.getScreenBrightness(getContext());
                downVideoPosition = getCurrentPosition();
                isChangedProgress = false;

                isSeekGesture = false;
                isVolumeGesture = false;
                isBrightnessGesture = false;

                break;
            case MotionEvent.ACTION_MOVE:
                float dx = event.getX() - downX;
                float dy = event.getY() - downY;
                touchMove(dx, dy, event.getX());
                break;
            case MotionEvent.ACTION_UP:
                touchScreen = false;
                hideVolumeDialog();
                hideBrightnewssDialog();
                hideSeekDialog();
                seekToNewVideoPosition();
                break;
        }
        return false;
    }

    //音量，亮度，播放进度等手势判断
    protected void touchMove(float dx, float dy, float x) {

        if (isLocked) {
            return;
        }

        float absDx = Math.abs(dx);
        float absDy = Math.abs(dy);

        int distance = getWidth();

        if (!isSeekGesture && !isVolumeGesture && !isBrightnessGesture) {

            //触发了播放进度拖拽手势
            if (absDx > absDy && absDx >= MINI_GESTURE_DISTANCE) {
                isSeekGesture = true;
            }
            //触发了亮度调节手势
            else if (absDy > absDx && x <= distance / 2 && absDy >= MINI_GESTURE_DISTANCE) {
                isBrightnessGesture = true;
            }
            //触发了音量调节手势
            else if (absDy > absDx && x > distance / 2 && absDy >= MINI_GESTURE_DISTANCE) {
                isVolumeGesture = true;
            }
        }

        if (isSeekGesture && !isLive() && isSupportSeek) {
            changeProgress(dx);
            return;
        }

        if (isBrightnessGesture && isSupportBrightness) {
            changeBrightness(dy);
            return;
        }

        if (isVolumeGesture && isSupportVolume) {
            changeVolume(dy);
        }
    }

    //region 播放进度手势处理
    protected void seekToNewVideoPosition() {
        if (isChangedProgress) {
            seekTo(newVideoPosition);
            isChangedProgress = false;

            startProgressTimer();
        }
    }

    protected void changeProgress(float dx) {
        cancelProgressTimer();

        int distance = getWidth();
        long videoDuration = getDuration();
        newVideoPosition = downVideoPosition + (int) (dx / distance * videoDuration);
        if (newVideoPosition >= videoDuration) {
            newVideoPosition = videoDuration;
        }
        String progressText = VideoUtils.stringForTime(newVideoPosition) + "/" + VideoUtils.stringForTime(videoDuration);
        int progress = (int) ((float) newVideoPosition / videoDuration * 100);
        showSeekDialog(progressText, progress);
        isChangedProgress = true;
    }

    protected void showSeekDialog(String progressText, int seekBarProgress) {
        if (seekDialog == null) {
            seekDialog = newSeekDialogInstance();
        }
        seekDialog.showSeekDialog(progressText, this);
        setProgressTextWithTouch(seekBarProgress);
    }

    protected void hideSeekDialog() {
        if (seekDialog != null) {
            seekDialog.dismiss();
        }
    }

    //用于SeekDialog的继承扩展
    protected SeekDialog newSeekDialogInstance() {
        return new SeekDialog(getContext(), R.style.volume_brightness_theme);
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        cancelProgressTimer();
        cancelControlViewTimer();
        downVideoPosition = getCurrentPosition();
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (fromUser) {
            long videoDuration = getDuration();
            newVideoPosition = progress * videoDuration / 100;
            String progressText = VideoUtils.stringForTime(newVideoPosition) + "/" + VideoUtils.stringForTime(videoDuration);
            showSeekDialog(progressText, progress);
        }
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        seekTo(newVideoPosition);
        hideSeekDialog();
        startProgressTimer();
        startControlViewTimer();
    }

    protected void setProgressTextWithTouch(int progress) {
        currentTimeText.setText(VideoUtils.stringForTime(newVideoPosition));
        seekBar.setProgress(progress);
    }

    //endregion

    //region 亮度手势操作处理
    protected void changeBrightness(float dy) {
        //屏幕亮度区间0.0 ~ 1.0
        int distance = getHeight();

        float newBrightness = downBrightness - dy / distance;
        if (newBrightness < 0.0f) {
            newBrightness = 0.0f;
        }
        if (newBrightness > 1.0f) {
            newBrightness = 1.0f;
        }
        VideoUtils.setScreenBrightness(getContext(), newBrightness);
        showBrightnewssDialog((int) (newBrightness * 100));
    }

    protected void showBrightnewssDialog(int volumeProgress) {
        if (brightnessDialog == null) {
            brightnessDialog = newBrightnessDialogInstance();
        }
        brightnessDialog.showBrightnewssDialog(volumeProgress, this);
    }

    protected void hideBrightnewssDialog() {
        if (brightnessDialog != null) {
            brightnessDialog.dismiss();
        }
    }

    //用于BrightnessDialog的继承扩展
    protected BrightnessDialog newBrightnessDialogInstance() {
        return new BrightnessDialog(getContext(), R.style.volume_brightness_theme);
    }

    //endregion

    //region 音量手势操作处理
    protected void changeVolume(float dy) {

        int maxVolume = getStreamMaxVolume();

        int distance = getHeight();

        float newVolume = downVolume - dy / distance * maxVolume;

        if (newVolume < 0) {
            newVolume = 0;
        }
        if (newVolume > maxVolume) {
            newVolume = maxVolume;
        }

        setStreamVolume((int) newVolume);

        showVolumeDialog((int) (newVolume / maxVolume * 100));
    }

    protected void showVolumeDialog(int volumeProgress) {
        if (volumeDialog == null) {
            volumeDialog = newVolumeDialogInstance();
        }
        volumeDialog.showVolumeDialog(volumeProgress, this);
    }

    protected void hideVolumeDialog() {
        if (volumeDialog != null) {
            volumeDialog.dismiss();
        }
    }

    //用于VolumeDialog的继承扩展
    protected VolumeDialog newVolumeDialogInstance() {
        return new VolumeDialog(getContext(), R.style.volume_brightness_theme);
    }

    //endregion

    //endregion
}
