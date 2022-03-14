package com.illyasr.mydempviews.view.roll;

/**
 * Created by bullet on 2018/8/1.
 */

public class UPMarqueeViewData {
    private String title;
    private String value;
    private String url;
    public UPMarqueeViewData(String title, String value, String url) {
        super();
        this.title = title;
        this.value = value;
        this.url = url;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }
    public String getValue() {
        return value;
    }
    public void setValue(String value) {
        this.value = value;
    }
}
