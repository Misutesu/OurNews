package com.team60.ournews.module.presenter.impl;

import android.graphics.BitmapFactory;

import com.team60.ournews.MyApplication;
import com.team60.ournews.R;
import com.team60.ournews.module.bean.User;
import com.team60.ournews.module.connection.RetrofitUtil;
import com.team60.ournews.module.model.NoDataResult;
import com.team60.ournews.module.model.UploadResult;
import com.team60.ournews.module.presenter.EditUserPresenter;
import com.team60.ournews.module.view.EditUserView;
import com.team60.ournews.util.ErrorUtil;

import java.io.File;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.DisposableSubscriber;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * Created by Misutesu on 2016/12/27 0027.
 */

public class EditUserPresenterImpl implements EditUserPresenter {

    private EditUserView mView;

    public EditUserPresenterImpl(EditUserView mView) {
        this.mView = mView;
    }

    @Override
    public void saveInfo(final long id, final String token, final String nickName, final int sex, String photo) {
        if (photo.equals("")) {
            changeInfo(id, token, nickName, sex, photo);
        } else {
            File file = new File(photo);
            if (!file.exists()) {
                mView.saveEnd();
                mView.saveError(MyApplication.getContext().getString(R.string.photo_file_error));
            } else {
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
                BitmapFactory.decodeFile(photo, options);
                RequestBody requestFile = RequestBody.create(MediaType.parse(options.outMimeType), file);
                MultipartBody.Part body = MultipartBody.Part.createFormData("upload", file.getName(), requestFile);
                RequestBody description = RequestBody.create(MultipartBody.FORM, "Description");

                mView.addSubscription(RetrofitUtil.newInstance().uploadImage(description, body)
                        .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableSubscriber<UploadResult>() {
                            @Override
                            protected void onStart() {
                                request(1);
                            }

                            @Override
                            public void onComplete() {
                                mView.saveEnd();
                            }

                            @Override
                            public void onError(Throwable e) {
                                e.printStackTrace();
                                onComplete();
                                mView.saveError(MyApplication.getContext().getString(R.string.internet_error));
                            }

                            @Override
                            public void onNext(UploadResult result) {
                                if (result.getResult().equals("success")) {
                                    changeInfo(id, token, nickName, sex, result.getData().get(0));
                                } else {
                                    mView.saveError(ErrorUtil.getErrorMessage(result.getErrorCode()));
                                }
                            }
                        }));
            }
        }
    }

    private void changeInfo(long id, String token, final String nickName, final int sex, final String photo) {
        String sexStr;
        if (sex != -1)
            sexStr = String.valueOf(sex);
        else
            sexStr = "";

        mView.addSubscription(RetrofitUtil.newInstance().changeInfo(id, token, nickName, sexStr, photo)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSubscriber<NoDataResult>() {
                    @Override
                    protected void onStart() {
                        request(1);
                    }

                    @Override
                    public void onComplete() {
                        mView.saveEnd();
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        onComplete();
                        mView.saveError(MyApplication.getContext().getString(R.string.internet_error));
                    }

                    @Override
                    public void onNext(NoDataResult result) {
                        if (result.getResult().equals("success")) {
                            User user = User.newInstance();
                            if (!nickName.equals(""))
                                user.setNickName(nickName);
                            if (sex != -1)
                                user.setSex(sex);
                            if (!photo.equals(""))
                                user.setPhoto(photo);

                            mView.saveSuccess();
                        } else {
                            mView.saveError(ErrorUtil.getErrorMessage(result.getErrorCode()));
                        }
                    }
                }));
    }
}
