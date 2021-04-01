package com.example.pricechecker;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.List;

public class ShoppingListRepository {

    private Context context;
    private PriceCheckerDatabase db;
    private RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase sqlDb) {
            super.onCreate(sqlDb);

            // If you want to keep data through app restarts,
            // comment out the following block
            PriceCheckerDatabase.databaseWriteExecutor.execute(() -> {
                // Populate the database in the background.
                // If you want to start with more words, just add them.
                ProductListDao dao = db.productListDao();
                dao.deleteAll();

                ProductItem item = new ProductItem("0001", "Banana", 100, "https://emojipedia-us.s3.dualstack.us-west-1.amazonaws.com/thumbs/120/apple/237/banana_1f34c.png");
                dao.insertAll(item);
                item = new ProductItem("0002", "Apple", 400, "https://emojipedia-us.s3.dualstack.us-west-1.amazonaws.com/thumbs/120/apple/237/red-apple_1f34e.png");
                dao.insertAll(item);
                item = new ProductItem("0003", "Sparkles", 1000, "https://emojipedia-us.s3.dualstack.us-west-1.amazonaws.com/thumbs/120/apple/237/sparkles_2728.png");
                dao.insertAll(item);
            });
        }
    };

    public ShoppingListRepository(Context context) {
        PriceCheckerDatabase db = Room.databaseBuilder(context,
                PriceCheckerDatabase.class, "database-name")
                .addCallback(sRoomDatabaseCallback)
                .build();
        this.db = db;
        this.context = context;
    }

    public LiveData<List<CartItem>> getList() {
        // TODO: Add Webservice to check backend if shopping cart has been updated.
        return db.itemDao().getAll();
    }

    public LiveData<ProductItem> getProductById(String id) {
        return db.productListDao().getProductById(id);
    }

    // You must call this on a non-UI thread or your app will throw an exception. Room ensures
    // that you're not doing any long running operations on the main thread, blocking the UI.
    public void insert(final CartItem... items) {
        PriceCheckerDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                db.itemDao().insertAll(items);
            }
        });
    }

    public void delete(CartItem item) {
        PriceCheckerDatabase.databaseWriteExecutor.execute(() -> {
            ShoppingListDao dao = db.itemDao();
            dao.delete(item);
        });
    }
}
