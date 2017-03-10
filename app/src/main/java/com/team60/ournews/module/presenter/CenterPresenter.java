package com.team60.ournews.module.presenter;

/**
 * Created by wujiaquan on 2017/3/10.
 */

public interface CenterPresenter {
    void getNewList(long id, String token, long uid, int type, int page, int sort);
}
