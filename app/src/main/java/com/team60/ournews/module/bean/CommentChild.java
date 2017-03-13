package com.team60.ournews.module.bean;

/**
 * Created by wujiaquan on 2017/3/13.
 */

public class CommentChild {
    private long id;
    private long cid;
    private String content;
    private String createTime;
    private OtherUser user;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getCid() {
        return cid;
    }

    public void setCid(long cid) {
        this.cid = cid;
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

    public OtherUser getUser() {
        return user;
    }

    public void setUser(OtherUser user) {
        this.user = user;
    }
}
