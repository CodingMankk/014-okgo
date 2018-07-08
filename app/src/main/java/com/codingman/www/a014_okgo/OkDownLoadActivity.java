package com.codingman.www.a014_okgo;

import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Progress;
import com.lzy.okgo.request.GetRequest;
import com.lzy.okserver.OkDownload;
import com.lzy.okserver.download.DownloadListener;
import com.lzy.okserver.download.DownloadTask;
import com.lzy.okserver.task.XExecutor.OnAllTaskEndListener;
import com.orhanobut.logger.Logger;

import java.io.File;
import java.util.Map;

/**
 * @function: okGoDownLoad 下载demo
 */

public class OkDownLoadActivity extends AppCompatActivity implements OnAllTaskEndListener{

    private OkDownload mOkDownload;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_okdownload);

        mOkDownload = OkDownload.getInstance();
        //全局设置
        okDownLoadSet();


    }

    /**
     * [A]全局设置
     */
    private void okDownLoadSet() {

        //[1]设置下载的文件存储的目录路径
        String path = Environment.getExternalStorageDirectory().getAbsolutePath()+"/download/";
        mOkDownload.setFolder(path);
        //[2]设置同时下载的文件数量
        mOkDownload.getThreadPool().setCorePoolSize(5);
        //[3]所有下载任务设置监听,implement接口 OnAllTaskEndListener
        mOkDownload.addOnAllTaskEndListener(this);
    }

    /**
     * [B]全部任务的操控
     * @param action
     */
    private void okDownloadControl(int action){

        //[1]任务全部开始/继续下载所有的已经暂停的任务
       mOkDownload.startAll();
        //[2]任务全部下载中任务暂停
        mOkDownload.pauseAll();
        //[3]移除所有任务但不删除已经下载的任务文件
        mOkDownload.removeAll();
       //[4]移除所有任务--删除已经下载的任务文件
        mOkDownload.removeAll(true);
        //[5]根据tag移除任务
        mOkDownload.removeTask("taskTag");
        //[6]获取当前所有下载任务的map
        Map<String, DownloadTask> taskMap = mOkDownload.getTaskMap();
        //[7]根据tag获取任务
        DownloadTask taskTag1 = mOkDownload.getTask("taskTag");
        //[8]标识为tag的任务是否存在
        boolean taskTag = mOkDownload.hasTask("taskTag");

    }


    /**
     * [3]基本断点的下载方法
     */

    private void okGoDownLoadOneFile(){
        String url = "";
        //1. 创建request
        GetRequest<File> request = OkGo.<File>get(url)
                .headers("aaa", "111")
                .params("bbb", "222");
        //2.创建task
        DownloadTask task = OkDownload.request("taskTag", request) //传入tag和下载请求
                .extra1("apk")  //下载任务额外字段
                .save() //第一次创建任务调用save
                .register(new LogDownloadListener());//当前任务的回调监听


//        task.register();
        task.start(); //开始一个新任务，或者继续下载暂停的任务都是这个方法
        task.pause(); //将一个下载中的任务暂停
        task.remove();//移除一个任务，无论这个任务是在下载中、暂停、完成还是其他任何状态，都        可以直接移除这个任务
        task.remove();
        task.restart(); //重新下载一个任务。重新下载会先删除以前的任务，同时也会删除文件，然后从        头开始重新下载该文件。
        task.priority(100);
        task.folder("path/download");//单独指定当前下载任务的文件夹目录
        task.fileName("指定的下载的文件名称"); //手动指定下载文件的文件名称
//        task.extra1();
//        task.extra2();
//        task.extra3();
        task.save();


    }


    @Override
    public void onAllTaskEnd() {

        Toast.makeText(this, "所有任务下载已经完成", Toast.LENGTH_SHORT).show();
    }



    private class LogDownloadListener extends DownloadListener{

        public LogDownloadListener() {
            super("LogDownloadListener");
        }

        // onStart()方法是在下载请求之前执行的，所以可以做一些请求之前相关的事，比如修改请求参数，加密，显示对话框等等
        @Override
        public void onStart(Progress progress) {
            Logger.d("onStart:"+progress);
        }

        @Override
        public void onProgress(Progress progress) {
            Logger.d("onProgress:"+progress);
        }

        @Override
        public void onError(Progress progress) {
            Logger.d("onError:"+progress);
            progress.exception.printStackTrace();
        }

        //在调用onFinish()之前，还会额外调用一次onProgress()方法，这样的好处可以在onProgress方法中捕获到所有的状态变化，方便管理
        @Override
        public void onFinish(File file, Progress progress) {
            Logger.d("onFinish:"+progress);
        }

        @Override
        public void onRemove(Progress progress) {
            Logger.d("onRemove:"+progress);
        }
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        //[x]移除所有任务的监听
        mOkDownload.removeOnAllTaskEndListener(this);
    }
}
