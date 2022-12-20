package com.illyasr.mydempviews.ui.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.illyasr.bilibili.biliplayer.utils.NetWorkUtils;
import com.illyasr.mydempviews.MainPresent;
import com.illyasr.mydempviews.MyApplication;
import com.illyasr.mydempviews.R;
import com.illyasr.mydempviews.base.BaseActivity;
import com.illyasr.mydempviews.databinding.ActivityMyLocationBinding;
import com.illyasr.mydempviews.util.GpsUtil;
import com.illyasr.mydempviews.util.PhoneUtil;
import com.jeremyliao.liveeventbus.LiveEventBus;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import pub.devrel.easypermissions.EasyPermissions;

/**
 * 获取一些基本信息(当前定位,手机状态等)
 */
public class MyLocationActivity extends BaseActivity<ActivityMyLocationBinding, MainPresent> {


    private Location location;
    //动态申请健康运动权限
    private static final String[] ACTIVITY_RECOGNITION_PERMISSION = {Manifest.permission.ACTIVITY_RECOGNITION};
    private double latitude;
    private double longitude;


    @Override
    protected int setLayoutId() {
        return R.layout.activity_my_location;
    }

    private StringBuffer sb = new StringBuffer();


    private SensorManager mSensorManager;
    private MySensorEventListener mListener;
    private int mStepDetector = 0;  // 自应用运行以来STEP_DETECTOR检测到的步数
    private int mStepCounter = 0;   // 自系统开机以来STEP_COUNTER检测到的步数
    @Override
    protected void initData() {

        //  监听器注册
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mListener = new MySensorEventListener();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            // 检查该权限是否已经获取
            int get = ContextCompat.checkSelfPermission(this, ACTIVITY_RECOGNITION_PERMISSION[0]);
            // 权限是否已经 授权 GRANTED---授权  DINIED---拒绝
            if (get != PackageManager.PERMISSION_GRANTED) {
                // 如果没有授予该权限，就去提示用户请求自动开启权限
                ActivityCompat.requestPermissions(this, ACTIVITY_RECOGNITION_PERMISSION, 321);
            }
        }

        String[] messions = new String[]{
                Manifest.permission.ACCESS_FINE_LOCATION
        };
        if (EasyPermissions.hasPermissions(this, messions)) {
            // 获取到了权限
            GetLoc();
         /*
            startLocation(this, new OnLocationListenter() {
                @Override
                public void getLatitudeAndLongitude(double longitude, double latitude) {
//                    aMapLocationClient.startLocation();
                    GetLoc(longitude,latitude);
                }
            });*/

        } else {
            //获取位置失败
            if (onLocationListenter != null) {
                onLocationListenter.getLatitudeAndLongitude(0, 0);
            }
            EasyPermissions.requestPermissions(this, getResources().getString(R.string.toast_1), 100, messions);
        }

