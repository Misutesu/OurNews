package com.team60.ournews.module.bean;

import android.content.SharedPreferences;
import android.text.TextUtils;

import com.team60.ournews.common.Constants;
import com.team60.ournews.util.MyUtil;

/**
 * Created by Misutesu on 2016/12/26 0026.
 */

public class User {

    private long id;
    private String loginName;
    private String nickName;
    private int sex = -1;
    private String photo;
    private String token;
    private int pushState = -1;

    private static User user = new User();
    private static SharedPreferences sharedPreferences;

    public static User newInstance() {
        if (sharedPreferences == null)
            synchronized (User.class) {
                sharedPreferences = MyUtil.getSharedPreferences(Constants.SHARED_PREFERENCES_USER);
            }
        return user;
    }

    public static void breakLogin() {
        sharedPreferences.edit().clear().apply();
        user.setId(0);
        user.setLoginName(null);
        user.setNickName(null);
        user.setSex(0);
        user.setPhoto(null);
        user.setToken(null);
    }

    public static boolean isLogin() {
        if (user.getId() != 0 && !TextUtils.isEmpty(user.getLoginName()) && !TextUtils.isEmpty(user.getNickName())
                && !TextUtils.isEmpty(user.getPhoto()) && !TextUtils.isEmpty(user.getToken()))
            return true;
        return false;
    }

    public long getId() {
        if (id == 0)
            id = sharedPreferences.getLong("id", 0);
        return id;
    }

    public void setId(long id) {
        if (sharedPreferences.edit().putLong("id", id).commit())
            this.id = id;
    }

    public String getLoginName() {
        if (TextUtils.isEmpty(loginName))
            loginName = sharedPreferences.getString("loginName", "");
        return loginName;
    }

    public void setLoginName(String loginName) {
        if (sharedPreferences.edit().putString("loginName", loginName).commit())
            this.loginName = loginName;
    }

    public String getNickName() {
        if (TextUtils.isEmpty(nickName))
            nickName = sharedPreferences.getString("nickName", "");
        return nickName;
    }

    public void setNickName(String nickName) {
        if (sharedPreferences.edit().putString("nickName", nickName).commit())
            this.nickName = nickName;
    }

    public int getSex() {
        if (sex == -1)
            sex = sharedPreferences.getInt("sex", -1);
        return sex;
    }

    public void setSex(int sex) {
        if (sharedPreferences.edit().putInt("sex", sex).commit())
            this.sex = sex;
    }

    public String getPhoto() {
        if (TextUtils.isEmpty(photo))
            photo = sharedPreferences.getString("photo", "");
        return photo;
    }

    public void setPhoto(String photo) {
        if (sharedPreferences.edit().putString("photo", photo).commit())
            this.photo = photo;
    }

    public String getToken() {
        if (TextUtils.isEmpty(token))
            token = sharedPreferences.getString("token", "");
        return token;
    }

    public void setToken(String token) {
        if (sharedPreferences.edit().putString("token", token).commit())
            this.token = token;
    }

    public int getPushState() {
        if (pushState == -1)
            pushState = sharedPreferences.getInt("pushState", -1);
        return pushState;
    }

    public void setPushState(int pushState) {
        if (sharedPreferences.edit().putInt("pushState", pushState).commit())
            this.pushState = pushState;
    }
}
