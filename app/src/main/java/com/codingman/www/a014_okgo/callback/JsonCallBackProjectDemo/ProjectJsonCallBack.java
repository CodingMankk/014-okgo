package com.codingman.www.a014_okgo.callback.JsonCallBackProjectDemo;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.lzy.okgo.callback.AbsCallback;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * @function:
 * 对应于项目的中的通用json解析+业务json解析模型，具体需要项目需求
 *
 *
 */

public abstract class ProjectJsonCallBack<T> extends AbsCallback<T>{


    @Override
    public T convertResponse(Response response) throws Throwable {

        Type superclass = getClass().getGenericSuperclass();
        Type[] arguments = ((ParameterizedType) superclass).getActualTypeArguments();
        //得到第二层泛型的所有类型
        Type type = arguments[0];



        if (!(type instanceof ParameterizedType)){
            throw  new IllegalStateException("没有填写正确的泛型参数");
        }
        ParameterizedType type1 = (ParameterizedType) type;
        Type rawType = type1.getRawType();
        Type[] actualTypeArguments = type1.getActualTypeArguments();
        Type actualTypeArgument = actualTypeArguments[0];

        ResponseBody body = response.body();
        if (body == null){
            return null;
        }

        Gson gson = new Gson();
        JsonReader jsonReader = new JsonReader(body.charStream());
        if (rawType != ProjectResponseBean.class){
            T data = gson.fromJson(jsonReader,type);
            response.close();
            return data;
        }else {
            //待查看
            if (actualTypeArgument == Void.class){
                    gson.fromJson(jsonReader,SimpleResponse.class);
                response.close();
//                return (T) SimpleResponse.toProjectResponse();
            }else{
                ProjectResponseBean projectResponse= gson.fromJson(jsonReader,type);
                response.close();
                int code = projectResponse.code;
                if (0 == code){
                    return (T)projectResponse;
                }else if (104 == code){
                    throw new IllegalStateException("用户信息授权无效");
                }
            }

        }
        


        return null;
    }
}
