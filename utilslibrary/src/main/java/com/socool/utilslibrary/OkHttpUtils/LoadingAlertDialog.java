package com.socool.utilslibrary.OkHttpUtils;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.socool.utilslibrary.R;


/**
 * Created by PC on 2017/9/10.
 */

public class LoadingAlertDialog extends AlertDialog {

    private Context mContext;
    private ProgressBar mBar;
    private TextView mMessage;

    public LoadingAlertDialog(Context context) {
        super(context, R.style.LoadDialog);
        mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_loading_layout);
        //点击imageview外侧区域，动画不会消失
        setCanceledOnTouchOutside(false);
        mBar = (ProgressBar) findViewById(R.id.bar);
        mMessage = (TextView) findViewById(R.id.message);
    }

    public void show(String msg) {
        super.show();
        if (mMessage != null) {
            mMessage.setText(msg);
        }
    }

    //设置进度图片
    public void setIndeterminateDrawable(int drawable) {
        mBar.setIndeterminateDrawable(mContext.getResources().getDrawable(drawable));
    }

    //设置字体颜色
    public void setTextColor(int color) {
        mMessage.setTextColor(color);
    }
}
