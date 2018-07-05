package com.codingman.www.a014_okgo;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.callback.BitmapCallback;
import com.lzy.okgo.callback.Callback;
import com.lzy.okgo.callback.FileCallback;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.HttpParams;
import com.lzy.okgo.model.Progress;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.PostRequest;
import com.lzy.okgo.request.base.Request;
import com.orhanobut.logger.Logger;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;

public class MainActivity extends AppCompatActivity {

    private TextView mTv;
    private ImageView mIv;
    private ProgressBar mPb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Logger.init("okGo");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mTv = (TextView) findViewById(R.id.tv);
        mIv = (ImageView) findViewById(R.id.iv);
        mPb = (ProgressBar) findViewById(R.id.pb);
        mPb.setMax(100);


    }

    /**
     * [1]基本的post可以设置的参数
     */
    private void OkGoParamsSet() {

        //okGo支持的参数的设置
        List<String> values = new ArrayList<>();
        List<File> files = new ArrayList<>();
        List<HttpParams.FileWrapper> fileWrappers = new ArrayList<>();

        String url = "";
        OkGo.<String>post(url)
                .tag(this) //请求tag，用于取消对应的请求；
                .isMultipart(true) //强制使用multipart/form-data 表单上传数据
                .isSpliceUrl(true) //强制将params参数拼接在url后面；
                .retryCount(3)
                .cacheKey("key")
                .cacheTime(5000)
                .cacheMode(CacheMode.DEFAULT)
                .headers("headers1", "headersValue1")
                .headers("headers2", "headersValue2")
                .params("params1", "paramsValue1")
                .params("params2", "paramsValue2")
                .params("file1", new File("filepath1"))
                .params("file2", new File("filepath2"))
                .addUrlParams("key", values) //一个key传递多个参数
                .addFileParams("key", files) //一个key传递多个文件
                .addFileWrapperParams("key", fileWrappers)  //一个key传递多个封装好的文件；
                .execute(new Callback<String>() {
                    @Override
                    public void onStart(Request<String, ? extends Request> request) {
                        //UI线程，请求网络之前调用，添加/修改/移除 请求参数
                    }

                    @Override
                    public void onSuccess(Response<String> response) {
                        //UI线程，请求成功之后调用
                    }

                    @Override
                    public void onCacheSuccess(Response<String> response) {
                        //UI线程 缓存读取成功之后调用
                    }

                    @Override
                    public void onError(Response<String> response) {
                        //UI线程 请求失败之后调用
                    }

                    @Override
                    public void onFinish() {
                        ////UI线程 请求结束之后调用，无论网络请求成功或者失败；
                    }

                    @Override
                    public void uploadProgress(Progress progress) {
                        //上传进度回调，只有请求方式包含请求体才回调;
                    }

                    @Override
                    public void downloadProgress(Progress progress) {
                        //下载进度回调
                    }

                    @Override
                    public String convertResponse(okhttp3.Response response) throws Throwable {
                        //子线程，耗时操作
                        //将response 数据转化为string类型；
                        return null;
                    }
                });
    }

    /**
     * [2]最简单的请求
     */

    private void okGoSimpleRequest() {
        String url = "";
        OkGo.<String>get(url)
                .tag(this)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {

                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                    }
                });
    }


    /**
     * [3] 请求图片
     */
    public void okGoGetBitmap(View view) {
        String url = "https://timgsa.baidu" +
                ".com/timg?image&quality=80&size=b9999_10000&sec=1530802143624&di" +
                "=26ff85a74642bf0436b4c62cbf9f4c43&imgtype=0&src=http%3A%2F%2Fpic5.photophoto" +
                ".cn%2F20071228%2F0034034901778224_b.jpg";
        OkGo.<Bitmap>get(url)
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
                        mTv.setText(response.getException().toString());
                    }

                    @Override
                    public void downloadProgress(Progress progress) {
                        super.downloadProgress(progress);
                        Logger.i("Progress:" + (progress.fraction) * 100);
                        mPb.setProgress((int) (progress.fraction * 100));
                        setTitle("Loading");

                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        setTitle("Completed");
                    }
                });
    }

    /**
     * [4]基本文件的下载
     */

    public void okGoFileDownLoadSimple(View view) {
        String url = "https://mirrors.tuna.tsinghua.edu.cn/apache/struts/2.3.34/struts-2.3.34-all" +
                ".zip";
        String fileName = getFileName(url);
        String filePath = Environment.getExternalStorageDirectory().getAbsolutePath();
        OkGo.<File>get(url)
                .tag(this)
                .execute(new FileCallback(filePath, fileName) {
                    @Override
                    public void onSuccess(Response<File> response) {
                        if (response.isSuccessful()) {
                            mTv.setText("DownLoad file succeed!!!");
                        }

                    }

                    @Override
                    public void onError(Response<File> response) {
                        super.onError(response);
                        mTv.setText(response.getException().toString());
                    }

                    @Override
                    public void onStart(Request<File, ? extends Request> request) {
                        super.onStart(request);
                        mPb.setProgress(0);
                    }

                    @Override
                    public void downloadProgress(Progress progress) {
                        super.downloadProgress(progress);
                        mTv.setText("Progress：" + progress.fraction * 100 + "%"+"\n" +
                                "totalSize：" + progress.totalSize/1024/1024 +"MB"+"\n" +
                                "currentSize:" + progress.currentSize/1024/1024+"MB" + "\n" +
                                "speed：" + progress.speed/1024+"KB/s"
                        );
                        mPb.setProgress((int) (progress.fraction * 100));
                    }
                });
    }

    /**
     * [5]上传String类的文本--未测试
     * @param
     * @return
     */

    public void okGoUpLoadStringText(View view){
        String url = "";
        OkGo.<String>post(url)
                .tag(this)
                .upString("上传的文本数据")
                .upString("上传的文本数据2", MediaType.parse("application/xml"))
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        if (response.isSuccessful()){
                            mTv.setText(response.toString()); //返回服务器给的响应
                        }
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        mTv.setText(response.getException().toString());
                    }
                });
    }


    /**
     * [6] 上传json数据：本质也是String类型的数据
     * @param
     * @return
     */

    public void okGoPostJson(View view){

        String url = "";
        Map<String, String> params = new HashMap<>();
        params.put("k1","v1");
        params.put("k2","v2");
        params.put("k3","客户端提供的Json字符串");

        //使用fastJson解析
        String s = JSONArray.toJSONString(params);

        OkGo.<String>post(url)
                .tag(this)
                .upJson(s)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        if(response.isSuccessful()){
                            mTv.setText("服务器返回状态：json上传成功！");
                        }
                    }
                });

    }


    /**
     * [7] 上传文件--一个key对应于多个文件
     * @param
     * @return
     */

    public void okGoPostFiles(View view){
        String url = "";

        List<File> files = new ArrayList<>();
        List<HttpParams.FileWrapper> fileWrappers = new ArrayList<>();
        OkGo.<String>post(url)
                .tag(this)
                .isMultipart(true)//强制使用表单上传
                .params("params","v1")
                .params("file1",new File("filePath1"))
                .params("file2",new File("filePath2"))
                .addFileParams("key",files)
                .addFileWrapperParams("key", fileWrappers)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        if(response.isSuccessful()){
                            mTv.setText("from server:key-Files upLoad success!");
                        }
                    }
                });

    }

    /**
     * [8]单文件上传，一个key对一个文件
     * @param view
     */
    public void okGoPostFile(View view){

        String url = "";

        List<File> files = new ArrayList<>();
        PostRequest<String> request = OkGo.<String>post(url)
//                .tag(this)
                .tag("okGoPostFile")
                .headers("k1", "v1")
                .headers("k2", "v2")
                .params("params1", "pv1")
                .params("params2", "pv2");

        int size = files.size();
        for (int i=0; i<size; i++){
            request.params("file_"+i,files.get(i));
        }

        request.execute(new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
                if(response.isSuccessful()){
                    mTv.setText("from server:key-File upLoad success!");
                }
            }
        });

    }


    //一般的文件下载路径：
    //  http://down.360safe.com/360Root/3 6 0 R o o t S e t u p . e x e
    //  0                               32                            47
    //  separatorIndex = 31; path.length=47;
    // 32--47 len = 15; 32-47开始；
    private String getFileName(String path) {
        int separatorIndex = path.lastIndexOf("/"); //获取最后一个"/"的索引值；
        return (separatorIndex < 0) ? path : path.substring(separatorIndex + 1, path.length());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        /**
         * 取消请求
         */
        OkGo.getInstance().cancelTag(this);
        OkGo.getInstance().cancelAll();
        OkGo.cancelAll(OkHttpClient);
        OkGo.cancelTag(OkHttpClient,"okGoPostFile");
    }
}












