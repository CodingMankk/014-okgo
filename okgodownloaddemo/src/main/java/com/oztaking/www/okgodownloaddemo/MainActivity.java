package com.oztaking.www.okgodownloaddemo;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.BitmapCallback;
import com.lzy.okgo.model.Progress;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.GetRequest;
import com.lzy.okserver.OkDownload;
import com.lzy.okserver.download.DownloadTask;
import com.lzy.okserver.task.XExecutor;

import java.io.File;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements XExecutor.OnAllTaskEndListener {


    private String tag = "tag";

    private LogDownloadListener mListener;

    private OkDownload mOkDownload;
    private TextView mTvMsg;
    private TextView mTvCurrentSize;
    private TextView mTvTotalSize;
    private TextView mTvStatus;
    private TextView mTvSpeed;
    private ProgressBar mPb;
    private DownloadTask mTask;
    private ImageView mIv;
    private TextView mTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        okDownLoadSet();
        okGODownLoadCreateTask();
    }

    public void initView() {

        mIv = (ImageView) findViewById(R.id.iv);


        mTv = (TextView) findViewById(R.id.tv);
        mTvMsg = (TextView) findViewById(R.id.tv_msg);
        mTvCurrentSize = (TextView) findViewById(R.id.tv_CurrentSize);
        mTvTotalSize = (TextView) findViewById(R.id.tv_TotalSize);
        mTvStatus = (TextView) findViewById(R.id.tv_status);
        mTvSpeed= (TextView) findViewById(R.id.tv_speed);

        mPb = (ProgressBar) findViewById(R.id.pb);

    }

    /**
     * [A]全局设置
     */
    private void okDownLoadSet() {

        mOkDownload = OkDownload.getInstance();

        //[1]设置下载的文件存储的目录路径
        String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/download/";
        mOkDownload.setFolder(path);
        //[2]设置同时下载的文件数量
        mOkDownload.getThreadPool().setCorePoolSize(5);
        //[3]所有下载任务设置监听,implement接口 OnAllTaskEndListener
        mOkDownload.addOnAllTaskEndListener(this);
    }


    public void startAll(View view) {

        mOkDownload.startAll();
        mTvMsg.setText("开始所有任务");

    }

    public void pauseAll(View view) {
        mOkDownload.pauseAll();

        mTvMsg.setText("暂停所有任务");
    }

    public void removeAll(View view) {
        mOkDownload.removeAll();
        mPb.setProgress(0);
        mTvMsg.setText("移除所有任务");
        mTvCurrentSize.setText("0M/");

    }

    public void removeDeleteAll(View view) {
        mOkDownload.removeAll(true);
        mPb.setProgress(0);
        mTvMsg.setText("移除所有任务及文件");
        mTvCurrentSize.setText("0M/");

    }


    public void removeTask(View view) {
        mOkDownload.removeTask(tag);
        mPb.setProgress(0);

    }

    public void getTaskMap(View view) {
        Map<String, DownloadTask> taskMap = mOkDownload.getTaskMap();
        mTvMsg.setText(taskMap.toString());

    }

    public void getTask(View view) {
        DownloadTask task = mOkDownload.getTask(tag);
        mTvMsg.setText("执行的任务是：" + task.toString());

    }

    public void hasTask(View view) {
        boolean b = mOkDownload.hasTask(tag);
        mTvMsg.setText("该任务任然是否存在：" + b);
    }


    private void okGODownLoadCreateTask() {

        String apkname = "爱奇艺";

        String apk1url = "http://121.29.10.1/f5.market.mi-img.com/download/AppStore/0b8b552a1df0a8bc417a5afae3a26b2fb1342a909/com.qiyi.video.apk";

        mTv.setText(apkname);

        GetRequest<File> request = OkGo.<File>get(apk1url)
                .headers("aaa", "111")
                .params("bbb", "222");

        mListener = new LogDownloadListener(tag,mPb,mTvCurrentSize,mTvTotalSize,mTvSpeed);
        mTask = OkDownload.request(tag, request)
                .save()
                .register(mListener);
    }


    public void okGoGetBitmap() {
        String apkiconUrl = "http://file.market.xiaomi.com/thumbnail/PNG/l114/AppStore/0c10c4c0155c9adf1282af008ed329378d54112ac";

        OkGo.<Bitmap>get(apkiconUrl)
                .tag(this)
                .execute(new BitmapCallback() {
                    @Override
                    public void onSuccess(Response<Bitmap> response) {
                        if (response.isSuccessful()) {
                            mIv.setImageBitmap(response.body());
                        }
                    }

                    @Override
                    public void onError(Response<Bitmap> response) {
                        super.onError(response);

                    }

                    @Override
                    public void downloadProgress(Progress progress) {
                        super.downloadProgress(progress);
                        setTitle("Loading");

                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        setTitle("Completed");
                    }
                });
    }



    //==========单个任务的方法====================
    public void start(View view) {
//        if (mTask != null){
//            mTask.save();
//        }
        okGoGetBitmap();
        mTask.start();
        mTvMsg.setText("正在下载");

    }

    public void pause(View view) {
        mTask.pause();
        mTvMsg.setText("已经暂停");
    }

    public void remove(View view) {
        mTask.remove();
        mTask.save();
        mPb.setProgress(0);
        mTvMsg.setText("任务移除");
        mTvCurrentSize.setText("0M/");
    }

    public void removeDelete(View view) {
        mTask.remove(true);
        mTask.save();
        mPb.setProgress(0);
        mTvMsg.setText("任务移除&文件被删除");
        mTvCurrentSize.setText("0M/");
    }


    public void restart(View view) {
        if (mTask != null){
            mTask.restart();
        }

        mPb.setProgress(0);
        mTvMsg.setText("正在下载");
    }


    //在任务下载结束/暂停都会调用该方法
    @Override
    public void onAllTaskEnd() {
        Toast.makeText(this, "所有任务下载已经完成", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mOkDownload.removeOnAllTaskEndListener(this);
    }
}
