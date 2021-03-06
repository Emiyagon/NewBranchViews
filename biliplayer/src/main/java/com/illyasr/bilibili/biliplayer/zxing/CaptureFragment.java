/*
 * Copyright (C) 2019 Jenly Yu
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.illyasr.bilibili.biliplayer.zxing;

import android.Manifest;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.zxing.Result;
import com.king.zxing.CameraScan;
import com.king.zxing.DefaultCameraScan;
import com.king.zxing.ViewfinderView;
import com.king.zxing.util.LogUtils;
import com.king.zxing.util.PermissionUtils;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.camera.view.PreviewView;
import androidx.fragment.app.Fragment;

/**
 * @author <a href="mailto:jenly1314@gmail.com">Jenly</a>
 */
public class CaptureFragment extends Fragment implements com.king.zxing.CameraScan.OnScanResultCallback {

    private static final int CAMERA_PERMISSION_REQUEST_CODE = 0X86;

    private View mRootView;

    protected PreviewView previewView;
    protected ViewfinderView viewfinderView;
    protected View ivFlashlight;

    private com.king.zxing.CameraScan mCameraScan;

    public static CaptureFragment newInstance() {

        Bundle args = new Bundle();

        CaptureFragment fragment = new CaptureFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        int layoutId = getLayoutId();
        if(isContentView(layoutId)){
            mRootView = createRootView(inflater,container);
        }
        initUI();
        return mRootView;
    }

    /**
     * ?????????
     */
    public void initUI(){
        previewView = mRootView.findViewById(getPreviewViewId());
        int viewfinderViewId = getViewfinderViewId();
        if(viewfinderViewId != 0){
            viewfinderView = mRootView.findViewById(viewfinderViewId);
        }
        int ivFlashlightId = getFlashlightId();
        if(ivFlashlightId != 0){
            ivFlashlight = mRootView.findViewById(ivFlashlightId);
            if(ivFlashlight != null){
                ivFlashlight.setOnClickListener(v -> onClickFlashlight());
            }
        }
        initCameraScan();
        startCamera();
    }

    /**
     * ???????????????
     */
    protected void onClickFlashlight(){
        toggleTorchState();
    }

    /**
     * ?????????CameraScan
     */
    public void initCameraScan(){
        mCameraScan = new DefaultCameraScan(this,previewView);
        mCameraScan.setOnScanResultCallback(this);
    }

    /**
     * ??????????????????
     */
    public void startCamera(){
        if(mCameraScan != null){
            if(PermissionUtils.checkPermission(getContext(), Manifest.permission.CAMERA)){
                mCameraScan.startCamera();
            }else{
                LogUtils.d("checkPermissionResult != PERMISSION_GRANTED");
                PermissionUtils.requestPermission(this,Manifest.permission.CAMERA,CAMERA_PERMISSION_REQUEST_CODE);
            }
        }
    }

    /**
     * ????????????
     */
    private void releaseCamera(){
        if(mCameraScan != null){
            mCameraScan.release();
        }
    }

    /**
     * ??????????????????????????????/?????????
     */
    protected void toggleTorchState(){
        if(mCameraScan != null){
            boolean isTorch = mCameraScan.isTorchEnabled();
            mCameraScan.enableTorch(!isTorch);
            if(ivFlashlight != null){
                ivFlashlight.setSelected(!isTorch);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == CAMERA_PERMISSION_REQUEST_CODE){
            requestCameraPermissionResult(permissions,grantResults);
        }
    }

    /**
     * ??????Camera??????????????????
     * @param permissions
     * @param grantResults
     */
    public void requestCameraPermissionResult(@NonNull String[] permissions, @NonNull int[] grantResults){
        if(PermissionUtils.requestPermissionsResult(Manifest.permission.CAMERA,permissions,grantResults)){
            startCamera();
        }else{
            getActivity().finish();
        }
    }

    @Override
    public void onDestroy() {
        releaseCamera();
        super.onDestroy();
    }

    /**
     * ??????true?????????????????????{@link #createRootView(LayoutInflater, ViewGroup)}????????????false????????????????????????{@link #createRootView(LayoutInflater, ViewGroup)}
     * @param layoutId
     * @return ????????????true
     */
    public boolean isContentView(@LayoutRes int layoutId){
        return true;
    }

    /**
     * ??????{@link #mRootView}
     * @param inflater
     * @param container
     * @return
     */
    @NonNull
    public View createRootView(LayoutInflater inflater, ViewGroup container){
        return inflater.inflate(getLayoutId(),container,false);
    }

    /**
     * ??????id
     * @return
     */
    public int getLayoutId(){
        return com.king.zxing.R.layout.zxl_capture;
    }

    /**
     * {@link #viewfinderView} ??? ID
     * @return ????????????{@code R.id.viewfinderView}, ????????????????????????????????????0
     */
    public int getViewfinderViewId(){
        return com.king.zxing.R.id.viewfinderView;
    }


    /**
     * ????????????{@link #previewView} ???ID
     * @return
     */
    public int getPreviewViewId(){
        return com.king.zxing.R.id.previewView;
    }

    /**
     * ?????? {@link #ivFlashlight} ???ID
     * @return  ????????????{@code R.id.ivFlashlight}, ??????????????????????????????????????????0
     */
    public int getFlashlightId(){
        return com.king.zxing.R.id.ivFlashlight;
    }

    /**
     * Get {@link com.king.zxing.CameraScan}
     * @return {@link #mCameraScan}
     */
    public CameraScan getCameraScan(){
        return mCameraScan;
    }

    /**
     * ????????????????????????
     * @param result ????????????
     * @return ??????true???????????????????????????????????????????????????false?????????????????????????????????
     */
    @Override
    public boolean onScanResultCallback(Result result) {
        return false;
    }

    //--------------------------------------------

    public View getRootView() {
        return mRootView;
    }


}
