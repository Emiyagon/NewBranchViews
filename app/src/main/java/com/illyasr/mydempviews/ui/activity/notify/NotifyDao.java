package com.illyasr.mydempviews.ui.activity.notify;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.illyasr.mydempviews.db.WordEntity;

import java.util.List;
@Dao
public interface NotifyDao {
    @Insert
    void  insertWords(NotifyBean... words);
    @Update
    int updateWords(NotifyBean... words);
    @Delete
    void deleteWords(NotifyBean...words);

    @Query("DELETE FROM CLOCK")
    void deleteWords();

    @Query("SELECT * FROM CLOCK ORDER BY ID DESC")
    List<NotifyBean> getAllWords();
}
