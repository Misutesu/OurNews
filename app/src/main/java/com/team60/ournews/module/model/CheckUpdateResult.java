package com.team60.ournews.module.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Misutesu on 2017/4/20 0020.
 */

public class CheckUpdateResult {

    /**
     * result : success
     * errorCode : 0
     * data : {"nowVersion":1,"nowVersionName":"V1.0.2","minVersion":1,"updateTime":1492685849000,"fileName":"OurNews.apk","fileSize":9000000,"description":"第一个版本"}
     */

    private String result;
    @SerializedName("error_code")
    private String errorCode;
    private DataBean data;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * nowVersion : 1
         * nowVersionName : "V1.0.2"
         * minVersion : 1
         * updateTime : "2017年4月9日"
         * fileName : OurNews.apk
         * fileSize : 9000000
         * description : 第一个版本
         */

        @SerializedName("now_version")
        private int nowVersion;
        @SerializedName("now_version_name")
        private String nowVersionName;
        @SerializedName("min_version")
        private int minVersion;
        @SerializedName("update_time")
        private String updateTime;
        @SerializedName("file_name")
        private String fileName;
        @SerializedName("file_size")
        private long fileSize;
        private String description;

        public int getNowVersion() {
            return nowVersion;
        }

        public void setNowVersion(int nowVersion) {
            this.nowVersion = nowVersion;
        }

        public String getNowVersionName() {
            return nowVersionName;
        }

        public void setNowVersionName(String nowVersionName) {
            this.nowVersionName = nowVersionName;
        }

        public int getMinVersion() {
            return minVersion;
        }

        public void setMinVersion(int minVersion) {
            this.minVersion = minVersion;
        }

        public String getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(String updateTime) {
            this.updateTime = updateTime;
        }

        public String getFileName() {
            return fileName;
        }

        public void setFileName(String fileName) {
            this.fileName = fileName;
        }

        public long getFileSize() {
            return fileSize;
        }

        public void setFileSize(long fileSize) {
            this.fileSize = fileSize;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }
    }
}
