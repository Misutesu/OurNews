package com.team60.ournews.module.ui.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.team60.ournews.R;
import com.team60.ournews.module.adapter.SearchActivityRecyclerViewAdapter;
import com.team60.ournews.common.Constants;
import com.team60.ournews.module.ui.activity.base.BaseActivity;
import com.team60.ournews.util.MyUtil;
import com.team60.ournews.util.UiUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SearchActivity extends BaseActivity {

    @BindView(R.id.activity_search_top_view)
    View mTopView;
    @BindView(R.id.activity_search_back_btn)
    ImageView mBackBtn;
    @BindView(R.id.activity_search_edit)
    EditText mSearchEdit;
    @BindView(R.id.activity_search_clear_btn)
    ImageView mClearBtn;
    @BindView(R.id.activity_search_search_btn)
    ImageView mSearchBtn;
    @BindView(R.id.activity_search_recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.activity_search_layout)
    LinearLayout mLayout;

    private List<String> histories;
    private SearchActivityRecyclerViewAdapter mAdapter;

    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setBackgroundDrawableResource(R.color.transparent);
        hideNavigationBar();
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);
        init(savedInstanceState);
        setListener();
    }

    @Override
    public void init(Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
            mTopView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, UiUtil.getStatusBarHeight()));

        sharedPreferences = MyUtil.getSharedPreferences(Constants.SHARED_PREFERENCES_HISTORY);

        if (histories == null)
            histories = new ArrayList<>();

        String historiesStr = sharedPreferences.getString("histories", null);
        if (!TextUtils.isEmpty(historiesStr)) {
            try {
                JSONArray jsonArray = new JSONArray(historiesStr);
                for (int i = 0; i < jsonArray.length(); i++) {
                    histories.add(jsonArray.getJSONObject(i).getString("history"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        mAdapter = new SearchActivityRecyclerViewAdapter(this, histories);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(mAdapter);

        if (histories.size() != 0) {
            mRecyclerView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void setListener() {
        mLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mClearBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSearchEdit.setText("");
            }
        });

        mSearchEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!mSearchEdit.getText().toString().equals("")) {
                    if (mClearBtn.getVisibility() == View.GONE)
                        mClearBtn.setVisibility(View.VISIBLE);
                } else {
                    if (mClearBtn.getVisibility() == View.VISIBLE)
                        mClearBtn.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mSearchEdit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH || (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                    MyUtil.closeKeyBord(mSearchEdit);
                    startSearchResultActivity(mSearchEdit.getText().toString(), true);
                    return true;
                }
                return false;
            }
        });

        mAdapter.setOnItemClickListener(new SearchActivityRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onTextItemClick(int position) {
                startSearchResultActivity(histories.get(position), false);
            }

            @Override
            public void onClearBtnClick() {
                sharedPreferences.edit().clear().apply();
                histories.clear();
                mAdapter.notifyDataSetChanged();
                mRecyclerView.setVisibility(View.GONE);
            }
        });

        mSearchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startSearchResultActivity(mSearchEdit.getText().toString(), true);
            }
        });
    }

    private void startSearchResultActivity(String searchText, boolean type) {
        if (!searchText.equals("")) {
            if (type) {
                String historiesStr = sharedPreferences.getString("histories", null);
                try {
                    JSONArray jsonArray;
                    if (TextUtils.isEmpty(historiesStr)) {
                        jsonArray = new JSONArray();
                    } else {
                        jsonArray = new JSONArray(historiesStr);
                    }
                    boolean has = false;
                    for (int i = 0; i < jsonArray.length() && !has; i++) {
                        if (jsonArray.getJSONObject(i).getString("history").equals(searchText)) {
                            has = true;
                        }
                    }
                    if (!has) {
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("history", searchText);
                        JSONArray jsonArray1 = new JSONArray();
                        jsonArray1.put(jsonObject);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            jsonArray1.put(jsonArray.getJSONObject(i));
                        }
                        sharedPreferences.edit().putString("histories", jsonArray1.toString()).apply();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    JSONArray jsonArray = new JSONArray(sharedPreferences.getString("histories", null));
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("history", searchText);
                    JSONArray jsonArray1 = new JSONArray();
                    jsonArray1.put(jsonObject);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        if (!jsonArray.getJSONObject(i).getString("history").equals(searchText))
                            jsonArray1.put(jsonArray.getJSONObject(i));
                    }
                    sharedPreferences.edit().putString("histories", jsonArray1.toString()).apply();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            Intent intent = new Intent(SearchActivity.this, SearchResultActivity.class);
            intent.putExtra("searchText", searchText);
            startActivity(intent);
            finish();
        }
    }
}
