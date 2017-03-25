package com.team60.ournews.module.ui.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.team60.ournews.R;
import com.team60.ournews.event.ChangeStyleEvent;
import com.team60.ournews.event.ChangeViewPagerPageEvent;
import com.team60.ournews.event.ShowSnackEvent;
import com.team60.ournews.module.bean.New;
import com.team60.ournews.module.presenter.HomePresenter;
import com.team60.ournews.module.presenter.impl.HomePresenterImpl;
import com.team60.ournews.module.ui.fragment.base.BaseFragment;
import com.team60.ournews.module.view.HomeView;
import com.team60.ournews.util.SkipUtil;
import com.team60.ournews.util.ThemeUtil;
import com.team60.ournews.widget.AdvertisementView;
import com.team60.ournews.widget.BrowseView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends BaseFragment implements HomeView {

    @BindView(R.id.fragment_home_advertisement_view)
    AdvertisementView mAdvertisementView;
    @BindView(R.id.fragment_home_comic_browse_view)
    BrowseView mComicBrowseView;
    @BindView(R.id.fragment_home_game_browse_view)
    BrowseView mGameBrowseView;
    @BindView(R.id.fragment_home_society_browse_view)
    BrowseView mSocietyBrowseView;
    @BindView(R.id.fragment_home_play_browse_view)
    BrowseView mPlayBrowseView;
    @BindView(R.id.fragment_home_technology_browse_view)
    BrowseView mTechnologyBrowseView;
    @BindView(R.id.fragment_home_swipe_refresh_layout)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.fragment_home_scroll_layout)
    NestedScrollView mScrollLayout;
    @BindView(R.id.fragment_home_retry_btn)
    Button mRetryBtn;

    private HomePresenter mPresenter;

    private List<BrowseView> mBrowseViews;
    private SparseArray<List<New>> news;

    private boolean isViewCreated;
    private boolean isUIVisible;

    public HomeFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this, view);
        EventBus.getDefault().register(this);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();
        setListener();

        isViewCreated = true;
        if (news.size() == 0) {
            getData();
        } else {
            showData(false);
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            isUIVisible = true;
            getData();
        } else {
            isUIVisible = false;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mAdvertisementView.startScroll();
    }

    @Override
    public void onPause() {
        super.onPause();
        mAdvertisementView.stopScroll();
    }

    @Override
    public void onDestroyView() {
        EventBus.getDefault().unregister(this);
        super.onDestroyView();
    }

    @Override
    public void init() {
        mPresenter = new HomePresenterImpl(getContext(), this);

        mSwipeRefreshLayout.setColorSchemeColors(ThemeUtil.getColor(getActivity().getTheme(), R.attr.colorPrimary));

        if (news == null) news = new SparseArray<>();

        mBrowseViews = new ArrayList<>();
        mBrowseViews.add(mComicBrowseView);
        mBrowseViews.add(mGameBrowseView);
        mBrowseViews.add(mSocietyBrowseView);
        mBrowseViews.add(mPlayBrowseView);
        mBrowseViews.add(mTechnologyBrowseView);

        mComicBrowseView.setTypeText(getString(R.string.ACG));
        mComicBrowseView.setTypeImg(R.drawable.comic);
        mGameBrowseView.setTypeText(getString(R.string.game));
        mGameBrowseView.setTypeImg(R.drawable.game);
        mSocietyBrowseView.setTypeText(getString(R.string.sociology));
        mSocietyBrowseView.setTypeImg(R.drawable.sociology);
        mPlayBrowseView.setTypeText(getString(R.string.play));
        mPlayBrowseView.setTypeImg(R.drawable.play);
        mTechnologyBrowseView.setTypeText(getString(R.string.technology));
        mTechnologyBrowseView.setTypeImg(R.drawable.technology);
    }

    @Override
    public void setListener() {
        mScrollLayout.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollY == 0) {
                    mSwipeRefreshLayout.setEnabled(true);
                } else {
                    mSwipeRefreshLayout.setEnabled(false);
                }
            }
        });

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getHomeNews(-1);
            }
        });

        mRetryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSwipeRefreshLayout.setRefreshing(true);
                getHomeNews(-1);
                mRetryBtn.setVisibility(View.GONE);
            }
        });

        mAdvertisementView.setOnActionListener(new AdvertisementView.OnActionListener() {
            @Override
            public void onNewClick(New n, View view) {
                SkipUtil.startNewActivity(getActivity(), n, view);
            }
        });

        for (int i = 0; i < 5; i++) {
            final int position = i;
            mBrowseViews.get(i).setOnActionListener(new BrowseView.OnActionListener() {
                @Override
                public void onBtnClick() {
                    EventBus.getDefault().post(new ChangeViewPagerPageEvent(position + 1));
                }

                @Override
                public void onRefreshClick() {
                    getHomeNews(position + 1);
                }

                @Override
                public void onTwoClickTooClose(String message) {
                    showSnackBar(message);
                }

                @Override
                public void onNewClick(New n, View view) {
                    SkipUtil.startNewActivity(getActivity(), n, view);
                }
            });
        }
    }

    private void getData() {
        if (isUIVisible && isViewCreated && news.size() == 0) {
            mSwipeRefreshLayout.setRefreshing(true);
            getHomeNews(-1);
        }
    }

    private void getHomeNews(int type) {
        if (type == -1) {
            mAdvertisementView.startRefresh();
            for (BrowseView browseView : mBrowseViews) {
                browseView.startRefresh();
            }
        } else {
            mBrowseViews.get(type - 1).startLocalRefresh();
        }
        mPresenter.getHomeNews(type);
    }

    private void showData(boolean hasAnim) {
        for (int i = 0; i < 5; i++) {
            mBrowseViews.get(i).setData(this.news.get(i + 1), hasAnim);
        }
        mAdvertisementView.setData(this.news.get(6));
        if (mScrollLayout.getVisibility() == View.GONE) mScrollLayout.setVisibility(View.VISIBLE);
        if (mRetryBtn.getVisibility() == View.VISIBLE) mRetryBtn.setVisibility(View.GONE);
    }

    @Override
    public void getNewsEnd() {
        if (mSwipeRefreshLayout.isRefreshing())
            mSwipeRefreshLayout.setRefreshing(false);
        mAdvertisementView.endRefresh();
        for (BrowseView browseView : mBrowseViews) {
            browseView.endRefresh();
        }
    }

    @Override
    public void getNewsSuccess(SparseArray<List<New>> news, int type) {
        this.news = news;
        if (type == -1) {
            showData(true);
        } else {
            mBrowseViews.get(type - 1).setData(news.get(type), true);
        }
    }

    @Override
    public void getNewsError(String message) {
        if (mScrollLayout.getVisibility() == View.GONE) mRetryBtn.setVisibility(View.VISIBLE);
        showSnackBar(message);
    }

    @Override
    public void showSnackBar(String message) {
        EventBus.getDefault().post(new ShowSnackEvent(message));
    }

    @Subscribe(threadMode = ThreadMode.MAIN, priority = 100)
    public void onChangeStyle(ChangeStyleEvent event) {
        mSwipeRefreshLayout.setColorSchemeColors(event.getColorPrimary()[1]);
        ThemeUtil.changeColor(event.getColorItemBackground(), new ThemeUtil.OnColorChangeListener() {
            @Override
            public void onColorChange(int color) {
                for (BrowseView browseView : mBrowseViews) {
                    browseView.setItemBackgroundColor(color);
                }
            }
        });
        ThemeUtil.changeColor(event.getColorIcon(), new ThemeUtil.OnColorChangeListener() {
            @Override
            public void onColorChange(int color) {
                for (BrowseView browseView : mBrowseViews) {
                    browseView.setIconColor(color);
                }
            }
        });
        ThemeUtil.changeColor(event.getColorText1(), new ThemeUtil.OnColorChangeListener() {
            @Override
            public void onColorChange(int color) {
                for (BrowseView browseView : mBrowseViews) {
                    browseView.setTextColor1(color);
                }
            }
        });
        ThemeUtil.changeColor(event.getColorText3(), new ThemeUtil.OnColorChangeListener() {
            @Override
            public void onColorChange(int color) {
                for (BrowseView browseView : mBrowseViews) {
                    browseView.setTextColor3(color);
                }
            }
        });
    }
}
