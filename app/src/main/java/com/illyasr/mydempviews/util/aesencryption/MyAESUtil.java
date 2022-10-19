package com.illyasr.mydempviews.util.aesencryption;


import android.os.Build;

import androidx.annotation.RequiresApi;

import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * java使用AES加密解密 AES-128-ECB加密
 * 与mysql数据库aes加密算法通用
 * 数据库aes加密解密
 * -- 加密
 *    SELECT to_base64(AES_ENCRYPT('www.gowhere.so','jkl;POIU1234++=='));
 *    -- 解密
 *    SELECT AES_DECRYPT(from_base64('Oa1NPBSarXrPH8wqSRhh3g=='),'jkl;POIU1234++==');
 * @author Sieben
 *
 */
public class MyAESUtil {
    /**
     * AES加密字符串
     *
     * @param content
     *            需要被加密的字符串
     * @param password
     *            加密需要的密码
     * @return 密文
     */
    public static byte[] encrypt(String content, String password) {
        try {
            KeyGenerator kgen = KeyGenerator.getInstance("AES");// 创建AES的Key生产者
            kgen.init(128, new SecureRandom(password.getBytes()));// 利用用户密码作为随机数初始化出
            //加密没关系，SecureRandom是生成安全随机数序列，password.getBytes()是种子，只要种子相同，序列就一样，所以解密只要有password就行
            SecretKey secretKey = kgen.generateKey();// 根据用户密码，生成一个密钥
            byte[] enCodeFormat = secretKey.getEncoded();// 返回基本编码格式的密钥，如果此密钥不支持编码，则返回
            SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");// 转换为AES专用密钥
            Cipher cipher = Cipher.getInstance("AES");// 创建密码器
            byte[] byteContent = content.getBytes("utf-8");
            cipher.init(Cipher.ENCRYPT_MODE, key);// 初始化为加密模式的密码器
            byte[] result = cipher.doFinal(byteContent);// 加密
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 解密AES加密过的字符串
     *
     * @param content
     *            AES加密过过的内容
     * @param password
     *            加密时的密码
     * @return 明文
     */
    public static byte[] decrypt(byte[] content, String password) {
        try {
            KeyGenerator kgen = KeyGenerator.getInstance("AES");// 创建AES的Key生产者
            kgen.init(128, new SecureRandom(password.getBytes()));
            SecretKey secretKey = kgen.generateKey();// 根据用户密码，生成一个密钥
            byte[] enCodeFormat = secretKey.getEncoded();// 返回基本编码格式的密钥
            SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");// 转换为AES专用密钥
            Cipher cipher = Cipher.getInstance("AES");// 创建密码器
            cipher.init(Cipher.DECRYPT_MODE, key);// 初始化为解密模式的密码器
            byte[] result = cipher.doFinal(content);
            return result; // 明文
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public static void main(String[] args) throws Exception {
        String content = "密码1993";
        String password = "加密密码";
        System.out.println("需要加密的内容：" + content);
        byte[] encrypt = encrypt(content, password);
        System.out.println("加密后的2进制密文：" + new String(encrypt));
        String hexStr = ParseSystemUtil.parseByte2HexStr(encrypt);
        System.out.println("加密后的16进制密文:" + hexStr);
        byte[] byte2 = ParseSystemUtil.parseHexStr2Byte(hexStr);
        System.out.println("加密后的2进制密文：" + new String(byte2));
        byte[] decrypt = decrypt(byte2, password);
        System.out.println("解密后的内容：" + new String(decrypt,"utf-8"));


    }

    /**
     *  原先的哔哩哔哩链接加密之后的解密方法
     * @param url
     * @return
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String OnRes64(String url) {
        /**
         const url2 = url.slice(9)
         const m = s=>s.replace(/[^A-Za-z0-9\+\/]/g,"")
         const S = a=>m(a.replace(/[-_]/g, (e=>"-" == e ? "+" : "/")))
         S(url2)
         */
        String url2 = url.substring(9);
//        String m = url2.replace("/[^A-Za-z0-9\\+\\/]/g","");
        String S = url2.replace("/[-]/g","+");
        String S1 = S.replace("/[_]/g","/");

        String result = S1.replace("/[^A-Za-z0-9\\+\\/]/g","");

        return decode(result);
    }


    /**
     * base64加密
     * @param text
     * @return
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String encode(String text) {
        return Base64.getEncoder().encodeToString(text.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * base64 解码
     * @param encodedText
     * @return
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String decode(String encodedText) {
        return new String(Base64.getDecoder().decode(encodedText), StandardCharsets.UTF_8);
    }
}
