package com.example.pricechecker;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class ProductItem {

    @PrimaryKey
    @NonNull
    public String id;

    @ColumnInfo(name = "product_name")
    public String name;

    @ColumnInfo(name = "product_cents")
    public int cents;

    @ColumnInfo(name = "product_image")
    public String imageUrl;

    public ProductItem(String id, String name, int cents, String imageUrl) {
        this.id = id;
        this.name = name;
        this.cents = cents;
        this.imageUrl = imageUrl;
    }
}
