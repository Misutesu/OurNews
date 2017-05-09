package com.team60.ournews.module.presenter.impl;

import android.content.Context;

import com.team60.ournews.R;
import com.team60.ournews.common.Constants;
import com.team60.ournews.module.bean.New;
import com.team60.ournews.module.model.ListNewResult;
import com.team60.ournews.module.presenter.CenterPresenter;
import com.team60.ournews.module.view.CenterView;
import com.team60.ournews.util.ErrorUtil;
import com.team60.ournews.util.RetrofitUtil;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.DisposableSubscriber;

/**
 * Created by wujiaquan on 2017/3/10.
 */

public class CenterPresenterImpl implements CenterPresenter {

    private Context mContext;
    private CenterView mView;

    //创建Presenter,获得View(Activity)对象
    public CenterPresenterImpl(Context context, CenterView view) {
        mContext = context;
        mView = view;
    }

    /**
     * View(Activity)中调用此方法开始请求网络数据
     * 请求完毕后调用View(Activity)中对应的方法通知界面请求完成(包含完成、失败、成功 具体其他的看View定义)
     */
    @Override
    public void getNewList(long id, String token, long uid, int type, final int page, int sort) {
        //Flowable是RxJava数据发射源(被观察者)
        //ApiStore里面的每一个方法实际上都是返回的一个Flowable对象
        Flowable<ListNewResult> flowable;
        if (type == 0) {
            flowable = RetrofitUtil.newInstance()//获取ApiStore对象
                    //调用此方法会放回一个Flowable也就是RxJava数据发射源(被观察者)
                    .getCollections(id, token, uid, page, Constants.NEW_EVERY_PAGE_SIZE, sort);
        } else {
            flowable = RetrofitUtil.newInstance().getHistory(id, token, id, page, Constants.NEW_EVERY_PAGE_SIZE, sort);
        }
        //View的这个addSubscription的方法是通用的
        //每次在进行一个请求的时候都要把这个请求添加进一个队列
        //目的是在View(Activity)被摧毁的时候统一取消掉所有的网络请求
        mView.addSubscription(
                //RxJava数据发射源(被观察者)
                flowable
                        //设置被观察者在哪一个线程执行
                        .subscribeOn(Schedulers.io())
                        //设置数据接受者(订阅者)在哪里一线程执行
                        .observeOn(AndroidSchedulers.mainThread())
                        //把Flowable被观察者与订阅者进行连接
                        //最后会返回一个Disposable对象
                        //Disposable是一个事件对象(可以看成一个请求)
                        //把Disposable添加到事件队列中方便一次性取消
                        .subscribeWith(new DisposableSubscriber<ListNewResult>() {
                            //此方法会在被观察者开始时执行
                            //从RxJava2.0开始必须在onStart方法里面调用request方法
                            //目的是通知被观察者自己接收到了消息
                            @Override
                            protected void onStart() {
                                request(1);
                            }

                            //此方法会在被观察者代码执行完毕后执行(如果出现异常则不会执行)
                            //所以要在下面onError里面调用onComplete
                            @Override
                            public void onComplete() {
                                //调用View(Activity)中的请求结束方法
                                //一般是用来通知Activity关闭加载动画
                                mView.onGetNewsEnd();
                            }

                            //此方法会在被观察者代码发生异常时执行
                            //此时不会调用onComplete方法,所以在这里手动调用
                            @Override
                            public void onError(Throwable e) {
                                e.printStackTrace();
                                //手动调用onComplete
                                onComplete();
                                //调用View(Activity)的获取信息错误方法
                                //具体需要的参数看方法规定的传递
                                mView.onGetNewsError(mContext.getString(R.string.internet_error), page);
                            }

                            //在被观察者调用onNext的时候会被调用
                            //在Retrofit框架中,网络请求成功时会默认调用此方法
                            @Override
                            public void onNext(ListNewResult result) {
                                //网络请求成功会调用此方法
                                //在给Retrofit添加了Gson转换器后
                                //会自动把服务器返回的JSON字符串转换成一个对象(参数必须要对应)
                                //这里使用AndroidStudio的GsonFormat插件自动创建一个ListNewResult类
                                //如果出现返回的参数和使用GsonFormat插件创建的对象不对应
                                //则会调用onError方法 不会调用onNext方法

                                //通过服务器返回的result字段是否为success判断结果
                                if (result.getResult().equals("success")) {
                                    List<New> news = new ArrayList<>();
                                    for (int i = 0; i < result.getData().getNews().size(); i++) {
                                        ListNewResult.DataBean.NewsBean bean = result.getData().getNews().get(i);
                                        New n = new New();
                                        n.setId(bean.getId());
                                        n.setTitle(bean.getTitle());
                                        n.setCover(bean.getCover());
                                        n.setAbstractContent(bean.getAbstractContent());
                                        n.setCreateTime(bean.getCreateTime());
                                        n.setType(bean.getType());
                                        news.add(n);
                                    }
                                    //View的请求成功方法需要一个List<New>对象
                                    //这个和Gson请求下来的对象虽然不一样
                                    //但是他们里面的信息是一致的
                                    //所以自己创建一个List<New>对象
                                    //手动把Gson请求的对象转换成View中需要的对象
                                    //然后传递给View表示成功
                                    //上面的循环就是转换过程(实际上就是set get)
                                    mView.onGetNewsSuccess(news, page);
                                } else {
                                    //若result不为success则调用View的请求失败方法
                                    //并把错误信息传递给View进行显示
                                    //这个ErrorUtil.getErrorMessage方法师为了把错误码转换为错误信息
                                    //比如错误码是101,则显示参数错误
                                    mView.onGetNewsError(ErrorUtil.getErrorMessage(result.getErrorCode()), page);
                                }
                            }
                        }));
    }
}
