package com.team60.ournews.module.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Misutesu on 2017/3/6 0006.
 */

public class ListNewResult {

    /**
     * result : success
     * errorCode : 0
     * data : [{"id":10,"title":"aaa","cover":"bbb.png","abstact":"ccc","createTime":111111111111,"type":1},{"id":10,"title":"aaa","cover":"bbb.png","abstact":"ccc","createTime":111111111111,"type":1}]
     */

    private String result;
    @SerializedName("error_code")
    private int errorCode;
    private List<DataBean> data;

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

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * id : 10
         * title : aaa
         * cover : bbb.png
         * abstact : ccc
         * createTime : 111111111111
         * type : 1
         */

        private long id;
        private String title;
        private String cover;
        @SerializedName("abstract")
        private String abstractContent;
        @SerializedName("create_time")
        private String createTime;
        private int type;

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getCover() {
            return cover;
        }

        public void setCover(String cover) {
            this.cover = cover;
        }

        public String getAbstact() {
            return abstractContent;
        }

        public void setAbstractContent(String abstractContent) {
            this.abstractContent = abstractContent;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }
    }
}
