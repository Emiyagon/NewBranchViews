package com.illyasr.mydempviews.ui.activity.tts;

import static android.content.Context.SENSOR_SERVICE;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;

import com.illyasr.mydempviews.MainPresent;
import com.illyasr.mydempviews.R;
import com.illyasr.mydempviews.base.BaseFragment;
import com.illyasr.mydempviews.databinding.FragmentCompressBinding;

/**
 * TODO
 *
 * @author qingshilin
 * @version 1.0
 * @date 2022/10/9 16:10
 */
public class CompassFragment extends BaseFragment<FragmentCompressBinding, MainPresent> {

    private SensorManager mSensorManager;
    private SensorEventListener mSensorEventListener;
    private float val;

    public static CompassFragment newInstance() {
        CompassFragment fragment = new CompassFragment();
        Bundle bundle = new Bundle();

        return fragment;
    }

    @Override
    public int setContent() {
        return R.layout.fragment_compress;
    }

    @Override
    protected void initView() {
        mSensorManager = (SensorManager) getActivity().getSystemService(SENSOR_SERVICE);
        mSensorEventListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                val = event.values[0];
                mBindingView.ccv.setVal(val);
            }
            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {
            }
        };
        mSensorManager.registerListener(mSensorEventListener, mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION),
                SensorManager.SENSOR_DELAY_GAME);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mSensorManager.unregisterListener(mSensorEventListener);
    }
}
