package com.haofan.monthly_budget_buddy.Daos;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;
import android.widget.ListView;

import com.haofan.monthly_budget_buddy.entities.Item;

import java.util.List;

@Dao
public interface ItemDao {

    @Query("SELECT * FROM item ORDER BY idate DESC")
    LiveData<List<Item>> getAll();

    @Query("SELECT * FROM item WHERE iid LIKE :id")
    LiveData<Item> findById(int id);

    @Query("SELECT * FROM item WHERE iyear_month LIKE :year_month")
    LiveData<List<Item>> findByYearAndMonth (String year_month);

    @Query("SELECT * FROM item WHERE iclass LIKE 'expense' AND iyear_month LIKE :year_month")
    LiveData<List<Item>> getAllExpenses (String year_month);

    @Query("SELECT * FROM item WHERE iclass LIKE 'revenue' AND iyear_month LIKE :year_month")
    LiveData<List<Item>> getAllRevenues (String year_month);

    @Query("DELETE FROM item WHERE iid = :id")
    void deleteById(int id);

    @Insert
    void insert(Item item);

    @Delete
    void delete(Item item);

    @Update
    void update(Item item);

}
