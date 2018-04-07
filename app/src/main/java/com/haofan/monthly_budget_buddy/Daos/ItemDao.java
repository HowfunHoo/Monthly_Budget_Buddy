package com.haofan.monthly_budget_buddy.Daos;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.widget.ListView;

import com.haofan.monthly_budget_buddy.entities.Item;

import java.util.List;

@Dao
public interface ItemDao {

    @Query("SELECT * FROM item ORDER BY idate DESC")
    LiveData<List<Item>> getAll();

    @Query("SELECT * FROM item WHERE iyear_month LIKE :year_month")
    LiveData<List<Item>> findByYearAndMonth (String year_month);

    @Query("SELECT * FROM item WHERE iclass LIKE 'expense'")
    LiveData<List<Item>> getAllExpenses ();

    @Query("SELECT * FROM item WHERE iclass LIKE 'revenue'")
    LiveData<List<Item>> getAllRevenues ();




    @Insert
    void insert(Item item);

    @Delete
    void delete(Item item);


}