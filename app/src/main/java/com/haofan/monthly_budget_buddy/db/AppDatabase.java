package com.haofan.monthly_budget_buddy.db;

import android.arch.persistence.db.SupportSQLiteOpenHelper;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.DatabaseConfiguration;
import android.arch.persistence.room.InvalidationTracker;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverter;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;

import com.haofan.monthly_budget_buddy.Daos.ItemDao;
import com.haofan.monthly_budget_buddy.Daos.MonthlyStatusDao;
import com.haofan.monthly_budget_buddy.entities.DateConverter;
import com.haofan.monthly_budget_buddy.entities.Item;
import com.haofan.monthly_budget_buddy.entities.MonthlyStatus;

@Database(entities = {Item.class, MonthlyStatus.class}, version = 1)
@TypeConverters({DateConverter.class})
public abstract class AppDatabase extends RoomDatabase{

    private static AppDatabase INSTANCE;

    public abstract ItemDao itemDao();

    public abstract MonthlyStatusDao monthlyStatusDao();

    public static AppDatabase getInMemoryDatabase(Context context) {
        if (INSTANCE == null) {
            INSTANCE =
                    Room.inMemoryDatabaseBuilder(context.getApplicationContext(), AppDatabase.class)
                            .allowMainThreadQueries()
                            .build();
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }


    @Override
    protected SupportSQLiteOpenHelper createOpenHelper(DatabaseConfiguration config) {
        return null;
    }

    @Override
    protected InvalidationTracker createInvalidationTracker() {
        return null;
    }
}
