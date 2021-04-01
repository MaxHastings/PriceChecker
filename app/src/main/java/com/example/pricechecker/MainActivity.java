package com.example.pricechecker;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity implements ShoppingListAdapter.Listener {

    private static final String[] CAMERA_PERMISSION = new String[]{Manifest.permission.CAMERA};
    private static final int CAMERA_REQUEST_CODE = 10;
    private static final int SCAN_QR_REQUEST_CODE = 420;
    protected RecyclerView mRecyclerView;
    protected ShoppingListAdapter mAdapter;
    protected RecyclerView.LayoutManager mLayoutManager;
    protected TextView totalPrice;
    protected ShoppingListViewModel model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        model = new ViewModelProvider(this).get(ShoppingListViewModel.class);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> {
            if (hasCameraPermission()) {
                enableCamera();
            } else {
                requestPermission();
            }
        });

        // BEGIN_INCLUDE(initializeRecyclerView)
        mRecyclerView = findViewById(R.id.recyclerView);
        totalPrice = findViewById(R.id.totalPrice);

        // LinearLayoutManager is used here, this will layout the elements in a similar fashion
        // to the way ListView would layout elements. The RecyclerView.LayoutManager defines how
        // elements are laid out.
        mLayoutManager = new LinearLayoutManager(this);

        mRecyclerView.setLayoutManager(mLayoutManager);

        model.getItems().observe(this, items -> {
            // update UI
            mAdapter = new ShoppingListAdapter(items, MainActivity.this);
            // Set CustomAdapter as the adapter for RecyclerView.
            mRecyclerView.setAdapter(mAdapter);
            // END_INCLUDE(initializeRecyclerView)
            int totalCents = 0;
            for (int i = 0; i < items.size(); i++) {
                totalCents += items.get(i).cents;
            }
            totalPrice.setText("Total: " + CentsFormatter.centsToString(totalCents));
        });
    }

    private boolean hasCameraPermission() {
        return ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(
                this,
                CAMERA_PERMISSION,
                CAMERA_REQUEST_CODE
        );
    }

    private void enableCamera() {
        Intent intent = new Intent(this, ScanQRActivity.class);
        startActivityForResult(intent, SCAN_QR_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        for (int i = 0; i < permissions.length; i++) {
            String permission = permissions[i];
            int result = grantResults[i];
            handlePermissionResponse(permission, result);
        }
    }

    public void handlePermissionResponse(String permission, int result) {
        if (permission.equals("android.permission.CAMERA")) {
            if (result == PackageManager.PERMISSION_GRANTED) {
                enableCamera();
            } else {
                Snackbar.make(findViewById(android.R.id.content), "Scanning items require Camera permission", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SCAN_QR_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                String productCode = data.getData().toString();
                model.getProductById(productCode).observe(this, new Observer<ProductItem>() {
                    @Override
                    public void onChanged(ProductItem productItem) {
                        String text;
                        if (productItem == null) {
                            text = "Item not found. Try again!";
                        } else {
                            text = "Added item to cart!";
                            CartItem item = new CartItem(productItem.name, productItem.cents, productItem.imageUrl);
                            model.insert(item);
                        }
                        Snackbar.make(findViewById(android.R.id.content), text, Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    }
                });
            }
        }
    }

    @Override
    public void onItemLongClick(CartItem item) {
        new RemoveItemDialog(() -> {
            model.delete(item);
        }).show(getSupportFragmentManager(), "RemoveItemDialog");
    }
}
