package com.team60.ournews.module.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Misutesu on 2017/3/12 0012.
 */

public class CheckLoginResult {

    /**
     * result : success
     * errorCode : 0
     * data : {"id":1,"loginName":"Misutesu","nickName":"Misutesu","sex":"1","sign":"asdsad","birthday":0,"photo":"NoImage","token":"f7b7c047e84d491890eb70301802eb23"}
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
         * id : 1
         * loginName : Misutesu
         * nickName : Misutesu
         * sex : 1
         * sign : asdsad
         * birthday : 0
         * photo : NoImage
         */

        private int id;
        @SerializedName("login_name")
        private String loginName;
        @SerializedName("nick_name")
        private String nickName;
        private int sex;
        private String sign;
        private int birthday;
        private String photo;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getLoginName() {
            return loginName;
        }

        public void setLoginName(String loginName) {
            this.loginName = loginName;
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
    }
}
