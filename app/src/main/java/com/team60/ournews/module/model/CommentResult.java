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
     * data : {"comments":[{"id":30,"content":"hdhxj","createTime":"32295天前","user":{"id":1,"nickName":"Misutesu","sex":1,"photo":"dc75cccf96cc430db1a1740007d6b2da.jpeg"}},{"id":29,"content":"公益事业","createTime":"41690天前","user":{"id":3,"nickName":"18684040127","sex":0,"photo":"NoImage"}},{"id":2,"content":"弟弟你是","createTime":"2017-03-06 00:13:18","user":{"id":1,"nickName":"Misutesu","sex":1,"photo":"dc75cccf96cc430db1a1740007d6b2da.jpeg"}}]}
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
        private List<CommentsBean> comments;

        public List<CommentsBean> getComments() {
            return comments;
        }

        public void setComments(List<CommentsBean> comments) {
            this.comments = comments;
        }

        public static class CommentsBean {
            /**
             * id : 30
             * content : hdhxj
             * createTime : 32295天前
             * user : {"id":1,"nickName":"Misutesu","sex":1,"photo":"dc75cccf96cc430db1a1740007d6b2da.jpeg"}
             */

            private int id;
            private String content;
            @SerializedName("create_time")
            private String createTime;
            private UserBean user;

            public int getId() {
                return id;
            }

            public void setId(int id) {
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
                 * id : 1
                 * nickName : Misutesu
                 * sex : 1
                 * photo : dc75cccf96cc430db1a1740007d6b2da.jpeg
                 */

                private int id;
                @SerializedName("nick_name")
                private String nickName;
                private int sex;
                private String photo;

                public int getId() {
                    return id;
                }

                public void setId(int id) {
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
}
