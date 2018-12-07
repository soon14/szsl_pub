package com.bsoft.hospital.pub.suzhoumh.util.aes;

import android.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * AES加密，解密
 * Created by Administrator on 2016/4/21.
 */
public class AESUtil {

    private final static String KEY = "0123456789solake";//长度必须16


    // 加密
    public static String encrypt(String content) {

        if(content == null || "".equals(content))
            return "";

        if (KEY == null) {
            System.out.print("Key为空null");
            return null;
        }
        // 判断Key是否为16位
        if (KEY.length() != 16) {
            System.out.print("Key长度不是16位");
            return null;
        }
        try {
            byte[] raw = KEY.getBytes();
            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");//"算法/模式/补码方式"
            IvParameterSpec iv = new IvParameterSpec("0102030405060708".getBytes());//使用CBC模式，需要一个向量iv，可增加加密算法的强度
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
            byte[] encrypted = cipher.doFinal(content.getBytes());

            return Base64.encodeToString(encrypted, Base64.DEFAULT);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e);
        }
        return "";
    }

    // 解密
    public static String decrypt(String content) {
        if(content == null || "".equals(content))
            return "";

        try {
            // 判断Key是否正确
            if (KEY == null) {
                System.out.print("Key为空null");
                return null;
            }
            // 判断Key是否为16位
            if (KEY.length() != 16) {
                System.out.print("Key长度不是16位");
                return null;
            }
            byte[] raw = KEY.getBytes("ASCII");
            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            IvParameterSpec iv = new IvParameterSpec("0102030405060708"
                    .getBytes());
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
            byte[] encrypted = Base64.decode(content, Base64.DEFAULT);//先用base64解密
            try {
                byte[] original = cipher.doFinal(encrypted);
                String originalString = new String(original);
                return originalString;
            } catch (Exception e) {
                e.printStackTrace();
                return "";
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return "";
        }
    }
}
