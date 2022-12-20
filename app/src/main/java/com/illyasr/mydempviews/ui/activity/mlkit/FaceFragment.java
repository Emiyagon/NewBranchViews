package com.illyasr.mydempviews.ui.activity.mlkit;

import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.mlkit.vision.face.Face;
import com.illyasr.mydempviews.base.BaseFragment;
import com.king.mlkit.vision.camera.AnalyzeResult;
import com.king.mlkit.vision.camera.BaseCameraScanActivity;
import com.king.mlkit.vision.camera.BaseCameraScanFragment;
import com.king.mlkit.vision.camera.CameraScan;
import com.king.mlkit.vision.camera.analyze.Analyzer;
import com.king.mlkit.vision.face.analyze.FaceDetectionAnalyzer;

import java.util.List;

/**
 * TODO
 *
 * @author qingshilin
 * @version 1.0
 * @date 2022/12/8 15:24
 */
public class FaceFragment extends BaseCameraScanFragment<List<Face>> {
    public static FaceFragment newInstance() {
        FaceFragment fragment = new FaceFragment();
        Bundle bundle = new Bundle();

        return fragment;
    }

    @Nullable
    @Override
    public Analyzer<List<Face>> createAnalyzer() {
        return new FaceDetectionAnalyzer();
    }

    @Override
    public void onScanResultCallback(@NonNull AnalyzeResult<List<Face>> result) {

        getCameraScan().setAnalyzeImage(false);
        /**
         cameraScan.setAnalyzeImage(false)
         val bitmap = result.bitmap.drawRect {canvas,paint ->
         for (data in result.result) {
         canvas.drawRect(data.boundingBox,paint)
         data.allContours
         for(contour in data.allContours){
         for (point in contour.points){
         canvas.drawCircle(point.x,point.y,2f,paint)}}}}


         val config = AppDialogConfig(this, R.layout.result_dialog)
         config.setOnClickConfirm {
         AppDialog.INSTANCE.dismissDialog()
         cameraScan.setAnalyzeImage(true)
         }.setOnClickCancel {
         AppDialog.INSTANCE.dismissDialog()
         finish()
         }
         val imageView = config.getView<ImageView>(R.id.ivDialogContent)
         imageView.setImageBitmap(bitmap)
         AppDialog.INSTANCE.showDialog(config,false)
         */
        CameraScan cameraScan=  getCameraScan().setAnalyzeImage(false);
        Bitmap bitmap =result.getBitmap();
        List<Face> faces = result.getResult();



    }

    @Override
    public void onScanResultFailure() {
        super.onScanResultFailure();
    }
}
