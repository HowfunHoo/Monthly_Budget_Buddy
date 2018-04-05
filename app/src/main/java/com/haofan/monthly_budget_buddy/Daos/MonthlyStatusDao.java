package com.haofan.monthly_budget_buddy.Daos;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.haofan.monthly_budget_buddy.entities.MonthlyStatus;

import java.util.List;

@Dao
public interface MonthlyStatusDao {

    @Query("SELECT * FROM monthlyStatus")
    List<MonthlyStatus> getAll();

    @Insert
    void insert(MonthlyStatus monthlyStatus);

    @Delete
    void delete(MonthlyStatus monthlyStatus);
}
