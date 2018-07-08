package com.oztaking.www.okgodownloaddemo;

import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.lzy.okgo.model.Progress;
import com.lzy.okserver.download.DownloadListener;
import com.orhanobut.logger.Logger;

import java.io.File;

/**
 * @function:
 */

public class LogDownloadListener extends DownloadListener {

    private ProgressBar mPb;
    private TextView mTvCurrentSize;
    private TextView mTvTotalSize;
    private TextView mTvSpeed;


    public LogDownloadListener() {
        super("LogDownloadListener");
    }

    public LogDownloadListener(Object tag, ProgressBar progressBar, TextView currentSize, TextView totalSize,TextView speed ) {
        super(tag);
        this.mPb = progressBar;
        mPb.setMax(100);
        this.mTvCurrentSize = currentSize;
        this.mTvTotalSize = totalSize;
        this.mTvSpeed = speed;

    }

    // onStart()方法是在下载请求之前执行的，所以可以做一些请求之前相关的事，比如修改请求参数，加密，显示对话框等等
    @Override
    public void onStart(Progress progress) {
        mTvTotalSize.setText(progress.totalSize/1024/1024+"M");
        mPb.setProgress(0);
        Logger.d("onStart:" + progress);
    }

    @Override
    public void onProgress(Progress progress) {
        Logger.d("onProgress:" + progress);
        mPb.setProgress((int)(progress.fraction*100.00));
        mTvTotalSize.setText(progress.totalSize/1024/1024+"M");
        mTvCurrentSize.setText(progress.currentSize/1024/1024+"M/");
        mTvSpeed.setText(progress.speed/1024+"KB/s");
    }

    @Override
    public void onError(Progress progress) {
        Logger.d("onError:" + progress);
        progress.exception.printStackTrace();
    }

    //在调用onFinish()之前，还会额外调用一次onProgress()方法，这样的好处可以在onProgress方法中捕获到所有的状态变化，方便管理
    @Override
    public void onFinish(File file, Progress progress) {

        Logger.d("onFinish:" + progress);
        mTvSpeed.setVisibility(View.INVISIBLE);
        mTvCurrentSize.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onRemove(Progress progress) {

        Logger.d("onRemove:" + progress);
    }
}


