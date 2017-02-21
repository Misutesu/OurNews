package com.team60.ournews.module.presenter.impl;

import android.graphics.BitmapFactory;

import com.team60.ournews.MyApplication;
import com.team60.ournews.R;
import com.team60.ournews.common.Constants;
import com.team60.ournews.module.connection.RetrofitUtil;
import com.team60.ournews.module.model.User;
import com.team60.ournews.module.presenter.EditUserPresenter;
import com.team60.ournews.module.view.EditUserView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Misutesu on 2016/12/27 0027.
 */

public class EditUserPresenterImpl implements EditUserPresenter {

    private User user = User.newInstance();

    private EditUserView mView;

    public EditUserPresenterImpl(EditUserView mView) {
        this.mView = mView;
    }

    @Override
    public void saveInfo(final String nickName, final int sex, final String photo) {
        Observable.create(new Observable.OnSubscribe<Object>() {
            @Override
            public void call(Subscriber<? super Object> subscriber) {
                try {
                    String photoName = null;
                    if (photo != null) {
                        File file = new File(photo);
                        if (!file.exists())
                            subscriber.onError(new Exception(MyApplication.getContext().getString(R.string.photo_file_error)));
                        else {
                            BitmapFactory.Options options = new BitmapFactory.Options();
                            options.inJustDecodeBounds = true;
                            BitmapFactory.decodeFile(photo, options);

                            RequestBody requestFile = RequestBody.create(MediaType.parse(options.outMimeType), file);
                            MultipartBody.Part body = MultipartBody.Part.createFormData("upload", file.getName(), requestFile);
                            RequestBody description = RequestBody.create(MultipartBody.FORM, "Description");

                            JSONObject jsonObject = new JSONObject(RetrofitUtil.newInstance().uploadImage(description, body).execute().body().string());

                            if (jsonObject.getString("result").equals("success")) {
                                photoName = jsonObject.getJSONArray("image_list").getString(0);
                            } else {
                                if (jsonObject.getInt("error_code") == Constants.FILE_IS_NOT_PHOTO) {
                                    subscriber.onError(new Exception(MyApplication.getContext().getString(R.string.file_is_not_photo)));
                                } else if (jsonObject.getInt("error_code") == Constants.PHOTO_TOO_BIG) {
                                    subscriber.onError(new Exception(MyApplication.getContext().getString(R.string.photo_too_big)));
                                }
                            }
                        }
                    }
                    JSONObject jsonObject = null;
                    if (photoName != null && sex != -1 && nickName != null) {
                        jsonObject = new JSONObject(RetrofitUtil.newInstance()
                                .changePhotoAndSexAndNickName(user.getId(), photoName, sex, nickName).execute().body().string());
                    } else if (photoName != null && sex != -1) {
                        jsonObject = new JSONObject(RetrofitUtil.newInstance()
                                .changePhotoAndSex(user.getId(), photoName, sex).execute().body().string());
                    } else if (nickName != null && sex != -1) {
                        jsonObject = new JSONObject(RetrofitUtil.newInstance()
                                .changeSexAndNickName(user.getId(), sex, nickName).execute().body().string());
                    } else if (photoName != null && nickName != null) {
                        jsonObject = new JSONObject(RetrofitUtil.newInstance()
                                .changePhotoAndNickName(user.getId(), photoName, nickName).execute().body().string());
                    } else if (photoName != null) {
                        jsonObject = new JSONObject(RetrofitUtil.newInstance()
                                .changePhoto(user.getId(), photoName).execute().body().string());
                    } else if (sex != -1) {
                        jsonObject = new JSONObject(RetrofitUtil.newInstance()
                                .changeSex(user.getId(), sex).execute().body().string());
                    } else if (nickName != null) {
                        jsonObject = new JSONObject(RetrofitUtil.newInstance()
                                .changeNickName(user.getId(), nickName).execute().body().string());
                    }
                    if (jsonObject != null) {
                        if (jsonObject.getString("result").equals("success")) {
                            if (photoName != null)
                                user.setPhoto(photoName);
                            if (sex != -1)
                                user.setSex(sex);
                            if (nickName != null)
                                user.setNickName(nickName);
                            subscriber.onNext(null);
                        } else {
                            if (jsonObject.getInt("error_code") == Constants.CHANGE_INFO_ERROR) {
                                subscriber.onError(new Exception(MyApplication.getContext().getString(R.string.change_info_error)));
                            }
                        }
                    } else {
                        subscriber.onError(new Exception(MyApplication.getContext().getString(R.string.change_info_error)));
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    subscriber.onError(new Exception(MyApplication.getContext().getString(R.string.internet_error)));
                } catch (JSONException e) {
                    e.printStackTrace();
                    subscriber.onError(new Exception(MyApplication.getContext().getString(R.string.server_error)));
                } finally {
                    subscriber.onCompleted();
                }
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Object>() {
                    @Override
                    public void onCompleted() {
                        mView.saveEnd();
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        onCompleted();
                        mView.saveError(e.getMessage());
                    }

                    @Override
                    public void onNext(Object o) {
                        mView.saveSuccess();
                    }
                });
    }
}
