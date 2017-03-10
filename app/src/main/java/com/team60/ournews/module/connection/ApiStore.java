package com.team60.ournews.module.connection;

import com.team60.ournews.module.model.CommentResult;
import com.team60.ournews.module.model.ContentResult;
import com.team60.ournews.module.model.HomeNewResult;
import com.team60.ournews.module.model.ListNewResult;
import com.team60.ournews.module.model.LoginResult;
import com.team60.ournews.module.model.NoDataResult;
import com.team60.ournews.module.model.UploadResult;

import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by Misutesu on 2016/12/26 0026.
 */

public interface ApiStore {

    @POST("register")
    Observable<NoDataResult> register(@Query("login_name") String loginName,
                                      @Query("password") String password,
                                      @Query("time") long time,
                                      @Query("key") String key);

    @POST("login")
    Observable<LoginResult> login(@Query("login_name") String loginName
            , @Query("password") String password
            , @Query("time") long time);

    @POST("changeInfo")
    Observable<NoDataResult> changeInfo(@Query("id") long id,
                                        @Query("token") String token,
                                        @Query("nick_name") String nickName,
                                        @Query("sex") String sex,
                                        @Query("photo") String photo);

    @POST("getHomeNews")
    Observable<HomeNewResult> getHomeNews();

    @POST("getHomeNews")
    Observable<HomeNewResult> getHomeNewsUseType(@Query("type") int type);

    @POST("getNewList")
    Observable<ListNewResult> getNewListUseType(@Query("type") int type,
                                                @Query("page") int page,
                                                @Query("size") int size,
                                                @Query("sort") int sort);

    @POST("searchNew")
    Observable<ListNewResult> searchNew(@Query("name") String name,
                                        @Query("page") int page,
                                        @Query("size") int size,
                                        @Query("sort") int sort);

    @POST("getCollections")
    Observable<ListNewResult> getCollections(@Query("id") long id,
                                             @Query("token") String token,
                                             @Query("uid") long uid,
                                             @Query("page") int page,
                                             @Query("size") int size,
                                             @Query("sort") int sort);

    @POST("getHistory")
    Observable<ListNewResult> getHistory(@Query("id") long id,
                                         @Query("token") String token,
                                         @Query("uid") long uid,
                                         @Query("page") int page,
                                         @Query("size") int size,
                                         @Query("sort") int sort);

    @POST("collectNew")
    Observable<NoDataResult> collectNew(@Query("nid") long nid,
                                        @Query("uid") long uid,
                                        @Query("token") String token,
                                        @Query("type") int type);

    @POST("getNewContent")
    Observable<ContentResult> getNewContentUseId(@Query("nid") long id);

    @POST("getNewContent")
    Observable<ContentResult> getNewContentUseId(@Query("nid") long id, @Query("uid") long uid);

    @POST("sendComment")
    Observable<NoDataResult> sendCommentUseNewId(@Query("uid") long uid,
                                                 @Query("nid") long nid,
                                                 @Query("content") String content,
                                                 @Query("time") long time,
                                                 @Query("key") String key);

    @POST("getComment")
    Observable<CommentResult> getCommentsUseId(@Query("nid") long nid,
                                               @Query("page") int page,
                                               @Query("size") int size,
                                               @Query("sort") int sort);

    @Multipart
    @POST("uploadImage")
    Observable<UploadResult> uploadImage(@Part("description") RequestBody description, @Part MultipartBody.Part file);

    @Multipart
    @POST("uploadImage")
    Observable<UploadResult> uploadImage(@PartMap Map<String, RequestBody> params);
}
