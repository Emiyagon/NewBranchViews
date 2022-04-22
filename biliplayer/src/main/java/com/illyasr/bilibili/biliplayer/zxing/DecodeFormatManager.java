package com.illyasr.bilibili.biliplayer.zxing;

import android.content.Intent;
import android.net.Uri;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.DecodeHintType;
import com.illyasr.bilibili.biliplayer.zxing.analyze.Intents;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import androidx.annotation.NonNull;

/**
 * @author <a href="mailto:jenly1314@gmail.com">Jenly</a>
 */
public final class DecodeFormatManager {

    /**
     * 所有的
     */
    public static final Map<DecodeHintType,Object> ALL_HINTS = new EnumMap<>(DecodeHintType.class);
    /**
     * CODE_128 (最常用的一维码)
     */
    public static final Map<DecodeHintType,Object> CODE_128_HINTS = createDecodeHint(BarcodeFormat.CODE_128);
    /**
     * QR_CODE (最常用的二维码)
     */
    public static final Map<DecodeHintType,Object> QR_CODE_HINTS = createDecodeHint(BarcodeFormat.QR_CODE);
    /**
     * 一维码
     */
    public static final Map<DecodeHintType,Object> ONE_DIMENSIONAL_HINTS = new EnumMap<>(DecodeHintType.class);
    /**
     * 二维码
     */
    public static final Map<DecodeHintType,Object> TWO_DIMENSIONAL_HINTS = new EnumMap<>(DecodeHintType.class);

    /**
     * 默认
     */
    public static final Map<DecodeHintType,Object> DEFAULT_HINTS = new EnumMap<>(DecodeHintType.class);

    static {
        //all hints
        addDecodeHintTypes(ALL_HINTS,getAllFormats());
        //one dimension
        addDecodeHintTypes(ONE_DIMENSIONAL_HINTS,getOneDimensionalFormats());
        //Two dimension
        addDecodeHintTypes(TWO_DIMENSIONAL_HINTS,getTwoDimensionalFormats());
        //default hints
        addDecodeHintTypes(DEFAULT_HINTS,getDefaultFormats());
    }

    /**
     * 所有支持的{@link BarcodeFormat}
     * @return
     */
    private static List<BarcodeFormat> getAllFormats(){
        List<BarcodeFormat> list = new ArrayList<>();
        list.add(BarcodeFormat.AZTEC);
        list.add(BarcodeFormat.CODABAR);
        list.add(BarcodeFormat.CODE_39);
        list.add(BarcodeFormat.CODE_93);
        list.add(BarcodeFormat.CODE_128);
        list.add(BarcodeFormat.DATA_MATRIX);
        list.add(BarcodeFormat.EAN_8);
        list.add(BarcodeFormat.EAN_13);
        list.add(BarcodeFormat.ITF);
        list.add(BarcodeFormat.MAXICODE);
        list.add(BarcodeFormat.PDF_417);
        list.add(BarcodeFormat.QR_CODE);
        list.add(BarcodeFormat.RSS_14);
        list.add(BarcodeFormat.RSS_EXPANDED);
        list.add(BarcodeFormat.UPC_A);
        list.add(BarcodeFormat.UPC_E);
        list.add(BarcodeFormat.UPC_EAN_EXTENSION);
        return list;
    }

