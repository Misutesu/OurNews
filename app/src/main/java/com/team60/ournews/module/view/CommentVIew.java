package com.team60.ournews.module.view;

import com.team60.ournews.module.bean.Comment;
import com.team60.ournews.module.view.base.BaseView;

import java.util.List;

/**
 * Created by Misutesu on 2016/12/29 0029.
 */

public interface CommentVIew extends BaseView {
    void getCommentsEnd();

    void getCommentsSuccess(List<Comment> comments, int page);

    void getCommentsError(String message, int page);
}
