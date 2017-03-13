package com.team60.ournews.module.connection;

import com.team60.ournews.module.model.CheckLoginResult;
import com.team60.ournews.module.model.CommentResult;
import com.team60.ournews.module.model.ContentResult;
import com.team60.ournews.module.model.HomeNewResult;
import com.team60.ournews.module.model.ListNewResult;
import com.team60.ournews.module.model.LoginResult;
import com.team60.ournews.module.model.NoDataResult;
import com.team60.ournews.module.model.UploadResult;

import java.util.Map;

import io.reactivex.Flowable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
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
    Flowable<NoDataResult> register(@Query("login_name") String loginName,
                                    @Query("password") String password,
                                    @Query("time") long time,
                                    @Query("key") String key);

    @POST("login")
    Flowable<LoginResult> login(@Query("login_name") String loginName
            , @Query("password") String password
            , @Query("time") long time);

    @POST("checkLogin")
    Flowable<CheckLoginResult> checkLogin(@Query("id") long id
            , @Query("token") String token);

    @POST("changeInfo")
    Flowable<NoDataResult> changeInfo(@Query("id") long id,
                                      @Query("token") String token,
                                      @Query("nick_name") String nickName,
                                      @Query("sex") String sex,
                                      @Query("photo") String photo);

    @POST("getHomeNews")
    Flowable<HomeNewResult> getHomeNews();

    @POST("getHomeNews")
    Flowable<HomeNewResult> getHomeNewsUseType(@Query("type") int type);

    @POST("getNewList")
    Flowable<ListNewResult> getNewListUseType(@Query("type") int type,
                                              @Query("page") int page,
                                              @Query("size") int size,
                                              @Query("sort") int sort);

    @POST("searchNew")
    Flowable<ListNewResult> searchNew(@Query("name") String name,
                                      @Query("page") int page,
                                      @Query("size") int size,
                                      @Query("sort") int sort);

    @POST("getCollections")
    Flowable<ListNewResult> getCollections(@Query("id") long id,
                                           @Query("token") String token,
                                           @Query("uid") long uid,
                                           @Query("page") int page,
                                           @Query("size") int size,
                                           @Query("sort") int sort);

    @POST("getHistory")
    Flowable<ListNewResult> getHistory(@Query("id") long id,
                                       @Query("token") String token,
                                       @Query("uid") long uid,
                                       @Query("page") int page,
                                       @Query("size") int size,
                                       @Query("sort") int sort);

    @POST("collectNew")
    Flowable<NoDataResult> collectNew(@Query("nid") long nid,
                                      @Query("uid") long uid,
                                      @Query("token") String token,
                                      @Query("type") int type);

    @POST("getNewContent")
    Flowable<ContentResult> getNewContentUseId(@Query("nid") long id);

    @POST("getNewContent")
    Flowable<ContentResult> getNewContentUseId(@Query("nid") long id, @Query("uid") long uid);

    @POST("sendComment")
    Flowable<NoDataResult> sendCommentUseNewId(@Query("uid") long uid,
                                               @Query("nid") long nid,
                                               @Query("content") String content,
                                               @Query("time") long time,
                                               @Query("token") String token,
                                               @Query("key") String key);

    @POST("getComment")
    Flowable<CommentResult> getCommentsUseId(@Query("nid") long nid,
                                             @Query("page") int page,
                                             @Query("size") int size,
                                             @Query("sort") int sort);

    @POST("getComment")
    Flowable<CommentResult> getCommentsUseId(@Query("uid") long uid,
                                             @Query("nid") long nid,
                                             @Query("page") int page,
                                             @Query("size") int size,
                                             @Query("sort") int sort);

    @Multipart
    @POST("uploadImage")
    Flowable<UploadResult> uploadImage(@Part("description") RequestBody description, @Part MultipartBody.Part file);

    @Multipart
    @POST("uploadImage")
    Flowable<UploadResult> uploadImage(@PartMap Map<String, RequestBody> params);
}
