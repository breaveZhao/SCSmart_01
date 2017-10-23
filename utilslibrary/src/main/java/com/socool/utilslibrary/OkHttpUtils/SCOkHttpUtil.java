package com.socool.utilslibrary.OkHttpUtils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.util.Log;

import com.socool.utilslibrary.ToastUtils.AppMsg;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;
import com.zhy.http.okhttp.request.RequestCall;

import java.util.Map;

import okhttp3.Call;


/**
 * Created by 123 on 2017/9/18.
 */

public class SCOkHttpUtil {
    private static SCOkHttpUtil okHttpUtil;
    private static String cancelTag = null;
    private static String url = null;
    private static Map<String, String> valueAndKey = null;
    private static onFailListener mfailListener = null;
    private static onSuccessListener msuccessListener = null;
    private static OnResultListener mOnResultListener = null;
    private static LoadingAlertDialog loadingAlertDialog = null;
    private static AlertDialog yourLoadingAlertDialog = null;
    private static RequestCall call;
    private static Context context;
    private static boolean ishow = true;

    /***
     * 使用前先注册*/
    public static SCOkHttpUtil create(Context mContext) {
        unRegister();
        context = mContext;
        okHttpUtil = new SCOkHttpUtil();
        return okHttpUtil;
    }

    /**
     * 初始化网络请求的参数.
     * 带参数的请求
     */
    public static SCOkHttpUtil initValue(String mUrl, Map<String, String> mkeyAndValue) {
        if (context == null) {
            try {
                throw new SCOkHttpError("使用这个框架前，请在Activity中onCreate函数中注册，" + "\n"
                        + "当一个页面销毁的时候请在OnDestroy中反注册，否则可能会报错");
            } catch (SCOkHttpError myOkHttpError) {
                myOkHttpError.printStackTrace();
                Log.e("MyOkHttpError", myOkHttpError.getMessage());
            }
        }
        if (cancelTag == null) {
            unRegister();
            url = mUrl;
            cancelTag = mUrl;
            valueAndKey = mkeyAndValue;
            loadingAlertDialog = new LoadingAlertDialog(context);
        } else {
            CancelRequest();
            unRegister();
            url = mUrl;
            cancelTag = mUrl;
            valueAndKey = mkeyAndValue;
            loadingAlertDialog = new LoadingAlertDialog(context);
        }
        return okHttpUtil;
    }

    /**
     * 初始化网络请求的参数
     * 不带参数的请求
     */
    public static SCOkHttpUtil initValue(String mUrl) {
        if (context == null) {
            try {
                throw new SCOkHttpError("使用这个框架前，请在Activity中onCreate函数中注册，" + "\n" + "当一个页面销毁的时候请在OnDestroy中反注册，否则可能会报错");
            } catch (SCOkHttpError myOkHttpError) {
                myOkHttpError.printStackTrace();
                Log.e("MyOkHttpError", myOkHttpError.getMessage());
            }
        }
        if (cancelTag == null) {
            unRegister();
            url = mUrl;
            cancelTag = mUrl;
            loadingAlertDialog = new LoadingAlertDialog(context);
        } else {
            CancelRequest();
            unRegister();
            url = mUrl;
            cancelTag = mUrl;
            loadingAlertDialog = new LoadingAlertDialog(context);
        }
        okHttpUtil = new SCOkHttpUtil();
        return okHttpUtil;
    }


    /**
     * 执行post方法
     */
    public void doPost() {
        if (url != null) {
            ShowDialog();
            cancelTag = url;
            call = OkHttpUtils
                    .post()
                    .url(url)
                    .params(valueAndKey)
                    .build();

            call.execute(new StringCallback() {
                @Override
                public void onError(Call call, Exception e, int id) {
                    cancelTag = "";
                    DissDialog();
                    AppMsg.makeText((Activity) context, "网络加载失败", AppMsg.STYLE_ALERT).show();
                    if (mfailListener != null) {
                        mfailListener.onFail(e);
                    }
                    if (mOnResultListener != null) {
                        mOnResultListener.onFail(e);
                    }
                }


                @Override
                public void onResponse(String response, int id) {
                    cancelTag = "";
                    DissDialog();
                    if (msuccessListener != null) {
                        msuccessListener.onSuccess(response);
                    }

                    if (mOnResultListener != null) {
                        mOnResultListener.onSuccess(response);
                    }
                }
            });
        }
    }

