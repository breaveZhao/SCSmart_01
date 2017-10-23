package com.socool.utilslibrary;

import android.util.Log;

/**
 * Created by 李良海 on 2017/10/19.
 * Function：
 */

public class HexUtils {
    /**
     * 字符串转换成十六进制字符串
     * //@param String str 待转换的int字符串
     *
     * @return String 每个Byte之间空格分隔，如: [61 6C 6B]
     */
    public static String str2HexStr(String str) {
        if (str == null) {
            return "";
        } else {
            StringBuffer buffer = new StringBuffer();
            String[] value = str.split(":");
            for (int i = 0; i < value.length; i++) {
                buffer.append(str2Hex(value[i]));
                if (i < (value.length - 1)) {
                    buffer.append(" ");
                }
            }
            return buffer.toString();
        }
    }


    private static String str2Hex(String str) {
        String stt = "";
        if (str == null || str.equals("")) {
            return stt;
        } else {
            stt = Integer.toHexString(Integer.valueOf(str));
            if (stt.length() == 0) {
                return "";
            } else if (stt.length() == 1) {
                return "0" + stt.toUpperCase();
            } else {
                return stt.toUpperCase();
            }
        }
    }


    /**
     * 十六进制转换字符串
     * //@param String str Byte字符串(Byte之间无分隔符 如:[616C6B])
     *
     * @return String 对应的字符串
     */
    public static String hexStr2Str(String hexStr) {
        if (hexStr == null || hexStr.equals("")) {
            return "";
        } else {
            StringBuffer value = new StringBuffer();
            String[] values = hexStr.split(" ");
            for (int i = 0; i < values.length; i++) {
                value.append(Integer.parseInt(values[i], 16));
                if (i < values.length - 1) {
                    value.append(":");
                }
            }
            return value.toString();
        }
    }

    /**
     * bytes转换成十六进制字符串
     * //@param byte[] b byte数组
     *
     * @return String 每个Byte值之间空格分隔
     */
    public static String byte2HexStr(byte[] b) {
        String stmp = "";
        StringBuilder sb = new StringBuilder("");
        for (int n = 0; n < b.length; n++) {
            stmp = Integer.toHexString(b[n] & 0xFF);
            sb.append((stmp.length() == 1) ? "0" + stmp : stmp);
            sb.append(" ");
        }
        return sb.toString().toUpperCase().trim();
    }


    /*
    * 异或求职*/
    public static byte getXor(byte[] datas) {

        byte temp = datas[0];

        for (int i = 1; i < datas.length; i++) {
            temp ^= datas[i];
        }

        return temp;
    }

    /**
     * bytes字符串转换为Byte值
     * //@param String src Byte字符串，每个Byte之间没有分隔符
     *
     * @return byte[]
     */
    public static byte[] hexStringToBytes(String hexString) {
        if (hexString == null || hexString.equals("")) {
            return null;
        }
        hexString = hexString.toUpperCase().replace(" ","");
        int length = hexString.length() / 2;
        char[] hexChars = hexString.toCharArray();
        byte[] d = new byte[length];
        for (int i = 0; i < length; i++) {
            int pos = i * 2;
            d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));

        }
        return d;
    }

    private static byte charToByte(char c) {
        return (byte) "0123456789ABCDEF".indexOf(c);
    }


    /**
     * @param typedef_hexStr 操作类型
     * @param order_hexStr   操作命令
     *                       拼装16 str进制的数据
     */

    public static String setValue_HexStr(String typedef_hexStr, String order_hexStr) {
        String currentID_hex = HexUtils.str2HexStr("15");//当前id
        String typedef_hex = typedef_hexStr;//操作类型
        String order = "";
        for (int i = 0; i < 18 - order_hexStr.length(); i++) {
            order = order + "0";
        }
        String order_hex = order_hexStr + order;
        String time_hex = TimeUtils.TimeSecond();
        Log.e("TAG_ZY", "最后的数据 = " + currentID_hex + typedef_hex + order_hex + time_hex);
        return currentID_hex + typedef_hex + order_hex + time_hex;
    }

    /**
     * @param typedef_str 操作类型
     * @param order_str   操作命令
     */

    public static byte[] setValue_Str_2(String typedef_str, String order_str) {
        byte[] value = new byte[15];
        value[0] = hexStringToBytes(HexUtils.str2HexStr("15"))[0];
        value[1] = hexStringToBytes(HexUtils.str2HexStr(typedef_str))[0];
        byte[] uids = order_str.getBytes();
        for (int i = 0; i < uids.length; i++) {
            value[i + 2] = order_str.getBytes()[i];
        }
        String time_hex = TimeUtils.TimeSecond();
        for (int i = 0; i < hexStringToBytes(time_hex).length; i++) {
            value[11 + i] = hexStringToBytes(time_hex)[hexStringToBytes(time_hex).length - 1 - i];
        }
        return value;
    }

    public static byte[] getValue_byteArr(String hexStr) {
        byte[] a = hexStringToBytes(hexStr);
        byte b = getXor(a);
        byte[] c = new byte[16];
        for (int i = 0; i < 16; i++) {
            if (i < 11) {
                c[i] = a[i];
            } else if (i == 11) {
                c[i] = b;
            } else {
                c[i] = a[i - 1];
            }
        }
        return c;
    }

    public static byte[] getValue_byteArr(byte[] value) {
        byte[] a = value;
        byte b = getXor(a);
        byte[] c = new byte[16];
        for (int i = 0; i < 16; i++) {
            if (i < 11) {
                c[i] = a[i];
            } else if (i == 11) {
                c[i] = b;
            } else {
                c[i] = a[i - 1];
            }
        }
        return c;
    }
}
