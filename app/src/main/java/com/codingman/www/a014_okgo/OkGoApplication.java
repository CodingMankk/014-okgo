package com.codingman.www.a014_okgo;

import android.app.Application;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheEntity;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.cookie.CookieJarImpl;
import com.lzy.okgo.cookie.store.DBCookieStore;
import com.lzy.okgo.cookie.store.MemoryCookieStore;
import com.lzy.okgo.cookie.store.SPCookieStore;
import com.lzy.okgo.https.HttpsUtils;
import com.lzy.okgo.interceptor.HttpLoggingInterceptor;
import com.lzy.okgo.model.HttpHeaders;
import com.lzy.okgo.model.HttpParams;

import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

import okhttp3.OkHttpClient;

/**
 * @function
 */

public class OkGoApplication extends Application{

    /*
    可以配置log开关
    全局的超时时间
    全局cookie管理策略
    Https配置
    超时重连次数
    公共的请求头和请求参数等信息
    */

    @Override
    public void onCreate() {
        super.onCreate();

        //最简单的配置
        OkGo init = OkGo.getInstance().init(this);

        OkHttpClient.Builder builder = new OkHttpClient.Builder();

        //[1]log拦截器
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor("OkGo");
        loggingInterceptor.setPrintLevel(HttpLoggingInterceptor.Level.BODY);
        loggingInterceptor.setColorLevel(Level.INFO);
        builder.addInterceptor(loggingInterceptor);

        //[2]配置超时时间
        builder.readTimeout(OkGo.DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS);
        builder.writeTimeout(OkGo.DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS);
        builder.connectTimeout(OkGo.DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS);


        //[3]配置cookie
        builder.cookieJar(new CookieJarImpl(new SPCookieStore(this)));
        builder.cookieJar(new CookieJarImpl(new DBCookieStore(this)));
        builder.cookieJar(new CookieJarImpl(new MemoryCookieStore()));

        //[4]信任所有证书？？？
        HttpsUtils.SSLParams sslParams1 = HttpsUtils.getSslSocketFactory();
        builder.sslSocketFactory(sslParams1.sSLSocketFactory,sslParams1.trustManager);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.put("key1","value1");
        httpHeaders.put("key2","value2");
        httpHeaders.put("key3","value3");

        HttpParams params = new HttpParams();
        params.put("key1","value1");
        params.put("key2","value2");
        params.put("key3","value3");

        OkGo.getInstance().init(this)
                .setOkHttpClient(builder.build())
                .setCacheMode(CacheMode.NO_CACHE)
                .setCacheTime(CacheEntity.CACHE_NEVER_EXPIRE)
                .setRetryCount(3)
                .addCommonHeaders(httpHeaders)
                .addCommonParams(params);

    }
}
