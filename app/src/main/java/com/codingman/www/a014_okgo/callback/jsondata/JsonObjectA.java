package com.codingman.www.a014_okgo.callback.jsondata;

/**
 * @function:
 */

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
    public JsonData.JsonObjectA.Data data;

    public class Data{
        public long id;
        public String name;
        public int age;
    }
}
