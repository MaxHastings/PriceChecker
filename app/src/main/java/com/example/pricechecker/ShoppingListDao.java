package com.example.pricechecker;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ShoppingListDao {

    @Query("SELECT * FROM CartItem")
    LiveData<List<CartItem>> getAll();

    @Insert
    void insertAll(CartItem... items);

    @Delete
    void delete(CartItem item);
}
