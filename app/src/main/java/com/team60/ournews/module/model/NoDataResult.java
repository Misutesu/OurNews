package com.team60.ournews.module.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Misutesu on 2017/3/5 0005.
 */

public class NoDataResult {

    /**
     * result : success
     * errorCode : 0
     * data :
     */

    private String result;
    @SerializedName("error_code")
    private int errorCode;
    private String data;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "NoDataResult{" +
                "result='" + result + '\'' +
                ", errorCode=" + errorCode +
                ", data='" + data + '\'' +
                '}';
    }
}