        mBindingView.tvStep.setOnClickListener(v -> {
            LiveEventBus
                    .get("some_key")
                    .post("some_value");
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            mSensorManager.registerListener(mListener, mSensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR),
                    SensorManager.SENSOR_DELAY_NORMAL);
            mSensorManager.registerListener(mListener, mSensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER),
                    SensorManager.SENSOR_DELAY_NORMAL);
        }
    }
    @Override
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(mListener);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 321) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    //提示用户手动开启权限
                    new AlertDialog.Builder(this)
                            .setTitle("健康运动权限")
                            .setMessage("健康运动权限不可用")
                            .setPositiveButton("立即开启", (dialog12, which) -> {
                                // 跳转到应用设置界面
                                Intent intent = new Intent();
                                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                Uri uri = Uri.fromParts("package", getPackageName(), null);
                                intent.setData(uri);
                                startActivityForResult(intent, 123);
                            })
                            .setNegativeButton("取消", (dialog1, which) -> {
                                Toast.makeText(getApplicationContext(), "没有获得权限，应用无法运行！", Toast.LENGTH_SHORT).show();
                                finish();
                            }).setCancelable(false).show();
                }
            }
        }
    }


    //放入经纬度就可以了
    public String getAddress(double latitude, double longitude) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(latitude,
                    longitude, 1);
            if (addresses.size() > 0) {
                Address address = addresses.get(0);
                String data = address.toString();
                int startCity = data.indexOf("1:\"") + "1:\"".length();
                int endCity = data.indexOf("\"", startCity);
                String city = data.substring(startCity, endCity);

                int startPlace = data.indexOf("feature=") + "feature=".length();
                int endplace = data.indexOf(",", startPlace);
                String place = data.substring(startPlace, endplace);
                return city + place ;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "获取失败";
    }
    private void GetLoc() {
        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);


        // 如果不确定哪些位置提供器可用，可以用下面方法判断 。获取所有可用的位置提供器
        List<String> providerList = lm.getProviders(true);
        String provider;
        if (providerList.contains(LocationManager.GPS_PROVIDER)) {
            provider = LocationManager.GPS_PROVIDER;
        } else if (providerList.contains(LocationManager.NETWORK_PROVIDER)){
            provider = LocationManager.NETWORK_PROVIDER;
        } else{
            // 当没有可用的位置提供器时，弹出Toash提示用户
            Toast.makeText(this, "No Location provider to use", Toast.LENGTH_SHORT).show();
            return;
        }

        Criteria criteria = new Criteria();
        // 设置查询条件
        criteria.setAccuracy(Criteria.ACCURACY_FINE); // 设置准确而非粗糙的精度
        criteria.setPowerRequirement(Criteria.POWER_LOW); // 设置相对省电而非耗电，一般高耗电量会换来更精确的位置信息
        criteria.setAltitudeRequired(false); // 不需要提供海拔信息
        criteria.setSpeedRequired(false); // 不需要速度信息
        criteria.setCostAllowed(false); // 不能产生费用
        //getBestProvider 只有允许访问调用活动的位置供应商将被返回
        String providerName = lm.getBestProvider(criteria, true);
        if (providerName != null) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "没有权限", Toast.LENGTH_SHORT).show();
                //获取位置失败
                if (onLocationListenter != null) {
                    onLocationListenter.getLatitudeAndLongitude(0, 0);
                }
                return;
            }


            //通过provider获得Location的对象
             location = lm.getLastKnownLocation(provider);
//            location = lm.getLastKnownLocation(providerName);
//            location = lm.getCurrentLocation(providerName);

            if (location != null) {
                //获取维度信息
                latitude = location.getLatitude();
                //获取经度信息
                longitude = location.getLongitude();
            }


            sb = new StringBuffer();
            sb.append("私人信息\n");
            sb.append("手机型号 : "+ PhoneUtil.getSystemModel()+"\n");
            sb.append("手机系统语言 : "+ PhoneUtil.getSystemLanguage()+"\n");
            sb.append("手机系统版本号 : "+ PhoneUtil.getSystemVersion()+"\n");
            sb.append("手机厂商 : "+ PhoneUtil.getDeviceBrand()+"\n");
            sb.append("手机网络状态 : "+ NetWorkUtils.getNetworkTypeName(MyApplication.getInstance()) +"\n");
            sb.append("网络可用状态 : "+
                    (isNetworkConnected(MyApplication.getInstance())||isWifiConnected(MyApplication.getInstance())||isMobileConnected(MyApplication.getInstance())||ping())
                    +"\n");
            sb.append("当前纬度  lat = " +latitude +"\n");
            sb.append("当前经度  log = " + longitude +"\n");
            sb.append("当前位置   "+getAddress(latitude,longitude) +"\n");
//            sb.append("当前位置   "+aMapLocationListener.);

            mBindingView. tvMsg.setText(sb.toString());
