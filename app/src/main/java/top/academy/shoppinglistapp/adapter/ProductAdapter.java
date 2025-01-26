package top.academy.shoppinglistapp.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import java.util.List;

import top.academy.shoppinglistapp.R;
import top.academy.shoppinglistapp.database.AppDatabase;
import top.academy.shoppinglistapp.entity.Product;

/**
 * Адаптер для отображения списка списков покупок в RecyclerView.
 * Этот адаптер управляет привязкой данных списка покупок к представлениям и удалением элементов.
 */
public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {
    private List<Product> products;
    private Context context;
    private AppDatabase db;

    /**
     * Конструктор для ShoppingListAdapter.
     *
     * @param products Список списков покупок для отображения.
     * @param context Контекст активности или фрагмента, использующего этот адаптер.
     */
    public ProductAdapter(List<Product> products, Context context) {
        this.products = products;
        this.context = context;
        db = Room.databaseBuilder(context, AppDatabase.class, "shopping-list-database")
                .allowMainThreadQueries()
                .build();
    }

    /**
     * Создает новый ViewHolder для RecyclerView.
     *
     * @param parent ViewGroup, в который будет добавлено новое представление после его привязки к позиции адаптера.
     * @param viewType Тип представления нового представления.
     * @return Новый ShoppingListViewHolder, который содержит представление заданного типа представления.
     */
    @NonNull
    @Override
    public ProductAdapter.ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product, parent, false);
        return new ProductViewHolder(view);
    }

    /**
     * Привязывает данные списка покупок к ViewHolder в указанной позиции.
     *
     * @param holder ViewHolder, который должен быть обновлен для представления содержимого элемента в данной позиции в наборе данных.
     * @param position Позиция элемента в наборе данных адаптера.
     */
    @Override
    public void onBindViewHolder(@NonNull ProductAdapter.ProductViewHolder holder, int position) {
        holder.productNameTextView.setText(products.get(position).getName());
    }

    /**
     * Удаляет элемент списка покупок в указанной позиции из базы данных и адаптера.
     *
     * @param position Позиция элемента, который должен быть удален.
     */
    public void removeItem(int position) {
        Product product = this.products.get(position);
        new Thread(() -> {
            db.productsDao().delete(product);

            ((Activity)context).runOnUiThread(() -> {
                this.products.remove(position);
                notifyItemRemoved(position);
            });
        }).start();
    }

    /**
     * Возвращает общее количество элементов в наборе данных, удерживаемом адаптером.
     *
     * @return Общее количество элементов в этом адаптере.
     */
    @Override
    public int getItemCount() {
        return products.size();
    }

    /**
     * Класс ViewHolder для ProductAdapter.
     * Хранит представления для каждого элемента в RecyclerView.
     */
    public static class ProductViewHolder extends RecyclerView.ViewHolder{
        public TextView productNameTextView;

        /**
         * Конструктор для ProductViewHolder.
         *
         * @param itemView Корневое представление макета элемента.
         */
        public ProductViewHolder(View itemView) {
            super(itemView);
            productNameTextView = itemView.findViewById(R.id.nameTextView);

        }
    }
}
