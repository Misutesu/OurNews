package com.team60.ournews.module.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Misutesu on 2017/3/6 0006.
 */

public class CommentResult {

    /**
     * result : success
     * errorCode : 0
     * data : [{"id":10,"content":"aaa","createTime":"2017","user":{"id":10,"nickName":"ddd","sex":1,"photo":"eee.png"}},{"id":10,"content":"aaa","createTime":"2017","user":{"id":10,"nickName":"ddd","sex":1,"photo":"eee.png"}}]
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
         * content : aaa
         * createTime : 2017
         * user : {"id":10,"nickName":"ddd","sex":1,"photo":"eee.png"}
         */

        private long id;
        private String content;
        @SerializedName("create_time")
        private String createTime;
        private UserBean user;

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

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public UserBean getUser() {
            return user;
        }

        public void setUser(UserBean user) {
            this.user = user;
        }

        public static class UserBean {
            /**
             * id : 10
             * nickName : ddd
             * sex : 1
             * photo : eee.png
             */

            private long id;
            @SerializedName("create_time")
            private String nickName;
            private int sex;
            private String photo;

            public long getId() {
                return id;
            }

            public void setId(long id) {
                this.id = id;
            }

            public String getNickName() {
                return nickName;
            }

            public void setNickName(String nickName) {
                this.nickName = nickName;
            }

            public int getSex() {
                return sex;
            }

            public void setSex(int sex) {
                this.sex = sex;
            }

            public String getPhoto() {
                return photo;
            }

            public void setPhoto(String photo) {
                this.photo = photo;
            }
        }
    }
}
