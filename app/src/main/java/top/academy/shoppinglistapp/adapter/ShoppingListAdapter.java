package top.academy.shoppinglistapp.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import java.util.List;

import top.academy.shoppinglistapp.R;
import top.academy.shoppinglistapp.database.AppDatabase;
import top.academy.shoppinglistapp.entity.ShoppingList;

/**
 * Адаптер для отображения списка списков покупок в RecyclerView.
 * Этот адаптер управляет привязкой данных списка покупок к представлениям и удалением элементов.
 */
public class ShoppingListAdapter extends RecyclerView.Adapter<ShoppingListAdapter.ShoppingListViewHolder> {
    private List<ShoppingList> shoppingLists;
    private Context context;
    private AppDatabase db;

    /**
     * Конструктор для ShoppingListAdapter.
     *
     * @param shoppingLists Список списков покупок для отображения.
     * @param context Контекст активности или фрагмента, использующего этот адаптер.
     */
    public ShoppingListAdapter(List<ShoppingList> shoppingLists, Context context) {
        this.shoppingLists = shoppingLists;
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
    public ShoppingListAdapter.ShoppingListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_shopping_list, parent, false);
        return new ShoppingListViewHolder(view);
    }

    /**
     * Привязывает данные списка покупок к ViewHolder в указанной позиции.
     *
     * @param holder ViewHolder, который должен быть обновлен для представления содержимого элемента в данной позиции в наборе данных.
     * @param position Позиция элемента в наборе данных адаптера.
     */
    @Override
    public void onBindViewHolder(@NonNull ShoppingListAdapter.ShoppingListViewHolder holder, int position) {
        holder.shoppingNameTextView.setText(shoppingLists.get(position).getTitle());
        holder.deleteShoppingButton.setOnClickListener(v-> removeItem(position));
    }

    /**
     * Удаляет элемент списка покупок в указанной позиции из базы данных и адаптера.
     *
     * @param position Позиция элемента, который должен быть удален.
     */
    public void removeItem(int position) {
        ShoppingList shoppingList = shoppingLists.get(position);
        new Thread(() -> {
            db.shoppingListDao().delete(shoppingList);

            ((Activity)context).runOnUiThread(() -> {
                shoppingLists.remove(position);
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
        return shoppingLists.size();
    }

    /**
     * Класс ViewHolder для ShoppingListAdapter.
     * Хранит представления для каждого элемента в RecyclerView.
     */
    public static class ShoppingListViewHolder extends RecyclerView.ViewHolder{
        public TextView shoppingNameTextView;
        public Button deleteShoppingButton;

        /**
         * Конструктор для ShoppingListViewHolder.
         *
         * @param itemView Корневое представление макета элемента.
         */
        public ShoppingListViewHolder(View itemView) {
            super(itemView);
            shoppingNameTextView = itemView.findViewById(R.id.nameTextView);
            deleteShoppingButton = itemView.findViewById(R.id.deleteButton);

        }
    }
}
