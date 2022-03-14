package com.illyasr.mydempviews.bean;

public class TabBean {

    private int clickId;

    private Object picId;// 可以是图片资源,可以是bitmap,可以是pic网址

    private String name;

    private int textColor;

    public TabBean() {
    }

    public TabBean(String name,int clickId ) {
        this.clickId = clickId;
        this.name = name;
    }

    public TabBean(String name) {
        this.name = name;
    }

    public TabBean(int clickId, Object picId, String name) {
        this.clickId = clickId;
        this.picId = picId;
        this.name = name;
    }

    public TabBean(Object picId, String name) {
        this.picId = picId;
        this.name = name;
    }

    public int getTextColor() {
        return textColor;
    }

    public TabBean setTextColor(int textColor) {
        this.textColor = textColor;
        return this;

    }

    public int getClickId() {
        return clickId;
    }

    public TabBean setClickId(int clickId) {
        this.clickId = clickId;
        return this;
    }

    public Object getPicId() {
        return picId;
    }

    public TabBean setPicId(Object picId) {
        this.picId = picId;
        return this;
    }

    public String getName() {
        return name;
    }

    public TabBean setName(String name) {
        this.name = name;
        return this;
    }
}
