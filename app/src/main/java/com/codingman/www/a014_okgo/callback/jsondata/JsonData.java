package com.codingman.www.a014_okgo.callback.jsondata;

import android.content.Context;
import android.content.res.AssetManager;
import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.orhanobut.logger.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @function: 常见的四种类型的json类型的数据
 */

public class JsonData {


    Context context;

    /*
      【数据类型A】-最外层数据类型是JsonObject，data数据也是JsonObject
    {
        "code":0,
         "msg":"请求成功",
         "data":{
                "id":123456,
                "name":"张三",
                "age":18
                }
    }*/


    public class JsonObjectA {
        public int code;
        public String msg;
        public Data data;

        public class Data{
            public long id;
            public String name;
            public int age;
        }

    }



    /*
       【数据类型B】-最外层数据类型是 JsonObject ，data 数据是 JsonArray
    {
      "code": 0,
      "msg": "请求成功",
      "data": [
        {
          "id": 123456,
          "name": "张三",
          "age": 18
        },
        {
          "id": 123456,
          "name": "张三",
          "age": 18
        },
        {
          "id": 123456,
          "name": "张三",
          "age": 18
        }
      ]
    }*/

    public class JsonObjectB{
        public long code;
        public String msg;
        public List<Data> data;

        public class Data {
            public  long id;
            public String name;
            public int age;
        }

    }



    /* 【数据类型C】-没有固定的 msg、code 字段格式包装，服务器任意返回对象
      "data":{
      "id": 123456,
      "name": "张三",
      "age": 18
    }*/


    /* 【数据类型D】-最外层数据类型是 JsonArray ，内部数据是 JsonObject
    [
      {
        "id":123456,
        "name":"张三",
        "age":18
      },
      {
        "id":123456,
        "name":"张三",
        "age":18
      },
      {
        "id":123456,
        "name":"张三",
        "age":18
      }
    ]*/

    public class JsonObjectCorD {
        public  long id;
        public String name;
        public int age;
    }


    /**
     * [1]数据类型A，B，C，都是最正常的解析方式
     */
    public  void JsonParseJsonObjectA(){
        //        //返回的json数据
        //        InputStream inputStream = context.getClass().getClassLoader().getResourceAsStream
        //                ("assets/JsonA.json");

        String json = getJson("JsonA.json", context);
        Gson gson = new Gson();
        JsonData.JsonObjectA jsonObjectA = gson.fromJson(json, JsonData.JsonObjectA.class);

    }


    /**
     * [2]数据类型D
     */
    public  void JsonParseJsonObjectD(){
        String json = getJson("JsonD.json", context);
        Gson gson = new Gson();
        //        Type type = new TypeToken<List<JsonObjectD>>() {}.getType();
        TypeToken<List<JsonObjectD>> typeToken = new TypeToken<List<JsonObjectD>>() {
        };
        Type type = typeToken.getType();
        List<JsonObjectD> jsonObjectD = gson.fromJson(json, type);

    }

    /**
     * [3] 直接把流解析成对象
     */

    public void JsonParseStream2Class(){

        String url ="";

        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder()
                .get()
                .url(url)
                .build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()){
                    Reader reader = response.body().charStream();
                    Gson gson = new Gson();
                    JsonReader jsonReader = new JsonReader(reader);

                    TypeToken<List<JsonObjectD>> typeToken = new TypeToken<List<JsonObjectD>>() {
                    };
                    Type type = typeToken.getType();

                    List<JsonObjectD> jsonObjectDList = gson.fromJson(reader, type);


                }
            }
        });


    }






    @NonNull
    private static String getJson(String name, Context context) {
        String line;

        StringBuilder builder = new StringBuilder();

        try {
            //获取assets资源管理器
            AssetManager am = context.getAssets();
            //通过管理器打开文件并读取
            InputStream in = am.open(name);
            InputStreamReader ir = new InputStreamReader(in);
            BufferedReader br = new BufferedReader(ir);
            while ((line = br.readLine()) != null) {
                builder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        Logger.json(builder.toString());
        return builder.toString();
    }

}
