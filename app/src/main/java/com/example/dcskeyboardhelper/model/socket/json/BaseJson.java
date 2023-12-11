package com.example.dcskeyboardhelper.model.socket.json;

import com.google.gson.annotations.Expose;

public class BaseJson<T> {
    @Expose
    private int errorCode;
    @Expose
    private String errorMsg;
    @Expose
    private int type;//消息类型代码，0代表保活消息，1代表操作消息
    @Expose
    private T data;

    public BaseJson(int type, T data) {
        this.type = type;
        this.data = data;
        errorMsg = "";
    }

    public BaseJson(int type) {
        this.type = type;
        errorMsg = "";
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
