package com.example.pricechecker;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

public class ShoppingListViewModel extends AndroidViewModel {
    private MutableLiveData<List<CartItem>> items;
    private ShoppingListRepository repository;

    public ShoppingListViewModel(Application application) {
        super(application);
        repository = new ShoppingListRepository(application.getApplicationContext());
    }

    public LiveData<List<CartItem>> getItems() {
        return repository.getList();
    }

    void insert(CartItem item) {
        repository.insert(item);
    }

    void delete(CartItem item) {
        repository.delete(item);
    }

    LiveData<ProductItem> getProductById(String id) {
        return repository.getProductById(id);
    }
}
