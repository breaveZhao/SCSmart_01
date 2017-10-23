package com.socool.utilslibrary;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by 李良海 on 2017/10/19.
 * Function：
 */

public class TimeUtils {
    /**
     * 获取当前时间戳
     */

    public static String TimeSecond() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd hh:mm");
        String dateString = "2000/01/01 00:00:00";
        Date date = null;
        try {
            date = df.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long s = date.getTime();
        String result = HexUtils.str2HexStr((System.currentTimeMillis() - s)/1000 + "");
        if (result.length() == 8){
            StringBuffer buffer = new StringBuffer();
            for (int i = 0;i < result.length();i = i + 2){
                buffer.append(result.substring(i,i+2));
            }
            return buffer.toString();
        }else {
            return "";
        }
    }







}
