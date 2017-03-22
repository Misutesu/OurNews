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
     * data : {"news":[{"id":92,"title":"电影级《巫师3：狂猎》截图 每一张都是一幅风景画","cover":"gamersky_02small_04_20171182284B9.jpg","abstract":"电影级《巫师3：狂猎》截图 每一张都是一幅风景画","createTime":"2017年3月6日 00:13","type":"2","manager":{"id":1,"nickName":"Misutesu","sex":1,"sign":"Type-Moon","birthday":19950727,"photo":"NoImage"},"commentNum":0,"historyNum":2},{"id":91,"title":"《炉石传说》超长维护没闲着 封停近万个作弊账号","cover":"gamersky_02small_04_20171181550102.jpg","abstract":"《炉石传说》超长维护没闲着 封停近万个作弊账号","createTime":"2017年3月6日 00:13","type":"2","manager":{"id":1,"nickName":"Misutesu","sex":1,"sign":"Type-Moon","birthday":19950727,"photo":"NoImage"},"commentNum":0,"historyNum":1},{"id":90,"title":"《看门狗2》推出免费试玩活动 可体验全部游戏内容","cover":"gamersky_02small_04_2017118922625.jpg","abstract":"《看门狗2》推出免费试玩活动 可体验全部游戏内容","createTime":"2017年3月6日 00:13","type":"2","manager":{"id":1,"nickName":"Misutesu","sex":1,"sign":"Type-Moon","birthday":19950727,"photo":"NoImage"},"commentNum":0,"historyNum":1},{"id":89,"title":"悲情！《魔兽世界》新N服面临关闭 口碑不好事与愿违","cover":"gamersky_01small_02_20171181015341.jpg","abstract":"《魔兽世界》新怀旧服已经开启了一段时间了，这一次是由Nostalrius和Elysium共同合作开设","createTime":"2017年3月6日 00:13","type":"2","manager":{"id":1,"nickName":"Misutesu","sex":1,"sign":"Type-Moon","birthday":19950727,"photo":"NoImage"},"commentNum":0,"historyNum":1},{"id":88,"title":"日本网友翻出了10余年前的NDS古董游戏机 竟然能直接开机玩","cover":"gamersky_01small_02_2017118916416.jpg","abstract":"日本网友翻出了10余年前的NDS古董游戏机 竟然能直接开机玩","createTime":"2017年3月6日 00:13","type":"2","manager":{"id":1,"nickName":"Misutesu","sex":1,"sign":"Type-Moon","birthday":19950727,"photo":"NoImage"},"commentNum":10,"historyNum":2}]}
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
             * id : 92
             * title : 电影级《巫师3：狂猎》截图 每一张都是一幅风景画
             * cover : gamersky_02small_04_20171182284B9.jpg
             * abstract : 电影级《巫师3：狂猎》截图 每一张都是一幅风景画
             * createTime : 2017年3月6日 00:13
             * type : 2
             * manager : {"id":1,"nickName":"Misutesu","sex":1,"sign":"Type-Moon","birthday":19950727,"photo":"NoImage"}
             * commentNum : 0
             * historyNum : 2
             */

            private int id;
            private String title;
            private String cover;
            @SerializedName("abstract")
            private String abstractContent;
            @SerializedName("create_time")
            private String createTime;
            private int type;
            private ManagerBean manager;
            @SerializedName("comment_num")
            private int commentNum;
            @SerializedName("collection_num")
            private int collectionNum;
            @SerializedName("history_num")
            private int historyNum;

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

            public ManagerBean getManager() {
                return manager;
            }

            public void setManager(ManagerBean manager) {
                this.manager = manager;
            }

            public int getCommentNum() {
                return commentNum;
            }

            public void setCommentNum(int commentNum) {
                this.commentNum = commentNum;
            }

            public int getCollectionNum() {
                return collectionNum;
            }

            public void setCollectionNum(int collectionNum) {
                this.collectionNum = collectionNum;
            }

            public int getHistoryNum() {
                return historyNum;
            }

            public void setHistoryNum(int historyNum) {
                this.historyNum = historyNum;
            }

            public static class ManagerBean {
                /**
                 * id : 1
                 * nickName : Misutesu
                 * sex : 1
                 * sign : Type-Moon
                 * birthday : 19950727
                 * photo : NoImage
                 */

                private int id;
                @SerializedName("nick_name")
                private String nickName;
                private int sex;
                private String sign;
                private int birthday;
                private String photo;

                public int getId() {
                    return id;
                }

                public void setId(int id) {
                    this.id = id;
                }

                public String getNickName() {
                    return nickName;
                }

                public void setNickName(String nickName) {
                    this.nickName = nickName;
                }

                public int getSex() {
                    return sex;
                }

                public void setSex(int sex) {
                    this.sex = sex;
                }

                public String getSign() {
                    return sign;
                }

                public void setSign(String sign) {
                    this.sign = sign;
                }

                public int getBirthday() {
                    return birthday;
                }

                public void setBirthday(int birthday) {
                    this.birthday = birthday;
                }

                public String getPhoto() {
                    return photo;
                }

                public void setPhoto(String photo) {
                    this.photo = photo;
                }
            }
        }
    }
}
