package top.academy.shoppinglistapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import top.academy.shoppinglistapp.R;
import top.academy.shoppinglistapp.entity.ListProduct;

public class ShoppingListAdapter extends RecyclerView.Adapter<ShoppingListAdapter.ShoppingListViewHolder> {
    private List<ListProduct> listProducts;
    private OnItemClickListener listener;

    public ShoppingListAdapter(List<ListProduct> listProducts) {
        this.listProducts = listProducts;
    }

    public void setListProducts(List<ListProduct> listProducts, OnItemClickListener listener) {
        this.listProducts = listProducts;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ShoppingListAdapter.ShoppingListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_shopping_list, parent, false);
        return new ShoppingListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ShoppingListAdapter.ShoppingListViewHolder holder, int position) {
        ListProduct listProduct = listProducts.get(position);
        holder.shoppingName.setText(listProduct.getName());
        holder.deleteShoppingButton.setOnClickListener(v-> listener.onDeleteClick(listProduct));
    }

    @Override
    public int getItemCount() {
        return listProducts.size();
    }

    public static class ShoppingListViewHolder extends RecyclerView.ViewHolder{
        public TextView shoppingName;
        public Button deleteShoppingButton;

        public ShoppingListViewHolder(View itemView) {
            super(itemView);
            shoppingName = itemView.findViewById(R.id.nameTextView);
            deleteShoppingButton = itemView.findViewById(R.id.deleteButton);
        }
    }

    public interface OnItemClickListener {
        void onDeleteClick(ListProduct listProducts);
    }
}
