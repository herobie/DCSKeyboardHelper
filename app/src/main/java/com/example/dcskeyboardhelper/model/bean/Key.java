package com.example.dcskeyboardhelper.model.bean;

//按键名及其对应的KeyEvent编码
public class Key {
    private String name;
    private int code;

    public Key(String name, int code) {
        this.name = name;
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
