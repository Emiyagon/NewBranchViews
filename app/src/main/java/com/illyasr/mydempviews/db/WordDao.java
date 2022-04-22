package com.illyasr.mydempviews.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

/**
 * TODO
 * Database access object
 * @author qingshilin
 * @version 1.0
 * @date 2022/4/22 15:29
 */
@Dao
public interface WordDao {
    @Insert
    void  insertWords(WordEntity... words);
    @Update
    int updateWords(WordEntity... words);
    @Delete
    void deleteWords(WordEntity...words);

    @Query("DELETE FROM WORD")
    void deleteWords();

    @Query("SELECT * FROM WORD ORDER BY ID DESC")
    List<WordEntity> getAllWords();

}
