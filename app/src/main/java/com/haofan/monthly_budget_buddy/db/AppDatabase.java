package com.haofan.monthly_budget_buddy.db;

import android.arch.persistence.db.SupportSQLiteOpenHelper;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.DatabaseConfiguration;
import android.arch.persistence.room.InvalidationTracker;
import android.arch.persistence.room.RoomDatabase;

import com.haofan.monthly_budget_buddy.Daos.ItemDao;
import com.haofan.monthly_budget_buddy.Daos.MonthlyStatusDao;
import com.haofan.monthly_budget_buddy.entities.Item;
import com.haofan.monthly_budget_buddy.entities.MonthlyStatus;

@Database(entities = {Item.class, MonthlyStatus.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase{

    public abstract ItemDao itemDao();

    public abstract MonthlyStatusDao monthlyStatusDao();
    
    @Override
    protected SupportSQLiteOpenHelper createOpenHelper(DatabaseConfiguration config) {
        return null;
    }

    @Override
    protected InvalidationTracker createInvalidationTracker() {
        return null;
    }
}
