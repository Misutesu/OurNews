package com.team60.ournews.module.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Misutesu on 2017/3/6 0006.
 */

public class ContentResult {

    /**
     * result : success
     * errorCode : 0
     * data : {"id":"95","content":"近日有日本网友发布了《TYPE\u2010MOON ACE VOL.10》上的部分内容，其中提到了新生《月姬》(月姬Remake)正在锐意制作中的消息。此外，目前公开了最新的视觉图与人设，舞台为从首都近郊到都内。另外这次还将追加女性教师这个新角色，并将之前没有登场的角色进行再设定。<json>{\"name\":\"121640271.jpg\",\"width\":\"600\",\"height\":\"450\"}<\/json>在谈到关于\u201c新生\u201d的意思是，武内表示本作叫Remake是为了让玩家在一次体验到「月姬」这款游戏。不过在考虑该如何进行的过程中，产生了\u201c新生\u201d这个感念的结果。<json>{\"name\":\"121640273.jpg\",\"width\":\"239\",\"height\":\"272\"}<\/json>另外，新角色、舞台、背景，爱尔奎特与希耶尔的新形象也有详细的介绍。<br/>不过，这次虽然公开了很多信息，但最为关键的发售日并没有公开。而网友们对发售的时间也并没有抱有很高的期待。还有人说即使公布了发售日期，还有\u201c延期发售\u201d这个神器在手。另外还有人直接呼唤诺艾尔登场\u2026\u2026<json>{\"name\":\"121640274.jpg\",\"width\":\"480\",\"height\":\"854\"}<\/json>","isCollected":-1}
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
        /**
         * id : 95
         * content : 近日有日本网友发布了《TYPE‐MOON ACE VOL.10》上的部分内容，其中提到了新生《月姬》(月姬Remake)正在锐意制作中的消息。此外，目前公开了最新的视觉图与人设，舞台为从首都近郊到都内。另外这次还将追加女性教师这个新角色，并将之前没有登场的角色进行再设定。<json>{"name":"121640271.jpg","width":"600","height":"450"}</json>在谈到关于“新生”的意思是，武内表示本作叫Remake是为了让玩家在一次体验到「月姬」这款游戏。不过在考虑该如何进行的过程中，产生了“新生”这个感念的结果。<json>{"name":"121640273.jpg","width":"239","height":"272"}</json>另外，新角色、舞台、背景，爱尔奎特与希耶尔的新形象也有详细的介绍。<br/>不过，这次虽然公开了很多信息，但最为关键的发售日并没有公开。而网友们对发售的时间也并没有抱有很高的期待。还有人说即使公布了发售日期，还有“延期发售”这个神器在手。另外还有人直接呼唤诺艾尔登场……<json>{"name":"121640274.jpg","width":"480","height":"854"}</json>
         * isCollected : -1
         */

        private long id;
        private String content;
        @SerializedName("is_collected")
        private int isCollected;

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public int getIsCollected() {
            return isCollected;
        }

        public void setIsCollected(int isCollected) {
            this.isCollected = isCollected;
        }
    }
}
