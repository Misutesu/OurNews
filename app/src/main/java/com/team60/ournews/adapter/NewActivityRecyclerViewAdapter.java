//package com.team60.ournews.adapter;
//
//import android.content.Context;
//import android.support.annotation.NonNull;
//import android.support.v7.widget.RecyclerView;
//import android.view.View;
//import android.view.ViewGroup;
//
//import com.team60.ournews.module.model.New;
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
//    private final String DIFFERENCE_STRING = "AozakiShiki";
//
//    private Context context;
//    private New n;
//    private List<String> contents;
//
//    public NewActivityRecyclerViewAdapter(@NonNull Context context, @NonNull New n) {
//        this.context = context;
//        this.n = n;
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
//        return super.getItemViewType(position);
//    }
//
//    @Override
//    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        return null;
//    }
//
//    @Override
//    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
//        if (holder instanceof TitleViewHolder) {
//
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
//        return contents == null ? 2 : contents.size() + 2;
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
//        public TitleViewHolder(View itemView) {
//            super(itemView);
//        }
//    }
//
//    public class TimeViewHolder extends RecyclerView.ViewHolder {
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