    /**
     * 执行get方法
     */
    public void doGet() {
        if (url != null && url.startsWith("http")) {
            ShowDialog();
            cancelTag = url;
            call = OkHttpUtils
                    .get()
                    .url(url)
                    .build();
            call.execute(new StringCallback() {
                @Override
                public void onError(Call call, Exception e, int id) {
                    okHttpUtil.cancelTag = "";
                    DissDialog();
                    AppMsg.makeText((Activity) context, "网络加载失败", AppMsg.STYLE_ALERT).show();
                    if (mfailListener != null) {
                        mfailListener.onFail(e);
                    }
                    if (mOnResultListener != null) {
                        mOnResultListener.onFail(e);
                    }

                }

                @Override
                public void onResponse(String response, int id) {
                    cancelTag = "";
                    DissDialog();
                    if (msuccessListener != null) {
                        msuccessListener.onSuccess(response);
                    }

                    if (mOnResultListener != null) {
                        mOnResultListener.onSuccess(response);
                    }
                }
            });
        }
    }

    /**
     * 取消请求操作
     */
    public static void CancelRequest() {
        Log.e("Tag = ", "" + cancelTag);
        if (cancelTag != null) {
            call.cancel();
        }
    }

    /**
     * 成功的返回结果
     */
    public interface onSuccessListener {
        public void onSuccess(String result);
    }


    public SCOkHttpUtil setOnSuccessListener(onSuccessListener successListener) {
        msuccessListener = successListener;
        return okHttpUtil;
    }


    /**
     * 失败的返回结果
     */
    public interface onFailListener {
        public void onFail(Exception e);
    }


    public SCOkHttpUtil setOnFailListener(onFailListener failListener) {
        mfailListener = failListener;
        return okHttpUtil;
    }


    /**
     * 返回请求结果
     */

    public interface OnResultListener {
        public void onFail(Exception e);

        public void onSuccess(String result);

    }

    public SCOkHttpUtil setResultListener(OnResultListener resultListener) {
        this.mOnResultListener = resultListener;
        return okHttpUtil;
    }


    /**
     * 还原所有的设置
     */
    private static void unRegister() {
        url = null;
        cancelTag = null;
        valueAndKey = null;
        mfailListener = null;
        msuccessListener = null;
        call = null;
        ishow = true;
        if (loadingAlertDialog != null) {
            if (loadingAlertDialog.isShowing()) {
                loadingAlertDialog.dismiss();
                loadingAlertDialog = null;
            } else {
                loadingAlertDialog = null;
            }
        }
        okHttpUtil = null;
    }

    /**
     * 显示diaolog
     */


    private static void ShowDialog() {
        if (ishow) {
            if (yourLoadingAlertDialog == null) {
                if (loadingAlertDialog != null) {
                    if (!loadingAlertDialog.isShowing()) {
                        loadingAlertDialog.show("加载中...");
                    }
                }
            } else {
                if (!yourLoadingAlertDialog.isShowing()) {
                    yourLoadingAlertDialog.show();
                }
            }
        }
    }

    /**
     * 关闭dialog
     */
    private static void DissDialog() {
        if (ishow) {
            if (yourLoadingAlertDialog == null) {
                if (loadingAlertDialog != null) {
                    if (loadingAlertDialog.isShowing()) {
                        loadingAlertDialog.dismiss();
                    }
                }
            } else {
                if (yourLoadingAlertDialog.isShowing()) {
                    yourLoadingAlertDialog.dismiss();
                }
            }
        }
    }

    /**
     * @param isShow                 是否判断
     * @param yourLoadingAlertDialog 你的网络加载动画，传入null ，那么就会是用系统默认的
     */
    public SCOkHttpUtil isShowDialog(boolean isShow, AlertDialog yourLoadingAlertDialog) {
        if (yourLoadingAlertDialog == null) {

        } else {
            this.yourLoadingAlertDialog = yourLoadingAlertDialog;
        }
        ishow = isShow;
        return okHttpUtil;
    }

}
