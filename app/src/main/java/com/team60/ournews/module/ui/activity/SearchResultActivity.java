package com.team60.ournews.module.ui.activity;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.transition.Explode;
import android.transition.Transition;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.team60.ournews.R;
import com.team60.ournews.common.Constants;
import com.team60.ournews.listener.MyRecyclerViewOnScrollListener;
import com.team60.ournews.listener.MyTransitionListener;
import com.team60.ournews.module.adapter.TypeFragmentRecyclerViewAdapter;
import com.team60.ournews.module.bean.New;
import com.team60.ournews.module.presenter.SearchResultPresenter;
import com.team60.ournews.module.presenter.impl.SearchResultPresenterImpl;
import com.team60.ournews.module.ui.activity.base.BaseActivity;
import com.team60.ournews.module.view.SearchResultView;
import com.team60.ournews.util.SkipUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SearchResultActivity extends BaseActivity implements SearchResultView {

    @BindView(R.id.activity_search_result_coordinator_layout)
    CoordinatorLayout mCoordinatorLayout;
    @BindView(R.id.activity_search_result_tool_bar)
    Toolbar mToolBar;
    @BindView(R.id.activity_search_result_recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.activity_search_result_no_search_text)
    TextView mNoSearchText;
    @BindView(R.id.activity_search_result_retry_btn)
    Button mRetryBtn;
    @BindView(R.id.activity_search_result_progress_bar)
    ProgressBar mProgressBar;

    private String searchStr;
    private List<New> news;

    private TypeFragmentRecyclerViewAdapter mAdapter;

    private SearchResultPresenter mPresenter;

    private int page = 1;
    private int sort = 1;
    private boolean isLoad = false;
    private boolean hasMore = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);
        ButterKnife.bind(this);
        init(savedInstanceState);
        setListener();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Explode explodeIn = new Explode();
            explodeIn.setDuration(400);
            explodeIn.addListener(new MyTransitionListener() {
                @Override
                public void onTransitionEnd(Transition transition) {
                    mPresenter.searchNews(searchStr, page, sort);
                }
            });
            getWindow().setEnterTransition(explodeIn);

            Explode explodeOut = new Explode();
            explodeOut.setDuration(400);
            getWindow().setExitTransition(explodeOut);
        }
    }

    @Override
    public void init(Bundle savedInstanceState) {
        mPresenter = new SearchResultPresenterImpl(this, this);

        searchStr = getIntent().getStringExtra("searchText");

        mToolBar.setTitle(getString(R.string.search) + ":" + searchStr);
        setSupportActionBar(mToolBar);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (news == null)
            news = new ArrayList<>();

        mAdapter = new TypeFragmentRecyclerViewAdapter(this, news);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void setListener() {
        mToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mRecyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return isLoad;
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

        mAdapter.setOnItemClickListener(new TypeFragmentRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, New n) {
                SkipUtil.startNewActivity(SearchResultActivity.this, n, view);
            }
        });
    }

    private void startLoadMore() {
        isLoad = true;
        mAdapter.setLoadMore(true);
        mPresenter.searchNews(searchStr, page + 1, sort);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.tool_bar_search) {
            startActivity(new Intent(SearchResultActivity.this, SearchActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void searchEnd() {
        isLoad = false;
        mProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void searchSuccess(List<New> news, int page) {
        if (page == 1) {
            if (news.size() == 0) {
                mNoSearchText.setVisibility(View.VISIBLE);
            } else {
                this.news.addAll(news);
//                mAdapter.notifyItemRangeInserted(0, this.news.size());
                mAdapter.notifyDataSetChanged();
                ObjectAnimator.ofFloat(mRecyclerView, "alpha", 0f, 1f).setDuration(250).start();
            }
        } else {
            if (news.size() == 0) {
                hasMore = false;
                showSnackBar(getString(R.string.no_more));
            } else {
                this.page++;
                this.news.addAll(news);
                mAdapter.notifyItemRangeInserted(this.news.size(), this.news.size() + news.size() - 1);
                if (news.size() < Constants.NEW_EVERY_PAGE_SIZE)
                    hasMore = false;
            }
        }
    }

    @Override
    public void searchError(String message) {
        if (news.size() == 0) {
            mRetryBtn.setVisibility(View.VISIBLE);
        } else {
            showSnackBar(message);
        }
    }

    @Override
    public void showSnackBar(String message) {
        Snackbar.make(mCoordinatorLayout, message, Snackbar.LENGTH_LONG).show();
    }
}
