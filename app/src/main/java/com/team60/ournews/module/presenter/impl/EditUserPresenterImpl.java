package com.team60.ournews.module.presenter.impl;

import android.content.Context;
import android.graphics.BitmapFactory;

import com.team60.ournews.R;
import com.team60.ournews.module.bean.User;
import com.team60.ournews.util.RetrofitUtil;
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

    private Context mContext;
    private EditUserView mView;

    public EditUserPresenterImpl(Context context, EditUserView view) {
        mContext = context;
        mView = view;
    }

    @Override
    public void saveInfo(final long id, final String token, final String nickName, final int sex
            , final String birthday, String photo) {
        //如果没有photo参数(表示本次修改用户信息没有修改头像)
        //则直接修改信息
        //否则先上传图片
        if (photo.equals("")) {
            //直接修改信息
            changeInfo(id, token, nickName, sex, birthday, photo);
        } else {
            //上传图片
            File file = new File(photo);
            if (!file.exists()) {
                //图片文件不存在
                mView.saveEnd();
                mView.saveError(mContext.getString(R.string.photo_file_error));
            } else {
                //压缩图片
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
                BitmapFactory.decodeFile(photo, options);
                //创建上传的Request
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
                                mView.saveError(mContext.getString(R.string.internet_error));
                            }

                            @Override
                            public void onNext(UploadResult result) {
                                if (result.getResult().equals("success")) {
                                    //上传成功开始修改信息
                                    changeInfo(id, token, nickName, sex, birthday, result.getData().get(0));
                                } else {
                                    mView.saveError(ErrorUtil.getErrorMessage(result.getErrorCode()));
                                }
                            }
                        }));
            }
        }
    }

    private void changeInfo(long id, String token, final String nickName, final int sex
            , final String birthday, final String photo) {
        String sexStr;
        if (sex != -1) sexStr = String.valueOf(sex);
        else sexStr = "";
        mView.addSubscription(RetrofitUtil.newInstance().changeInfo(id, token, nickName, sexStr, birthday, photo)
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
                        mView.saveError(mContext.getString(R.string.internet_error));
                    }

                    @Override
                    public void onNext(NoDataResult result) {
                        if (result.getResult().equals("success")) {
                            User user = User.newInstance();
                            if (!nickName.equals("")) user.setNickName(nickName);
                            if (sex != -1) user.setSex(sex);
                            if (!photo.equals("")) user.setPhoto(photo);
                            if (!birthday.equals("")) user.setBirthday(Integer.valueOf(birthday));

                            mView.saveSuccess();
                        } else {
                            mView.saveError(ErrorUtil.getErrorMessage(result.getErrorCode()));
                        }
                    }
                }));
    }
}
