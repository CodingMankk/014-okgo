package com.codingman.www.a014_okgo.callback;


import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.lzy.okgo.callback.AbsCallback;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import okhttp3.ResponseBody;

/**
 * @function: 通用的JsonCallBack, 没有经过优化
 */

public abstract class JsonCallBackOptimiseFinal<T> extends AbsCallback<T> {

    public JsonCallBackOptimiseFinal() {
    }

    private Type type;
    private Class<T> clazz;

    public JsonCallBackOptimiseFinal(Type type) {
        this.type = type;
    }

    public JsonCallBackOptimiseFinal(Class<T> clazz) {
        this.clazz = clazz;
    }

    @Override
    public T convertResponse(okhttp3.Response response) throws Throwable {

        ResponseBody body = response.body();
        if (body == null) {
            return null;
        }

        T data = null;
        Gson gson = new Gson();
        JsonReader jsonReader = new JsonReader(body.charStream());

        if (type != null) {
            data = gson.fromJson(jsonReader, type);
        } else if (clazz != null) {
            data = gson.fromJson(jsonReader, clazz);
        } else {
            // 优化代码：
            Type genType = getClass().getGenericSuperclass();
            Type type = ((ParameterizedType) genType).getActualTypeArguments()[0];
            data = gson.fromJson(jsonReader, type);
        }

        return data;
    }

}
