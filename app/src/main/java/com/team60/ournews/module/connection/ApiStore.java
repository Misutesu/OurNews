package com.team60.ournews.module.connection;

import com.team60.ournews.module.model.CheckLoginResult;
import com.team60.ournews.module.model.CheckUpdateResult;
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

    /**
     * 用户注册
     * @param loginName 用户名
     * @param password 密码
     * @param time 当前时间戳
     * @param key 服务器规定的key值
     */
    @POST("register")
    Flowable<NoDataResult> register(@Query("login_name") String loginName,
                                    @Query("password") String password,
                                    @Query("time") long time,
                                    @Query("key") String key);

    /**
     * 用户登录
     * @param loginName 用户名
     * @param password 服务器规定的登录密码加密
     * @param time 当前时间戳
     */
    @POST("login")
    Flowable<LoginResult> login(@Query("login_name") String loginName,
                                @Query("password") String password,
                                @Query("time") long time);

    /**
     * 检测登录状态(获取用户信息)
     * @param id 用户id
     * @param token 用户token
     */
    @POST("checkLogin")
    Flowable<CheckLoginResult> checkLogin(@Query("id") long id,
                                          @Query("token") String token);

    /**
     * 检测更新并获取新版本信息
     * @param time 当前时间戳
     * @param key 服务器规定的key
     */
    @POST("checkUpdate")
    Flowable<CheckUpdateResult> checkUpdate(@Query("time") long time,
                                            @Query("key") String key);

    /**
     * 更改用户信息
     * @param id 用户id
     * @param token 用户token
     * @param nickName 修改后的昵称
     * @param sex 修改后的性别
     * @param birthday 修改后的生日
     * @param photo 修改后的头像名
     */
    @POST("changeInfo")
    Flowable<NoDataResult> changeInfo(@Query("id") long id,
                                      @Query("token") String token,
                                      @Query("nick_name") String nickName,
                                      @Query("sex") String sex,
                                      @Query("birthday") String birthday,
                                      @Query("photo") String photo);

    /**
     * 获取首页新闻内容
     */
    @POST("getHomeNews")
    Flowable<HomeNewResult> getHomeNews();

    /**
     * 获取首页某类新闻内容
     * @param type 需要的新闻类型
     */
    @POST("getHomeNews")
    Flowable<HomeNewResult> getHomeNewsUseType(@Query("type") int type);

    /**
     * 获取某类新闻列表
     * @param type 需要的新闻类型
     * @param page 获取第几页
     * @param size 每页获取几条数据
     * @param sort 排序
     */
    @POST("getNewList")
    Flowable<ListNewResult> getNewListUseType(@Query("type") int type,
                                              @Query("page") int page,
                                              @Query("size") int size,
                                              @Query("sort") int sort);

    /**
     * 搜索新闻
     * @param name 要搜索的名字
     * @param page 获取第几页
     * @param size 每页获取几条数据
     * @param sort 排序
     */
    @POST("searchNew")
    Flowable<ListNewResult> searchNew(@Query("name") String name,
                                      @Query("page") int page,
                                      @Query("size") int size,
                                      @Query("sort") int sort);

    /**
     * 获取用户收藏的新闻列表
     * @param id 当前登录用户id
     * @param token 当前登录用户token
     * @param uid 需要获取的用户id
     * @param page 获取第几页
     * @param size 每页获取几条数据
     * @param sort 排序
     */
    @POST("getCollections")
    Flowable<ListNewResult> getCollections(@Query("uid") long id,
                                           @Query("token") String token,
                                           @Query("oid") long uid,
                                           @Query("page") int page,
                                           @Query("size") int size,
                                           @Query("sort") int sort);

    /**
     * 获取用户浏览历史
     * @param id 当前登录用户id
     * @param token 当前登录用户token
     * @param uid 需要获取的用户id
     * @param page 获取第几页
     * @param size 每页获取几条数据
     * @param sort 排序
     */
    @POST("getHistory")
    Flowable<ListNewResult> getHistory(@Query("uid") long id,
                                       @Query("token") String token,
                                       @Query("oid") long uid,
                                       @Query("page") int page,
                                       @Query("size") int size,
                                       @Query("sort") int sort);

    /**
     * 收藏新闻
     * @param nid 需要收藏的新闻id
     * @param uid 当前登录用户id
     * @param token 当前登录用户token
     * @param type 收藏或取消收藏
     */
    @POST("collectNew")
    Flowable<NoDataResult> collectNew(@Query("nid") long nid,
                                      @Query("uid") long uid,
                                      @Query("token") String token,
                                      @Query("type") int type);

    /**
     * 未登录时获取新闻内容
     * @param id 新闻id
     */
    @POST("getNewContent")
    Flowable<ContentResult> getNewContentUseId(@Query("nid") long id);

    /**
     * 登陆后获取新闻内容
     * @param id 新闻id
     * @param uid 当前登录用户id
     */
    @POST("getNewContent")
    Flowable<ContentResult> getNewContentUseId(@Query("nid") long id,
                                               @Query("uid") long uid);

    /**
     * 发表新闻评论
     * @param uid 当前登录用户id
     * @param nid 新闻id
     * @param content 评论内容
     * @param time 当前时间戳
     * @param token 当前登录用户token
     * @param key 服务器规定的key
     */
    @POST("sendComment")
    Flowable<NoDataResult> sendCommentUseNewId(@Query("uid") long uid,
                                               @Query("nid") long nid,
                                               @Query("content") String content,
                                               @Query("time") long time,
                                               @Query("token") String token,
                                               @Query("key") String key);

    /**
     * 未登录时获取新闻评论
     * @param nid 新闻id
     * @param page 获取第几页
     * @param size 每页获取几条数据
     * @param sort 排序
     */
    @POST("getComment")
    Flowable<CommentResult> getCommentsUseId(@Query("nid") long nid,
                                             @Query("page") int page,
                                             @Query("size") int size,
                                             @Query("sort") int sort);

    /**
     * 登陆后获取新闻评论
     * @param uid 登录用户id
     * @param nid 新闻id
     * @param page 获取第几页
     * @param size 每页获取几条数据
     * @param sort 排序
     */
    @POST("getComment")
    Flowable<CommentResult> getCommentsUseId(@Query("uid") long uid,
                                             @Query("nid") long nid,
                                             @Query("page") int page,
                                             @Query("size") int size,
                                             @Query("sort") int sort);

    /**
     * 点赞评论
     * @param cid 评论id
     * @param uid 登录用户id
     * @param token 登录用户token
     * @param type 点赞或取消点赞
     */
    @POST("lickComment")
    Flowable<NoDataResult> lickComment(@Query("cid") long cid,
                                       @Query("uid") long uid,
                                       @Query("token") String token,
                                       @Query("type") int type);

    /**
     * 发表子评论(暂时没用)
     * @param uid 登录用户id
     * @param cid 评论id
     * @param content 子评论内容
     * @param time 时间戳
     * @param token 登录用户token
     * @param key 服务器规定的key
     */
    @POST("sendChildComment")
    Flowable<NoDataResult> sendChildComment(@Query("uid") long uid,
                                            @Query("cid") long cid,
                                            @Query("content") String content,
                                            @Query("time") long time,
                                            @Query("token") String token,
                                            @Query("key") String key);

    @Multipart
    @POST("uploadImage")
    Flowable<UploadResult> uploadImage(@Part("description") RequestBody description, @Part MultipartBody.Part file);

    @Multipart
    @POST("uploadImage")
    Flowable<UploadResult> uploadImage(@PartMap Map<String, RequestBody> params);
}
