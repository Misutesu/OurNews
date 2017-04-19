package com.team60.ournews.module.presenter;

/**
 * Created by Misutesu on 2016/12/27 0027.
 */

public interface EditUserPresenter {
    void saveInfo(long id, String token, String nickName, int sex, String birthday, String photo);
}
