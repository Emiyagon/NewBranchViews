package com.illyasr.mydempviews;


import static android.hardware.Sensor.TYPE_ACCELEROMETER;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.view.View;

import com.illyasr.mydempviews.base.BaseActivity;
import com.illyasr.mydempviews.databinding.ActivitySplashBinding;
import com.illyasr.mydempviews.util.RxTimerUtil;

// 只是一个透明页面
public class SplashActivity extends BaseActivity<ActivitySplashBinding,MainPresent> {

    private SensorManager     sensorManager;
    private Sensor            sensor;
    private SoundPool         soundPool;
    private boolean           isPlayAudio;  //是否正在播放音频
    private Vibrator          vibrator;     //振动器对象
    private boolean           isVibrator;   //是否正在振动
    private int               musicStreamId;  //通过SoundPool加载得到的音频id
    private float             playVolume;   //音量比率值

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (null != msg) {
                switch (msg.what) {
                    case 10:
                        isPlayAudio = false;
                        break;
                    case 12:
                        isVibrator = false;
                        break;
                    default:
                        break;
                }
            }
        }
    };


    @Override
    protected int setLayoutId() {
        return R.layout.activity_splash;
    }

    @Override
    protected void initData() {

        //第一步：先获得加速度传感器
        sensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        if(sensorManager ==null) return;
        sensor = (sensorManager.getDefaultSensor(TYPE_ACCELEROMETER));


        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);   //传感器管理器
        sensor = sensorManager.getDefaultSensor(TYPE_ACCELEROMETER);     //此处传入1 也可以，Sensor中加速度传感器对应的int值为1

        soundPool = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);        //音效池
        musicStreamId = soundPool.load(SplashActivity.this, R.raw.beep, 1);//根据id加载音频

        AudioManager audioManager = (AudioManager) getSystemService(AUDIO_SERVICE); //用来获取音量
        int curVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);    //当前音量值
        int maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);   //最大音量
        playVolume = (float) (curVolume * 1.0 / maxVolume);   //音量比率值

        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);          //振动器

//        RxTimerUtil.timer(2000, number -> {
//            startActivity(new Intent(SplashActivity.this, MainActivity.class));
//            finish();
//        });


        mBindingView.bar.setCountdownTime(10);
        mBindingView.bar.startCountDown();
//        mBindingView.bar.setShowGdText(true);
        mBindingView.bar.setAddCountDownListener(() -> {
            startActivity(new Intent(SplashActivity.this, MainActivity.class));
            finish();
        });
        mBindingView.bar.setOnClickListener(v -> {
            mBindingView.bar.stopCountDown();
            startActivity(new Intent(SplashActivity.this, MainActivity.class));
            finish();
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                int type = event.sensor.getType();  //获取传感器类型
                if (type == 1) {   //等价于  type==TYPE_ACCELEROMETER
                    float x = event.values[0];
                    float y = event.values[1];
                    float z = event.values[2];
                    //摇动灵敏度取决于后面的常量值，这里定义了15
                    if (Math.abs(x) > 15 && Math.abs(y) > 15 && Math.abs(z) > 15) {

                       // playShakeAudio();//播放摇一摇的音频
                        // vibratorPhone();//开启手机震动
                        vibrator.vibrate(300);  //振动时长300ms
                       // showCusDialog();//展示dialog
                        // 这里理论上就是那些憨批厂商们做的一些骚操作,比如下载别的应用啊,跳转自家app啊,这里我就偷个懒,直接跳转到首页并且杀死这个页面
                      showToast("回到首页");

                        RxTimerUtil.timer(550, number -> {
                            if (!mBindingView.bar.isIsend()){
                                mBindingView.bar.stopCountDown();
                            }
                            startActivity(new Intent(SplashActivity.this, MainActivity.class));
                            vibrator.cancel();
                            finish();
                        });



                    }
                }
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {
                //灵敏度变化时调用。灵敏度级别参考：SensorManager.SENSOR_DELAY_GAME
            }
        }, sensor, SensorManager.SENSOR_DELAY_GAME);
    }
}
