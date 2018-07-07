package com.codingman.www.a014_okgo.callback.jsondata;

import java.util.List;

/**
 * @function:
 */

public class JsonObjectB {

    public long code;
    public String msg;
    public List<JsonData.JsonObjectB.Data> data;

    public class Data {
        public  long id;
        public String name;
        public int age;
    }
}
