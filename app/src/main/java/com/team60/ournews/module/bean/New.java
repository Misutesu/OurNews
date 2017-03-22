package com.team60.ournews.module.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Misutesu on 2016/12/28 0028.
 */

public class New implements Parcelable {
    private long id;
    private String title;
    private String cover;
    private String abstractContent;
    private String content;
    private String createTime;
    private int type;
    private int isCollection;
    private int commentNum;
    private int historyNum;
    private int collectionNum;
    private ManagerUser managerUser;

    public New() {
    }

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

    public String getAbstractContent() {
        return abstractContent;
    }

    public void setAbstractContent(String abstractContent) {
        this.abstractContent = abstractContent;
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

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getIsCollection() {
        return isCollection;
    }

    public void setIsCollection(int isCollection) {
        this.isCollection = isCollection;
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

    public ManagerUser getManagerUser() {
        return managerUser;
    }

    public void setManagerUser(ManagerUser managerUser) {
        this.managerUser = managerUser;
    }

    protected New(Parcel in) {
        id = in.readLong();
        title = in.readString();
        cover = in.readString();
        abstractContent = in.readString();
        content = in.readString();
        createTime = in.readString();
        type = in.readInt();
        isCollection = in.readInt();
        commentNum = in.readInt();
        historyNum = in.readInt();
        collectionNum = in.readInt();
        managerUser = in.readParcelable(ManagerUser.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(title);
        dest.writeString(cover);
        dest.writeString(abstractContent);
        dest.writeString(content);
        dest.writeString(createTime);
        dest.writeInt(type);
        dest.writeInt(isCollection);
        dest.writeInt(commentNum);
        dest.writeInt(historyNum);
        dest.writeInt(collectionNum);
        dest.writeParcelable(managerUser, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<New> CREATOR = new Creator<New>() {
        @Override
        public New createFromParcel(Parcel in) {
            return new New(in);
        }

        @Override
        public New[] newArray(int size) {
            return new New[size];
        }
    };
}
