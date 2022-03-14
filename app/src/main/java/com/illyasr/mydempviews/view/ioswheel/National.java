package com.illyasr.mydempviews.view.ioswheel;

/**
 * Created by bullet on 2018/7/12.
 */

public class National {
/*
* "id":"13","name":"瑶族"
* */

    private int id;
    private String name;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public National(int id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public String toString() {
        return "National{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
