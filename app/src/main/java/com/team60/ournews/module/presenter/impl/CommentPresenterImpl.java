package com.team60.ournews.module.presenter.impl;

import com.team60.ournews.MyApplication;
import com.team60.ournews.R;
import com.team60.ournews.common.Constants;
import com.team60.ournews.module.bean.Comment;
import com.team60.ournews.module.bean.OtherUser;
import com.team60.ournews.module.connection.RetrofitUtil;
import com.team60.ournews.module.model.CommentResult;
import com.team60.ournews.module.presenter.CommentPresenter;
import com.team60.ournews.module.view.CommentVIew;
import com.team60.ournews.util.ErrorUtil;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Misutesu on 2016/12/29 0029.
 */

public class CommentPresenterImpl implements CommentPresenter {

    private CommentVIew mView;

    public CommentPresenterImpl(CommentVIew mView) {
        this.mView = mView;
    }

    @Override
    public void getComments(final long nid, final int page, final int sort) {
        mView.addSubscription(RetrofitUtil.newInstance()
                .getCommentsUseId(nid, page, Constants.COMMENT_EVERY_PAGE_SIZE, sort)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<CommentResult>() {
                    @Override
                    public void onCompleted() {
                        mView.getCommentsEnd();
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        onCompleted();
                        mView.getCommentsError(MyApplication.getContext().getString(R.string.internet_error), page);
                    }

                    @Override
                    public void onNext(CommentResult result) {
                        if (result.getResult().equals("success")) {
                            List<Comment> comments = new ArrayList<>();
                            for (int i = 0; i < result.getData().size(); i++) {
                                Comment comment = new Comment();
                                comment.setId(result.getData().get(i).getId());
                                comment.setNid(nid);
                                comment.setContent(result.getData().get(i).getContent());
                                comment.setCreateTime(result.getData().get(i).getCreateTime());
                                OtherUser otherUser = new OtherUser();
                                otherUser.setId(result.getData().get(i).getUser().getId());
                                otherUser.setNickName(result.getData().get(i).getUser().getNickName());
                                otherUser.setSex(result.getData().get(i).getUser().getSex());
                                otherUser.setPhoto(result.getData().get(i).getUser().getPhoto());
                                comment.setUser(otherUser);
                                comments.add(comment);
                            }
                            mView.getCommentsSuccess(comments, page);
                        } else {
                            mView.getCommentsError(ErrorUtil.getErrorMessage(result.getErrorCode()), page);
                        }
                    }
                }));
    }
}
