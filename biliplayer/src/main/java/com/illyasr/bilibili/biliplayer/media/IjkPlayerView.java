package com.illyasr.bilibili.biliplayer.media;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.media.AudioManager;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.OrientationEventListener;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.IntDef;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.view.MotionEventCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.ViewPropertyAnimatorListenerAdapter;

import com.illyasr.bilibili.biliplayer.danmaku.BaseDanmakuConverter;
import com.illyasr.bilibili.biliplayer.danmaku.BiliDanmukuParser;
import com.illyasr.bilibili.biliplayer.danmaku.OnDanmakuListener;
import com.illyasr.bilibili.biliplayer.utils.AnimHelper;
import com.illyasr.bilibili.biliplayer.utils.MotionEventUtils;
import com.illyasr.bilibili.biliplayer.utils.NavUtils;
import com.illyasr.bilibili.biliplayer.utils.NetWorkUtils;
import com.illyasr.bilibili.biliplayer.utils.SDCardUtils;
import com.illyasr.bilibili.biliplayer.utils.SoftInputUtils;
import com.illyasr.bilibili.biliplayer.utils.StringUtils;
import com.illyasr.bilibili.biliplayer.utils.WindowUtils;
import com.illyasr.bilibili.biliplayer.widgets.MarqueeTextView;
import com.illyasr.bilibili.biliplayer.widgets.ShareDialog;
import com.zhuosen.bilibili.biliplayer.R;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.ArrayList;
import java.util.List;
import master.flame.danmaku.controller.DrawHandler;
import master.flame.danmaku.controller.IDanmakuView;
import master.flame.danmaku.danmaku.loader.ILoader;
import master.flame.danmaku.danmaku.loader.IllegalDataException;
import master.flame.danmaku.danmaku.loader.android.DanmakuLoaderFactory;
import master.flame.danmaku.danmaku.model.BaseDanmaku;
import master.flame.danmaku.danmaku.model.DanmakuTimer;
import master.flame.danmaku.danmaku.model.android.DanmakuContext;
import master.flame.danmaku.danmaku.model.android.Danmakus;
import master.flame.danmaku.danmaku.parser.BaseDanmakuParser;
import master.flame.danmaku.danmaku.parser.IDataSource;
import tv.danmaku.ijk.media.player.IMediaPlayer;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;

import static android.view.GestureDetector.OnGestureListener;
import static android.view.GestureDetector.SimpleOnGestureListener;
import static android.widget.SeekBar.OnSeekBarChangeListener;

import static com.illyasr.bilibili.biliplayer.utils.StringUtils.generateTime;
import static tv.danmaku.ijk.media.player.IMediaPlayer.OnInfoListener;

/**
 * Created by
 *
 *   tv_open_edit_danmaku  //???????????????
 * 	iv_danmaku_control    //???????????????????????????
 * 	2428 ???
 */
/**
 * Created by long on 2016/10/24.
 */
public class IjkPlayerView extends FrameLayout implements View.OnClickListener {

    // ??????????????????
    private static final int MAX_VIDEO_SEEK = 1000;
    // ???????????????????????????
    private static final int DEFAULT_HIDE_TIMEOUT = 5000;
    // ??????????????????
    private static final int MSG_UPDATE_SEEK = 10086;
    // ??????????????????
    private static final int MSG_ENABLE_ORIENTATION = 10087;
    // ??????????????????
    private static final int MSG_TRY_RELOAD = 10088;
    // ????????????
    private static final int INVALID_VALUE = -1;
    // ?????????????????????????????????????????????????????????????????????
    private static final int INTERVAL_TIME = 1000;

