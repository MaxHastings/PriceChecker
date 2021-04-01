package com.example.pricechecker;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class ShoppingListAdapter extends RecyclerView.Adapter<ShoppingListAdapter.ViewHolder> {

    private List<CartItem> items;
    private Listener listener;

    /**
     * Initialize the dataset of the Adapter.
     *
     * @param items List<Item> containing the data to populate views to be used
     *              by RecyclerView.
     */
    public ShoppingListAdapter(List<CartItem> items, Listener listener) {
        this.items = items;
        this.listener = listener;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item, viewGroup, false);

        return new ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        CartItem item = items.get(position);
        viewHolder.getNameView().setText(item.name);
        viewHolder.getPriceView().setText(CentsFormatter.centsToString(item.cents));
        Picasso.get().load(item.imageUrl).into(viewHolder.getProductImage());
        viewHolder.view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                listener.onItemLongClick(item);
                return true;
            }
        });
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return items.size();
    }

    public interface Listener {
        void onItemLongClick(CartItem item);
    }

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView nameView;
        private final TextView priceView;
        private final ImageView productImage;

        private final View view;

        public ViewHolder(View view) {
            super(view);
            this.view = view;
            nameView = view.findViewById(R.id.productName);
            priceView = view.findViewById(R.id.productPrice);
            productImage = view.findViewById(R.id.productImage);
        }

        public ImageView getProductImage() {
            return productImage;
        }

        public TextView getNameView() {
            return nameView;
        }

        public TextView getPriceView() {
            return priceView;
        }
    }
}

