package com.codingman.www.a014_okgo.callback;

import android.app.Activity;
import android.app.ProgressDialog;
import android.view.Window;

import com.lzy.okgo.request.base.Request;

/**
 * @function: 自定义callBack
 */

public abstract class DialogCallBack<T> extends JsonCallBackOptimiseFinal<T>{

    private ProgressDialog mProgressDialog;

    public DialogCallBack(Activity activity) {
        super();
        initDialog(activity);
    }

    private void initDialog(Activity activity){
        mProgressDialog = new ProgressDialog(activity);
        mProgressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setMessage("七牛网络中...");
    }

    @Override
    public void onStart(Request<T, ? extends Request> request) {
        if (mProgressDialog != null && !mProgressDialog.isShowing()){
            mProgressDialog.show();
        }
    }

    @Override
    public void onFinish() {
        if (mProgressDialog != null && !mProgressDialog.isShowing()){
            mProgressDialog.dismiss();
        }

    }
}
