package com.codingman.www.a014_okgo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.callback.Callback;
import com.lzy.okgo.model.HttpParams;
import com.lzy.okgo.model.Progress;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


    }

    private void OkGoParamsSet(){

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
                .headers("headers1","headersValue1")
                .headers("headers2","headersValue2")
                .params("params1","paramsValue1")
                .params("params2","paramsValue2")
                .params("file1",new File("filepath1"))
                .params("file2",new File("filepath2"))
                .addUrlParams("key",values) //一个key传递多个参数
                .addFileParams("key",files) //一个key传递多个文件
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
}



















