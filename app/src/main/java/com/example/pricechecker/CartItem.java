package com.example.pricechecker;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class CartItem {

    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "product_name")
    public String name;

    @ColumnInfo(name = "product_cents")
    public int cents;

    @ColumnInfo(name = "product_image")
    public String imageUrl;

    public CartItem(String name, int cents, String imageUrl) {
        this.name = name;
        this.cents = cents;
        this.imageUrl = imageUrl;
    }
}
