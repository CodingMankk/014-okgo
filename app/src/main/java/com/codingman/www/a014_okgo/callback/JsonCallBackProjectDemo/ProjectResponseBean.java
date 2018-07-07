package com.codingman.www.a014_okgo.callback.JsonCallBackProjectDemo;

/**
 * @function: 全项目通用的Json JavaBean
 */

public class ProjectResponseBean<T> {

    public int code;
    public String msg;
    public T data; //单纯的业务模块需要的数据

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
