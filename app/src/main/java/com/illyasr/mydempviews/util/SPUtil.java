package com.illyasr.mydempviews.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Base64;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.illyasr.mydempviews.MyApplication;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author bullet
 * @date 2018/8/13
 * sharepreferences 工具类
 */

public class SPUtil {
    //Context.MODE_WORLD_APPEND
    private static String TOKEN_VALUE = "token";//token名字
    private static final String spFileName = "welcomePage";    //第一次进入
    private static final String isLogin = "is_login";    //登录状态
    public static String SP_FILE_NAME = "my_app";//SharedPreferences文件名字
    public static String SP_FILTER = "filter";
    /**
     * 应用存储名称空间
     */
    protected static String NAME_SAPCE = "";

    /**
     * 应用存储键值前缀
     */
    protected static String PREFIX = "";

    /**
     * 初始化工具类
     *
     * @param organizationName 组织名称
     * @param projectName      当前项目名称
     */
    public static void init(String organizationName, String projectName) {
        NAME_SAPCE = String.format("%s_%s", organizationName, projectName);
        PREFIX = String.format("%s_", projectName);
    }

    /**
     * 判断是否第一次进入app
     */
    public static Boolean getFirstInfo(Context context) {
        SharedPreferences setPreferences = context.getSharedPreferences(
                spFileName, Context.MODE_PRIVATE);
        Boolean result = setPreferences.getBoolean("is_first", true);
        return result;
    }

