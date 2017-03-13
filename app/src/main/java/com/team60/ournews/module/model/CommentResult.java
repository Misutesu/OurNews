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
     * data : {"comments":[{"id":9,"content":"饿哦给一下","createTime":"2017-03-06 00:13:18","user":{"id":1,"nickName":"Misutesu","sex":1,"photo":"dc75cccf96cc430db1a1740007d6b2da.jpeg"},"isLike":1,"likeNum":1,"commentChildrenList":[{"id":1,"content":"123","createTime":"2017-03-06 00:13:18","user":{"id":1,"nickName":"Misutesu","sex":1,"photo":"dc75cccf96cc430db1a1740007d6b2da.jpeg"}},{"id":3,"content":"123","createTime":"2017-03-06 00:13:18","user":{"id":1,"nickName":"Misutesu","sex":1,"photo":"dc75cccf96cc430db1a1740007d6b2da.jpeg"}},{"id":4,"content":"123","createTime":"2017-03-06 00:13:18","user":{"id":1,"nickName":"Misutesu","sex":1,"photo":"dc75cccf96cc430db1a1740007d6b2da.jpeg"}}],"childNum":13},{"id":8,"content":"地宫一个月","createTime":"2017-03-06 00:13:18","user":{"id":1,"nickName":"Misutesu","sex":1,"photo":"dc75cccf96cc430db1a1740007d6b2da.jpeg"},"isLike":0,"likeNum":0,"commentChildrenList":[],"childNum":0},{"id":7,"content":"顶你后","createTime":"2017-03-06 00:13:18","user":{"id":1,"nickName":"Misutesu","sex":1,"photo":"dc75cccf96cc430db1a1740007d6b2da.jpeg"},"isLike":0,"likeNum":0,"commentChildrenList":[],"childNum":0},{"id":6,"content":"您给我说","createTime":"2017-03-06 00:13:18","user":{"id":1,"nickName":"Misutesu","sex":1,"photo":"dc75cccf96cc430db1a1740007d6b2da.jpeg"},"isLike":0,"likeNum":0,"commentChildrenList":[],"childNum":0},{"id":5,"content":"蛤蟆","createTime":"2017-03-06 00:13:18","user":{"id":1,"nickName":"Misutesu","sex":1,"photo":"dc75cccf96cc430db1a1740007d6b2da.jpeg"},"isLike":0,"likeNum":0,"commentChildrenList":[],"childNum":0},{"id":4,"content":"爹婆婆哄孩子","createTime":"2017-03-06 00:13:18","user":{"id":1,"nickName":"Misutesu","sex":1,"photo":"dc75cccf96cc430db1a1740007d6b2da.jpeg"},"isLike":0,"likeNum":0,"commentChildrenList":[],"childNum":0}]}
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
             * id : 9
             * content : 饿哦给一下
             * createTime : 2017-03-06 00:13:18
             * user : {"id":1,"nickName":"Misutesu","sex":1,"photo":"dc75cccf96cc430db1a1740007d6b2da.jpeg"}
             * isLike : 1
             * likeNum : 1
             * commentChildrenList : [{"id":1,"content":"123","createTime":"2017-03-06 00:13:18","user":{"id":1,"nickName":"Misutesu","sex":1,"photo":"dc75cccf96cc430db1a1740007d6b2da.jpeg"}},{"id":3,"content":"123","createTime":"2017-03-06 00:13:18","user":{"id":1,"nickName":"Misutesu","sex":1,"photo":"dc75cccf96cc430db1a1740007d6b2da.jpeg"}},{"id":4,"content":"123","createTime":"2017-03-06 00:13:18","user":{"id":1,"nickName":"Misutesu","sex":1,"photo":"dc75cccf96cc430db1a1740007d6b2da.jpeg"}}]
             * childNum : 13
             */

            private int id;
            private String content;
            @SerializedName("create_time")
            private String createTime;
            private UserBean user;
            @SerializedName("is_like")
            private int isLike;
            @SerializedName("like_num")
            private int likeNum;
            @SerializedName("child_num")
            private int childNum;
            @SerializedName("comment_children")
            private List<CommentChildrenBean> commentChildrenList;

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

            public int getIsLike() {
                return isLike;
            }

            public void setIsLike(int isLike) {
                this.isLike = isLike;
            }

            public int getLikeNum() {
                return likeNum;
            }

            public void setLikeNum(int likeNum) {
                this.likeNum = likeNum;
            }

            public int getChildNum() {
                return childNum;
            }

            public void setChildNum(int childNum) {
                this.childNum = childNum;
            }

            public List<CommentChildrenBean> getCommentChildrenList() {
                return commentChildrenList;
            }

            public void setCommentChildrenList(List<CommentChildrenBean> commentChildrenList) {
                this.commentChildrenList = commentChildrenList;
            }

            public static class CommentChildrenBean {
                /**
                 * id : 1
                 * content : 123
                 * createTime : 2017-03-06 00:13:18
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
