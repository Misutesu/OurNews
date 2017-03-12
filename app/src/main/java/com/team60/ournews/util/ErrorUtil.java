package com.team60.ournews.util;

import com.team60.ournews.module.bean.Error;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Misutesu on 2017/3/5 0005.
 */

public class ErrorUtil {
    // 服务器错误
    private static final int SERVER_ERROR = 100;
    // 所传参数有误
    private static final int VALUES_ERROR = 101;
    // 连接超时
    private static final int CONNECT_TIME_OUT = 102;
    // KEY错误
    private static final int KEY_ERROR = 103;
    // 用户名长度不正确
    private static final int LOGIN_NAME_LENGTH_ERROR = 1001;
    // 密码长度不正确
    private static final int PASSWORD_LENGTH_ERROR = 1002;
    // 用户名已存在
    private static final int LOGIN_NAME_IS_EXIST = 1003;
    // 用户名不存在
    private static final int LOGIN_NAME_NO_EXIST = 2001;
    // 密码错误
    private static final int PASSWORD_ERROR = 2002;
    // 修改用户信息错误或用户不存在
    private static final int CHANGE_INFO_ERROR = 3001;
    // Token错误
    private static final int TOKEN_ERROR = 3002;
    // 用户被封禁
    public static final int USER_NO_ONLINE = 3003;
    // 用户不存在(ID)
    private static final int USER_NO_EXIST = 5001;
    // 新闻不存在(ID)
    private static final int NEW_NO_EXIST = 7001;
    // 新闻未上线
    private static final int NEW_NO_ONLINE = 8001;
    // 已有收藏
    public static final int HAS_COLLECTION = 9001;
    // 没收藏过
    public static final int NO_COLLECTION = 9002;

    // 上传文件不是图片
    private static final int UPLOAD_NO_IMAGE = 100001;
    // 上传文件过大
    private static final int UPLOAD_FILE_TOO_BIG = 100002;

    private static List<Error> errorList;

    public static String getErrorMessage(int code) {
        getErrorList();
        Error error = null;
        for (Error e : errorList) {
            if (code == e.getCode())
                error = e;
        }
        if (error == null)
            error = new Error(code, "错误码:" + String.valueOf(code));
        return error.getMessage();
    }

    private static void getErrorList() {
        if (errorList == null) {
            errorList = new ArrayList<>();
            errorList.add(new Error(SERVER_ERROR, "服务器错误"));
            errorList.add(new Error(VALUES_ERROR, "所传参数有误"));
            errorList.add(new Error(CONNECT_TIME_OUT, "连接超时"));
            errorList.add(new Error(KEY_ERROR, "KEY错误"));
            errorList.add(new Error(LOGIN_NAME_LENGTH_ERROR, "用户名长度不正确"));
            errorList.add(new Error(PASSWORD_LENGTH_ERROR, "密码长度不正确"));
            errorList.add(new Error(LOGIN_NAME_IS_EXIST, "用户名已存在"));
            errorList.add(new Error(LOGIN_NAME_NO_EXIST, "用户名不存在"));
            errorList.add(new Error(PASSWORD_ERROR, "密码错误"));
            errorList.add(new Error(CHANGE_INFO_ERROR, "修改用户信息错误或用户不存在"));
            errorList.add(new Error(TOKEN_ERROR, "Token错误"));
            errorList.add(new Error(USER_NO_ONLINE, "用户被封禁"));
            errorList.add(new Error(USER_NO_EXIST, "用户不存在"));
            errorList.add(new Error(NEW_NO_EXIST, "新闻不存在"));
            errorList.add(new Error(HAS_COLLECTION, "已有收藏"));
            errorList.add(new Error(NO_COLLECTION, "没收藏过"));
            errorList.add(new Error(NEW_NO_ONLINE, "新闻未上线"));
            errorList.add(new Error(UPLOAD_NO_IMAGE, "上传文件不是图片"));
            errorList.add(new Error(UPLOAD_FILE_TOO_BIG, "上传文件过大"));
        }
    }
}
