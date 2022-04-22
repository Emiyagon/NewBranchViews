package com.illyasr.mydempviews.db;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;

/**
 * TODO
 *
 * @author qingshilin
 * @version 1.0
 * @date 2022/4/22 15:33
 */
@Database(entities = {WordEntity.class},version = 1, exportSchema = false)
public abstract class WordDatabase extends RoomDatabase {
    public abstract WordDao getWordDao();
    @NonNull
    @Override
    protected SupportSQLiteOpenHelper createOpenHelper(DatabaseConfiguration config) {
        return null;
    }

    @NonNull
    @Override
    protected InvalidationTracker createInvalidationTracker() {
        return null;
    }

    @Override
    public void clearAllTables() {

    }
}
