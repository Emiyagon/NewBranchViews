package com.zhuosen.bilibili.biliplayer.zxing.analyze;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.PlanarYUVLuminanceSource;
import com.google.zxing.ReaderException;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;
import com.zhuosen.bilibili.biliplayer.zxing.CaptureActivity;
import com.zhuosen.bilibili.biliplayer.zxing.config.Constant;

import java.util.Map;

/**
 * TODO
 *
 * @author qingshilin
 * @version 1.0
 * @date 2022/4/7 15:41
 */

public final class DecodeHandler extends Handler {

    private static final String TAG = DecodeHandler.class.getSimpleName();

    private final CaptureActivity activity;
    private final MultiFormatReader multiFormatReader;
    private boolean running = true;

    public DecodeHandler(CaptureActivity activity, Map<DecodeHintType, Object> hints) {
        multiFormatReader = new MultiFormatReader();
        multiFormatReader.setHints(hints);
        this.activity = activity;
    }

    @Override
    public void handleMessage(Message message) {
        if (!running) {
            return;
        }
        switch (message.what) {
            case Constant.DECODE:

                decode((byte[]) message.obj, message.arg1, message.arg2);
                break;
            case Constant.QUIT:
                running = false;
                Looper.myLooper().quit();
                break;
        }
    }

    /**
     *
     * 解码
     */
    private void decode(byte[] data, int width, int height) {

        Result rawResult = null;

        byte[] rotatedData = new byte[data.length];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                rotatedData[x * height + height - y - 1] = data[x + y * width];
            }
        }
        int tmp = width; // Here we are swapping, that's the difference to #11
        width = height;
        height = tmp;
        data = rotatedData;

        PlanarYUVLuminanceSource source = activity.getCameraManager()
                .buildLuminanceSource(data, width, height);


        if (source != null) {
            BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));

            try {
                rawResult = multiFormatReader.decodeWithState(bitmap);
            } catch (ReaderException re) {

                //Log.i("解码异常",re.toString());
            } finally {
                multiFormatReader.reset();
            }
        }



        Handler handler = activity.getHandler();
        if (rawResult != null) {

            if (handler != null) {
                Message message = Message.obtain(handler,
                        Constant.DECODE_SUCCEEDED, rawResult);
                message.sendToTarget();
            }
        } else {
            if (handler != null) {
                Message message = Message.obtain(handler, Constant.DECODE_FAILED);
                message.sendToTarget();
            }
        }
    }

}
