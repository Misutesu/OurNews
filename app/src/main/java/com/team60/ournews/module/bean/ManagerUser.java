package com.team60.ournews.module.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by wujiaquan on 2017/3/22.
 */

public class ManagerUser implements Parcelable{
    private long id;
    private String nickName;
    private int sex;
    private String sign;
    private int birthday;
    private String photo;

    public ManagerUser() {
    }

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

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public int getBirthday() {
        return birthday;
    }

    public void setBirthday(int birthday) {
        this.birthday = birthday;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    protected ManagerUser(Parcel in) {
        id = in.readLong();
        nickName = in.readString();
        sex = in.readInt();
        sign = in.readString();
        birthday = in.readInt();
        photo = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(nickName);
        dest.writeInt(sex);
        dest.writeString(sign);
        dest.writeInt(birthday);
        dest.writeString(photo);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ManagerUser> CREATOR = new Creator<ManagerUser>() {
        @Override
        public ManagerUser createFromParcel(Parcel in) {
            return new ManagerUser(in);
        }

        @Override
        public ManagerUser[] newArray(int size) {
            return new ManagerUser[size];
        }
    };
}
