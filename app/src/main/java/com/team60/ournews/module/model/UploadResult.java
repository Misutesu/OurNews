package com.team60.ournews.module.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Misutesu on 2017/3/5 0005.
 */

public class UploadResult {

    /**
     * result : success
     * errorCode : 0
     * data : ["aaa.png","bbb.png"]
     */

    private String result;
    @SerializedName("error_code")
    private int errorCode;
    private List<String> data;

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

    public List<String> getData() {
        return data;
    }

    public void setData(List<String> data) {
        this.data = data;
    }
}
