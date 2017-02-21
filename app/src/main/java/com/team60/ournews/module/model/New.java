package com.team60.ournews.module.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.SparseArray;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

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
    private int commentNum;

    public static List<New> getNewList(JSONObject jsonObject) throws JSONException {
        List<New> news = new ArrayList<>();
        JSONArray jsonArray = jsonObject.getJSONArray("news");
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonNew = jsonArray.getJSONObject(i);
            New n = new New();
            n.setId(jsonNew.getLong("id"));
            n.setTitle(jsonNew.getString("title"));
            n.setCover(jsonNew.getString("cover"));
            n.setAbstractContent(jsonNew.getString("abstract"));
            n.setCreateTime(jsonNew.getString("createtime"));
            n.setType(jsonNew.getInt("type"));
            news.add(n);
        }
        return news;
    }

    public static SparseArray<List<New>> getHomeNews(String json) throws JSONException {
        SparseArray<List<New>> news = null;
        JSONObject jsonObject = new JSONObject(json);
        if (jsonObject.getString("result").equals("success")) {
            news = new SparseArray<>();
            for (int type = 1; type < 7; type++) {
                JSONArray jsonArray = jsonObject.getJSONArray(String.valueOf(type));
                List<New> list = new ArrayList<>();
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject j = jsonArray.getJSONObject(i);
                    New n = new New();
                    n.setId(j.getLong("id"));
                    n.setTitle(j.getString("title"));
                    n.setCover(j.getString("cover"));
                    n.setCreateTime(j.getString("createtime"));
                    n.setType(j.getInt("type"));
                    list.add(n);
                }
                news.append(type, list);
            }
        }
        return news;
    }

    public static SparseArray<List<New>> getHomeNewsUseType(String json, int type) throws JSONException {
        SparseArray<List<New>> news = null;
        JSONObject jsonObject = new JSONObject(json);
        if (jsonObject.getString("result").equals("success")) {
            news = new SparseArray<>();
            JSONArray jsonArray = jsonObject.getJSONArray(String.valueOf(type));
            List<New> list = new ArrayList<>();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject j = jsonArray.getJSONObject(i);
                New n = new New();
                n.setId(j.getLong("id"));
                n.setTitle(j.getString("title"));
                n.setCover(j.getString("cover"));
                n.setCreateTime(j.getString("createtime"));
                n.setType(j.getInt("type"));
                list.add(n);
            }
            news.append(type, list);
        }
        return news;
    }

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

    public int getCommentNum() {
        return commentNum;
    }

    public void setCommentNum(int commentNum) {
        this.commentNum = commentNum;
    }

    protected New(Parcel in) {
        id = in.readLong();
        title = in.readString();
        cover = in.readString();
        abstractContent = in.readString();
        content = in.readString();
        createTime = in.readString();
        type = in.readInt();
        commentNum = in.readInt();
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
        dest.writeInt(commentNum);
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
