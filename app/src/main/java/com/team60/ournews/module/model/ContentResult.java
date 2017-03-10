package com.team60.ournews.module.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Misutesu on 2017/3/6 0006.
 */

public class ContentResult {

    /**
     * result : success
     * errorCode : 0
     * data : {"id":10,"content":"aaa","is_collected":1,"commentNum":10,"historyNum":20,"collectionNum":30}
     */

    private String result;
    @SerializedName("error_code")
    private int errorCode;
    private DataBean data;

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

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * id : 10
         * content : aaa
         * commentNum : 10
         * historyNum : 20
         * collectionNum : 30
         */

        private long id;
        private String content;
        @SerializedName("is_collected")
        private int isCollected = -1;
        @SerializedName("comment_num")
        private int commentNum;
        @SerializedName("history_num")
        private int historyNum;
        @SerializedName("collection_num")
        private int collectionNum;

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public int getIsCollected() {
            return isCollected;
        }

        public void setIsCollected(int isCollected) {
            this.isCollected = isCollected;
        }

        public int getCommentNum() {
            return commentNum;
        }

        public void setCommentNum(int commentNum) {
            this.commentNum = commentNum;
        }

        public int getHistoryNum() {
            return historyNum;
        }

        public void setHistoryNum(int historyNum) {
            this.historyNum = historyNum;
        }

        public int getCollectionNum() {
            return collectionNum;
        }

        public void setCollectionNum(int collectionNum) {
            this.collectionNum = collectionNum;
        }
    }
}
