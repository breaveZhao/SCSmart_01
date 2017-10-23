package com.socool.utilslibrary.OkHttpUtils;

import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by 李良海 on 2017/10/23.
 * Function：
 */

public class CodeResult {
    /**
     * 获取code 然后判断code类型
     */
    public static void getCode(String value, Context context) {
        int code = -1;
        if (value != null) {
            try {
                JSONObject obj = new JSONObject(value);
                code = obj.getInt("code");
                codeStyle(code, context);
                code = -1;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private static void codeStyle(int code, Context context) {
        switch (code) {
            case -1:
                //数据获取失败
                break;
        }

    }
}
