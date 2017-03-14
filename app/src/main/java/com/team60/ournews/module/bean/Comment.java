package com.team60.ournews.module.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by Misutesu on 2016/12/29 0029.
 */

public class Comment implements Parcelable {

    private long id;
    private long nid;
    private String content;
    private String createTime;
    private int isLike;
    private int lickNum;
    private int childNum;
    private OtherUser user;
    private List<CommentChild> childList;

    public Comment() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getNid() {
        return nid;
    }

    public void setNid(long nid) {
        this.nid = nid;
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

    public int getIsLike() {
        return isLike;
    }

    public void setIsLike(int isLike) {
        this.isLike = isLike;
    }

    public int getLickNum() {
        return lickNum;
    }

    public void setLickNum(int lickNum) {
        this.lickNum = lickNum;
    }

    public int getChildNum() {
        return childNum;
    }

    public void setChildNum(int childNum) {
        this.childNum = childNum;
    }

    public OtherUser getUser() {
        return user;
    }

    public void setUser(OtherUser user) {
        this.user = user;
    }

    public List<CommentChild> getChildList() {
        return childList;
    }

    public void setChildList(List<CommentChild> childList) {
        this.childList = childList;
    }

    protected Comment(Parcel in) {
        id = in.readLong();
        nid = in.readLong();
        content = in.readString();
        createTime = in.readString();
        isLike = in.readInt();
        lickNum = in.readInt();
        childNum = in.readInt();
        user = in.readParcelable(OtherUser.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeLong(nid);
        dest.writeString(content);
        dest.writeString(createTime);
        dest.writeInt(isLike);
        dest.writeInt(lickNum);
        dest.writeInt(childNum);
        dest.writeParcelable(user, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Comment> CREATOR = new Creator<Comment>() {
        @Override
        public Comment createFromParcel(Parcel in) {
            return new Comment(in);
        }

        @Override
        public Comment[] newArray(int size) {
            return new Comment[size];
        }
    };
}