    /**
     * 二维码
     * 包括如下几种格式：
     *  {@link BarcodeFormat#CODABAR}
     *  {@link BarcodeFormat#CODE_39}
     *  {@link BarcodeFormat#CODE_93}
     *  {@link BarcodeFormat#CODE_128}
     *  {@link BarcodeFormat#EAN_8}
     *  {@link BarcodeFormat#EAN_13}
     *  {@link BarcodeFormat#ITF}
     *  {@link BarcodeFormat#RSS_14}
     *  {@link BarcodeFormat#RSS_EXPANDED}
     *  {@link BarcodeFormat#UPC_A}
     *  {@link BarcodeFormat#UPC_E}
     *  {@link BarcodeFormat#UPC_EAN_EXTENSION}
     * @return
     */
    private static List<BarcodeFormat> getOneDimensionalFormats(){
        List<BarcodeFormat> list = new ArrayList<>();
        list.add(BarcodeFormat.CODABAR);
        list.add(BarcodeFormat.CODE_39);
        list.add(BarcodeFormat.CODE_93);
        list.add(BarcodeFormat.CODE_128);
        list.add(BarcodeFormat.EAN_8);
        list.add(BarcodeFormat.EAN_13);
        list.add(BarcodeFormat.ITF);
        list.add(BarcodeFormat.RSS_14);
        list.add(BarcodeFormat.RSS_EXPANDED);
        list.add(BarcodeFormat.UPC_A);
        list.add(BarcodeFormat.UPC_E);
        list.add(BarcodeFormat.UPC_EAN_EXTENSION);
        return list;
    }

    /**
     * 二维码
     * 包括如下几种格式：
     *  {@link BarcodeFormat#AZTEC}
     *  {@link BarcodeFormat#DATA_MATRIX}
     *  {@link BarcodeFormat#MAXICODE}
     *  {@link BarcodeFormat#PDF_417}
     *  {@link BarcodeFormat#QR_CODE}
     * @return
     */
    private static List<BarcodeFormat> getTwoDimensionalFormats(){
        List<BarcodeFormat> list = new ArrayList<>();
        list.add(BarcodeFormat.AZTEC);
        list.add(BarcodeFormat.DATA_MATRIX);
        list.add(BarcodeFormat.MAXICODE);
        list.add(BarcodeFormat.PDF_417);
        list.add(BarcodeFormat.QR_CODE);
        return list;
    }

    /**
     * 默认支持的格式
     * 包括如下几种格式：
     *  {@link BarcodeFormat#QR_CODE}
     *  {@link BarcodeFormat#UPC_A}
     *  {@link BarcodeFormat#EAN_13}
     *  {@link BarcodeFormat#CODE_128}
     * @return
     */
    private static List<BarcodeFormat> getDefaultFormats(){
        List<BarcodeFormat> list = new ArrayList<>();
        list.add(BarcodeFormat.QR_CODE);
        list.add(BarcodeFormat.UPC_A);
        list.add(BarcodeFormat.EAN_13);
        list.add(BarcodeFormat.CODE_128);
        return list;
    }

    private static <T> List<T> singletonList(T o){
        return Collections.singletonList(o);
    }

    /**
     * 支持解码的格式
     * @param barcodeFormats {@link BarcodeFormat}
     * @return
     */
    public static Map<DecodeHintType,Object> createDecodeHints(@NonNull BarcodeFormat... barcodeFormats){
        Map<DecodeHintType,Object> hints = new EnumMap<>(DecodeHintType.class);
        addDecodeHintTypes(hints, Arrays.asList(barcodeFormats));
        return hints;
    }

    /**
     * 支持解码的格式
     * @param barcodeFormat {@link BarcodeFormat}
     * @return
     */
    public static Map<DecodeHintType,Object> createDecodeHint(@NonNull BarcodeFormat barcodeFormat){
        Map<DecodeHintType,Object> hints = new EnumMap<>(DecodeHintType.class);
        addDecodeHintTypes(hints,singletonList(barcodeFormat));
        return hints;
    }

    /**
     *
     * @param hints
     * @param formats
     */
    private static void addDecodeHintTypes(Map<DecodeHintType,Object> hints,List<BarcodeFormat> formats){
        // Image is known to be of one of a few possible formats.
        hints.put(DecodeHintType.POSSIBLE_FORMATS, formats);
        // Spend more time to try to find a barcode; optimize for accuracy, not speed.
        hints.put(DecodeHintType.TRY_HARDER, Boolean.TRUE);
        // Specifies what character encoding to use when decoding, where applicable (type String)
        hints.put(DecodeHintType.CHARACTER_SET, "UTF-8");
    }



