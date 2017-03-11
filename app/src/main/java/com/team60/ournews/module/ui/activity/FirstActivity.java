package com.team60.ournews.module.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.team60.ournews.module.bean.New;
import com.team60.ournews.module.ui.activity.base.BaseActivity;

import org.json.JSONException;
import org.json.JSONObject;

public class FirstActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init(savedInstanceState);
    }

    @Override
    public void onBackPressed() {
    }

    @Override
    public void init(Bundle savedInstanceState) {
        String pushContent = getIntent().getStringExtra("pushContent");
        if (TextUtils.isEmpty(pushContent)) {
            startMain();
        } else {
            try {
                JSONObject jsonObject = new JSONObject(pushContent);
                switch (jsonObject.getString("type")) {
                    case "new":
                        JSONObject newJSON = jsonObject.getJSONObject("data");
                        New n = new New();
                        n.setId(newJSON.getLong("id"));
                        n.setTitle(newJSON.getString("title"));
                        n.setCover(newJSON.getString("cover"));
                        n.setAbstractContent(newJSON.getString("abstract"));
                        n.setCreateTime(newJSON.getString("create_time"));
                        n.setType(newJSON.getInt("type"));
                        Intent intent = new Intent(FirstActivity.this, NewActivity.class);
                        intent.putExtra(New.class.getName(), n);
                        startActivity(intent);
                        overridePendingTransition(0, 0);
                        break;
                    default:
                        startMain();
                }
            } catch (JSONException e) {
                e.printStackTrace();
                startMain();
            }
        }
        finish();
    }

    private void startMain() {
        startActivity(new Intent(this, MainActivity.class));
    }

    @Override
    public void setListener() {

    }
}
