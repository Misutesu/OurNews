package com.team60.ournews.module.connection;

import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Query;

/**
 * Created by Misutesu on 2016/12/26 0026.
 */

public interface ApiStore {

    @POST("register")
    Call<ResponseBody> register(@Query("loginname") String loginName, @Query("password") String password);

    @POST("login")
    Call<ResponseBody> login(@Query("loginname") String loginName, @Query("password") String password);

    @POST("changeInfo")
    Call<ResponseBody> changeNickName(@Query("id") long id, @Query("nickname") String nickName);

    @POST("changeInfo")
    Call<ResponseBody> changePhoto(@Query("id") long id, @Query("photo") String photo);

    @POST("changeInfo")
    Call<ResponseBody> changeSex(@Query("id") long id, @Query("sex") int sex);

    @POST("changeInfo")
    Call<ResponseBody> changeSexAndNickName(@Query("id") long id, @Query("sex") int sex, @Query("nickname") String nickName);

    @POST("changeInfo")
    Call<ResponseBody> changePhotoAndNickName(@Query("id") long id, @Query("photo") String photo, @Query("nickname") String nickName);

    @POST("changeInfo")
    Call<ResponseBody> changePhotoAndSex(@Query("id") long id, @Query("photo") String photo, @Query("sex") int sex);

    @POST("changeInfo")
    Call<ResponseBody> changePhotoAndSexAndNickName(@Query("id") long id, @Query("photo") String photo, @Query("sex") int sex, @Query("nickname") String nickName);

    @POST("getHomeNews")
    Call<ResponseBody> getHomeNews();

    @POST("getHomeNews")
    Call<ResponseBody> getHomeNewsUseType(@Query("type") int type);

    @POST("getNewList")
    Call<ResponseBody> getNewListUseType(@Query("type") int type, @Query("page") int page, @Query("size") int size, @Query("sort") int sort);

    @POST("searchNew")
    Call<ResponseBody> searchNew(@Query("name") String name, @Query("page") int page, @Query("size") int size, @Query("sort") int sort);

    @POST("getNewContent")
    Call<ResponseBody> getNewContentUseId(@Query("id") long id);

    @POST("getNewContent")
    Call<ResponseBody> getNewContentUseId(@Query("id") long id, @Query("uid") long uid);

    @POST("sendComment")
    Call<ResponseBody> sendCommentUseNewId(@Query("uid") long uid, @Query("nid") long nid, @Query("content") String content, @Query("createtime") String createTime);

    @POST("getComment")
    Call<ResponseBody> getCommentsUseId(@Query("nid") long nid, @Query("page") int page, @Query("size") int size, @Query("sort") int sort);

    @Multipart
    @POST("uploadImage")
    Call<ResponseBody> uploadImage(@Part("description") RequestBody description, @Part MultipartBody.Part file);

    @Multipart
    @POST("uploadImage")
    Call<ResponseBody> uploadImage(@PartMap Map<String, RequestBody> params);
}