//            Log.i("获取经纬度", "定位方式： " + providerName + "  维度：" + latitude + "  经度：" + longitude);
        } else {
            Toast.makeText(this, "1.请检查网络连接 \n2.请打开我的位置", Toast.LENGTH_SHORT).show();
            sb = new StringBuffer();
            sb.append("私人信息\n");
            sb.append("手机型号 : "+ PhoneUtil.getSystemModel()+"\n");
            sb.append("手机系统语言 : "+ PhoneUtil.getSystemLanguage()+"\n");
            sb.append("手机系统版本号 : "+ PhoneUtil.getSystemVersion()+"\n");
            sb.append("手机厂商 : "+ PhoneUtil.getDeviceBrand()+"\n");
            sb.append("手机网络状态 : "+ NetWorkUtils.getNetworkTypeName(MyApplication.getInstance()) +"\n");
            sb.append("网络可用状态 : "+
                    (isNetworkConnected(MyApplication.getInstance())||isWifiConnected(MyApplication.getInstance())||isMobileConnected(MyApplication.getInstance())||ping())
                    +"\n");
            sb.append("当前纬度  lat = " +0 +"\n");
            sb.append("当前经度  log = " + 0 +"\n");
            sb.append("当前位置  定位失败 " +"\n");

           mBindingView. tvMsg.setText(sb.toString());
        }




    }

    private class MySensorEventListener implements SensorEventListener {
        @Override
        public void onSensorChanged(SensorEvent event) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                System.out.println("@@@:" + event.sensor.getType() + "--" + Sensor.TYPE_STEP_DETECTOR + "--" + Sensor.TYPE_STEP_COUNTER);

                if (event.sensor.getType() == Sensor.TYPE_STEP_DETECTOR) {
                    if (event.values[0] == 1.0f) {
                        mStepDetector++;
                    }
                } else if (event.sensor.getType() == Sensor.TYPE_STEP_COUNTER) {
                    mStepCounter = (int) event.values[0];
                }

               String desc = String.format(Locale.CHINESE, "设备检测到您当前走了%d步，自开机以来总数为%d步",
                       mStepDetector,//今天的步数,正常来说华为小米拿到的数据是对的
                       mStepCounter);//历史总步数,关机会清零(正常操作),但是红魔拿到的这个却不对,所以最终还是需要去健康里面获取数据
                mBindingView.tvStep.setText(desc);
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    //----------------------- 高德-*---------------------------------------
    public OnLocationListenter onLocationListenter;
    public AMapLocationClient aMapLocationClient;

    /**
     * 高德地图参数配置
     *
     * @return
     */
    public AMapLocationClientOption parameterConfiguration() {
        //初始化AMapLocationClientOption对象
        AMapLocationClientOption mLocationOption = new AMapLocationClientOption();
        //设置定位模式（采取高精度定位模式）
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //设置单次定位
        mLocationOption.setOnceLocation(true);
        //获取3s内最精确的一次定位结果
        mLocationOption.setOnceLocationLatest(true);
        //设置是否返回地址信息（默认返回地址信息）
        mLocationOption.setNeedAddress(true);
        //设置是否允许模拟位置,默认为true，允许模拟位置
//        mLocationOption.setMockEnable(true);
        //单位是毫秒，默认30000毫秒，建议超时时间不要低于8000毫秒。
//        mLocationOption.setHttpTimeOut(20000);
        return mLocationOption;
    }

    /**
     * 开始定位
     *
     * @return
     */
    public void startLocation(Context context, OnLocationListenter onLocationListenter) {
        this.onLocationListenter = onLocationListenter;
        //初始化定位
        try {
            aMapLocationClient = new AMapLocationClient(context);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //设置定位成功监听
        aMapLocationClient.setLocationOption(parameterConfiguration());
        //成功结果监听
        aMapLocationClient.setLocationListener(aMapLocationListener);
        //检测是否有GPS权限



    }

    /**
     * 定位监听
     */
    public interface OnLocationListenter {
        /**
         * 获取经纬度
         *
         * @param longitude 经度
         * @param latitude  纬度
         */
        void getLatitudeAndLongitude(double longitude, double latitude);
    }

    AMapLocationListener aMapLocationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation aMapLocation) {
            if (onLocationListenter != null) {
                aMapLocationClient.onDestroy();
                onLocationListenter.getLatitudeAndLongitude(aMapLocation.getLongitude(), aMapLocation.getLatitude());
            }
        }
    };



    //
    /**
     * 定位回调监听器
     */
    public AMapLocationListener mLocationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation amapLocation) {
            if (!GpsUtil.isGpsEnabled(getApplicationContext())) {
               showToast("GPS不可用");
            } else {
                if (amapLocation != null) {
                    if (amapLocation.getErrorCode() == 0) {
                        //定位成功回调信息，设置相关消息
                        amapLocation.getLocationType();//获取当前定位结果来源，如网络定位结果，详见定位类型表
                        double currentLat = amapLocation.getLatitude();//获取纬度
                        double currentLon = amapLocation.getLongitude();//获取经度
//                        LatLonPoint latLonPoint = new LatLonPoint(currentLat, currentLon);  // latlng形式的
                        /*currentLatLng = new LatLng(currentLat, currentLon);*/   //latlng形式的
                        Log.i("currentLocation", "currentLat : " + currentLat + " currentLon : " + currentLon);
                        amapLocation.getAccuracy();//获取精度信息
                    } else {
                        //显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
                        Log.e("AmapError", "location Error, ErrCode:"
                                + amapLocation.getErrorCode() + ", errInfo:"
                                + amapLocation.getErrorInfo());
                    }
                }
            }
        }
    };
}
