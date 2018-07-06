package com.codingman.www.a014_okgo;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONArray;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.adapter.Call;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.callback.BitmapCallback;
import com.lzy.okgo.callback.Callback;
import com.lzy.okgo.callback.FileCallback;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.convert.StringConvert;
import com.lzy.okgo.model.HttpParams;
import com.lzy.okgo.model.Progress;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.PostRequest;
import com.lzy.okgo.request.base.Request;
import com.orhanobut.logger.Logger;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;

public class MainActivity extends AppCompatActivity {

    private TextView mTv;
    private ImageView mIv;
    private ProgressBar mPb;

    private String mBaseUrl = "http://192.168.0.12/okhttpDemo";
    private String mBaseOkGoUrl = "http://192.168.1.104/okGoServer";

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
     * [3] 请求图片---测试成功
     */
    public void okGoGetBitmap(View view) {
        String url = "https://timgsa.baidu" +
                ".com/timg?image&quality=80&size=b9999_10000&sec=1530802143624&di" +
                "=26ff85a74642bf0436b4c62cbf9f4c43&imgtype=0&src=http%3A%2F%2Fpic5.photophoto" +
                ".cn%2F20071228%2F0034034901778224_b.jpg";

        String mNativeUrlPic1 = "http://192.168.0.12:8080/1.jpg"; //本地tomcat服务器图片；
        String mNativeUrlPic2 = "http://192.168.0.12:8080/2.jpg"; //本地tomcat服务器图片；
        String mNativeUrlPic3 = "http://192.168.0.12:8080/3.jpg"; //本地tomcat服务器图片；

        String mNativeUrlPic4 = "http://192.168.1.104:8080/1.jpg"; //本地tomcat服务器图片；
        String mNativeUrlPic5 = "http://192.168.1.104:8080/2.jpg"; //本地tomcat服务器图片；
        String mNativeUrlPic6 = "http://192.168.1.104:8080/3.jpg"; //本地tomcat服务器图片；



        OkGo.<Bitmap>get(mNativeUrlPic4)
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
     * [4]基本文件的下载--测试成功
     */

    public void okGoFileDownLoadSimple(View view) {
        String url = "https://mirrors.tuna.tsinghua.edu.cn/apache/struts/2.3.34/struts-2.3.34-all" +
                ".zip";

        String url1 = "http://yc.jb51.net:81/201710/books/AndroidLauncher_jb51.net.rar";

        String url2 = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1530891265967&di=f3b0108bc890fac94c42a00f47b23957&imgtype=0&src=http%3A%2F%2Fpic20.photophoto.cn%2F20110902%2F0034034471873095_b.jpg";



        final String fileName = getFileName(url2);
        String filePath = Environment.getExternalStorageDirectory().getAbsolutePath();
        Logger.i(filePath);
        OkGo.<File>get(url2)
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
                        mTv.setText(
                                "文件名称：" + fileName + "\n" +
                                        "Progress：" + progress.fraction * 100 + "%" + "\n" +
                                        "totalSize：" + progress.totalSize / 1024 / 1024 + "MB" + "\n" +
                                        "currentSize:" + progress.currentSize / 1024 / 1024 + "MB" + "\n" +
                                        "speed：" + progress.speed / 1024 + "KB/s"
                        );
                        mPb.setProgress((int) (progress.fraction * 100));
                    }
                });
    }

    /**
     * [5]上传String类的文本--测试通过
     *
     * @param
     * @return
     */

    public void okGoUpLoadStringText(View view) {
        String url = "http://192.168.0.12:8080/okhttpDemo/postString";
        OkGo.<String>post(url)
                .tag(this)
                .upString("upload string 11111")
                .upString("upload string 22222", MediaType.parse("application/xml"))
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        if (response.isSuccessful()) {
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
     * [6] 上传json数据：本质也是String类型的数据-测试通过
     *
     * @param
     * @return
     */

    public void okGoPostJson(View view) {

        String url = "http://192.168.0.12:8080/okhttpDemo/postString";

        Map<String, String> params = new HashMap<>();
        params.put("k1", "String 1");
        params.put("k2", "String 2");
        params.put("k3", "from android device JsonString");

        //使用fastJson解析
        String s = JSONArray.toJSONString(params);

        OkGo.<String>post(url)
                .tag(this)
                .upJson(s)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        if (response.isSuccessful()) {
                            mTv.setText("服务器返回状态:" + response.toString());
                        }
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        mTv.setText("服务器返回状态：" + response.getException().toString());
                    }
                });
    }


    /**
     * [7] 上传文件--一个key对应于多个文件-未测试
     *
     * @param
     * @return
     */

    public void okGoPostFiles(View view) {

        String url = "http://192.168.0.12:8080/okhttpDemo/postFile";

        List<File> files = new ArrayList<>();
        List<HttpParams.FileWrapper> fileWrappers = new ArrayList<>();
        OkGo.<String>post(url)
                .tag(this)
                .isMultipart(true)//强制使用表单上传
                .params("params", "v1")
                .params("file1", new File("filePath1"))
                .params("file2", new File("filePath2"))
                .addFileParams("key", files)
                .addFileWrapperParams("key", fileWrappers)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        if (response.isSuccessful()) {
                            mTv.setText("from server:key-Files upLoad success!" + response.toString());
                        }
                    }
                });

    }


    /**
     * [8]单文件上传，一个key对一个文件--未测试
     *
     * @param view
     */
    public void okGoPostFile(View view) {

        String url = "http://192.168.0.12:8080/okhttpDemo/postFile";

        List<File> files = new ArrayList<>();
        PostRequest<String> request = OkGo.<String>post(url)
//                .tag(this)
                .tag("okGoPostFile")
                .headers("k1", "v1")
                .headers("k2", "v2")
                .params("params1", "pv1")
                .params("params2", "pv2");

        int size = files.size();
        for (int i = 0; i < size; i++) {
            request.params("file_" + i, files.get(i));
        }

        request.execute(new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
                if (response.isSuccessful()) {
                    mTv.setText("from server:key-File upLoad success!");
                }
            }
        });

    }


    /**
     * [9]上传单一的文件--测试成功，服务器代码不会写
     *
     * 必须增加读写权限
     * @param view
     *
     * <uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE"/>
     * <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"/>
     */
    public void okGoPostOneFiles(View view) {

        String url = "http://192.168.0.12:8080/okhttpDemo/postFile";

        final String fileName = "struts-2.3.34-all.zip";
        String filePath = Environment.getExternalStorageDirectory().getAbsolutePath();
        File file  = new File(filePath,fileName);
        if (!file.exists()) {
            Toast.makeText(this, "file not exist!!!", Toast.LENGTH_SHORT).show();
            return;
        }

        OkGo.<String>post(url)
                .tag(this)
//                .isMultipart(true)//强制使用表单上传
                .params(fileName, file)
//                .params("file1", new File(filePath))
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        if (response.isSuccessful()) {
                            mTv.setText("from server:" + response.toString());
                        }
                    }

                    @Override
                    public void uploadProgress(Progress progress) {
                        super.uploadProgress(progress);
                        mTv.setText(
                                "文件名称：" + fileName + "\n" +
                                        "Progress：" + progress.fraction * 100 + "%" + "\n" +
                                        "totalSize：" + progress.totalSize / 1024 / 1024 + "MB" + "\n" +
                                        "currentSize:" + progress.currentSize / 1024 / 1024 + "MB" + "\n" +
                                        "speed：" + progress.speed / 1024 + "KB/s"
                        );
                        mPb.setProgress((int) (progress.fraction * 100));
                    }
                });
    }


    /**
     * [10] 同步请求-1--测试成功
     * @param
     * @return
     */

    public void okGoSyncGet(View v){

        final String url = "http://192.168.0.12:8080/okhttpDemo/login";

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    okhttp3.Response response = OkGo.<String>get(url)
                            .tag(this)
                            .params("username", "ozTaking")
                            .params("password", "88888888")
                            .execute();
                    String s = response.body().string();
                    Logger.d("from server:"+ s);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * [11]同步请求-2  测试成功
     * @param
     * @return
     */
    public void okGoSyncGet2(View v){

        final String url = "http://192.168.0.12:8080/okhttpDemo/login";

        new Thread(new Runnable() {
            @Override
            public void run() {
                Call<String> call = OkGo.<String>get(url)
                        .tag(this)
                        .params("username", "ozTaking")
                        .params("password", "88888888")
                        .converter(new StringConvert())
                        .adapt();

                try {
                    Response<String> response = call.execute();
                    response.body();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    //参考文章：https://github.com/jeasonlzy/okhttp-OkGo/wiki/%E4%BC%A0%E5%8F%82%E4%B8%8E%E6%8A%93%E5%8C%85%EF%BC%88%E5%BF%85%E7%9C%8B%EF%BC%89

    /**
     * [12] GET、HEAD等无请求体的传参方式
     *
     * @param
     * @return
     */

    public void okGetWithParamsNoRequest(View v){

        final String url = "http://192.168.0.12:8080/okhttpDemo/login";
        final String url1 = "http://192.168.1.104:8080/okGoServer/login";


        OkGo.<String>get(url1)
                .tag(this)
                .params("paramskey1","111")
                .params("paramskey2","222")
                .params("paramskey3","333")
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {

                    }
                });

    }

    //POST, PUT, DELETE 等有请求体的传参

    /**
     * [13-1] 请求参数无文件
     * @param
     * @return
     */

    public void okGoWithParams(View view){

        final String url = "http://192.168.1.104:8080/okGoServer/login";

        OkGo.<String>post(url)
                .tag(this)
                .params("k1","v1")
                .params("k2","v2")
                .params("k3","v3")
                .params("k4","我是中文测试文字")
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                    }
                });
    }



    /**
     * [13-2] 请求参数无文件-请求时候在url上拼接参数
     * @param
     * @return
     */

    public void okGoUrlWithParams(View view){

        final String url = "http://192.168.1.104:8080/okGoServer/login";

        OkGo.<String>post(url)
                .tag(this)
                .isSpliceUrl(true)  //不仅请求体有参数，url上也有参数了，默认情况下，这个参        数是false，只在你需要的情况下拼接就好了。
                .params("k1","v1")
                .params("k2","v2")
                .params("k3","v3")
                .params("k4","我是中文测试文字")
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                    }
                });
    }


    /**
     * [13-3] 请求参数有文件
     * @param
     * @return
     */

    public void okGoWithParamsWithFile(View view){

        final String url = "http://192.168.1.104:8080/okGoServer/postFile";


        String url1 = "http://yc.jb51.net:81/201710/books/AndroidLauncher_jb51.net.rar";

        String url2 = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1530891265967&di=f3b0108bc890fac94c42a00f47b23957&imgtype=0&src=http%3A%2F%2Fpic20.photophoto.cn%2F20110902%2F0034034471873095_b.jpg";

        final String fileName = getFileName(url2);

//        final String fileName = "AndroidLauncher_jb51.net.rar";
        String filePath = Environment.getExternalStorageDirectory().getAbsolutePath();
        File file  = new File(filePath,fileName);
        if (!file.exists()) {
            Toast.makeText(this, "file not exist!!!", Toast.LENGTH_SHORT).show();
            return;
        }

        OkGo.<String>post(url)
                .tag(this)
                .params("k1","v1")
                .params("k2","v2")
                .params("k3","v3")
                .params("k4","我是中文测试文字")
                .params(fileName,file)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                    }
                });
    }


    /**
     * [13-4]请求参数没有文件，依然想用 multipart上传
     * @param
     * @return
     */
    public void okGoWithParamsMultipart(View view){

        final String url = "http://192.168.1.104:8080/okGoServer/login";

        OkGo.<String>post(url)
                .tag(this)
                .isMultipart(true)
                .params("k1","v1")
                .params("k2","v2")
                .params("k3","v3")
                .params("k4","我是中文测试文字")
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                    }
                });
    }


    /**
     * [13-4]一个key传多个值，或者多个文件怎么传
     * @param
     * @return
     */
    public void okGoOneKey2Files(View view){

        final String url = "http://192.168.1.104:8080/okGoServer/login";

        String url2 = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1530891265967&di=f3b0108bc890fac94c42a00f47b23957&imgtype=0&src=http%3A%2F%2Fpic20.photophoto.cn%2F20110902%2F0034034471873095_b.jpg";

        final String fileName = getFileName(url2);

        //        final String fileName = "AndroidLauncher_jb51.net.rar";
        String filePath = Environment.getExternalStorageDirectory().getAbsolutePath();
        File file  = new File(filePath,fileName);
        File file2  = new File(filePath,fileName);
        if (!file.exists()) {
            Toast.makeText(this, "file not exist!!!", Toast.LENGTH_SHORT).show();
            return;
        }

        List<File> files = new ArrayList<>();
        files.add(file);
        files.add(file2);

        List<String> params = new ArrayList<>();
        params.add("111");
        params.add("222");


        OkGo.<String>post(url)
                .tag(this)
                .isMultipart(true)
                .params("aaa","333")
                .addUrlParams("bbb",params)
                .addFileParams("ccc",files)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                    }
                });
    }

    //=====================up系列传参方式介绍===================================
    //


    /**
     * [14-1]
     * @param
     * @return
     */


    public void okGoPostUpString(View view){
        final String url = "http://192.168.1.104:8080/okGoServer/login";
        OkGo.<String>post(url)
                .tag(this)
                .upString("okGoPostUpString 测试代码")
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {

                    }
                });
    }


    /**
     *
     * @param view
     */
    public void okGoPostUpJson(View view) {

        String url = "http://192.168.1.104:8080/okhttpDemo/postString";

        Map<String, String> params = new HashMap<>();
        params.put("k1", "String 1");
        params.put("k2", "String 2");
        params.put("k3", "from android device JsonString");

        //使用fastJson解析
        String s = JSONArray.toJSONString(params);

        OkGo.<String>post(url)
                .tag(this)
                .upJson(s)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        if (response.isSuccessful()) {
                            mTv.setText("服务器返回状态:" + response.toString());
                        }
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        mTv.setText("服务器返回状态：" + response.getException().toString());
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
//        OkGo.cancelAll(OkHttpClient);
//        OkGo.cancelTag(OkHttpClient,"okGoPostFile");
    }
}












