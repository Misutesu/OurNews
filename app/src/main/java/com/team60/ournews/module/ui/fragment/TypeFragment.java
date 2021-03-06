package com.team60.ournews.module.ui.fragment;


import android.content.res.ColorStateList;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.team60.ournews.R;
import com.team60.ournews.common.Constants;
import com.team60.ournews.event.ChangeStyleEvent;
import com.team60.ournews.event.ShowSnackEvent;
import com.team60.ournews.listener.MyRecyclerViewOnScrollListener;
import com.team60.ournews.module.adapter.TypeFragmentRecyclerViewAdapter;
import com.team60.ournews.module.bean.New;
import com.team60.ournews.module.presenter.TypePresenter;
import com.team60.ournews.module.presenter.impl.TypePresenterImpl;
import com.team60.ournews.module.ui.fragment.base.BaseFragment;
import com.team60.ournews.module.view.TypeView;
import com.team60.ournews.util.SkipUtil;
import com.team60.ournews.util.ThemeUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class TypeFragment extends BaseFragment implements TypeView {

    @BindView(R.id.fragment_type_recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.fragment_type_swipe_refresh_layout)
    SwipeRefreshLayout mSwipeRefresh;
    @BindView(R.id.fragment_type_retry_btn)
    AppCompatButton mRetryBtn;

    private TypePresenter mPresenter;

    private TypeFragmentRecyclerViewAdapter mAdapter;
    private List<New> news;

    private boolean isLoad = false;
    private boolean hasMore = true;

    private boolean isViewCreated;
    private boolean isUIVisible;

    private int type;
    private int page = 1;
    private int sort = 1;

    private AlphaAnimation inAnimation = new AlphaAnimation(0, 1);
    private AlphaAnimation outAnimation = new AlphaAnimation(1, 0);

    public TypeFragment() {
    }

    public static TypeFragment newInstance(int type) {
        TypeFragment typeFragment = new TypeFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("type", type);
        typeFragment.setArguments(bundle);
        return typeFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) type = getArguments().getInt("type");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_type, container, false);
        ButterKnife.bind(this, view);
        EventBus.getDefault().register(this);
        setStatistics(true);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();
        setListener();

        isViewCreated = true;
        refreshNewList();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        isUIVisible = true;
        if (isVisibleToUser) {
            isUIVisible = true;
            refreshNewList();
        } else {
            isUIVisible = false;
        }
    }

    @Override
    public void onDestroyView() {
        EventBus.getDefault().unregister(this);
        super.onDestroyView();
    }

    @Override
    public void init() {
        inAnimation.setDuration(200);
        outAnimation.setDuration(200);

        mSwipeRefresh.setColorSchemeColors(ThemeUtil.getColor(getActivity().getTheme(), R.attr.colorPrimary));

        mPresenter = new TypePresenterImpl(getContext(), this);

        if (news == null) news = new ArrayList<>();
        mAdapter = new TypeFragmentRecyclerViewAdapter(getContext(), news);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void setListener() {
        mSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPresenter.getNewList(type, 1, sort);
            }
        });

        mAdapter.setOnItemClickListener(new TypeFragmentRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, New n) {
                SkipUtil.startNewActivity(getActivity(), n, view);
            }
        });

        mRecyclerView.addOnScrollListener(new MyRecyclerViewOnScrollListener(new MyRecyclerViewOnScrollListener.OnScrollBottomListener() {
            @Override
            public void onScrollBottom() {
                if (!isLoad && hasMore) {
                    startLoadMore();
                }
            }
        }));

        mRetryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refreshNewList();
                mRetryBtn.setVisibility(View.GONE);
                mSwipeRefresh.setEnabled(true);
            }
        });
    }

    private void startLoadMore() {
        isLoad = true;
        mAdapter.setLoadMore(true);
        mPresenter.getNewList(type, page + 1, sort);
    }

    private void refreshNewList() {
        if (isViewCreated && isUIVisible && news.size() == 0) {
            isLoad = true;
            mSwipeRefresh.setRefreshing(true);
            mPresenter.getNewList(type, page, sort);
        }
    }

    @Override
    public void getNewListEnd() {
        isLoad = false;
        if (mSwipeRefresh.isRefreshing())
            mSwipeRefresh.setRefreshing(false);
        mAdapter.setLoadMore(false);
    }

    @Override
    public void getNewListSuccess(List<New> news, int page) {
        if (page == 1) {
            if (this.news.size() != 0) {
                this.news.clear();
                mRecyclerView.startAnimation(outAnimation);
            }
            this.news.addAll(news);
            mAdapter.notifyDataSetChanged();
            mRecyclerView.startAnimation(inAnimation);

            this.page = 1;
            hasMore = this.news.size() >= Constants.NEW_EVERY_PAGE_SIZE;
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
    public void getNewListError(String message, int page) {
        if (news.size() == 0) {
            mRetryBtn.setVisibility(View.VISIBLE);
            mSwipeRefresh.setEnabled(false);
        } else {
            showSnackBar(message);
        }
    }

    @Override
    public void showSnackBar(String message) {
        EventBus.getDefault().post(new ShowSnackEvent(message));
    }

    @Subscribe(threadMode = ThreadMode.MAIN, priority = 100)
    public void onChangeStyle(ChangeStyleEvent event) {
        mSwipeRefresh.setColorSchemeColors(event.getColorPrimary()[1]);

        ThemeUtil.changeColor(event.getColorPrimary(), new ThemeUtil.OnColorChangeListener() {
            @Override
            public void onColorChange(int color) {
                ViewCompat.setBackgroundTintList(mRetryBtn, ColorStateList.valueOf(color));
            }
        });

        int childCount = mRecyclerView.getChildCount();
        for (int childIndex = 0; childIndex < childCount; childIndex++) {
            ViewGroup childView = (ViewGroup) mRecyclerView.getChildAt(childIndex);
            if (childView instanceof LinearLayout) {
                final TextView mTitleText = (TextView) childView.findViewById(R.id.item_type_new_title_text);
                final TextView mTimeText = (TextView) childView.findViewById(R.id.item_type_new_create_time_text);
                ThemeUtil.changeColor(event.getColorText1(), new ThemeUtil.OnColorChangeListener() {
                    @Override
                    public void onColorChange(int color) {
                        mTitleText.setTextColor(color);
                    }
                });
                ThemeUtil.changeColor(event.getColorText3(), new ThemeUtil.OnColorChangeListener() {
                    @Override
                    public void onColorChange(int color) {
                        mTimeText.setTextColor(color);
                    }
                });
            }
        }

        Class<RecyclerView> recyclerViewClass = RecyclerView.class;
        try {
            Field declaredField = recyclerViewClass.getDeclaredField("mRecycler");
            declaredField.setAccessible(true);
            Method declaredMethod = Class.forName(RecyclerView.Recycler.class.getName()).getDeclaredMethod("clear", (Class<?>[]) new Class[0]);
            declaredMethod.setAccessible(true);
            declaredMethod.invoke(declaredField.get(mRecyclerView), new Object[0]);
            RecyclerView.RecycledViewPool recycledViewPool = mRecyclerView.getRecycledViewPool();
            recycledViewPool.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
