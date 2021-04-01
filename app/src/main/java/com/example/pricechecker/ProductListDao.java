package com.example.pricechecker;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ProductListDao {
    @Query("SELECT * FROM ProductItem")
    LiveData<List<CartItem>> getAll();

    @Query("SELECT * FROM ProductItem WHERE id = :id")
    LiveData<ProductItem> getProductById(String id);

    @Insert
    void insertAll(ProductItem... items);

    @Delete
    void deleteAll(ProductItem... items);

}
