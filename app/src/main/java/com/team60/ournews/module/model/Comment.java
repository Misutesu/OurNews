package com.team60.ournews.module.model;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Misutesu on 2016/12/29 0029.
 */

public class Comment implements Parcelable{
    private long id;
    private long nid;
    private String content;
    private String createTime;
    private OtherUser user;

    public Comment() {
    }

    public static List<Comment> getComments(long nid, JSONObject jsonObject) throws JSONException {
        List<Comment> comments = new ArrayList<>();
        JSONArray jsonArray = jsonObject.getJSONArray("comments");
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jComment = jsonArray.getJSONObject(i);
            Comment comment = new Comment();
            comment.setId(jComment.getLong("id"));
            comment.setNid(nid);
            comment.setContent(jComment.getString("content"));
            comment.setCreateTime(jComment.getString("createtime"));
            JSONObject jUser = jComment.getJSONObject("user");
            OtherUser user = new OtherUser();
            user.setId(jUser.getLong("id"));
            user.setNickName(jUser.getString("nickname"));
            user.setPhoto(jUser.getString("photo"));
            user.setSex(jUser.getInt("sex"));
            comment.setUser(user);
            comments.add(comment);
        }
        return comments;
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

    public OtherUser getUser() {
        return user;
    }

    public void setUser(OtherUser user) {
        this.user = user;
    }

    protected Comment(Parcel in) {
        id = in.readLong();
        nid = in.readLong();
        content = in.readString();
        createTime = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeLong(nid);
        dest.writeString(content);
        dest.writeString(createTime);
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
