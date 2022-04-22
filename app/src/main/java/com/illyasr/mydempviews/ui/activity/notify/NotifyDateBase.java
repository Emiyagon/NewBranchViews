package com.illyasr.mydempviews.ui.activity.notify;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;

import com.illyasr.mydempviews.db.WordEntity;

/**
 * TODO
 *
 * @author qingshilin
 * @version 1.0
 * @date 2022/4/22 15:42
 */
@Database(entities = {NotifyBean.class},version = 1, exportSchema = false)
public abstract class NotifyDateBase extends RoomDatabase {
    public abstract NotifyDao getNotifyDao();

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
