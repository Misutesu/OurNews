package com.team60.ournews.module.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.view.View;
import android.widget.RelativeLayout;

import com.team60.ournews.R;
import com.team60.ournews.module.ui.activity.base.BaseActivity;
import com.team60.ournews.util.MyUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

public class InputActivity extends BaseActivity {

    public final static int CODE_EDIT = 105;

    @BindView(R.id.activity_input_edit)
    AppCompatEditText mInputEdit;
    @BindView(R.id.activity_input_btn)
    AppCompatButton mInputBtn;
    @BindView(R.id.activity_input_layout)
    RelativeLayout mInputLayout;

    private String text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setBackgroundDrawableResource(R.color.transparent);
        hideNavigationBar();
        setContentView(R.layout.activity_input);
        ButterKnife.bind(this);
        init(savedInstanceState);
        setListener();
    }

    @Override
    public void init(Bundle savedInstanceState) {
        text = getIntent().getStringExtra("text");
        if (text != null) {
            mInputEdit.setText(text);
        } else {
            text = "";
        }
        MyUtil.openKeyBord(this, mInputEdit);
    }

    @Override
    public void setListener() {
        mInputLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyUtil.closeKeyBord(InputActivity.this, mInputEdit);
                finish();
            }
        });

        mInputBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newText = mInputEdit.getText().toString();
                if (!newText.equals(text)) {
                    Intent intent = new Intent();
                    intent.putExtra("newText", newText);
                    setResult(CODE_EDIT, intent);
                }
                MyUtil.closeKeyBord(InputActivity.this, mInputEdit);
                finish();
            }
        });
    }

    @Override
    public void showSnackBar(String message) {

    }
}