    /**
     * 保存第一次打开的记录
     */
    public static void putFirstInfo(Context context) {
        SharedPreferences activityPreferences = context.getSharedPreferences(
                spFileName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = activityPreferences.edit();
        editor.putBoolean("is_first", false);
        editor.commit();
    }

    /**
     * 保存登录状态
     */
    public static void putLoginState(Context context, Boolean value) {
        SharedPreferences setPreferences = context.getSharedPreferences(
                isLogin, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = setPreferences.edit();
        editor.putBoolean("login_state", value);
        editor.commit();
    }

    /**
     * 获取登录状态
     */
    public static boolean getLoginState(Context context) {
        SharedPreferences setPreferences = context.getSharedPreferences(
                isLogin, Context.MODE_PRIVATE);
        return setPreferences.getBoolean("login_state", false);
    }


    /**
     * 保存sessiontoken
     */
    public static void saveSession(String session) {
        SPUtil.putString(MyApplication.getInstance(),SPUtil.SP_FILE_NAME,"SESSION",session);
    }

    /**
     * 获取sessiontoken
     */
    public static String getSession() {
        return "" + SPUtil.getString(MyApplication.getInstance(), "SESSION", "");
    }

    /**
     * 保存token
     */
    public static void saveToken(Context context,String token) {
        SPUtil.putString(context,SPUtil.SP_FILE_NAME,"TOKEN",token);
    }
    public static void saveToken(String token) {
        SPUtil.putString(MyApplication.getInstance(),SPUtil.SP_FILE_NAME,"TOKEN",token);
    }

    /**
     * 获取token
     */
    public static String getUserToken(Context context) {
        if (context == null) {
            return ""+SPUtil.getString(MyApplication.getInstance(),"TOKEN","");
        } else {
            return ""+SPUtil.getString(context,"TOKEN","");
        }

    }



    /**
     * 存储过期时间
     * int(单位:s)
     */
    public static void saveOverdueTime(Context context,int time) {
                SPUtil.putInt(context,"OVERDUE",time);
    }

    /**
     * 获取过期时间
     */
    public static int getOverdueTime(Context context) {
        return SPUtil.getInt(context,"OVERDUE", 0);
    }

    /**
     *
     * @param context
     * @param preferencesName
     * @param key
     * @param defaultValue
     * @return
     */
    public static int getInt(Context context, String preferencesName, String key, int defaultValue) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(preferencesName, Context.MODE_PRIVATE);
        return sharedPreferences.getInt(key, defaultValue);
    }
    public static int getInt(Context context, String key, int defaultValue) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SP_FILE_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getInt(key, defaultValue);
    }
    public static int getInt(String key, int defaultValue) {
        SharedPreferences sharedPreferences = MyApplication.getInstance().getSharedPreferences(SP_FILE_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getInt(key, defaultValue);
    }

    public static void putInt(Context context, String preferencesName, String key, int value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(preferencesName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(key, value);
        editor.apply();
    }
    public static void putInt(Context context,  String key, int value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SP_FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(key, value);
        editor.apply();
    }
    public static void putInt(String key, int value) {
        SharedPreferences sharedPreferences = MyApplication.getInstance().getSharedPreferences(SP_FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(key, value);
        editor.apply();
    }

    public static long getLong(Context context, String preferencesName, String key, long defaultValue) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(preferencesName, Context.MODE_PRIVATE);
        return sharedPreferences.getLong(key, defaultValue);
    }

    public static void putLong(Context context, String preferencesName, String key, long value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(preferencesName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong(key, value);
        editor.apply();
    }

    /*
    *   浮点型数据存取
    * */
    public static  void putFloat(Context context, String preferencesName, String key, float value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(preferencesName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putFloat(key, value);
        editor.apply();
    }

    public static float getFloat(Context context, String preferencesName, String key, float defaultValue) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(preferencesName, Context.MODE_PRIVATE);
        return sharedPreferences.getFloat(key, defaultValue);
    }



    public static String getString(Context context, String preferencesName, String key, String defaultValue) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(preferencesName, Context.MODE_PRIVATE);
        return sharedPreferences.getString(key, defaultValue);
    }

    public static String getString(Context context,String key, String defaultValue) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SP_FILE_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(key, defaultValue);
    }
    public static String getString( String key, String defaultValue) {
        SharedPreferences sharedPreferences = MyApplication.getInstance().getSharedPreferences(SP_FILE_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(key, defaultValue);
    }

    public static void putString(Context context, String preferencesName, String key, String value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(preferencesName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public static void putUserName(String value) {
        putString("USER_NAME",value);
    }
    public static String getUserName() {
        return getString("USER_NAME","");
    }


    public static void putString( String key, String value) {
        SharedPreferences sharedPreferences = MyApplication.getInstance().getSharedPreferences(SP_FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public static boolean getBoolean(Context context, String preferencesName, String key, boolean defaultValue) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(preferencesName, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(key, defaultValue);
    }

    public static boolean getBoolean(String key, boolean defaultValue) {
        SharedPreferences sharedPreferences = MyApplication.getInstance().getSharedPreferences(SP_FILE_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(key, defaultValue);
    }


    public static void putBoolean(Context context, String preferencesName, String key, boolean value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(preferencesName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }
    public static void putBoolean(String key, boolean value) {
        SharedPreferences sharedPreferences = MyApplication.getInstance().getSharedPreferences(SP_FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }
    public static void putBoolean(String preferencesName, String key, boolean value) {
        SharedPreferences sharedPreferences = MyApplication.getInstance().getSharedPreferences(preferencesName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    public static void remove(Context context, String preferencesName, String key) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(preferencesName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(key);
        editor.apply();
    }

    /**
     * 存储对象
     */
    public static final String USER_INFO = "user_info";
    /**
     * 保存序列化对象到本地
     *
     * @param context
     * @param fileName SP本地存储路径
     * @param key
     * @param object
     */
    public static void saveSerializableObject(Context context, String fileName, String key, Object object) throws IOException {
        SharedPreferences.Editor spEdit = context.getSharedPreferences(fileName, Context.MODE_PRIVATE).edit();
        //先将序列化结果写到byte缓存中，其实就分配一个内存空间
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream os = new ObjectOutputStream(bos);
        os.writeObject(object);//将对象序列化写入byte缓存
        //将序列化的数据转为16进制保存
        String bytesToHexString = bytesToHexString(bos.toByteArray());
        //保存该16进制数组
        spEdit.putString(key, bytesToHexString);
        spEdit.commit();
    }
    public static void saveSerializableObject(String key, Object object) throws IOException {
        SharedPreferences.Editor spEdit = MyApplication.getInstance().getSharedPreferences(SP_FILE_NAME, Context.MODE_PRIVATE).edit();
        //先将序列化结果写到byte缓存中，其实就分配一个内存空间
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream os = new ObjectOutputStream(bos);
        os.writeObject(object);//将对象序列化写入byte缓存
        //将序列化的数据转为16进制保存
        String bytesToHexString = bytesToHexString(bos.toByteArray());
        //保存该16进制数组
        spEdit.putString(key, bytesToHexString);
        spEdit.commit();
    }
    /**
     * desc:将数组转为16进制
     *
     * @param bArray
     * @return
     */
    public static String bytesToHexString(byte[] bArray) {
        if (bArray == null) {
            return null;
        }
        if (bArray.length == 0) {
            return "";
        }
        StringBuffer sb = new StringBuffer(bArray.length);
        String sTemp;
        for (int i = 0; i < bArray.length; i++) {
            sTemp = Integer.toHexString(0xFF & bArray[i]);
            if (sTemp.length() < 2)
                sb.append(0);
            sb.append(sTemp.toUpperCase());
        }
        return sb.toString();
    }

    /**
     *  取出对象
     */
    /**
     * 从本地反序列化获取对象
     *
     * @param context
     * @param key
     * @return
     */
    public static Object getSerializableObject(Context context, String fileName, String key) throws IOException, ClassNotFoundException {
        SharedPreferences sp = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        if (sp.contains(key)) {
            String string = sp.getString(key, "");
            if (TextUtils.isEmpty(string)) {
                return null;
            } else {
                //将16进制的数据转为数组，准备反序列化
                byte[] stringToBytes = StringToBytes(string);
                ByteArrayInputStream bis = new ByteArrayInputStream(stringToBytes);
                ObjectInputStream is = new ObjectInputStream(bis);
                //返回反序列化得到的对象
                Object readObject = is.readObject();
                return readObject;
            }
        }
        return null;
    }
    public static Object getSerializableObject( String key) throws IOException, ClassNotFoundException {
        SharedPreferences sp = MyApplication.getInstance().getSharedPreferences(SP_FILE_NAME, Context.MODE_PRIVATE);
        if (sp.contains(key)) {
            String string = sp.getString(key, "");
            if (TextUtils.isEmpty(string)) {
                return null;
            } else {
                //将16进制的数据转为数组，准备反序列化
                byte[] stringToBytes = StringToBytes(string);
                ByteArrayInputStream bis = new ByteArrayInputStream(stringToBytes);
                ObjectInputStream is = new ObjectInputStream(bis);
                //返回反序列化得到的对象
                Object readObject = is.readObject();
                return readObject;
            }
        }
        return null;
    }

    /**
     * desc:将16进制的数据转为数组
     *
     * @param data
     * @return
     */
    public static byte[] StringToBytes(String data) {
        String hexString = data.toUpperCase().trim();
        if (hexString.length() % 2 != 0) {
            return null;
        }
        byte[] retData = new byte[hexString.length() / 2];
        for (int i = 0; i < hexString.length(); i++) {
            int int_ch;  // 两位16进制数转化后的10进制数
            char hex_char1 = hexString.charAt(i); //两位16进制数中的第一位(高位*16)
            int int_ch1;
            if (hex_char1 >= '0' && hex_char1 <= '9')
                int_ch1 = (hex_char1 - 48) * 16;   // 0 的Ascll - 48
            else if (hex_char1 >= 'A' && hex_char1 <= 'F')
                int_ch1 = (hex_char1 - 55) * 16; // A 的Ascll - 65
            else
                return null;
            i++;
            char hex_char2 = hexString.charAt(i); //两位16进制数中的第二位(低位)
            int int_ch2;
            if (hex_char2 >= '0' && hex_char2 <= '9')
                int_ch2 = (hex_char2 - 48); // 0 的Ascll - 48
            else if (hex_char2 >= 'A' && hex_char2 <= 'F')
                int_ch2 = hex_char2 - 55; // A 的Ascll - 65
            else
                return null;
            int_ch = int_ch1 + int_ch2;
            retData[i / 2] = (byte) int_ch;//将转化后的数放入Byte里
        }
        return retData;
    }


    /**
     * 保存List
     * @param tag
     * @param datalist
     */
    public  static  <T> void setDataList(String preferencesName, String tag, List<T> datalist) {
        if (null == datalist || datalist.size() <= 0) {
            return;
        }
        SharedPreferences sharedPreferences = MyApplication.getInstance().getSharedPreferences(preferencesName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        //转换成json数据，再保存
        String strJson = gson.toJson(datalist);
        editor.clear();
        editor.putString(tag, strJson);
        editor.commit();

    }

    /**
     * 获取List
     * @param tag
     * @return
     */
    public static  <T> List<T> getDataList(String preferencesName, String tag) {
        SharedPreferences sharedPreferences = MyApplication.getInstance().getSharedPreferences(preferencesName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        List<T> datalist=new ArrayList<T>();
        String strJson = sharedPreferences.getString(tag, null);
        if (null == strJson) {
            return datalist;
        }
        Gson gson = new Gson();
        datalist = gson.fromJson(strJson, new TypeToken<List<T>>() {
        }.getType());
        return datalist;

    }
    public static void clearAll(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SP_FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }
    /*
    * 清除特定sp里面的数据
    * */
    public static void clearAllByName(Context context, String name) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(name, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }

    public static Map<String, ?> getAll(Context context, String preferencesName) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(preferencesName, Context.MODE_PRIVATE);
        return sharedPreferences.getAll();
    }

    /**
     * 放入base64
     */
    public static void putBase64Obj(Context context, String preferencesName, String key, Object obj) {
        if (obj == null) {
            putString(context, preferencesName, key, "");
        } else {
            Gson gson = new Gson();
            putString(context, preferencesName, key, Base64.encodeToString(gson.toJson(obj).getBytes(), Base64.DEFAULT));
        }
    }
    public static void putBase64Obj( String key, Object obj) {
        if (obj == null) {
            putString(MyApplication.getInstance(), SP_FILE_NAME, key, "");
        } else {
            Gson gson = new Gson();
            putString(MyApplication.getInstance(), SP_FILE_NAME, key, Base64.encodeToString(gson.toJson(obj).getBytes(), Base64.DEFAULT));
        }
    }

    /**
     * 获取base64之后的obj
     */
    public static <T> T getBase64Obj(Context context, String preferencesName, String key, Class<T> clazz) {
        String encodeStr = getString(context, preferencesName, key, null);
        if (TextUtils.isEmpty(encodeStr)) {
            return null;
        }
        String decodeStr = new String(Base64.decode(encodeStr, Base64.DEFAULT));
        Gson gson = new Gson();

        try {
            return gson.fromJson(decodeStr, clazz);
        } catch (Exception e) {
            return null;
        }
    }
    public static <T> T getBase64Obj(  String key, Class<T> clazz) {
        String encodeStr = getString(MyApplication.getInstance(), SP_FILE_NAME, key, null);
        if (TextUtils.isEmpty(encodeStr)) {
            return null;
        }
        String decodeStr = new String(Base64.decode(encodeStr, Base64.DEFAULT));
        Gson gson = new Gson();

        try {
            return gson.fromJson(decodeStr, clazz);
        } catch (Exception e) {
            return null;
        }
    }


    /**
     * 返回Editor实例
     *
     * @param ctx Context
     * @return editor
     */
    private static SharedPreferences.Editor getSPEditor(Context ctx) {
        SharedPreferences sp = ctx.getSharedPreferences(SP_FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        return editor;
    }

}
