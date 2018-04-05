package com.haofan.monthly_budget_buddy.Daos;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.widget.ListView;

import com.haofan.monthly_budget_buddy.entities.Item;

import java.util.List;

@Dao
public interface ItemDao {

    @Query("SELECT * FROM item")
    List<Item> getAll();

    @Query("SELECT * FROM item WHERE iyear_month LIKE :year_month")
    List<Item> findByYearAndMonth (String year_month);

    @Insert
    void insert(Item item);

    @Delete
    void delete(Item item);


}
