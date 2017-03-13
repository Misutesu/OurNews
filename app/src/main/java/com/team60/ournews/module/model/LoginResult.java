package com.team60.ournews.module.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Misutesu on 2017/3/5 0005.
 */

public class LoginResult {

    /**
     * result : success
     * errorCode : 0
     * data : {"id":12,"nickName":"Test","sex":1,"photo":"NoImage","loginName":"Test","token":"5095c4cfad764a6aa5e8262443671ca4","push_state",1}
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
        /**
         * id : 12
         * nick_name : Test
         * sex : 1
         * photo : NoImage
         * login_name : Test
         * token : 5095c4cfad764a6aa5e8262443671ca4
         */

        private int id;
        @SerializedName("nick_name")
        private String nickName;
        private int sex;
        private String photo;
        @SerializedName("login_name")
        private String loginName;
        private String token;

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

        public String getLoginName() {
            return loginName;
        }

        public void setLoginName(String loginName) {
            this.loginName = loginName;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }
    }
}
