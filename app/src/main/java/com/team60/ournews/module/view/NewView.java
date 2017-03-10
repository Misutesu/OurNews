package com.team60.ournews.module.view;

import com.team60.ournews.module.bean.New;
import com.team60.ournews.module.view.base.BaseView;

import org.json.JSONException;

/**
 * Created by Misutesu on 2016/12/28 0028.
 */

public interface NewView extends BaseView {
    void getNewContentEnd();

    void getNewContentSuccess(New n) throws JSONException;

    void getNewContentError(String message);

    void collectNewEnd();

    void collectNewSuccess();

    void collectNewError(String message);
}
