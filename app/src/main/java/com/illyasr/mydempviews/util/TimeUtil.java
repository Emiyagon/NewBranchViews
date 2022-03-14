package com.illyasr.mydempviews.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *    时间间隔util工具类
 */
public class TimeUtil {

    public static final String NYRSFM = "yyyy-MM-dd HH:mm:ss";
    public static final String NYR = "yyyy-MM-dd";
    public static final String YR = "MM-dd";
    public static final String SFM = "HH:mm:ss";

    /**
     *
     * @return
     */
    public static long getNow() {
        return System.currentTimeMillis();
    }
    /**
     * 获取当前时间
     */
    public static String getNowTime() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
//        System.out.println(df.format(new Date()));// new Date()为获取当前系统时间
        return df.format(new Date());
    }

    private static String getShortTime(long time) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
        return df.format(time);
    }

    /**
     * 获取特定字段的时间
     * @param str
     * @return
     */
    public static String getNowTime(String str) {
        SimpleDateFormat df = new SimpleDateFormat(str);//设置日期格式
//        System.out.println(df.format(new Date()));// new Date()为获取当前系统时间
        return df.format(new Date());
    }


    /**
     *
     *获取两个时间间隔
     * @param t1 初始时间( ms )
     * @param t2  终止时间
     * @return  秒
     */
    public static long getCurrentTime(long t1, long t2) {
        //  long s = (System.currentTimeMillis() - hqtime) / (1000 * 60);
        long time = (t2 - t1)/1000;//单位:s
            return time;
    }

    public static String Oril(long t1, long t2) {

        long time = getCurrentTime(t1, t2);
        long hour = time/60/60;
        long minute = time /60;
        long second = time;

        if (hour != 0) {
            return hour + "小时前";
        } else {
            if (minute != 0) {
                return minute + "分钟前";
            } else {
                return "刚刚";
            }
        }
//        return null;
    }

    /**
     * @author LuoB.
     * @param oldTime 较小的时间
     * @param newTime 较大的时间 (如果为空   默认当前时间 ,表示和当前时间相比)
     * @return -1 ：同一天.    0：昨天 .   1 ：至少是前天.
     * @throws ParseException 转换异常
     */
    public static int isYeaterday(Date oldTime,Date newTime) throws ParseException {
        if(newTime==null){
            newTime=new Date();
        }
        //将下面的 理解成  yyyy-MM-dd 00：00：00 更好理解点
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String todayStr = format.format(newTime);
        Date today = format.parse(todayStr);
        //昨天 86400000=24*60*60*1000 一天
        if((today.getTime()-oldTime.getTime())>0 && (today.getTime()-oldTime.getTime())<=86400000) {
            return 0;
        }
        else if((today.getTime()-oldTime.getTime())<=0){ //至少是今天
            return -1;
        }
        else{ //至少是前天
            return 1;
        }
    }

    /**
     *   朋友圈时间显示
     * @param oldTime
     * @param newTime
     * @return
     * @throws ParseException
     */
    public static String intervalShow(long oldTime,long newTime) throws ParseException {
        Date oldDate = new Date(oldTime);
        Date nowDate = new Date(newTime);
        int type = isYeaterday(oldDate, nowDate);

        switch (type) {
            default:
                return null;
            case 0:
                return "昨天";
            case -1:
                return Oril(oldTime,newTime);
            case 1:
                return getShortTime(oldTime);
        }

//        return null;
    }

    /**
     * yyyy-MM-dd HH:mm:ss 格式转间隔
     */

    public static String HowLongAgo(String time) {

        try {
            Date now = new Date(System.currentTimeMillis());//现在
            Date date = new  SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(time);
            int type = isYeaterday(date, now);
            switch (type) {
                default:
                    return null;
                case 0:
                    return "昨天";
                case -1:
                    return Oril(date.getTime(),now.getTime());
                case 1:
                    return getShortTime(date.getTime());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return "刚刚";
    }

}
