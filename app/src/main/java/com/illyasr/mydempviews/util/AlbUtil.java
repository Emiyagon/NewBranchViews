package com.illyasr.mydempviews.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * TODO
 * 数字大小写转换
 *
 * @author qingshilin
 * @version 1.0
 * @date 2022/6/10 16:01
 */
public class AlbUtil {
    private static String HanDigiStr[] = new String[]{"零", "壹", "贰", "叁", "肆", "伍", "陆", "柒", "捌", "玖"};

    private static String HanDiviStr[] = new String[]{"", "拾", "佰", "仟", "万", "拾", "佰", "仟",
            "亿", "拾", "佰", "仟", "万", "拾", "佰", "仟", "亿", "拾", "佰", "仟", "万", "拾", "佰", "仟"};

    /**
     * @param NumStr 输入字符串必须正整数，只允许前导空格(必须右对齐)，不宜有前导零
     * @return
     */
    public static String PositiveIntegerToHanStr(String NumStr) {
        StringBuffer sb = new StringBuffer();
        boolean lastZero = false;
        boolean hasValue = false; // 亿、万进位前有数值标记
        int len, n;
        len = NumStr.length();
        if (len > 15) return "数值过大!";
        for (int i = len - 1; i >= 0; i--) {
            if (NumStr.charAt(len - i - 1) == ' ')continue;
            n = NumStr.charAt(len - i - 1) - '0';
            if (n < 0 || n > 9) return "输入含非数字字符!";
            if (n != 0) {
                if (lastZero)
                    // 若干零后若跟非零值，只显示一个零
                    sb.append(HanDigiStr[0]);
                // 除了亿万前的零不带到后面
                // if( !( n==1 && (i%4)==1 && (lastZero || i==len-1) ) ) //
                // 如十进位前有零也不发壹音用此行
                // 十进位处于第一位不发壹音
                if (!(n == 1 && (i % 4) == 1 && i == len - 1))  sb.append(HanDigiStr[n]) ;
                sb.append(HanDiviStr[i]);  // 非零值后加进位，个位为空
                hasValue = true; // 置万进位前有值标记
            } else {
                // 亿万之间必须有非零值方显示万
                if ((i % 8) == 0 || ((i % 8) == 4 && hasValue))  sb.append(HanDiviStr[i]); // “亿”或“万”
            }
            if (i % 8 == 0) hasValue = false; // 万进位前有值标记逢亿复位
            lastZero = (n == 0) && (i % 4 != 0);
        }

        if (sb.length() == 0) return HanDigiStr[0]; // 输入空字符或"0"，返回"零"
        return sb.toString();
    }

    /**
     * 这个可以输出带小数点的
     * @param NumStr
     * @return
     */
    public static String BigMuch(String NumStr) {
        if (!isRedisNum(NumStr)){
            return "非法字符!";
        }
        StringBuffer sb = new StringBuffer();
        boolean lastZero = false;
        boolean hasValue = false; // 亿、万进位前有数值标记
        int len, n;
        if (!NumStr.contains(".")){
            return PositiveIntegerToHanStr(NumStr);
        }else {
            String pointStr = NumStr.substring(NumStr.indexOf(".")).replace(".","");
            NumStr = NumStr.substring(0, NumStr.indexOf("."));
            len = NumStr.trim().length();
            if (len > 15) return "数值过大!";
            for (int i = len - 1; i >= 0; i--) {
                if (NumStr.charAt(len - i - 1) == ' ')continue;
                n = NumStr.charAt(len - i - 1) - '0';
                if (n < 0 || n > 9 ) return "输入含非数字字符!";
                if (n != 0) {
                    if (lastZero)
                        // 若干零后若跟非零值，只显示一个零
                        sb.append(HanDigiStr[0]);
                    // 除了亿万前的零不带到后面
                    // if( !( n==1 && (i%4)==1 && (lastZero || i==len-1) ) ) //
                    // 如十进位前有零也不发壹音用此行
                    // 十进位处于第一位不发壹音
                    if (!(n == 1 && (i % 4) == 1 && i == len - 1))  sb.append(HanDigiStr[n]) ;
                    sb.append(HanDiviStr[i]);  // 非零值后加进位，个位为空
                    hasValue = true; // 置万进位前有值标记
                } else {
                    // 亿万之间必须有非零值方显示万
                    if ((i % 8) == 0 || ((i % 8) == 4 && hasValue))  sb.append(HanDiviStr[i]); // “亿”或“万”
                }
                if (i % 8 == 0) hasValue = false; // 万进位前有值标记逢亿复位
                lastZero = (n == 0) && (i % 4 != 0);
            }
            if (sb.length() == 0) sb.append("零"); // 输入空字符或"0"，返回"零"
            // 这里开始再增加小数点之后的
            sb.append("点");
            for (int i = 0 ; i <pointStr.length() ;i++ ){
                int  ne = pointStr.charAt(i ) - '0';
                sb.append(HanDigiStr[ne]);
            }
            return sb.toString();

        }

    }

    public static boolean isRedisNum(String phone) {
        // ([1-9]\d*\.?\d*)|(0\.\d*[1-9])
        String regex = "([1-9]\\d*\\.?\\d*)|(0\\.\\d*[1-9])";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(phone);
        return m.matches();
    }

}
