package com.team60.ournews.module.model;

import android.content.SharedPreferences;
import android.text.TextUtils;

import com.team60.ournews.common.Constants;
import com.team60.ournews.util.MyUtil;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Misutesu on 2016/12/26 0026.
 */

public class User {

    private long id;
    private String loginName;
    private String nickName;
    private int sex;
    private String photo;

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
        user = new User();
    }

    public static boolean isLogin() {
        if (user.getId() != 0 && !TextUtils.isEmpty(user.getLoginName()) && !TextUtils.isEmpty(user.getNickName())
                && !TextUtils.isEmpty(user.getPhoto()))
            return true;
        return false;
    }

    public static int getUserInfo(String str) throws JSONException {
        JSONObject jsonObject = new JSONObject(str);
        if (jsonObject.getString("result").equals("success")) {
            JSONObject userJSON = jsonObject.getJSONObject("user");
            user.setId(userJSON.getLong("id"));
            user.setLoginName(userJSON.getString("loginname"));
            user.setNickName(userJSON.getString("nickname"));
            user.setSex(userJSON.getInt("sex"));
            user.setPhoto(userJSON.getString("photo"));
            return 0;
        } else {
            if (jsonObject.getInt("error_code") == Constants.LOGINNAME_NO_EXISTS_OR_PASSWORD_ERROR) {
                return 1;
            } else {
                return 2;
            }
        }
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
        if (sex == 0)
            sex = sharedPreferences.getInt("sex", 0);
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
}
