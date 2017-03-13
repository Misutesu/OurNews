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

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.DisposableSubscriber;

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
                .subscribeWith(new DisposableSubscriber<CommentResult>() {
                    @Override
                    protected void onStart() {
                        request(1);
                    }

                    @Override
                    public void onComplete() {
                        mView.getCommentsEnd();
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        onComplete();
                        mView.getCommentsError(MyApplication.getContext().getString(R.string.internet_error), page);
                    }

                    @Override
                    public void onNext(CommentResult result) {
                        if (result.getResult().equals("success")) {
                            List<Comment> comments = new ArrayList<>();
                            for (int i = 0; i < result.getData().getComments().size(); i++) {
                                Comment comment = new Comment();
                                comment.setId(result.getData().getComments().get(i).getId());
                                comment.setNid(nid);
                                comment.setContent(result.getData().getComments().get(i).getContent());
                                comment.setCreateTime(result.getData().getComments().get(i).getCreateTime());
                                OtherUser otherUser = new OtherUser();
                                otherUser.setId(result.getData().getComments().get(i).getUser().getId());
                                otherUser.setNickName(result.getData().getComments().get(i).getUser().getNickName());
                                otherUser.setSex(result.getData().getComments().get(i).getUser().getSex());
                                otherUser.setPhoto(result.getData().getComments().get(i).getUser().getPhoto());
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
