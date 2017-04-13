package com.team60.ournews.module.ui.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.TextView;

import com.team60.ournews.R;
import com.team60.ournews.common.Constants;
import com.team60.ournews.event.ShowSnackEvent;
import com.team60.ournews.listener.MyRecyclerViewOnScrollListener;
import com.team60.ournews.module.adapter.TypeFragmentRecyclerViewAdapter;
import com.team60.ournews.module.bean.New;
import com.team60.ournews.module.bean.OtherUser;
import com.team60.ournews.module.presenter.CenterPresenter;
import com.team60.ournews.module.presenter.impl.CenterPresenterImpl;
import com.team60.ournews.module.ui.activity.NewActivity;
import com.team60.ournews.module.ui.fragment.base.BaseFragment;
import com.team60.ournews.module.view.CenterView;
import com.team60.ournews.util.SkipUtil;
import com.team60.ournews.util.ThemeUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CenterFragment extends BaseFragment implements CenterView {

    @BindView(R.id.user_fragment_recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.user_fragment_swipe_refresh)
    SwipeRefreshLayout mSwipeRefresh;
    @BindView(R.id.fragment_center_retry_btn)
    Button mRetryBtn;
    @BindView(R.id.fragment_center_no_data_text)
    TextView mNoDataText;

    private CenterPresenter mPresenter;

    private TypeFragmentRecyclerViewAdapter mAdapter;
    private List<New> news;

    private boolean isLoad = false;
    private boolean hasMore = true;

    private int page = 1;
    private int sort = 1;

    private OtherUser mOtherUser;
    private int type;

    private boolean isCreated;
    private boolean isUIVisible;

    private AlphaAnimation inAnimation = new AlphaAnimation(0, 1);
    private AlphaAnimation outAnimation = new AlphaAnimation(1, 0);

    public CenterFragment() {
    }

    public static CenterFragment newInstance(OtherUser mOtherUser, int type) {
        CenterFragment fragment = new CenterFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("otherUser", mOtherUser);
        bundle.putInt("type", type);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            Bundle bundle = getArguments();
            mOtherUser = bundle.getParcelable("otherUser");
            type = bundle.getInt("type");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_center, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();
        setListener();
        isCreated = true;
        refreshList();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            isUIVisible = true;
            refreshList();
        } else {
            isUIVisible = false;
        }
    }

    @Override
    public void init() {
        inAnimation.setDuration(200);
        outAnimation.setDuration(200);

        mPresenter = new CenterPresenterImpl(getContext(), this);

        mSwipeRefresh.setColorSchemeColors(ThemeUtil.getColor(getActivity().getTheme(), R.attr.colorPrimary));

        if (news == null) news = new ArrayList<>();
        mAdapter = new TypeFragmentRecyclerViewAdapter(getContext(), news);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(mAdapter);

        if (type == 0) {
            mNoDataText.setText(getString(R.string.no_collection));
        } else {
            mNoDataText.setText(getString(R.string.no_history));
        }
    }

    @Override
    public void setListener() {
        mRecyclerView.addOnScrollListener(new MyRecyclerViewOnScrollListener(new MyRecyclerViewOnScrollListener.OnScrollBottomListener() {
            @Override
            public void onScrollBottom() {
                if (!isLoad && hasMore) {
                    startLoadMore();
                }
            }
        }));

        mSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                long uid;
                if (mOtherUser == null) uid = user.getId();
                else uid = mOtherUser.getId();
                mPresenter.getNewList(user.getId(), user.getToken(), uid, type, 1, sort);
            }
        });

        mAdapter.setOnItemClickListener(new TypeFragmentRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, New n) {
                if (type == 0 && mOtherUser == null) {
                    SkipUtil.startNewActivityForResult(CenterFragment.this, n, view, NewActivity.COLLECTION_CHANGE);
                } else {
                    SkipUtil.startNewActivity(getActivity(), n, view);
                }
            }
        });
    }

    private void refreshList() {
        if (isUIVisible && isCreated && news.size() == 0 && mNoDataText.getVisibility() == View.GONE) {
            mSwipeRefresh.setRefreshing(true);
            isLoad = true;
            long uid;
            if (mOtherUser == null) uid = user.getId();
            else uid = mOtherUser.getId();
            mPresenter.getNewList(user.getId(), user.getToken(), uid, type, page, sort);
        }
    }

    private void startLoadMore() {
        isLoad = true;
        mAdapter.setLoadMore(true);
        long uid;
        if (mOtherUser == null) uid = user.getId();
        else uid = mOtherUser.getId();
        mPresenter.getNewList(user.getId(), user.getToken(), uid, type, page + 1, sort);
    }

    @Override
    public void showSnackBar(String message) {
        EventBus.getDefault().post(new ShowSnackEvent(message));
    }

    @Override
    public void onGetNewsEnd() {
        isLoad = false;
        if (mSwipeRefresh.isRefreshing())
            mSwipeRefresh.setRefreshing(false);
        mAdapter.setLoadMore(false);
    }

    @Override
    public void onGetNewsSuccess(List<New> news, int page) {
        if (page == 1) {
            if (news.size() == 0) {
                mNoDataText.setVisibility(View.VISIBLE);
            } else {
                mNoDataText.setVisibility(View.GONE);
                if (this.news.size() != 0) {
                    this.news.clear();
                    mRecyclerView.startAnimation(outAnimation);
                }
                this.news.addAll(news);
                mAdapter.notifyDataSetChanged();
                mRecyclerView.startAnimation(inAnimation);

                this.page = 1;
                if (this.news.size() < Constants.NEW_EVERY_PAGE_SIZE) {
                    hasMore = false;
                } else {
                    hasMore = true;
                }
            }
        } else {
            if (news.isEmpty()) {
                hasMore = false;
                showSnackBar(getString(R.string.no_more));
            } else {
                this.page++;
                this.news.addAll(news);
                mAdapter.notifyItemRangeInserted(this.news.size(), this.news.size() + news.size() - 1);
                if (news.size() < Constants.NEW_EVERY_PAGE_SIZE) {
                    hasMore = false;
                }
            }
        }
    }

    @Override
    public void onGetNewsError(String error, int page) {
        if (page == 1 && news.size() == 0) {
            mRetryBtn.setVisibility(View.VISIBLE);
        } else {
            showSnackBar(error);
        }
    }

    @OnClick(R.id.fragment_center_retry_btn)
    public void onClick() {
        refreshList();
        mRetryBtn.setVisibility(View.GONE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == NewActivity.COLLECTION_CHANGE) {
            if (resultCode == NewActivity.COLLECTION_CHANGE) {
                if (data != null) {
                    long nid = data.getLongExtra("nid", -1);
                    if (nid != -1) {
                        boolean isFind = false;
                        int position = 0;
                        for (; position < news.size() && !isFind; position++) {
                            if (news.get(position).getId() == nid) {
                                news.remove(position);
                                mAdapter.notifyItemRemoved(position);
                                isFind = true;
                            }
                        }
                        if (isFind && news.size() == 0) {
                            mNoDataText.setVisibility(View.VISIBLE);
                        }
                    }
                }
            }
        }
    }
}
