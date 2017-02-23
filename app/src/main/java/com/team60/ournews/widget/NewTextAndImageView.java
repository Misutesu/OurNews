package com.team60.ournews.widget;

import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.mistesu.frescoloader.FrescoLoader;
import com.mistesu.frescoloader.OnDownloadListener;
import com.team60.ournews.R;
import com.team60.ournews.util.MyUtil;
import com.team60.ournews.util.ThemeUtil;
import com.team60.ournews.util.UiUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Misutesu on 2016/12/28 0028.
 */

public class NewTextAndImageView extends LinearLayout {

    private final String DIFFERENCE_STRING = "AozakiShiki";

    private Context context;

    private String content;

    private List<String> contents;

    private OnActionListener onActionListener;

    public interface OnActionListener {
        void onPhotoLoadEnd(View view, String photoName);
    }

    public NewTextAndImageView(Context context) {
        super(context);
        init(context);
    }

    public NewTextAndImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public NewTextAndImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        this.context = context;

        setOrientation(VERTICAL);

        contents = new ArrayList<>();
    }

    public void setOnActionListener(OnActionListener onActionListener) {
        this.onActionListener = onActionListener;
    }

    public void setContent(String content) throws JSONException {
        this.content = content;
        startLoadContent();
    }

    private void startLoadContent() throws JSONException {
        String[] tempStrings = content.split("<br/>");
        for (String str : tempStrings) {
            List<String> list = getPhotoString(str);
            if (list != null) {
                contents.addAll(list);
            } else {
                contents.add(str);
            }
        }

        for (int i = 0; i < contents.size(); i++) {
            if (contents.get(i).startsWith(DIFFERENCE_STRING)) {
                final SimpleDraweeView simpleDraweeView = new SimpleDraweeView(context);
                JSONObject jsonObject = new JSONObject(contents.get(i).substring(DIFFERENCE_STRING.length(), contents.get(i).length()));
                int imgWidth = jsonObject.getInt("width");
                int imgHeight = jsonObject.optInt("height");
                final String name = jsonObject.optString("name");
                if (!(imgWidth == 0 || imgHeight == 0 || TextUtils.isEmpty(name))) {
                    int height = (UiUtil.getScreenWidth() - UiUtil.dip2px(24)) * imgHeight / imgWidth;
                    LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                        layoutParams.setMarginStart(UiUtil.dip2px(12));
                        layoutParams.setMarginEnd(UiUtil.dip2px(12));
                    }
                    if (i == 0) {
                        layoutParams.setMargins(UiUtil.dip2px(12), 0, UiUtil.dip2px(12), UiUtil.dip2px(16));
                    } else if (i == contents.size() - 1) {
                        layoutParams.setMargins(UiUtil.dip2px(12), UiUtil.dip2px(16), UiUtil.dip2px(12), UiUtil.dip2px(64));
                    } else {
                        layoutParams.setMargins(UiUtil.dip2px(12), UiUtil.dip2px(16), UiUtil.dip2px(12), UiUtil.dip2px(16));
                    }
                    simpleDraweeView.setLayoutParams(layoutParams);

                    this.addView(simpleDraweeView);

                    FrescoLoader.load(MyUtil.getPhotoUrl(name))
                            .resize(imgWidth, imgHeight)
                            .setOnDownloadListener(new OnDownloadListener() {
                                @Override
                                public void onDownloadSuccess() {
                                    simpleDraweeView.setOnClickListener(new OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            if (onActionListener != null)
                                                onActionListener.onPhotoLoadEnd(simpleDraweeView, name);
                                        }
                                    });
                                }

                                @Override
                                public void onDownloadFail() {

                                }
                            }).into(simpleDraweeView);
                }
            } else {
                TextView textView = new TextView(context);
                textView.setTextColor(ThemeUtil.getColor(context.getTheme(), R.attr.textColor2));
                textView.setTextSize(18);
                textView.setLineSpacing(0, 1.2f);
                textView.setTextIsSelectable(true);
                LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    layoutParams.setMarginStart(UiUtil.dip2px(12));
                    layoutParams.setMarginEnd(UiUtil.dip2px(12));
                }
                if (i == 0) {
                    layoutParams.setMargins(UiUtil.dip2px(12), 0, UiUtil.dip2px(12), UiUtil.dip2px(16));
                } else if (i == contents.size() - 1) {
                    layoutParams.setMargins(UiUtil.dip2px(12), UiUtil.dip2px(16), UiUtil.dip2px(12), UiUtil.dip2px(64));
                } else {
                    layoutParams.setMargins(UiUtil.dip2px(12), UiUtil.dip2px(16), UiUtil.dip2px(12), UiUtil.dip2px(16));
                }
                textView.setLayoutParams(layoutParams);

                String text = "\t\t\t\t" + contents.get(i);

                textView.setText(text);
                this.addView(textView);
            }
        }
    }

    private List<String> getPhotoString(String str) {
        List<String> list = null;
        while (true) {
            if (str.contains("<json>") && str.contains("</json>")) {
                if (list == null)
                    list = new ArrayList<>();
                int length = str.indexOf("<json>");
                if (length != 0) {
                    list.add(str.substring(0, length - 1));
                }
                list.add(DIFFERENCE_STRING + str.substring(str.indexOf("<json>") + 6, str.indexOf("</json>")));
                str = str.substring(str.indexOf("</json>") + 7, str.length());
            } else {
                if (list != null) {
                    list.add(str);
                }
                break;
            }
        }
        return list;
    }
}
