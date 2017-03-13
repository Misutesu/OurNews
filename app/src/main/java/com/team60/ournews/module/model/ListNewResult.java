package com.team60.ournews.module.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Misutesu on 2017/3/6 0006.
 */

public class ListNewResult {

    /**
     * result : success
     * errorCode : 0
     * data : {"news":[{"id":98,"title":"富坚义博复刊并非无的放矢 全职猎人能否及时救场？","cover":"gamersky_01small_02_201631517457E9.jpg","abstract":"富坚义博复刊并非无的放矢 全职猎人能否及时救场？","createTime":"2017-03-06 00:13:18","type":"1"},{"id":97,"title":"2016年日本动画界风雨飘摇 业界要完不是危言耸听","cover":"gamersky_06small_12_2017191451AB0.jpg","abstract":"2016年日本动画界风雨飘摇 业界要完不是危言耸听","createTime":"2017-03-06 00:13:18","type":"1"},{"id":96,"title":"《海贼王》文斯莫克家族分析 山治竟是光属性化身？","cover":"a12tg3eqwfqwf.png","abstract":"《海贼王》文斯莫克家族分析 山治竟是光属性化身？","createTime":"2017-03-06 00:13:18","type":"1"},{"id":95,"title":"《月姬》游戏宣布重制 发售日未定","cover":"24hwg2g32.png","abstract":"《月姬》游戏宣布重制 发售日未定","createTime":"2017-03-06 00:13:18","type":"1"},{"id":94,"title":"《你的名字。》超越《千与千寻》成为全球最卖座日本电影","cover":"141823261.jpg","abstract":"《你的名字。》超越《千与千寻》成为全球最卖座日本电影","createTime":"2017-03-06 00:13:18","type":"1"},{"id":26,"title":"型月社大作「魔法使之夜」今日发售 官方倒计时图欣赏","cover":"128200449326.jpg","abstract":"型月社大作「魔法使之夜」今日发售 官方倒计时图欣赏","createTime":"2017-03-06 00:13:18","type":"1"}]}
     */

    private String result;
    @SerializedName("error_code")
    private int errorCode;
    private DataBean data;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        private List<NewsBean> news;

        public List<NewsBean> getNews() {
            return news;
        }

        public void setNews(List<NewsBean> news) {
            this.news = news;
        }

        public static class NewsBean {
            /**
             * id : 98
             * title : 富坚义博复刊并非无的放矢 全职猎人能否及时救场？
             * cover : gamersky_01small_02_201631517457E9.jpg
             * abstract : 富坚义博复刊并非无的放矢 全职猎人能否及时救场？
             * createTime : 2017-03-06 00:13:18
             * type : 1
             */

            private int id;
            private String title;
            private String cover;
            @SerializedName("abstract")
            private String abstractContent;
            @SerializedName("create_time")
            private String createTime;
            private int type;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getCover() {
                return cover;
            }

            public void setCover(String cover) {
                this.cover = cover;
            }

            public String getAbstractContent() {
                return abstractContent;
            }

            public void setAbstractContent(String abstractContent) {
                this.abstractContent = abstractContent;
            }

            public String getCreateTime() {
                return createTime;
            }

            public void setCreateTime(String createTime) {
                this.createTime = createTime;
            }

            public int getType() {
                return type;
            }

            public void setType(int type) {
                this.type = type;
            }
        }
    }
}
