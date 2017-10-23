package com.socool.utilslibrary;


import android.annotation.SuppressLint;
import android.util.Log;

import java.util.Locale;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

/**
 * @author Administrator
 */
public class AESUtils {


    /**
     * AES 16位加密
     * @param seed 密钥
     * @param cleartext 待处理
     * @return 加密结果
     */
    private static String encrypt(String seed, String cleartext) throws Exception {
        byte[] result = encrypt(hexToByte(seed), hexToByte(cleartext));
        return byteToHex(result);
    }

    /**
     * 加密处理
     *
     * @param data 需要加密的数据
     * @return 加密后的数据
     * @throws Exception 异常
     */
    public static byte[] encrypt(byte[] data, String key1, String key2) throws Exception {

        // 组装数据
        String str1 = encrypt(key1, HexUtils.byte2HexStr(data).replace(" ",""));
        String str2 = str1.substring(0, 8);
        String str3 = str1.substring(8, str1.length());
        String str4 = str3 + HexUtils.str2HexStr("00:00:00:00").replace(" ","");
        String str5 = encrypt(key2, str4);
        String str6 = str2 + str5;
        return HexUtils.hexStringToBytes(str6);
    }

    /**
     * AES解密16位
     * @param seed 密钥
     * @param encrypted 已加密内容
     * @return 解密结果
     */
    public static String decrypt(String seed, String encrypted) throws Exception {
        byte[] rawKey = hexToByte(seed);
        byte[] enc = hexToByte(encrypted);
        byte[] result = decrypt(rawKey, enc);
        return byteToHex(result);
    }

//    /**
//     * 对密钥进行处理
//     * @param seed 待处理密钥
//     * @return 处理结果
//     */
//    private static byte[] getRawKey(byte[] seed) throws Exception {
//        KeyGenerator kgen = KeyGenerator.getInstance("AES");
//        SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
//        sr.setSeed(seed);
//        kgen.init(128, sr); // 192 and 256 bits may not be available
//        SecretKey skey = kgen.generateKey();
//        byte[] raw = skey.getEncoded();
//        return raw;
//    }


    /**
     * 进行加密
     * @param raw 密钥byte
     * @param clear 待处理byte
     * @return 加密结果
     */
    private static byte[] encrypt(byte[] raw, byte[] clear) throws Exception {
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        Cipher cipher = Cipher.getInstance("AES/ECB/NoPadding");
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
        byte[] encrypted = cipher.doFinal(clear);
        return encrypted;
    }

    /**
     * AES解密
     * @param raw
     * @param encrypted
     * @return
     */
    private static byte[] decrypt(byte[] raw, byte[] encrypted) throws Exception {
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        Cipher cipher = Cipher.getInstance("AES/ECB/NoPadding");
        cipher.init(Cipher.DECRYPT_MODE, skeySpec);
        byte[] decrypted = cipher.doFinal(encrypted);
        return decrypted;
    }

    /**
     * 将二进制转换成16进制
     * @param by 待转换二进制
     * @return
     */
    public static String byteToHex(byte[] by) {
        String str = "";
        for (int i = 0; i < by.length; i++) {
            int temp = by[i] & 0xFF;
            String strtemp = Integer.toHexString(temp);
            if (strtemp.length() < 2) {
                str += "0" + strtemp;
            } else {
                str += strtemp;
            }

        }
        return str.toUpperCase();
    }

    /**
     * 字符串转换十六进
     * @param str 待转换字符串
     * @return 转换结果
     */
    public static String strToHex(String str) {
        String stt="";
        Log.i("str",str);
        for(int i=str.length()/2-1;i>=0;i--)
        {
            stt+=str.substring(i*2,(i+1)*2);
        }
        return stt.toUpperCase();
    }

    /**
     * 二进制转换字符串
     * @param by 二进制
     * @return 转换结果
     */
    public static String byteToStr(byte[] by) {
        return new String(by);
    }

    /**
     * Mac地址字符串添加冒号
     * @param mac mac地址字符串（不带冒号）
     * @return 结果
     */
    public static String longMac(String mac) {
        if (mac.contains(":")) {
            return mac;
        } else  {
            String Mac=mac;
            if (mac.length() < 17) {
                Mac=mac.substring(0,2)+":"+mac.substring(2,4)+":"+mac.substring(4,6)+":"+mac.substring(6,8)+":"+mac.substring(8,10)+":"+mac.substring(10,12);
            }
            return Mac;
        }
    }

    /**
     * Mac地址字符串去掉冒号
     * @param mac mac地址字符串（带冒号）
     * @return 结果
     */
    public static String shortMac(String mac) {
        return mac.replaceAll(":", "");
    }

    /**
     * 将16进制 转换成二进制
     * @param hexString 待转换16进制
     * @return 转换结果
     */
    @SuppressLint("DefaultLocale")
    public static byte[] hexToByte(String hexString) {
        if (hexString == null || hexString.equals("")) {
            return null;
        }
        hexString = hexString.toUpperCase(Locale.getDefault());
        int length = hexString.length() / 2;
        char[] hexChars = hexString.toCharArray();
        byte[] data = new byte[length];
        for (int i = 0; i < length; i++) {
            int pos = i * 2;
            data[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
        }
        return data;
    }

    /**
     * char转byte
     * @param c char
     * @return 结果
     */
    private static byte charToByte(char c) {
        return (byte) "0123456789ABCDEF".indexOf(c);
    }
}
