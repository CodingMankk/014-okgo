package com.codingman.www.a014_okgo.callback;


import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.lzy.okgo.callback.AbsCallback;

import java.lang.reflect.Type;

import okhttp3.ResponseBody;

/**
 * @function: 通用的JsonCallBack,没有经过优化
 */

public abstract class JsonCallBackSimple<T> extends AbsCallback<T>{
    private Type type;
    private Class<T> clazz;

    public JsonCallBackSimple(Type type) {
        this.type = type;
    }

    public JsonCallBackSimple(Class<T> clazz) {
        this.clazz = clazz;
    }


    @Override
    public T convertResponse(okhttp3.Response response) throws Throwable {

        ResponseBody body = response.body();
        if (body == null){
            return null;
        }

        T data = null;
        Gson gson = new Gson();
        JsonReader jsonReader = new JsonReader(body.charStream());

        if (type!= null){
            data = gson.fromJson(jsonReader,type);
        }

        if (clazz != null){
            data = gson.fromJson(jsonReader,clazz);
        }


        data = gson.fromJson(jsonReader, type);
        return data;
    }


}
