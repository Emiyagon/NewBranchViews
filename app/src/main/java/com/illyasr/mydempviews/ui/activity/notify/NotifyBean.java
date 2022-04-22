package com.illyasr.mydempviews.ui.activity.notify;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.illyasr.mydempviews.R;
import com.illyasr.mydempviews.service.MyNetPics;

import java.io.Serializable;
import java.util.Date;

/**
 * TODO
 * 纪念日实体类
 * @author qingshilin
 * @version 1.0
 * @date 2022/4/22 10:35
 */
@Entity(tableName = "CLOCK")
public class NotifyBean implements Serializable {
    @PrimaryKey(autoGenerate = true)
    private int id;//自增id
    @ColumnInfo(name = "clock_name")
    private String name="靑シ菻";
    @ColumnInfo(name = "clock_now")
    private long nowDate=new Date().getTime();
    @ColumnInfo(name = "clock_end")
    private long EndDate;
    @ColumnInfo(name = "clock_extra")
    private String extra="";
    @ColumnInfo(name = "clock_title")
    private String title="";
    @ColumnInfo(name = "clock_pic")
    private Object picBg = MyNetPics.doubleMi;



    public NotifyBean( String title, long nowDate, long endDate) {
        this.nowDate = nowDate;
        EndDate = endDate;
        this.title = title;
    }

    public NotifyBean( String title, long nowDate, long endDate, String extra) {
        this.nowDate = nowDate;
        EndDate = endDate;
        this.title = title;
        this.extra = extra;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getNowDate() {
//        nowDate=new Date().getTime();
        return nowDate;
    }


    public void setNowDate(long now) {
        if (now<=0){
            nowDate=new Date().getTime();
        }else {
            this.nowDate = now;
        }
    }

    public long getEndDate() {
        return EndDate;
    }

    public void setEndDate(long endDate) {
        EndDate = endDate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }
}