    // ?????????IjkPlayer
    private IjkVideoView mVideoView;
    // ????????????????????????????????????????????????????????????
    public ImageView mPlayerThumb;
    // ??????
    private ProgressBar mLoadingView;
    // ??????
    private TextView mTvVolume;
    // ??????
    private TextView mTvBrightness;
    // ??????
    private TextView mTvFastForward;
    // ??????????????????
    private FrameLayout mFlTouchLayout;
    // ?????????????????????
    private ImageView mIvBack;
    // ??????????????????
    private MarqueeTextView mTvTitle;
    // ????????????TopBar
    private LinearLayout mFullscreenTopBar;
    // ????????????????????????
    private ImageView mIvBackWindow;
    // ???????????????TopBar
    private FrameLayout mWindowTopBar;
    // ?????????
    private ImageView mIvPlay;
    private ImageView mIvPlayCircle;
    // ????????????
    private TextView mTvCurTime;
    // ?????????
    private SeekBar mPlayerSeek;
    // ????????????
    private TextView mTvEndTime;
    // ??????????????????
    private ImageView mIvFullscreen;
    // BottomBar
    private LinearLayout mLlBottomBar;
    // ????????????????????????
    private FrameLayout mFlVideoBox;
    // ?????????
    private ImageView mIvPlayerLock;
    // ????????????
    private TextView mTvRecoverScreen;
    // ???????????????
    private TextView mTvSettings;
    private RadioGroup mAspectRatioOptions;
    // ?????????Activity
    private AppCompatActivity mAttachActivity;
    // ??????
    private TextView mTvReload;
    private View mFlReload;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == MSG_UPDATE_SEEK) {
                final int pos = _setProgress();
                if (!mIsSeeking && mIsShowBar && mVideoView.isPlaying()) {
                    // ?????????????????????MSG???????????????????????? Seek ?????????
                    msg = obtainMessage(MSG_UPDATE_SEEK);
                    sendMessageDelayed(msg, 1000 - (pos % 1000));
                }
            } else if (msg.what == MSG_ENABLE_ORIENTATION) {
                if (mOrientationListener != null) {
                    mOrientationListener.enable();
                }
            } else if (msg.what == MSG_TRY_RELOAD) {
                if (mIsNetConnected) {
                    reload();
                }
                msg = obtainMessage(MSG_TRY_RELOAD);
                sendMessageDelayed(msg, 3000);
            }
        }
    };
    // ????????????
    private AudioManager mAudioManager;
    // ????????????
    private GestureDetector mGestureDetector;
    // ????????????
    private int mMaxVolume;
    // ??????
    private boolean mIsForbidTouch = false;
    // ?????????????????????
    private boolean mIsShowBar = true;
    // ????????????
    private boolean mIsFullscreen;
    // ??????????????????
    private boolean mIsPlayComplete = false;
    // ???????????????????????????
    private boolean mIsSeeking;
    // ????????????
    private long mTargetPosition = INVALID_VALUE;
    // ????????????
    private int mCurPosition = INVALID_VALUE;
    // ????????????
    private int mCurVolume = INVALID_VALUE;
    // ????????????
    private float mCurBrightness = INVALID_VALUE;
    // ????????????
    private int mInitHeight;
    // ?????????/??????
    private int mWidthPixels;
    // ??????UI?????????
    private int mScreenUiVisibility;
    // ????????????????????????
    private OrientationEventListener mOrientationListener;
    // ??????????????????
    private boolean mIsNeverPlay = true;
    // ???????????????
    private OnInfoListener mOutsideInfoListener;
    private IMediaPlayer.OnCompletionListener mCompletionListener;
    // ??????????????????????????????
    private boolean mIsForbidOrientation = true;
    // ????????????????????????
    private boolean mIsAlwaysFullScreen = false;
    // ???????????????????????????
    private long mExitTime = 0;
    // ??????Matrix
    private Matrix mVideoMatrix = new Matrix();
    private Matrix mSaveMatrix = new Matrix();
    // ????????????????????????????????????
    private boolean mIsNeedRecoverScreen = false;
    // ??????????????????
    private int mAspectOptionsHeight;
    // ??????????????????????????????
    private int mInterruptPosition;
    private boolean mIsReady = false;


    public IjkPlayerView(Context context) {
        this(context, null);
    }

    public IjkPlayerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        _initView(context);
    }

    private void _initView(Context context) {
        if (context instanceof AppCompatActivity) {
            mAttachActivity = (AppCompatActivity) context;
        } else {
            throw new IllegalArgumentException("Context must be AppCompatActivity");
        }
        View.inflate(context, R.layout.layout_player_view, this);
        mVideoView = (IjkVideoView) findViewById(R.id.video_view);
        mPlayerThumb = (ImageView) findViewById(R.id.iv_thumb);
        mLoadingView = (ProgressBar) findViewById(R.id.pb_loading);
        mTvVolume = (TextView) findViewById(R.id.tv_volume);
        mTvBrightness = (TextView) findViewById(R.id.tv_brightness);
        mTvFastForward = (TextView) findViewById(R.id.tv_fast_forward);
        mFlTouchLayout = (FrameLayout) findViewById(R.id.fl_touch_layout);
        mIvBack = (ImageView) findViewById(R.id.iv_back);
        mTvTitle = (MarqueeTextView) findViewById(R.id.tv_title);
        mFullscreenTopBar = (LinearLayout) findViewById(R.id.fullscreen_top_bar);
        mIvBackWindow = (ImageView) findViewById(R.id.iv_back_window);
        mWindowTopBar = (FrameLayout) findViewById(R.id.window_top_bar);
        mIvPlay = (ImageView) findViewById(R.id.iv_play);
        mTvCurTime = (TextView) findViewById(R.id.tv_cur_time);
        mPlayerSeek = (SeekBar) findViewById(R.id.player_seek);
        mTvEndTime = (TextView) findViewById(R.id.tv_end_time);
        mIvFullscreen = (ImageView) findViewById(R.id.iv_fullscreen);
        mLlBottomBar = (LinearLayout) findViewById(R.id.ll_bottom_bar);
        mFlVideoBox = (FrameLayout) findViewById(R.id.fl_video_box);
        mIvPlayerLock = (ImageView) findViewById(R.id.iv_player_lock);
        mIvPlayCircle = (ImageView) findViewById(R.id.iv_play_circle);
        mTvRecoverScreen = (TextView) findViewById(R.id.tv_recover_screen);
        mTvReload = (TextView) findViewById(R.id.tv_reload);
        mFlReload = findViewById(R.id.fl_reload_layout);
        // ?????????????????????
        mTvSettings = (TextView) findViewById(R.id.tv_settings);
        mAspectRatioOptions = (RadioGroup) findViewById(R.id.aspect_ratio_group);
        mAspectOptionsHeight = getResources().getDimensionPixelSize(R.dimen.aspect_btn_size) * 4;
        mAspectRatioOptions.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.aspect_fit_parent) {
                    mVideoView.setAspectRatio(IRenderView.AR_ASPECT_FIT_PARENT);
                } else if (checkedId == R.id.aspect_fit_screen) {
                    mVideoView.setAspectRatio(IRenderView.AR_ASPECT_FILL_PARENT);
                } else if (checkedId == R.id.aspect_16_and_9) {
                    mVideoView.setAspectRatio(IRenderView.AR_16_9_FIT_PARENT);
                } else if (checkedId == R.id.aspect_4_and_3) {
                    mVideoView.setAspectRatio(IRenderView.AR_4_3_FIT_PARENT);
                }
                AnimHelper.doClipViewHeight(mAspectRatioOptions, mAspectOptionsHeight, 0, 150);
            }
        });
        _initMediaQuality();
        _initVideoSkip();
        _initReceiver();

        mIvPlay.setOnClickListener(this);
        mIvBack.setOnClickListener(this);
        mIvFullscreen.setOnClickListener(this);
        mIvBackWindow.setOnClickListener(this);
        mIvPlayerLock.setOnClickListener(this);
        mIvPlayCircle.setOnClickListener(this);
        mTvRecoverScreen.setOnClickListener(this);
        mTvSettings.setOnClickListener(this);
        mTvReload.setOnClickListener(this);
    }

    /**
     * ?????????
     */
    private void _initMediaPlayer() {
        // ?????? IjkMediaPlayer ???
        IjkMediaPlayer.loadLibrariesOnce(null);
        IjkMediaPlayer.native_profileBegin("libijkplayer.so");
        // ??????
        mAudioManager = (AudioManager) mAttachActivity.getSystemService(Context.AUDIO_SERVICE);
        mMaxVolume = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        // ??????
        try {
            int e = Settings.System.getInt(mAttachActivity.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS);
            float progress = 1.0F * (float) e / 255.0F;
            WindowManager.LayoutParams layout = mAttachActivity.getWindow().getAttributes();
            layout.screenBrightness = progress;
            mAttachActivity.getWindow().setAttributes(layout);
        } catch (Settings.SettingNotFoundException var7) {
            var7.printStackTrace();
        }
        // ??????
        mPlayerSeek.setMax(MAX_VIDEO_SEEK);
        mPlayerSeek.setOnSeekBarChangeListener(mSeekListener);
        // ????????????
        mVideoView.setOnInfoListener(mInfoListener);
        // ????????????
        mGestureDetector = new GestureDetector(mAttachActivity, mPlayerGestureListener);
        mFlVideoBox.setClickable(true);
        mFlVideoBox.setOnTouchListener(mPlayerTouchListener);
        // ??????????????????
        mOrientationListener = new OrientationEventListener(mAttachActivity) {
            @Override
            public void onOrientationChanged(int orientation) {
                _handleOrientation(orientation);
            }
        };
        if (mIsForbidOrientation) {
            // ????????????
            mOrientationListener.disable();
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (mInitHeight == 0) {
            mInitHeight = getHeight();
            mWidthPixels = getResources().getDisplayMetrics().widthPixels;
        }
    }

    /**============================ ?????????????????? ============================*/

    /**
     * Activity.onResume() ?????????
     */
    public void onResume() {
        if (mIsScreenLocked) {
            // ??????????????????????????????????????????Render??????????????????????????????????????????
            // ??????????????????????????????????????????????????????????????????????????????????????????????????????????????????
            mVideoView.setRender(IjkVideoView.RENDER_TEXTURE_VIEW);
            mIsScreenLocked = false;
        }
        mVideoView.resume();
        if (!mIsForbidTouch && !mIsForbidOrientation) {
            mOrientationListener.enable();
        }
        if (mCurPosition != INVALID_VALUE) {
            // ????????? seekTo ????????????????????????????????????????????????????????????????????
            seekTo(mCurPosition);
            mCurPosition = INVALID_VALUE;
        }
    }

    /**
     * Activity.onPause() ?????????
     */
    public void onPause() {
        mCurPosition = mVideoView.getCurrentPosition();
        mVideoView.pause();
        mIvPlay.setSelected(false);
        mOrientationListener.disable();
        _pauseDanmaku();
    }

    /**
     * Activity.onDestroy() ?????????
     *
     * @return ??????????????????
     */
    public int onDestroy() {
        // ??????????????????
        int curPosition = mVideoView.getCurrentPosition();
        mVideoView.destroy();
        IjkMediaPlayer.native_profileEnd();
        if (mDanmakuView != null) {
            // don't forget release!
            mDanmakuView.release();
            mDanmakuView = null;
        }
        if (mShareDialog != null) {
            mShareDialog.dismiss();
            mShareDialog = null;
        }
        mHandler.removeMessages(MSG_TRY_RELOAD);
        mHandler.removeMessages(MSG_UPDATE_SEEK);
        // ????????????
        mAttachActivity.unregisterReceiver(mBatteryReceiver);
        mAttachActivity.unregisterReceiver(mScreenReceiver);
        mAttachActivity.unregisterReceiver(mNetReceiver);
        // ??????????????????
        mAttachActivity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        return curPosition;
    }

    /**
     * ???????????????????????????????????????????????????????????????????????????????????????????????????
     *
     * @param keyCode
     * @return
     */
    public boolean handleVolumeKey(int keyCode) {
        if (keyCode == KeyEvent.KEYCODE_VOLUME_UP) {
            _setVolume(true);
            return true;
        } else if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {
            _setVolume(false);
            return true;
        } else {
            return false;
        }
    }

    /**
     * ??????????????????????????????
     *
     * @return
     */
    public boolean onBackPressed() {
        if (recoverFromEditVideo()) {
            return true;
        }
        if (mIsAlwaysFullScreen) {
            _exit();
            return true;
        } else if (mIsFullscreen) {
            mAttachActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            if (mIsForbidTouch) {
                // ?????????????????????
                mIsForbidTouch = false;
                mIvPlayerLock.setSelected(false);
                _setControlBarVisible(mIsShowBar);
            }
            return true;
        }
        return false;
    }

    /**
     * ??????????????????????????????
     *
     * @return
     */
    public IjkPlayerView init() {
        _initMediaPlayer();
        return this;
    }

    /**
     * ????????????
     *
     * @param url
     * @return
     */
    public IjkPlayerView switchVideoPath(String url) {
        return switchVideoPath(Uri.parse(url));
    }

    /**
     * ????????????
     *
     * @param uri
     * @return
     */
    public IjkPlayerView switchVideoPath(Uri uri) {
        if (mQualityData != null) {
            mQualityData.clear();
            mQualityData = null;
        }
        reset();
        return setVideoPath(uri);
    }

    /**
     * ??????????????????
     *
     * @param url
     * @return
     */
    public IjkPlayerView setVideoPath(String url) {
        return setVideoPath(Uri.parse(url));
    }

    /**
     * ??????????????????
     *
     * @param uri
     * @return
     */
    public IjkPlayerView setVideoPath(Uri uri) {
        mVideoView.setVideoURI(uri);
        if (mCurPosition != INVALID_VALUE) {
            seekTo(mCurPosition);
            mCurPosition = INVALID_VALUE;
        } else {
            seekTo(0);
        }
        return this;
    }

    /**
     * ????????????????????????????????????
     *
     * @param title
     */
    public IjkPlayerView setTitle(String title) {
        mTvTitle.setText(title);
        return this;
    }

    /**
     * ???????????????????????????
     */
    public IjkPlayerView alwaysFullScreen() {
        mIsAlwaysFullScreen = true;
        _setFullScreen(true);
        mIvFullscreen.setVisibility(GONE);
        mAttachActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        _setUiLayoutFullscreen();
        return this;
    }

    /**
     * ????????????
     *
     * @return
     */
    public void start() {
        if (mIsPlayComplete) {
            if (mDanmakuView != null && mDanmakuView.isPrepared()) {
                mDanmakuView.seekTo((long) 0);
                mDanmakuView.pause();
            }
            mIsPlayComplete = false;
        }
        if (!mVideoView.isPlaying()) {
            mIvPlay.setSelected(true);
//            if (mInterruptPosition > 0) {
//                mLoadingView.setVisibility(VISIBLE);
//                mHandler.sendEmptyMessage(MSG_TRY_RELOAD);
//            } else {
            mVideoView.start();
            // ????????????
            mHandler.sendEmptyMessage(MSG_UPDATE_SEEK);
//            }
        }
        if (mIsNeverPlay) {
            mIsNeverPlay = false;
            mIvPlayCircle.setVisibility(GONE);
            mLoadingView.setVisibility(VISIBLE);
            mIsShowBar = false;
            // ?????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
            _loadDanmaku();
        }
        // ?????????????????????????????????
        mAttachActivity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    /**
     * ????????????
     */
    public void reload() {
        mFlReload.setVisibility(GONE);
        mLoadingView.setVisibility(VISIBLE);
        if (mIsReady) {
            // ?????????????????????
            if (NetWorkUtils.isNetworkAvailable(mAttachActivity)) {
                mVideoView.reload();
                mVideoView.start();
//                start();
                if (mInterruptPosition > 0) {
                    seekTo(mInterruptPosition);
                    mInterruptPosition = 0;
                }
            }
        } else {
            mVideoView.release(false);
            mVideoView.setRender(IjkVideoView.RENDER_TEXTURE_VIEW);
            start();
        }
        // ????????????
        mHandler.removeMessages(MSG_UPDATE_SEEK);
        mHandler.sendEmptyMessage(MSG_UPDATE_SEEK);
    }

    /**
     * ??????????????????
     *
     * @return
     */
    public boolean isPlaying() {
        return mVideoView.isPlaying();
    }

    /**
     * ??????
     */
    public void pause() {
        mIvPlay.setSelected(false);
        if (mVideoView.isPlaying()) {
            mVideoView.pause();
        }
        _pauseDanmaku();
        // ?????????????????????????????????
        mAttachActivity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    /**
     * ??????
     *
     * @param position ??????
     */
    public void seekTo(int position) {
        mVideoView.seekTo(position);
        mDanmakuTargetPosition = position;
    }

    /**
     * ??????
     */
    public void stop() {
        pause();
        mVideoView.stopPlayback();
    }

    /**
     * ????????????
     */
    public void reset() {
        if (mIsEnableDanmaku && mDanmakuView != null) {
            _toggleDanmakuView(false);
            mDanmakuView.release();
            mDanmakuView = null;
            mIsEnableDanmaku = false;
        }
        mIsNeverPlay = true;
        mCurPosition = 0;
        stop();
        mVideoView.setRender(IjkVideoView.RENDER_TEXTURE_VIEW);
    }

    /**============================ ??????????????? ============================*/

    /**
     * SeekBar??????
     */
    private final OnSeekBarChangeListener mSeekListener = new OnSeekBarChangeListener() {

        private long curPosition;

        @Override
        public void onStartTrackingTouch(SeekBar bar) {
            mIsSeeking = true;
            _showControlBar(3600000);
            mHandler.removeMessages(MSG_UPDATE_SEEK);
            curPosition = mVideoView.getCurrentPosition();
        }

        @Override
        public void onProgressChanged(SeekBar bar, int progress, boolean fromUser) {
            if (!fromUser) {
                // We're not interested in programmatically generated changes to
                // the progress bar's position.
                return;
            }
            long duration = mVideoView.getDuration();
            // ??????????????????
            mTargetPosition = (duration * progress) / MAX_VIDEO_SEEK;
            int deltaTime = (int) ((mTargetPosition - curPosition) / 1000);
            String desc;
            // ??????????????????????????????????????????
            if (mTargetPosition > curPosition) {
                desc = generateTime(mTargetPosition) + "/" + generateTime(duration) + "\n" + "+" + deltaTime + "???";
            } else {
                desc = generateTime(mTargetPosition) + "/" + generateTime(duration) + "\n" + deltaTime + "???";
            }
            _setFastForward(desc);
        }

        @Override
        public void onStopTrackingTouch(SeekBar bar) {
            _hideTouchView();
            mIsSeeking = false;
            // ????????????
            seekTo((int) mTargetPosition);
            mTargetPosition = INVALID_VALUE;
            _setProgress();
            _showControlBar(DEFAULT_HIDE_TIMEOUT);
        }
    };

    /**
     * ????????????Runnable
     */
    private Runnable mHideBarRunnable = new Runnable() {
        @Override
        public void run() {
            _hideAllView(false);
        }
    };

    /**
     * ??????????????????????????????
     */
    private void _hideAllView(boolean isTouchLock) {
//        mPlayerThumb.setVisibility(View.GONE);
        mFlTouchLayout.setVisibility(View.GONE);
        mFullscreenTopBar.setVisibility(View.GONE);
        mWindowTopBar.setVisibility(View.GONE);
        mLlBottomBar.setVisibility(View.GONE);
        _showAspectRatioOptions(false);
        if (!isTouchLock) {
            mIvPlayerLock.setVisibility(View.GONE);
            mIsShowBar = false;
        }
        if (mIsEnableDanmaku) {
            mDanmakuPlayerSeek.setVisibility(GONE);
        }
        if (mIsNeedRecoverScreen) {
            mTvRecoverScreen.setVisibility(GONE);
        }
    }

    /**
     * ??????????????????????????????
     *
     * @param isShowBar
     */
    private void _setControlBarVisible(boolean isShowBar) {
        if (mIsNeverPlay) {
            mIvPlayCircle.setVisibility(isShowBar ? View.VISIBLE : View.GONE);
        } else if (mIsForbidTouch) {
            mIvPlayerLock.setVisibility(isShowBar ? View.VISIBLE : View.GONE);
        } else {
            mLlBottomBar.setVisibility(isShowBar ? View.VISIBLE : View.GONE);
            if (!isShowBar) {
                _showAspectRatioOptions(false);
            }
            // ???????????????????????????????????????
            if (mIsFullscreen) {
                // ???????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
                mTvSystemTime.setText(StringUtils.getCurFormatTime());
                mFullscreenTopBar.setVisibility(isShowBar ? View.VISIBLE : View.GONE);
                mWindowTopBar.setVisibility(View.GONE);
                mIvPlayerLock.setVisibility(isShowBar ? View.VISIBLE : View.GONE);
                if (mIsEnableDanmaku) {
                    mDanmakuPlayerSeek.setVisibility(isShowBar ? View.VISIBLE : View.GONE);
                }
                if (mIsNeedRecoverScreen) {
                    mTvRecoverScreen.setVisibility(isShowBar ? View.VISIBLE : View.GONE);
                }
            } else {
                mWindowTopBar.setVisibility(isShowBar ? View.VISIBLE : View.GONE);
                mFullscreenTopBar.setVisibility(View.GONE);
                mIvPlayerLock.setVisibility(View.GONE);
                if (mIsEnableDanmaku) {
                    mDanmakuPlayerSeek.setVisibility(GONE);
                }
                if (mIsNeedRecoverScreen) {
                    mTvRecoverScreen.setVisibility(View.GONE);
                }
            }
        }
    }

    /**
     * ???????????????????????????????????????
     */
    private void _toggleControlBar() {
        mIsShowBar = !mIsShowBar;
        _setControlBarVisible(mIsShowBar);
        if (mIsShowBar) {
            // ????????????????????????????????????
            mHandler.postDelayed(mHideBarRunnable, DEFAULT_HIDE_TIMEOUT);
            // ???????????? Seek ??????
            mHandler.sendEmptyMessage(MSG_UPDATE_SEEK);
        }
    }

    /**
     * ???????????????
     *
     * @param timeout ??????????????????
     */
    private void _showControlBar(int timeout) {
        if (!mIsShowBar) {
            _setProgress();
            mIsShowBar = true;
        }
        _setControlBarVisible(true);
        mHandler.sendEmptyMessage(MSG_UPDATE_SEEK);
        // ???????????????????????? Runnable????????? timeout=0 ???????????????????????????
        mHandler.removeCallbacks(mHideBarRunnable);
        if (timeout != 0) {
            mHandler.postDelayed(mHideBarRunnable, timeout);
        }
    }

    /**
     * ??????????????????????????????????????????
     */
    private void _togglePlayStatus() {
        if (mVideoView.isPlaying()) {
            pause();
        } else {
            start();
        }
    }

    /**
     * ??????????????????????????????
     */
    private void _refreshHideRunnable() {
        mHandler.removeCallbacks(mHideBarRunnable);
        mHandler.postDelayed(mHideBarRunnable, DEFAULT_HIDE_TIMEOUT);
    }

    /**
     * ???????????????
     */
    private void _togglePlayerLock() {
        mIsForbidTouch = !mIsForbidTouch;
        mIvPlayerLock.setSelected(mIsForbidTouch);
        if (mIsForbidTouch) {
            mOrientationListener.disable();
            _hideAllView(true);
        } else {
            if (!mIsForbidOrientation) {
                mOrientationListener.enable();
            }
            mFullscreenTopBar.setVisibility(View.VISIBLE);
            mLlBottomBar.setVisibility(View.VISIBLE);
            if (mIsEnableDanmaku) {
                mDanmakuPlayerSeek.setVisibility(VISIBLE);
            }
            if (mIsNeedRecoverScreen) {
                mTvRecoverScreen.setVisibility(VISIBLE);
            }
        }
    }

    /**
     * ???????????????????????????
     */
    private void _toggleMediaQuality() {
        if (mFlMediaQuality.getVisibility() == GONE) {
            mFlMediaQuality.setVisibility(VISIBLE);
        }
        if (mIsShowQuality) {
            ViewCompat.animate(mFlMediaQuality).translationX(mFlMediaQuality.getWidth()).setDuration(DEFAULT_QUALITY_TIME);
            mIsShowQuality = false;
        } else {
            ViewCompat.animate(mFlMediaQuality).translationX(0).setDuration(DEFAULT_QUALITY_TIME);
            mIsShowQuality = true;
        }
    }

    /**
     * ?????????????????????
     *
     * @param isShow
     */
    private void _showAspectRatioOptions(boolean isShow) {
        if (isShow) {
            AnimHelper.doClipViewHeight(mAspectRatioOptions, 0, mAspectOptionsHeight, 150);
        } else {
            ViewGroup.LayoutParams layoutParams = mAspectRatioOptions.getLayoutParams();
            layoutParams.height = 0;
        }
    }

    @Override
    public void onClick(View v) {
        _refreshHideRunnable();
        int id = v.getId();
        if (id == R.id.iv_back) {
            if (mIsAlwaysFullScreen) {
                _exit();
                return;
            }
//            mAttachActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            _toggleFullScreen();
        } else if (id == R.id.iv_back_window) {
            mAttachActivity.finish();
        } else if (id == R.id.iv_play || id == R.id.iv_play_circle) {
            _togglePlayStatus();
        } else if (id == R.id.iv_fullscreen) {
            _toggleFullScreen();
        } else if (id == R.id.iv_player_lock) {
            _togglePlayerLock();
        } else if (id == R.id.iv_media_quality) {
            if (!mIsShowQuality) {
                _toggleMediaQuality();
            }
        } else if (id == R.id.iv_cancel_skip) {
            mHandler.removeCallbacks(mHideSkipTipRunnable);
            _hideSkipTip();
        } else if (id == R.id.tv_do_skip) {
            mLoadingView.setVisibility(VISIBLE);
            // ????????????
            seekTo(mSkipPosition);
            mHandler.removeCallbacks(mHideSkipTipRunnable);
            _hideSkipTip();
            _setProgress();
        } else if (id == R.id.iv_danmaku_control) {
            _toggleDanmakuShow();
        } else if (id == R.id.tv_open_edit_danmaku) {
            if (mDanmakuListener == null || mDanmakuListener.isValid()) {
                editVideo();
                mEditDanmakuLayout.setVisibility(VISIBLE);
                SoftInputUtils.setEditFocusable(mAttachActivity, mEtDanmakuContent);
            }
        } else if (id == R.id.iv_cancel_send) {
            recoverFromEditVideo();
        } else if (id == R.id.iv_do_send) {
            recoverFromEditVideo();
            sendDanmaku(mEtDanmakuContent.getText().toString(), false);
            mEtDanmakuContent.setText("");
        } else if (id == R.id.input_options_more) {
            _toggleMoreColorOptions();
        } else if (id == R.id.iv_screenshot) {
            _doScreenshot();
        } else if (id == R.id.tv_recover_screen) {
            mVideoView.resetVideoView(true);
            mIsNeedRecoverScreen = false;
            mTvRecoverScreen.setVisibility(GONE);
        } else if (id == R.id.tv_settings) {
            _showAspectRatioOptions(true);
        } else if (id == R.id.tv_reload) {
            reload();
        }
    }

    /**==================== ????????????/???????????? ====================*/

    /**
     * ??????????????????
     */
    public IjkPlayerView enableOrientation() {
        mIsForbidOrientation = false;
        mOrientationListener.enable();
        return this;
    }

    /**
     * ?????????????????????????????????
     */
    private void _toggleFullScreen() {
        if (WindowUtils.getScreenOrientation(mAttachActivity) == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
            mAttachActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        } else {
            mAttachActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
    }

    /**
     * ???????????????????????????
     *
     * @param isFullscreen
     */
    private void _setFullScreen(boolean isFullscreen) {
        mIsFullscreen = isFullscreen;
        // ????????????????????????
        _toggleDanmakuView(isFullscreen);
        _handleActionBar(isFullscreen);
        _changeHeight(isFullscreen);
        mIvFullscreen.setSelected(isFullscreen);
        mHandler.post(mHideBarRunnable);
        mIvMediaQuality.setVisibility(isFullscreen ? VISIBLE : GONE);
        mLlBottomBar.setBackgroundResource(isFullscreen ? R.color.bg_video_view : android.R.color.transparent);
        if (mIsShowQuality && !isFullscreen) {
            _toggleMediaQuality();
        }
        // ??????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
        if (mIsNeedRecoverScreen) {
            if (isFullscreen) {
                mVideoView.adjustVideoView(1.0f);
                mTvRecoverScreen.setVisibility(mIsShowBar ? View.VISIBLE : View.GONE);
            } else {
                mVideoView.resetVideoView(false);
                mTvRecoverScreen.setVisibility(GONE);
            }
        }
        // ??????????????????????????????
        if (!isFullscreen) {
            _showAspectRatioOptions(false);
        }
    }

    /**
     * ??????????????????
     *
     * @param orientation
     */
    private void _handleOrientation(int orientation) {
        if (mIsNeverPlay) {
            return;
        }
        if (mIsFullscreen && !mIsAlwaysFullScreen) {
            // ???????????????????????????????????????????????????????????????????????????
            if (orientation >= 0 && orientation <= 30 || orientation >= 330) {
                // ??????????????????
                mAttachActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            }
        } else {
            // ??????????????????????????????
            if (orientation >= 60 && orientation <= 120) {
                mAttachActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE);
            } else if (orientation >= 240 && orientation <= 300) {
                mAttachActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            }
        }
    }

    /**
     * ???????????????????????????????????????????????????????????????3000ms??????????????????????????????????????????
     */
    private void _refreshOrientationEnable() {
        if (!mIsForbidOrientation) {
            mOrientationListener.disable();
            mHandler.removeMessages(MSG_ENABLE_ORIENTATION);
            mHandler.sendEmptyMessageDelayed(MSG_ENABLE_ORIENTATION, 3000);
        }
    }

    /**
     * ??????/?????? ActionBar
     *
     * @param isFullscreen
     */
    private void _handleActionBar(boolean isFullscreen) {
        ActionBar supportActionBar = mAttachActivity.getSupportActionBar();
        if (supportActionBar != null) {
            if (isFullscreen) {
                supportActionBar.hide();
            } else {
                supportActionBar.show();
            }
        }
    }

    /**
     * ????????????????????????
     *
     * @param isFullscreen
     */
    private void _changeHeight(boolean isFullscreen) {
        if (mIsAlwaysFullScreen) {
            return;
        }
        ViewGroup.LayoutParams layoutParams = getLayoutParams();
        if (isFullscreen) {
            // ???????????????????????????
            layoutParams.height = mWidthPixels;
        } else {
            // ????????????
            layoutParams.height = mInitHeight;
        }
        setLayoutParams(layoutParams);
    }

    /**
     * ??????UI???????????????
     */
    private void _setUiLayoutFullscreen() {
        if (Build.VERSION.SDK_INT >= 14) {
            // ???????????? Activity ??? DecorView
            View decorView = mAttachActivity.getWindow().getDecorView();
            // ?????????????????????Flag
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
                    View.SYSTEM_UI_FLAG_FULLSCREEN |
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                    View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION |
                    View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                    View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
            );
            mAttachActivity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
    }

    /**
     * ?????????????????????????????? Activity.configurationChanged() ??????
     * SYSTEM_UI_FLAG_LAYOUT_STABLE??????????????????????????????
     * SYSTEM_UI_FLAG_FULLSCREEN???Activity?????????????????????????????????????????????
     * SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN???Activity???????????????????????????????????????????????????????????????????????????Activity????????????????????????????????????
     * SYSTEM_UI_FLAG_HIDE_NAVIGATION?????????????????????(?????????)
     * SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION????????????View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
     * SYSTEM_UI_FLAG_IMMERSIVE?????????????????????????????????????????????????????????????????????
     * SYSTEM_UI_FLAG_IMMERSIVE_STICKY???????????????????????????????????????????????????????????????????????????????????????
     *
     * @param newConfig
     */
    public void configurationChanged(Configuration newConfig) {
        _refreshOrientationEnable();
        // ??????????????????SDK19????????????
        if (Build.VERSION.SDK_INT >= 14) {
            if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                // ???????????? Activity ??? DecorView
                View decorView = mAttachActivity.getWindow().getDecorView();
                // ??????????????????
                mScreenUiVisibility = decorView.getSystemUiVisibility();
                // ?????????????????????Flag
                decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
                        View.SYSTEM_UI_FLAG_FULLSCREEN |
                        View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                        View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION |
                        View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                        View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                );
                _setFullScreen(true);
                mAttachActivity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
                View decorView = mAttachActivity.getWindow().getDecorView();
                // ??????
                decorView.setSystemUiVisibility(mScreenUiVisibility);
                _setFullScreen(false);
                mAttachActivity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            }
        }
    }

    /**
     * ????????????????????????????????????{@link #alwaysFullScreen()}
     */
    private void _exit() {
        if (System.currentTimeMillis() - mExitTime > 2000) {
            Toast.makeText(mAttachActivity, "??????????????????", Toast.LENGTH_SHORT).show();
            mExitTime = System.currentTimeMillis();
        } else {
            mAttachActivity.finish();
        }
    }

    /**============================ ?????????????????? ============================*/

    /**
     * ????????????
     */
    private OnGestureListener mPlayerGestureListener = new SimpleOnGestureListener() {
        // ???????????????????????????????????????????????????true??????????????????false???????????????
        private boolean isDownTouch;
        // ??????????????????,????????????????????????true??????????????????false???????????????
        private boolean isVolume;
        // ?????????????????????????????????????????????true??????????????????false???????????????
        private boolean isLandscape;
        // ?????????????????????????????????
        private boolean isRecoverFromDanmaku;

        @Override
        public boolean onDown(MotionEvent e) {
            isDownTouch = true;
            isRecoverFromDanmaku = recoverFromEditVideo();
            return super.onDown(e);
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            if (!mIsForbidTouch && !mIsNeverPlay) {
                float mOldX = e1.getX(), mOldY = e1.getY();
                float deltaY = mOldY - e2.getY();
                float deltaX = mOldX - e2.getX();
                if (isDownTouch) {
                    // ???????????????????????????
                    isLandscape = Math.abs(distanceX) >= Math.abs(distanceY);
                    // ??????????????????????????????
                    isVolume = mOldX > getResources().getDisplayMetrics().widthPixels * 0.5f;
                    isDownTouch = false;
                }

                if (isLandscape) {
                    _onProgressSlide(-deltaX / mVideoView.getWidth());
                } else {
                    float percent = deltaY / mVideoView.getHeight();
                    if (isVolume) {
                        _onVolumeSlide(percent);
                    } else {
                        _onBrightnessSlide(percent);
                    }
                }
            }
            return super.onScroll(e1, e2, distanceX, distanceY);
        }

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            // ????????????????????????????????????????????????
            if (isRecoverFromDanmaku) {
                return true;
            }
            if (mIsShowQuality) {
                _toggleMediaQuality();
            } else {
                _toggleControlBar();
            }
            return true;
        }

        @Override
        public boolean onDoubleTap(MotionEvent e) {
            // ???????????????????????????????????????????????????????????????????????????
            if (mIsNeverPlay || isRecoverFromDanmaku) {
                return true;
            }
            if (!mIsForbidTouch) {
                _refreshHideRunnable();
                _togglePlayStatus();
            }
            return true;
        }
    };

    /**
     * ????????????Runnable
     */
    private Runnable mHideTouchViewRunnable = new Runnable() {
        @Override
        public void run() {
            _hideTouchView();
        }
    };

    /**
     * ????????????
     */
    private OnTouchListener mPlayerTouchListener = new OnTouchListener() {
        // ?????????????????????????????????????????????
        private static final int NORMAL = 1;
        private static final int INVALID_POINTER = 2;
        private static final int ZOOM_AND_ROTATE = 3;
        // ????????????
        private int mode = NORMAL;
        // ???????????????
        private PointF midPoint = new PointF(0, 0);
        // ????????????
        private float degree = 0;
        // ?????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
        private int fingerFlag = INVALID_VALUE;
        // ????????????
        private float oldDist;
        // ????????????
        private float scale;

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (MotionEventCompat.getActionMasked(event)) {
                case MotionEvent.ACTION_DOWN:
                    mode = NORMAL;
                    mHandler.removeCallbacks(mHideBarRunnable);
                    break;

                case MotionEvent.ACTION_POINTER_DOWN:
                    if (event.getPointerCount() == 3 && mIsFullscreen) {
                        _hideTouchView();
                        // ??????????????????????????????????????????????????????
                        mode = ZOOM_AND_ROTATE;
                        MotionEventUtils.midPoint(midPoint, event);
                        fingerFlag = MotionEventUtils.calcFingerFlag(event);
                        degree = MotionEventUtils.rotation(event, fingerFlag);
                        oldDist = MotionEventUtils.calcSpacing(event, fingerFlag);
                        // ??????????????? Matrix
                        mSaveMatrix = mVideoView.getVideoTransform();
                    } else {
                        mode = INVALID_POINTER;
                    }
                    break;

                case MotionEvent.ACTION_MOVE:
                    if (mode == ZOOM_AND_ROTATE) {
                        // ????????????
                        float newRotate = MotionEventUtils.rotation(event, fingerFlag);
                        mVideoView.setVideoRotation((int) (newRotate - degree));
                        // ????????????
                        mVideoMatrix.set(mSaveMatrix);
                        float newDist = MotionEventUtils.calcSpacing(event, fingerFlag);
                        scale = newDist / oldDist;
                        mVideoMatrix.postScale(scale, scale, midPoint.x, midPoint.y);
                        mVideoView.setVideoTransform(mVideoMatrix);
                    }
                    break;

                case MotionEvent.ACTION_POINTER_UP:
                    if (mode == ZOOM_AND_ROTATE) {
                        // ???????????????????????????????????????????????????
                        mIsNeedRecoverScreen = mVideoView.adjustVideoView(scale);
                        if (mIsNeedRecoverScreen && mIsShowBar) {
                            mTvRecoverScreen.setVisibility(VISIBLE);
                        }
                    }
                    mode = INVALID_POINTER;
                    break;
            }
            // ??????????????????
            if (mode == NORMAL) {
                if (mGestureDetector.onTouchEvent(event)) {
                    return true;
                }
                if (MotionEventCompat.getActionMasked(event) == MotionEvent.ACTION_UP) {
                    _endGesture();
                }
            }
            return false;
        }
    };

    /**
     * ???????????????
     *
     * @return
     */
    private int _setProgress() {
        if (mVideoView == null || mIsSeeking) {
            return 0;
        }
        // ???????????????????????????
        int position = Math.max(mVideoView.getCurrentPosition(), mInterruptPosition);
        // ??????????????????
        int duration = mVideoView.getDuration();
        if (duration > 0) {
            // ????????? Seek ??????????????????
            long pos = (long) MAX_VIDEO_SEEK * position / duration;
            mPlayerSeek.setProgress((int) pos);
            if (mIsEnableDanmaku) {
                mDanmakuPlayerSeek.setProgress((int) pos);
            }
        }
        // ????????????????????????????????????????????? Seek ????????????
        int percent = mVideoView.getBufferPercentage();
        mPlayerSeek.setSecondaryProgress(percent * 10);
        if (mIsEnableDanmaku) {
            mDanmakuPlayerSeek.setSecondaryProgress(percent * 10);
        }
        // ??????????????????
        mTvCurTime.setText(generateTime(position));
        mTvEndTime.setText(generateTime(duration));
        // ????????????????????????
        return position;
    }

    /**
     * ????????????
     *
     * @param time
     */
    private void _setFastForward(String time) {
        if (mFlTouchLayout.getVisibility() == View.GONE) {
            mFlTouchLayout.setVisibility(View.VISIBLE);
        }
        if (mTvFastForward.getVisibility() == View.GONE) {
            mTvFastForward.setVisibility(View.VISIBLE);
        }
        mTvFastForward.setText(time);
    }

    /**
     * ??????????????????
     */
    private void _hideTouchView() {
        if (mFlTouchLayout.getVisibility() == View.VISIBLE) {
            mTvFastForward.setVisibility(View.GONE);
            mTvVolume.setVisibility(View.GONE);
            mTvBrightness.setVisibility(View.GONE);
            mFlTouchLayout.setVisibility(View.GONE);
        }
    }

    /**
     * ??????????????????????????????????????????????????????????????????????????? SeekBar
     *
     * @param percent ???????????????
     */
    private void _onProgressSlide(float percent) {
        int position = mVideoView.getCurrentPosition();
        long duration = mVideoView.getDuration();
        // ??????????????????????????????100?????????????????????1/2
        long deltaMax = Math.min(100 * 1000, duration / 2);
        // ??????????????????
        long delta = (long) (deltaMax * percent);
        // ????????????
        mTargetPosition = delta + position;
        if (mTargetPosition > duration) {
            mTargetPosition = duration;
        } else if (mTargetPosition <= 0) {
            mTargetPosition = 0;
        }
        int deltaTime = (int) ((mTargetPosition - position) / 1000);
        String desc;
        // ??????????????????????????????????????????
        if (mTargetPosition > position) {
            desc = generateTime(mTargetPosition) + "/" + generateTime(duration) + "\n" + "+" + deltaTime + "???";
        } else {
            desc = generateTime(mTargetPosition) + "/" + generateTime(duration) + "\n" + deltaTime + "???";
        }
        _setFastForward(desc);
    }

    /**
     * ????????????????????????
     *
     * @param volume
     */
    private void _setVolumeInfo(int volume) {
        if (mFlTouchLayout.getVisibility() == View.GONE) {
            mFlTouchLayout.setVisibility(View.VISIBLE);
        }
        if (mTvVolume.getVisibility() == View.GONE) {
            mTvVolume.setVisibility(View.VISIBLE);
        }
        mTvVolume.setText((volume * 100 / mMaxVolume) + "%");
    }

    /**
     * ????????????????????????
     *
     * @param percent
     */
    private void _onVolumeSlide(float percent) {
        if (mCurVolume == INVALID_VALUE) {
            mCurVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
            if (mCurVolume < 0) {
                mCurVolume = 0;
            }
        }
        int index = (int) (percent * mMaxVolume) + mCurVolume;
        if (index > mMaxVolume) {
            index = mMaxVolume;
        } else if (index < 0) {
            index = 0;
        }
        // ????????????
        mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, index, 0);
        // ???????????????
        _setVolumeInfo(index);
    }


    /**
     * ???????????????????????????????????????????????? 1/15
     *
     * @param isIncrease ???????????????
     */
    private void _setVolume(boolean isIncrease) {
        int curVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        if (isIncrease) {
            curVolume += mMaxVolume / 15;
        } else {
            curVolume -= mMaxVolume / 15;
        }
        if (curVolume > mMaxVolume) {
            curVolume = mMaxVolume;
        } else if (curVolume < 0) {
            curVolume = 0;
        }
        // ????????????
        mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, curVolume, 0);
        // ???????????????
        _setVolumeInfo(curVolume);
        mHandler.removeCallbacks(mHideTouchViewRunnable);
        mHandler.postDelayed(mHideTouchViewRunnable, 1000);
    }

    /**
     * ????????????????????????
     *
     * @param brightness
     */
    private void _setBrightnessInfo(float brightness) {
        if (mFlTouchLayout.getVisibility() == View.GONE) {
            mFlTouchLayout.setVisibility(View.VISIBLE);
        }
        if (mTvBrightness.getVisibility() == View.GONE) {
            mTvBrightness.setVisibility(View.VISIBLE);
        }
        mTvBrightness.setText(Math.ceil(brightness * 100) + "%");
    }

    /**
     * ????????????????????????
     *
     * @param percent
     */
    private void _onBrightnessSlide(float percent) {
        if (mCurBrightness < 0) {
            mCurBrightness = mAttachActivity.getWindow().getAttributes().screenBrightness;
            if (mCurBrightness < 0.0f) {
                mCurBrightness = 0.5f;
            } else if (mCurBrightness < 0.01f) {
                mCurBrightness = 0.01f;
            }
        }
        WindowManager.LayoutParams attributes = mAttachActivity.getWindow().getAttributes();
        attributes.screenBrightness = mCurBrightness + percent;
        if (attributes.screenBrightness > 1.0f) {
            attributes.screenBrightness = 1.0f;
        } else if (attributes.screenBrightness < 0.01f) {
            attributes.screenBrightness = 0.01f;
        }
        _setBrightnessInfo(attributes.screenBrightness);
        mAttachActivity.getWindow().setAttributes(attributes);
    }

    /**
     * ??????????????????
     */
    private void _endGesture() {
        if (mTargetPosition >= 0 && mTargetPosition != mVideoView.getCurrentPosition()) {
            // ????????????????????????
            seekTo((int) mTargetPosition);
            mPlayerSeek.setProgress((int) (mTargetPosition * MAX_VIDEO_SEEK / mVideoView.getDuration()));
            if (mIsEnableDanmaku) {
                mDanmakuPlayerSeek.setProgress((int) (mTargetPosition * MAX_VIDEO_SEEK / mVideoView.getDuration()));
            }
            mTargetPosition = INVALID_VALUE;
        }
        // ??????????????????????????????
        _hideTouchView();
        _refreshHideRunnable();
        mCurVolume = INVALID_VALUE;
        mCurBrightness = INVALID_VALUE;
    }

    /**
     * ============================ ?????????????????? ============================
     */

    // ?????????????????????????????????????????????
    private boolean mIsRenderingStart = false;
    // ????????????????????????????????????????????????????????????
    private boolean mIsBufferingStart = false;

    // ????????????????????????
    private OnInfoListener mInfoListener = new OnInfoListener() {
        @Override
        public boolean onInfo(IMediaPlayer iMediaPlayer, int status, int extra) {
            _switchStatus(status);
            if (mOutsideInfoListener != null) {
                mOutsideInfoListener.onInfo(iMediaPlayer, status, extra);
            }
            return true;
        }
    };

    /**
     * ????????????????????????
     *
     * @param status
     */
    private void _switchStatus(int status) {
        Log.i("IjkPlayerView", "status " + status);
        switch (status) {
            case IMediaPlayer.MEDIA_INFO_BUFFERING_START:
                mIsBufferingStart = true;
                _pauseDanmaku();
                if (!mIsNeverPlay) {
                    mLoadingView.setVisibility(View.VISIBLE);
                }
                mHandler.removeMessages(MSG_TRY_RELOAD);
            case MediaPlayerParams.STATE_PREPARING:
                break;

            case MediaPlayerParams.STATE_PREPARED:
                mIsReady = true;
                break;

            case IMediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START:
                mIsRenderingStart = true;
            case IMediaPlayer.MEDIA_INFO_BUFFERING_END:
                mIsBufferingStart = false;
                mLoadingView.setVisibility(View.GONE);
                mPlayerThumb.setVisibility(View.GONE);
                // ????????????
                mHandler.removeMessages(MSG_UPDATE_SEEK);
                mHandler.sendEmptyMessage(MSG_UPDATE_SEEK);
                if (mSkipPosition != INVALID_VALUE) {
                    _showSkipTip(); // ??????????????????
                }
                if (mVideoView.isPlaying() && mIsNetConnected) {
                    mInterruptPosition = 0;
                    _resumeDanmaku();   // ????????????
                    if (!mIvPlay.isSelected()) {
                        // ?????????????????????????????????????????????
                        mVideoView.start();
                        mIvPlay.setSelected(true);
                    }
                }
                break;

            case MediaPlayerParams.STATE_PLAYING:
                mHandler.removeMessages(MSG_TRY_RELOAD);
                if (mIsRenderingStart && !mIsBufferingStart && mVideoView.getCurrentPosition() > 0) {
                    _resumeDanmaku();   // ????????????
                }
                break;
            case MediaPlayerParams.STATE_ERROR:
                mInterruptPosition = Math.max(mVideoView.getInterruptPosition(), mInterruptPosition);
                pause();
                if (mVideoView.getDuration() == -1 && !mIsReady) {
                    mLoadingView.setVisibility(View.GONE);
                    mPlayerThumb.setVisibility(View.GONE);
                    mIvPlayCircle.setVisibility(GONE);
                    mFlReload.setVisibility(VISIBLE);
                } else {
                    mLoadingView.setVisibility(VISIBLE);
                    mHandler.sendEmptyMessage(MSG_TRY_RELOAD);
                }
                break;

            case MediaPlayerParams.STATE_COMPLETED:
                pause();
                if (mVideoView.getDuration() == -1 ||
                        (mVideoView.getInterruptPosition() + INTERVAL_TIME < mVideoView.getDuration())) {
                    mInterruptPosition = Math.max(mVideoView.getInterruptPosition(), mInterruptPosition);
                    Toast.makeText(mAttachActivity, "????????????", Toast.LENGTH_SHORT).show();
                } else {
                    mIsPlayComplete = true;
                    if (mCompletionListener != null) {
                        mCompletionListener.onCompletion(mVideoView.getMediaPlayer());
                    }
                }
                break;
        }
    }

    /**============================ Listener ============================*/

    /**
     * Register a callback to be invoked when the media file
     * is loaded and ready to go.
     *
     * @param l The callback that will be run
     */
    public void setOnPreparedListener(IMediaPlayer.OnPreparedListener l) {
        mVideoView.setOnPreparedListener(l);
    }

    /**
     * Register a callback to be invoked when the end of a media file
     * has been reached during playback.
     *
     * @param l The callback that will be run
     */
    public void setOnCompletionListener(IMediaPlayer.OnCompletionListener l) {
        mCompletionListener = l;
//        mVideoView.setOnCompletionListener(l);
    }

    /**
     * Register a callback to be invoked when an error occurs
     * during playback or setup.  If no listener is specified,
     * or if the listener returned false, VideoView will inform
     * the user of any errors.
     *
     * @param l The callback that will be run
     */
    public void setOnErrorListener(IMediaPlayer.OnErrorListener l) {
        mVideoView.setOnErrorListener(l);
    }

    /**
     * Register a callback to be invoked when an informational event
     * occurs during playback or setup.
     *
     * @param l The callback that will be run
     */
    public void setOnInfoListener(OnInfoListener l) {
        mOutsideInfoListener = l;
    }

    /**
     * ?????????????????????
     *
     * @param danmakuListener
     */
    public IjkPlayerView setDanmakuListener(OnDanmakuListener danmakuListener) {
        mDanmakuListener = danmakuListener;
        return this;
    }

    /**
     * ============================ ??????????????? ============================
     */

    // ????????????/?????????????????????????????????
    private static final int DEFAULT_QUALITY_TIME = 300;
    /**
     * ??????????????????????????????????????????????????????1080P
     */
    public static final int MEDIA_QUALITY_SMOOTH = 0;
    public static final int MEDIA_QUALITY_MEDIUM = 1;
    public static final int MEDIA_QUALITY_HIGH = 2;
    public static final int MEDIA_QUALITY_SUPER = 3;
    public static final int MEDIA_QUALITY_BD = 4;

    private static final int QUALITY_DRAWABLE_RES[] = new int[]{
            R.mipmap.ic_media_quality_smooth, R.mipmap.ic_media_quality_medium, R.mipmap.ic_media_quality_high,
            R.mipmap.ic_media_quality_super, R.mipmap.ic_media_quality_bd
    };
    // ??????Video Url
    private SparseArray<String> mVideoSource = new SparseArray<>();
    // ????????????
    private String[] mMediaQualityDesc;
    // ?????????????????????
    private View mFlMediaQuality;
    // ?????????
    private TextView mIvMediaQuality;
    // ?????????????????????
    private ListView mLvMediaQuality;
    // ??????????????????????????????
    private AdapterMediaQuality mQualityAdapter;
    // ????????????
    private List<MediaQualityInfo> mQualityData;
    // ?????????????????????????????????
    private boolean mIsShowQuality = false;
    // ????????????????????????
    private
    @MediaQuality
    int mCurSelectQuality = MEDIA_QUALITY_SMOOTH;

    @Retention(RetentionPolicy.SOURCE)
    @Target({ElementType.PARAMETER, ElementType.FIELD, ElementType.METHOD})
    @IntDef({MEDIA_QUALITY_SMOOTH, MEDIA_QUALITY_MEDIUM, MEDIA_QUALITY_HIGH, MEDIA_QUALITY_SUPER, MEDIA_QUALITY_BD})
    public @interface MediaQuality {
    }

    /**
     * ??????????????????????????????
     */
    private void _initMediaQuality() {
        mMediaQualityDesc = getResources().getStringArray(R.array.media_quality);
        mFlMediaQuality = findViewById(R.id.fl_media_quality);
        mIvMediaQuality = (TextView) findViewById(R.id.iv_media_quality);
        mIvMediaQuality.setOnClickListener(this);
        mLvMediaQuality = (ListView) findViewById(R.id.lv_media_quality);
        mQualityAdapter = new AdapterMediaQuality(mAttachActivity);
        mLvMediaQuality.setAdapter(mQualityAdapter);
        mLvMediaQuality.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mCurSelectQuality != mQualityAdapter.getItem(position).getIndex()) {
                    setMediaQuality(mQualityAdapter.getItem(position).getIndex());
                    mLoadingView.setVisibility(VISIBLE);
                    start();
                }
                _toggleMediaQuality();
            }
        });
    }


    /**
     * ????????????
     *
     * @param mediaSmooth ??????
     * @param mediaMedium ??????
     * @param mediaHigh   ??????
     * @param mediaSuper  ??????
     * @param mediaBd     1080P
     * @return
     */
    public IjkPlayerView switchVideoSource(String mediaSmooth, String mediaMedium, String mediaHigh, String mediaSuper, String mediaBd) {
        reset();
        return setVideoSource(mediaSmooth, mediaMedium, mediaHigh, mediaSuper, mediaBd);
    }

    /**
     * ???????????????
     *
     * @param mediaSmooth ??????
     * @param mediaMedium ??????
     * @param mediaHigh   ??????
     * @param mediaSuper  ??????
     * @param mediaBd     1080P
     */
    public IjkPlayerView setVideoSource(String mediaSmooth, String mediaMedium, String mediaHigh, String mediaSuper, String mediaBd) {
        boolean isSelect = true;
        mQualityData = new ArrayList<>();
        if (mediaSmooth != null) {
            mVideoSource.put(MEDIA_QUALITY_SMOOTH, mediaSmooth);
            mQualityData.add(new MediaQualityInfo(MEDIA_QUALITY_SMOOTH, mMediaQualityDesc[MEDIA_QUALITY_SMOOTH], isSelect));
            mCurSelectQuality = MEDIA_QUALITY_SMOOTH;
            isSelect = false;
        }
        if (mediaMedium != null) {
            mVideoSource.put(MEDIA_QUALITY_MEDIUM, mediaMedium);
            mQualityData.add(new MediaQualityInfo(MEDIA_QUALITY_MEDIUM, mMediaQualityDesc[MEDIA_QUALITY_MEDIUM], isSelect));
            if (isSelect) {
                mCurSelectQuality = MEDIA_QUALITY_MEDIUM;
            }
            isSelect = false;
        }
        if (mediaHigh != null) {
            mVideoSource.put(MEDIA_QUALITY_HIGH, mediaHigh);
            mQualityData.add(new MediaQualityInfo(MEDIA_QUALITY_HIGH, mMediaQualityDesc[MEDIA_QUALITY_HIGH], isSelect));
            if (isSelect) {
                mCurSelectQuality = MEDIA_QUALITY_HIGH;
            }
            isSelect = false;
        }
        if (mediaSuper != null) {
            mVideoSource.put(MEDIA_QUALITY_SUPER, mediaSuper);
            mQualityData.add(new MediaQualityInfo(MEDIA_QUALITY_SUPER, mMediaQualityDesc[MEDIA_QUALITY_SUPER], isSelect));
            if (isSelect) {
                mCurSelectQuality = MEDIA_QUALITY_SUPER;
            }
            isSelect = false;
        }
        if (mediaBd != null) {
            mVideoSource.put(MEDIA_QUALITY_BD, mediaBd);
            mQualityData.add(new MediaQualityInfo(MEDIA_QUALITY_BD, mMediaQualityDesc[MEDIA_QUALITY_BD], isSelect));
            if (isSelect) {
                mCurSelectQuality = MEDIA_QUALITY_BD;
            }
        }
        mQualityAdapter.updateItems(mQualityData);
        mIvMediaQuality.setCompoundDrawablesWithIntrinsicBounds(null,
                ContextCompat.getDrawable(mAttachActivity, QUALITY_DRAWABLE_RES[mCurSelectQuality]), null, null);
        mIvMediaQuality.setText(mMediaQualityDesc[mCurSelectQuality]);
        setVideoPath(mVideoSource.get(mCurSelectQuality));
        return this;
    }

    /**
     * ???????????????
     *
     * @param quality ?????????
     *                {@link #MEDIA_QUALITY_SMOOTH,#MEDIA_QUALITY_MEDIUM,#MEDIA_QUALITY_HIGH,#MEDIA_QUALITY_SUPER,#MEDIA_QUALITY_BD}
     * @return
     */
    public IjkPlayerView setMediaQuality(@MediaQuality int quality) {
        if (mCurSelectQuality == quality || mVideoSource.get(quality) == null) {
            return this;
        }
        mQualityAdapter.setMediaQuality(quality);
        mIvMediaQuality.setCompoundDrawablesWithIntrinsicBounds(null,
                ContextCompat.getDrawable(mAttachActivity, QUALITY_DRAWABLE_RES[quality]), null, null);
        mIvMediaQuality.setText(mMediaQualityDesc[quality]);
        mCurSelectQuality = quality;
        if (mVideoView.isPlaying()) {
            mCurPosition = mVideoView.getCurrentPosition();
            mVideoView.release(false);
        }
        mVideoView.setRender(IjkVideoView.RENDER_TEXTURE_VIEW);
        setVideoPath(mVideoSource.get(quality));
        return this;
    }

    /**
     * ============================ ???????????? ============================
     */

    // ????????????
    private ImageView mIvCancelSkip;
    // ????????????
    private TextView mTvSkipTime;
    // ????????????
    private TextView mTvDoSkip;
    // ????????????
    private View mLlSkipLayout;
    // ??????????????????
    private int mSkipPosition = INVALID_VALUE;

    /**
     * ?????????????????????
     */
    private void _initVideoSkip() {
        mLlSkipLayout = findViewById(R.id.ll_skip_layout);
        mIvCancelSkip = (ImageView) findViewById(R.id.iv_cancel_skip);
        mTvSkipTime = (TextView) findViewById(R.id.tv_skip_time);
        mTvDoSkip = (TextView) findViewById(R.id.tv_do_skip);
        mIvCancelSkip.setOnClickListener(this);
        mTvDoSkip.setOnClickListener(this);
    }

    /**
     * ??????????????????
     *
     * @return
     */
    public int getCurPosition() {
        return mVideoView.getCurrentPosition();
    }

    /**
     * ??????????????????
     *
     * @param targetPosition ????????????,??????:ms
     */
    public IjkPlayerView setSkipTip(int targetPosition) {
        mSkipPosition = targetPosition;
        return this;
    }

    /**
     * ??????????????????
     */
    private void _showSkipTip() {
        if (mSkipPosition != INVALID_VALUE && mLlSkipLayout.getVisibility() == GONE) {
            mLlSkipLayout.setVisibility(VISIBLE);
            mTvSkipTime.setText(generateTime(mSkipPosition));
            AnimHelper.doSlideRightIn(mLlSkipLayout, mWidthPixels, 0, 800);
            mHandler.postDelayed(mHideSkipTipRunnable, DEFAULT_HIDE_TIMEOUT * 3);
        }
    }

    /**
     * ??????????????????
     */
    private void _hideSkipTip() {
        if (mLlSkipLayout.getVisibility() == GONE) {
            return;
        }
        ViewCompat.animate(mLlSkipLayout).translationX(-mLlSkipLayout.getWidth()).alpha(0).setDuration(500)
                .setListener(new ViewPropertyAnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(View view) {
                        mLlSkipLayout.setVisibility(GONE);
                    }
                }).start();
        mSkipPosition = INVALID_VALUE;
    }

    /**
     * ????????????????????????
     */
    private Runnable mHideSkipTipRunnable = new Runnable() {
        @Override
        public void run() {
            _hideSkipTip();
        }
    };

    /**
     * ============================ ?????? ============================
     */

    /**
     * ?????????????????????????????????????????????????????????????????????????????????
     */
    private static final int NORMAL_STATUS = 501;
    private static final int INTERRUPT_WHEN_PLAY = 502;
    private static final int INTERRUPT_WHEN_PAUSE = 503;

    private int mVideoStatus = NORMAL_STATUS;

    // ???????????????B??????A???????????????
    private static final int DANMAKU_TAG_BILI = 701;
    private static final int DANMAKU_TAG_ACFUN = 702;
    private static final int DANMAKU_TAG_CUSTOM = 703;

    @Retention(RetentionPolicy.SOURCE)
    @Target({ElementType.FIELD, ElementType.PARAMETER})
    @IntDef({DANMAKU_TAG_BILI, DANMAKU_TAG_ACFUN, DANMAKU_TAG_CUSTOM})
    public @interface DanmakuTag {
    }

    private
    @DanmakuTag
    int mDanmakuTag = DANMAKU_TAG_BILI;

    // ??????????????????
    private IDanmakuView mDanmakuView;
    // ????????????/????????????
    private ImageView mIvDanmakuControl;
    // ??????????????????????????????
    private TextView mTvOpenEditDanmaku;
    // ??????????????????????????????????????????
    private SeekBar mDanmakuPlayerSeek;
    // ???????????????????????????????????????
    private TextView mTvTimeSeparator;
    // ??????????????????
    private View mEditDanmakuLayout;
    // ?????????????????????
    private EditText mEtDanmakuContent;
    // ??????????????????
    private ImageView mIvCancelSend;
    // ????????????
    private ImageView mIvDoSend;

    // ????????????????????????
    private View mDanmakuOptionsBasic;
    // ???????????????????????????
    private RadioGroup mDanmakuTextSizeOptions;
    // ?????????????????????
    private RadioGroup mDanmakuTypeOptions;
    // ??????????????????
    private RadioButton mDanmakuCurColor;
    // ???????????????????????????
    private ImageView mDanmakuMoreColorIcon;
    // ??????????????????????????????
    private View mDanmakuMoreOptions;
    // ?????????????????????
    private RadioGroup mDanmakuColorOptions;

    // ??????????????????
    private DanmakuContext mDanmakuContext;
    // ???????????????
    private BaseDanmakuParser mDanmakuParser;
    // ???????????????
    private ILoader mDanmakuLoader;
    // ?????????????????????
    private BaseDanmakuConverter mDanmakuConverter;
    // ???????????????
    private OnDanmakuListener mDanmakuListener;
    // ??????????????????
    private boolean mIsEnableDanmaku = false;
    // ????????????
    private int mDanmakuTextColor = Color.WHITE;
    // ??????????????????
    private float mDanmakuTextSize = INVALID_VALUE;
    // ????????????
    private int mDanmakuType = BaseDanmaku.TYPE_SCROLL_RL;
    // ?????????????????????????????????
    private int mBasicOptionsWidth = INVALID_VALUE;
    // ????????????????????????????????????
    private int mMoreOptionsWidth = INVALID_VALUE;
    // ??????????????????????????????????????????????????????????????????????????????????????????????????????
    private long mDanmakuTargetPosition = INVALID_VALUE;

    /**
     * ???????????????
     */
    private void _initDanmaku() {
        // ????????????
        mDanmakuView = (IDanmakuView) findViewById(R.id.sv_danmaku);
        mIvDanmakuControl = (ImageView) findViewById(R.id.iv_danmaku_control);
        mTvOpenEditDanmaku = (TextView) findViewById(R.id.tv_open_edit_danmaku);
        mTvTimeSeparator = (TextView) findViewById(R.id.tv_separator);
        mEditDanmakuLayout = findViewById(R.id.ll_edit_danmaku);
        mEtDanmakuContent = (EditText) findViewById(R.id.et_danmaku_content);
        mIvCancelSend = (ImageView) findViewById(R.id.iv_cancel_send);
        mIvDoSend = (ImageView) findViewById(R.id.iv_do_send);
        mDanmakuPlayerSeek = (SeekBar) findViewById(R.id.danmaku_player_seek);
        mDanmakuPlayerSeek.setMax(MAX_VIDEO_SEEK);
        mDanmakuPlayerSeek.setOnSeekBarChangeListener(mSeekListener);

        int navigationBarHeight = NavUtils.getNavigationBarHeight(mAttachActivity);
        if (navigationBarHeight > 0) {
            // ??????????????????????????????????????????????????????????????????????????????
            mEditDanmakuLayout.setPadding(0, 0, navigationBarHeight, 0);
        }

        mIvDanmakuControl.setOnClickListener(this);
        mTvOpenEditDanmaku.setOnClickListener(this);
        mIvCancelSend.setOnClickListener(this);
        mIvDoSend.setOnClickListener(this);

        // ???????????????????????????
        int oneBtnWidth = getResources().getDimensionPixelOffset(R.dimen.danmaku_input_options_color_radio_btn_size);
        // ???????????????????????????????????? * 12 ?????????12???????????????
        mMoreOptionsWidth = oneBtnWidth * 12;
        mDanmakuOptionsBasic = findViewById(R.id.input_options_basic);
        mDanmakuMoreOptions = findViewById(R.id.input_options_more);
        mDanmakuMoreOptions.setOnClickListener(this);
        mDanmakuCurColor = (RadioButton) findViewById(R.id.input_options_color_current);
        mDanmakuMoreColorIcon = (ImageView) findViewById(R.id.input_options_color_more_icon);
        mDanmakuTextSizeOptions = (RadioGroup) findViewById(R.id.input_options_group_textsize);
        mDanmakuTypeOptions = (RadioGroup) findViewById(R.id.input_options_group_type);
        mDanmakuColorOptions = (RadioGroup) findViewById(R.id.input_options_color_group);
        mDanmakuTextSizeOptions.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.input_options_small_textsize) {
                    mDanmakuTextSize = 25f * (mDanmakuParser.getDisplayer().getDensity() - 0.6f) * 0.7f;
                } else if (checkedId == R.id.input_options_medium_textsize) {
                    mDanmakuTextSize = 25f * (mDanmakuParser.getDisplayer().getDensity() - 0.6f);
                }
            }
        });
        mDanmakuTypeOptions.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.input_options_rl_type) {
                    mDanmakuType = BaseDanmaku.TYPE_SCROLL_RL;
                } else if (checkedId == R.id.input_options_top_type) {
                    mDanmakuType = BaseDanmaku.TYPE_FIX_TOP;
                } else if (checkedId == R.id.input_options_bottom_type) {
                    mDanmakuType = BaseDanmaku.TYPE_FIX_BOTTOM;
                }
            }
        });
        mDanmakuColorOptions.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // ????????? tag ?????????????????????????????????
                String color = (String) findViewById(checkedId).getTag();
                mDanmakuTextColor = Color.parseColor(color);
                mDanmakuCurColor.setBackgroundColor(mDanmakuTextColor);
            }
        });
    }

    /**
     * ????????????????????????????????????????????????
     */
    private void _loadDanmaku() {
        if (mIsEnableDanmaku) {
            // ????????????
            mDanmakuContext = DanmakuContext.create();
            //???????????????video?????????????????????????????????????????????????????????????????????- -
//            mDanmakuContext.setDanmakuSync(new VideoDanmakuSync(this));
            if (mDanmakuParser == null) {
                mDanmakuParser = new BaseDanmakuParser() {
                    @Override
                    protected Danmakus parse() {
                        return new Danmakus();
                    }
                };
            }
            mDanmakuView.setCallback(new DrawHandler.Callback() {
                @Override
                public void prepared() {
                    // ??????????????????????????? _resumeDanmaku() ??????????????? prepared ?????????
                    if (mVideoView.isPlaying() && !mIsBufferingStart) {
                        mDanmakuView.start();
                    }
                }

                @Override
                public void updateTimer(DanmakuTimer timer) {
                }

                @Override
                public void danmakuShown(BaseDanmaku danmaku) {
                }

                @Override
                public void drawingFinished() {
                }
            });
            mDanmakuView.enableDanmakuDrawingCache(true);
            mDanmakuView.prepare(mDanmakuParser, mDanmakuContext);
        }
    }

    /**
     * ??????????????????
     *
     * @return
     */
    public IjkPlayerView enableDanmaku() {
        mIsEnableDanmaku = true;
        _initDanmaku();
        if (mIsAlwaysFullScreen) {
            _toggleDanmakuView(true);
        }
        return this;
    }

    /**
     * ??????????????????
     *
     * @param isEnable
     * @return
     */
    public IjkPlayerView enableDanmaku(boolean isEnable) {
        mIsEnableDanmaku = isEnable;
        if (mIsEnableDanmaku) {
            _initDanmaku();
            if (mIsAlwaysFullScreen) {
                _toggleDanmakuView(true);
            }
        }
        return this;
    }

    /**
     * ???????????????????????????????????????????????? bilibili ????????????????????????
     * ??????{@link #setDanmakuCustomParser}???????????????????????????????????????{@link #setDanmakuCustomParser}???????????????
     *
     * @param stream ????????????
     * @return
     */
    public IjkPlayerView setDanmakuSource(InputStream stream) {
        if (stream == null) {
            return this;
        }
        if (!mIsEnableDanmaku) {
            throw new RuntimeException("Danmaku is disable, use enableDanmaku() first");
        }
        if (mDanmakuLoader == null) {
            mDanmakuLoader = DanmakuLoaderFactory.create(DanmakuLoaderFactory.TAG_BILI);
        }
        try {
            mDanmakuLoader.load(stream);
        } catch (IllegalDataException e) {
            e.printStackTrace();
        }
        IDataSource<?> dataSource = mDanmakuLoader.getDataSource();
        if (mDanmakuParser == null) {
            mDanmakuParser = new BiliDanmukuParser();
        }
        mDanmakuParser.load(dataSource);
        return this;
    }

    /**
     * ???????????????????????????????????????????????? bilibili ????????????????????????
     * ??????{@link #setDanmakuCustomParser}???????????????????????????????????????{@link #setDanmakuCustomParser}???????????????
     *
     * @param uri ????????????
     * @return
     */
    public IjkPlayerView setDanmakuSource(String uri) {
        if (TextUtils.isEmpty(uri)) {
            return this;
        }
        if (!mIsEnableDanmaku) {
            throw new RuntimeException("Danmaku is disable, use enableDanmaku() first");
        }
        if (mDanmakuLoader == null) {
            mDanmakuLoader = DanmakuLoaderFactory.create(DanmakuLoaderFactory.TAG_BILI);
        }
        try {
            mDanmakuLoader.load(uri);
        } catch (IllegalDataException e) {
            e.printStackTrace();
        }
        IDataSource<?> dataSource = mDanmakuLoader.getDataSource();
        if (mDanmakuParser == null) {
            mDanmakuParser = new BiliDanmukuParser();
        }
        mDanmakuParser.load(dataSource);
        return this;
    }

    /**
     * ?????????????????????????????????{@link #setDanmakuSource}???????????????{@link #setDanmakuSource}??????
     *
     * @param parser    ?????????
     * @param loader    ?????????
     * @param converter ?????????
     * @return
     */
    public IjkPlayerView setDanmakuCustomParser(BaseDanmakuParser parser, ILoader loader, BaseDanmakuConverter converter) {
        mDanmakuParser = parser;
        mDanmakuLoader = loader;
        mDanmakuConverter = converter;
        return this;
    }

    /**
     * ??????/????????????
     *
     * @param isShow ????????????
     * @return
     */
    public IjkPlayerView showOrHideDanmaku(boolean isShow) {
        if (isShow) {
            mIvDanmakuControl.setSelected(false);
            mDanmakuView.show();
        } else {
            mIvDanmakuControl.setSelected(true);
            mDanmakuView.hide();
        }
        return this;
    }

    /**
     * ????????????
     *
     * @param text   ??????
     * @param isLive ????????????
     * @return ????????????
     */
    public void sendDanmaku(String text, boolean isLive) {
        if (!mIsEnableDanmaku) {
            throw new RuntimeException("Danmaku is disable, use enableDanmaku() first");
        }
        if (TextUtils.isEmpty(text)) {
            Toast.makeText(mAttachActivity, "????????????", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!mDanmakuView.isPrepared()) {
            Toast.makeText(mAttachActivity, "?????????????????????", Toast.LENGTH_SHORT).show();
            return;
        }
        BaseDanmaku danmaku = mDanmakuContext.mDanmakuFactory.createDanmaku(mDanmakuType);
        if (danmaku == null || mDanmakuView == null) {
            return;
        }
        if (mDanmakuTextSize == INVALID_VALUE) {
            mDanmakuTextSize = 25f * (mDanmakuParser.getDisplayer().getDensity() - 0.6f);
        }
        danmaku.text = text;
        danmaku.padding = 5;
        danmaku.isLive = isLive;
        danmaku.priority = 0;  // ????????????????????????????????????????????????
        danmaku.textSize = mDanmakuTextSize;
        danmaku.textColor = mDanmakuTextColor;
        danmaku.underlineColor = Color.GREEN;
        danmaku.setTime(mDanmakuView.getCurrentTime() + 500);
        mDanmakuView.addDanmaku(danmaku);

        if (mDanmakuListener != null) {
            if (mDanmakuConverter != null) {
                mDanmakuListener.onDataObtain(mDanmakuConverter.convertDanmaku(danmaku));
            } else {
                mDanmakuListener.onDataObtain(danmaku);
            }
        }
    }

    /**
     * ?????????????????????????????????????????????????????????????????????????????????????????????{@link #recoverFromEditVideo()}??????
     */
    public void editVideo() {
        if (mVideoView.isPlaying()) {
            pause();
            mVideoStatus = INTERRUPT_WHEN_PLAY;
        } else {
            mVideoStatus = INTERRUPT_WHEN_PAUSE;
        }
        _hideAllView(false);
    }

    /**
     * ???????????????????????????????????????????????????????????????{@link #editVideo()}??????
     *
     * @return ???????????????????????????
     */
    public boolean recoverFromEditVideo() {
        if (mVideoStatus == NORMAL_STATUS) {
            return false;
        }
        if (mIsFullscreen) {
            _recoverScreen();
        }
        if (mVideoStatus == INTERRUPT_WHEN_PLAY) {
            start();
        }
        mVideoStatus = NORMAL_STATUS;
        return true;
    }

    /**
     * ????????????
     */
    private void _resumeDanmaku() {
        if (mDanmakuView != null && mDanmakuView.isPrepared() && mDanmakuView.isPaused()) {
            if (mDanmakuTargetPosition != INVALID_VALUE) {
                mDanmakuView.seekTo(mDanmakuTargetPosition);
                mDanmakuTargetPosition = INVALID_VALUE;
            } else {
                mDanmakuView.resume();
            }
        }
    }

    /**
     * ????????????
     */
    private void _pauseDanmaku() {
        if (mDanmakuView != null && mDanmakuView.isPrepared()) {
            mDanmakuView.pause();
        }
    }

    /**
     * ?????????????????????/??????
     */
    private void _toggleDanmakuShow() {
        if (mIvDanmakuControl.isSelected()) {
            showOrHideDanmaku(true);
        } else {
            showOrHideDanmaku(false);
        }
    }

    /**
     * ????????????????????????View?????????/??????
     *
     * @param isShow ????????????
     */
    private void _toggleDanmakuView(boolean isShow) {
        if (mIsEnableDanmaku) {
            if (isShow) {
                mIvDanmakuControl.setVisibility(VISIBLE);
                mTvOpenEditDanmaku.setVisibility(VISIBLE);
                mTvTimeSeparator.setVisibility(VISIBLE);
                mDanmakuPlayerSeek.setVisibility(VISIBLE);
                mPlayerSeek.setVisibility(GONE);
            } else {
                mIvDanmakuControl.setVisibility(GONE);
                mTvOpenEditDanmaku.setVisibility(GONE);
                mTvTimeSeparator.setVisibility(GONE);
                mDanmakuPlayerSeek.setVisibility(GONE);
                mPlayerSeek.setVisibility(VISIBLE);
            }
        }

    }

    /**
     * ?????????????????????????????????
     */
    private void _recoverScreen() {
        // ????????????
        mEditDanmakuLayout.clearFocus();
        mEditDanmakuLayout.setVisibility(GONE);
        // ???????????????
        SoftInputUtils.closeSoftInput(mAttachActivity);
        // ????????????????????????UI?????????
        _setUiLayoutFullscreen();
        if (mDanmakuColorOptions.getWidth() != 0) {
            _toggleMoreColorOptions();
        }
    }

    /**
     * ???????????????????????????????????????
     */
    private void _toggleMoreColorOptions() {
        if (mBasicOptionsWidth == INVALID_VALUE) {
            mBasicOptionsWidth = mDanmakuOptionsBasic.getWidth();
        }
        if (mDanmakuColorOptions.getWidth() == 0) {
            AnimHelper.doClipViewWidth(mDanmakuOptionsBasic, mBasicOptionsWidth, 0, 300);
            AnimHelper.doClipViewWidth(mDanmakuColorOptions, 0, mMoreOptionsWidth, 300);
            ViewCompat.animate(mDanmakuMoreColorIcon).rotation(180).setDuration(150).setStartDelay(250).start();
        } else {
            AnimHelper.doClipViewWidth(mDanmakuOptionsBasic, 0, mBasicOptionsWidth, 300);
            AnimHelper.doClipViewWidth(mDanmakuColorOptions, mMoreOptionsWidth, 0, 300);
            ViewCompat.animate(mDanmakuMoreColorIcon).rotation(0).setDuration(150).setStartDelay(250).start();
        }
    }

    /**
     * ============================ ????????????????????????????????? ============================
     */

    // ????????????
    private ProgressBar mPbBatteryLevel;
    // ??????????????????
    private TextView mTvSystemTime;
    // ????????????
    private ImageView mIvScreenshot;
    // ???????????????????????????
    private BatteryBroadcastReceiver mBatteryReceiver;
    // ???????????????????????????
    private ScreenBroadcastReceiver mScreenReceiver;
    // ??????????????????
    private NetBroadcastReceiver mNetReceiver;
    // ????????????????????????,???????????????????????????????????????????????????????????????????????????
    private boolean mIsScreenLocked = false;
    // ??????????????????
    private ShareDialog mShareDialog;
    // ???????????????????????????????????????
    private ShareDialog.OnDialogClickListener mDialogClickListener;
    private ShareDialog.OnDialogClickListener mInsideDialogClickListener = new ShareDialog.OnDialogClickListener() {
        @Override
        public void onShare(Bitmap bitmap, Uri uri) {
            if (mDialogClickListener != null) {
                mDialogClickListener.onShare(bitmap, mVideoView.getUri());
            }
            File file = new File(mSaveDir, System.currentTimeMillis() + ".jpg");
            try {
                BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
                bos.flush();
                bos.close();
                Toast.makeText(mAttachActivity, "????????????????????????:" + file.getAbsolutePath(), Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                Toast.makeText(mAttachActivity, "??????????????????", Toast.LENGTH_SHORT).show();
            }

        }
    };
    private ShareDialog.OnDialogDismissListener mDialogDismissListener = new ShareDialog.OnDialogDismissListener() {
        @Override
        public void onDismiss() {
            recoverFromEditVideo();
        }
    };
    // ??????????????????
    private File mSaveDir;

    /**
     * ???????????????????????????????????????
     */
    private void _initReceiver() {
        mPbBatteryLevel = (ProgressBar) findViewById(R.id.pb_battery);
        mTvSystemTime = (TextView) findViewById(R.id.tv_system_time);
        mTvSystemTime.setText(StringUtils.getCurFormatTime());
        mBatteryReceiver = new BatteryBroadcastReceiver();
        mScreenReceiver = new ScreenBroadcastReceiver();
        mNetReceiver = new NetBroadcastReceiver();
        //??????????????????
        mAttachActivity.registerReceiver(mBatteryReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        mAttachActivity.registerReceiver(mScreenReceiver, new IntentFilter(Intent.ACTION_SCREEN_OFF));
        mAttachActivity.registerReceiver(mNetReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        mIvScreenshot = (ImageView) findViewById(R.id.iv_screenshot);
        mIvScreenshot.setOnClickListener(this);
        if (SDCardUtils.isAvailable()) {
            _createSaveDir(SDCardUtils.getRootPath() + File.separator + "IjkPlayView");
        }
    }

    /**
     * ??????
     */
    private void _doScreenshot() {
        editVideo();
        _showShareDialog(mVideoView.getScreenshot());
    }

    /**
     * ???????????????
     *
     * @param bitmap
     */
    private void _showShareDialog(Bitmap bitmap) {
        if (mShareDialog == null) {
            mShareDialog = new ShareDialog();
            mShareDialog.setClickListener(mInsideDialogClickListener);
            mShareDialog.setDismissListener(mDialogDismissListener);
            if (mDialogClickListener != null) {
                mShareDialog.setShareMode(true);
            }
        }
        mShareDialog.setScreenshotPhoto(bitmap);
        mShareDialog.show(mAttachActivity.getSupportFragmentManager(), "share");
    }

    /**
     * ????????????????????????
     *
     * @param dialogClickListener
     * @return
     */
    public IjkPlayerView setDialogClickListener(ShareDialog.OnDialogClickListener dialogClickListener) {
        mDialogClickListener = dialogClickListener;
        if (mShareDialog != null) {
            mShareDialog.setShareMode(true);
        }
        return this;
    }

    /**
     * ????????????
     *
     * @param path
     */
    private void _createSaveDir(String path) {
        mSaveDir = new File(path);
        if (!mSaveDir.exists()) {
            mSaveDir.mkdirs();
        } else if (!mSaveDir.isDirectory()) {
            mSaveDir.delete();
            mSaveDir.mkdirs();
        }
    }

    /**
     * ????????????????????????
     *
     * @param path
     */
    public IjkPlayerView setSaveDir(String path) {
        _createSaveDir(path);
        return this;
    }

    /**
     * ????????????????????????
     */
    class BatteryBroadcastReceiver extends BroadcastReceiver {

        // ??????????????????
        private static final int BATTERY_LOW_LEVEL = 15;

        @Override
        public void onReceive(Context context, Intent intent) {
            // ????????????????????????
            if (intent.getAction().equals(Intent.ACTION_BATTERY_CHANGED)) {
                int level = intent.getIntExtra("level", 0);
                int scale = intent.getIntExtra("scale", 100);
                // ???????????????
                int curPower = level * 100 / scale;
                int status = intent.getIntExtra("status", BatteryManager.BATTERY_HEALTH_UNKNOWN);
                // SecondaryProgress ????????????????????????Progress ????????????????????????
                if (status == BatteryManager.BATTERY_STATUS_CHARGING) {
                    mPbBatteryLevel.setSecondaryProgress(0);
                    mPbBatteryLevel.setProgress(curPower);
                    mPbBatteryLevel.setBackgroundResource(R.mipmap.ic_battery_charging);
                } else if (curPower < BATTERY_LOW_LEVEL) {
                    mPbBatteryLevel.setProgress(0);
                    mPbBatteryLevel.setSecondaryProgress(curPower);
                    mPbBatteryLevel.setBackgroundResource(R.mipmap.ic_battery_red);
                } else {
                    mPbBatteryLevel.setSecondaryProgress(0);
                    mPbBatteryLevel.setProgress(curPower);
                    mPbBatteryLevel.setBackgroundResource(R.mipmap.ic_battery);
                }
            }
        }
    }

    /**
     * ???????????????????????????
     */
    private class ScreenBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (Intent.ACTION_SCREEN_OFF.equals(intent.getAction())) {
                mIsScreenLocked = true;
            }
        }
    }

    private boolean mIsNetConnected;

    public class NetBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            // ??????????????????????????????????????????????????????
            if (ConnectivityManager.CONNECTIVITY_ACTION.equals(intent.getAction())) {
                mIsNetConnected = NetWorkUtils.isNetworkAvailable(mAttachActivity);
            }
        }
    }
}