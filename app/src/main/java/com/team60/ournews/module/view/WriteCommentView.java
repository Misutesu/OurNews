package com.team60.ournews.module.view;

import com.team60.ournews.module.view.base.BaseView;

/**
 * Created by Misutesu on 2016/12/29 0029.
 */

public interface WriteCommentView extends BaseView {
    void sendCommentEnd();

    void sendCommentSuccess();

    void sendCommentError(String message);
}