    //----------------------
    private static final Pattern COMMA_PATTERN = Pattern.compile(",");
    public static final Set<BarcodeFormat> ONE_D_FORMATS;
    public static final Set<BarcodeFormat> PRODUCT_FORMATS;
    public static final Set<BarcodeFormat> INDUSTRIAL_FORMATS;
    public static final Set<BarcodeFormat> QR_CODE_FORMATS = EnumSet.of(BarcodeFormat.QR_CODE);
    public static final Set<BarcodeFormat> DATA_MATRIX_FORMATS = EnumSet.of(BarcodeFormat.DATA_MATRIX);
    public static final Set<BarcodeFormat> AZTEC_FORMATS = EnumSet.of(BarcodeFormat.AZTEC);
    public static final Set<BarcodeFormat> PDF417_FORMATS = EnumSet.of(BarcodeFormat.PDF_417);
    static {
        PRODUCT_FORMATS = EnumSet.of(BarcodeFormat.UPC_A,
                BarcodeFormat.UPC_E,
                BarcodeFormat.EAN_13,
                BarcodeFormat.EAN_8,
                BarcodeFormat.RSS_14,
                BarcodeFormat.RSS_EXPANDED);
        INDUSTRIAL_FORMATS = EnumSet.of(BarcodeFormat.CODE_39,
                BarcodeFormat.CODE_93,
                BarcodeFormat.CODE_128,
                BarcodeFormat.ITF,
                BarcodeFormat.CODABAR);
        ONE_D_FORMATS = EnumSet.copyOf(PRODUCT_FORMATS);
        ONE_D_FORMATS.addAll(INDUSTRIAL_FORMATS);
    }
    private static final Map<String, Set<BarcodeFormat>> FORMATS_FOR_MODE;

    static {
        FORMATS_FOR_MODE = new HashMap<String, Set<BarcodeFormat>>();
        FORMATS_FOR_MODE.put(Intents.Scan.ONE_D_MODE, ONE_D_FORMATS);
        FORMATS_FOR_MODE.put(Intents.Scan.PRODUCT_MODE, PRODUCT_FORMATS);
        FORMATS_FOR_MODE.put(Intents.Scan.QR_CODE_MODE, QR_CODE_FORMATS);
        FORMATS_FOR_MODE.put(Intents.Scan.DATA_MATRIX_MODE, DATA_MATRIX_FORMATS);
        FORMATS_FOR_MODE.put(Intents.Scan.AZTEC_MODE, AZTEC_FORMATS);
        FORMATS_FOR_MODE.put(Intents.Scan.PDF417_MODE, PDF417_FORMATS);
    }

    public static Set<BarcodeFormat> parseDecodeFormats(Intent intent) {
        Iterable<String> scanFormats = null;
        CharSequence scanFormatsString = intent.getStringExtra(Intents.Scan.FORMATS);
        if (scanFormatsString != null) {
            scanFormats = Arrays.asList(COMMA_PATTERN.split(scanFormatsString));
        }
        return parseDecodeFormats(scanFormats, intent.getStringExtra(Intents.Scan.MODE));
    }
    public static Set<BarcodeFormat> parseDecodeFormats(Uri inputUri) {
        List<String> formats = inputUri.getQueryParameters(Intents.Scan.FORMATS);
        if (formats != null && formats.size() == 1 && formats.get(0) != null) {
            formats = Arrays.asList(COMMA_PATTERN.split(formats.get(0)));
        }
        return parseDecodeFormats(formats, inputUri.getQueryParameter(Intents.Scan.MODE));
    }
    private static Set<BarcodeFormat> parseDecodeFormats(Iterable<String> scanFormats, String decodeMode) {
        if (scanFormats != null) {
            Set<BarcodeFormat> formats = EnumSet.noneOf(BarcodeFormat.class);
            try {
                for (String format : scanFormats) {
                    formats.add(BarcodeFormat.valueOf(format));
                }
                return formats;
            } catch (IllegalArgumentException iae) {
                // ignore it then
            }
        }
        if (decodeMode != null) {
            return FORMATS_FOR_MODE.get(decodeMode);
        }
        return null;
    }
}
