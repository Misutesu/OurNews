//package com.team60.ournews.module.adapter;
//
//import android.content.Context;
//import android.os.Build;
//import android.support.annotation.NonNull;
//import android.support.v7.widget.RecyclerView;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//
//import com.team60.ournews.R;
//import com.team60.ournews.module.model.New;
//import com.team60.ournews.util.ThemeUtil;
//import com.team60.ournews.util.UiUtil;
//
//import org.json.JSONException;
//
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * Created by Administrator on 2017/2/23.
// */
//
//public class NewActivityRecyclerViewAdapter extends RecyclerView.Adapter {
//
//    private final int TITLE_TYPE = 0;
//    private final int TIME_TYPE = 1;
//    private final int TEXT_TYPE = 2;
//    private final int IMAGE_TYPE = 3;
//    private final int FOOTER_TYPE = 4;
//
//    private final String DIFFERENCE_STRING = "AozakiShiki";
//
//    private Context context;
//    private New n;
//    private String title;
//    private List<String> contents;
//
//    public NewActivityRecyclerViewAdapter(@NonNull Context context, @NonNull New n) {
//        this.context = context;
//        this.n = n;
//        title = n.getTitle();
//        contents = new ArrayList<>();
//    }
//
//    public void startShowNew(@NonNull RecyclerView recyclerView) {
//        try {
//            startLoadContent();
//            recyclerView.setAdapter(this);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//    }
//
//    @Override
//    public int getItemViewType(int position) {
//        if (title == null && position == 0) {
//            return TIME_TYPE;
//        } else if (title != null && position == 0) {
//            return TITLE_TYPE;
//        } else if (title != null && position == 1) {
//            return TIME_TYPE;
//        } else if (title != null && position != contents.size() + 2) {
//            return FOOTER_TYPE;
//        } else if (title == null && position != contents.size() + 1) {
//            return FOOTER_TYPE;
//        } else if (contents.get(position).startsWith(DIFFERENCE_STRING)) {
//            return IMAGE_TYPE;
//        }
//        return TEXT_TYPE;
//    }
//
//    @Override
//    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        if (viewType == TITLE_TYPE) {
//            TextView textView = new TextView(context);
//            textView.setTextColor(ThemeUtil.getColor(context.getTheme(), R.attr.textColor1));
//            textView.setTextSize(UiUtil.sp2px(context, 26));
//            textView.getPaint().setFakeBoldText(true);
//            int padding = UiUtil.dip2px(12);
//            textView.setPadding(padding, padding, padding, padding);
//            parent.addView(textView);
//            ViewGroup.LayoutParams layoutParams
//                    = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//            textView.setLayoutParams(layoutParams);
//            return new TitleViewHolder(textView);
//        } else if (viewType == TIME_TYPE) {
//            LinearLayout linearLayout = new LinearLayout(context);
//            linearLayout.setOrientation(LinearLayout.VERTICAL);
//            parent.addView(linearLayout);
//            ViewGroup.LayoutParams layoutParams
//                    = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//            linearLayout.setLayoutParams(layoutParams);
//
//            TextView textView = new TextView(context);
//            textView.setTextColor(ThemeUtil.getColor(context.getTheme(), R.attr.textColor3));
//            textView.setTextSize(UiUtil.sp2px(context, 12));
//            int padding = UiUtil.dip2px(12);
//            textView.setPadding(padding, padding, padding, padding);
//            linearLayout.addView(textView);
//            LinearLayout.LayoutParams layoutParamsText
//                    = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//            textView.setLayoutParams(layoutParamsText);
//
//            View view = new View(context);
//            view.setBackgroundColor(ThemeUtil.getColor(context.getTheme(), R.attr.viewColor));
//            linearLayout.addView(view);
//            LinearLayout.LayoutParams layoutParamsView
//                    = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//            layoutParamsView.setMargins(padding, padding, padding, padding);
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
//                layoutParamsView.setMarginStart(padding);
//                layoutParamsView.setMarginEnd(padding);
//            }
//            textView.setLayoutParams(layoutParamsView);
//            return new TimeViewHolder(LayoutInflater.from(context).inflate(0, parent, false));
//        } else if (viewType == TEXT_TYPE) {
//            return new TextViewHolder(LayoutInflater.from(context).inflate(0, parent, false));
//        } else if (viewType == IMAGE_TYPE) {
//            return new ImageViewHolder(LayoutInflater.from(context).inflate(0, parent, false));
//        }
//        return new FooterViewHolder(LayoutInflater.from(context).inflate(0, parent, false));
//    }
//
//    @Override
//    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
//        if (holder instanceof TitleViewHolder) {
//            ((TitleViewHolder) holder).mTitleText.setText(title);
//        } else if (holder instanceof TimeViewHolder) {
//
//        } else if (holder instanceof TextViewHolder) {
//
//        } else if (holder instanceof ImageViewHolder) {
//
//        } else if (holder instanceof FooterViewHolder) {
//
//        }
//    }
//
//    @Override
//    public int getItemCount() {
//        if (title == null)
//            return contents.size() + 2;
//        return contents.size() + 3;
//    }
//
//    private void startLoadContent() throws JSONException {
//        String[] tempStrings = n.getContent().split("<br/>");
//        for (String str : tempStrings) {
//            List<String> list = getPhotoString(str);
//            if (list != null) {
//                contents.addAll(list);
//            } else {
//                contents.add(str);
//            }
//        }
//    }
//
//    private List<String> getPhotoString(String str) {
//        List<String> list = null;
//        while (true) {
//            if (str.contains("<json>") && str.contains("</json>")) {
//                if (list == null)
//                    list = new ArrayList<>();
//                int length = str.indexOf("<json>");
//                if (length != 0) {
//                    list.add(str.substring(0, length - 1));
//                }
//                list.add(DIFFERENCE_STRING + str.substring(str.indexOf("<json>") + 6, str.indexOf("</json>")));
//                str = str.substring(str.indexOf("</json>") + 7, str.length());
//            } else {
//                if (list != null) {
//                    list.add(str);
//                }
//                break;
//            }
//        }
//        return list;
//    }
//
//    public class TitleViewHolder extends RecyclerView.ViewHolder {
//
//        private TextView mTitleText;
//
//        public TitleViewHolder(View itemView) {
//            super(itemView);
//            mTitleText = (TextView) itemView;
//        }
//    }
//
//    public class TimeViewHolder extends RecyclerView.ViewHolder {
//
//        private TextView mTimeText;
//
//        public TimeViewHolder(View itemView) {
//            super(itemView);
//        }
//    }
//
//    public class TextViewHolder extends RecyclerView.ViewHolder {
//
//        public TextViewHolder(View itemView) {
//            super(itemView);
//        }
//    }
//
//    public class ImageViewHolder extends RecyclerView.ViewHolder {
//
//        public ImageViewHolder(View itemView) {
//            super(itemView);
//        }
//    }
//
//    public class FooterViewHolder extends RecyclerView.ViewHolder {
//
//        public FooterViewHolder(View itemView) {
//            super(itemView);
//        }
//    }
//}
