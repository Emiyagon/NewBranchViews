package com.illyasr.mydempviews.ui.activity.guaxiang;

/**
 * TODO
 *
 * @author qingshilin
 * @version 1.0
 * @date 2022/5/12 19:21
 */
public class Divination {
    private String divName;//卦象名
    private String divTag;//卦象解释
    private String divEle;//象
    private String divPle;//断

    public Divination() {
    }

    public Divination(String divName, String divTag, String divEle, String divPle) {
        this.divName = divName;
        this.divTag = divTag;
        this.divEle = divEle;
        this.divPle = divPle;
    }

    public String getDivName() {
        return divName;
    }

    public void setDivName(String divName) {
        this.divName = divName;
    }

    public String getDivTag() {
        return divTag;
    }

    public void setDivTag(String divTag) {
        this.divTag = divTag;
    }

    public String getDivEle() {
        return divEle;
    }

    public void setDivEle(String divEle) {
        this.divEle = divEle;
    }

    public String getDivPle() {
        return divPle;
    }

    public void setDivPle(String divPle) {
        this.divPle = divPle;
    }
}
