package com.team60.ournews.module.bean;

/**
 * Created by Misutesu on 2017/3/5 0005.
 */

public class Error {
    private int code;
    private String message;

    public Error(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
