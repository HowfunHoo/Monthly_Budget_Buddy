package com.haofan.monthly_budget_buddy.Daos;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.haofan.monthly_budget_buddy.entities.MonthlyStatus;

import java.util.List;

@Dao
public interface MonthlyStatusDao {

    @Query("SELECT * FROM monthlyStatus")
    List<MonthlyStatus> getAll();

    @Query("SELECT * FROM monthlyStatus WHERE myear_month LIKE :year_month")
    MonthlyStatus findByYearAndMonth(String year_month);

    @Insert
    void insert(MonthlyStatus monthlyStatus);

    @Delete
    void delete(MonthlyStatus monthlyStatus);

    @Update
    void update(MonthlyStatus monthlyStatus);
}
