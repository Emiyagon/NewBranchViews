package com.illyasr.mydempviews.db;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * TODO
 * 数据库类
 * @author qingshilin
 * @version 1.0
 * @date 2022/4/22 15:21
 */
@Entity(tableName = "WORD")
public class WordEntity {
    @PrimaryKey(autoGenerate = true)
    private int id;//自增id


    // 下方都是这个类里面的变量
    @ColumnInfo(name = "english_word")
    private String word;

    @ColumnInfo(name = "chinese_mearning")
    private String chineseMeaning;

    public WordEntity(String word, String chineseMeaning) {
        this.word = word;
        this.chineseMeaning = chineseMeaning;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getChineseMeaning() {
        return chineseMeaning;
    }

    public void setChineseMeaning(String chineseMeaning) {
        this.chineseMeaning = chineseMeaning;
    }
}
