package top.academy.shoppinglistapp;

import android.graphics.Canvas;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import java.util.ArrayList;
import java.util.List;

import top.academy.shoppinglistapp.adapter.ShoppingListAdapter;
import top.academy.shoppinglistapp.database.AppDatabase;
import top.academy.shoppinglistapp.entity.ListProduct;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerShopping;
    private ShoppingListAdapter shoppingListAdapter;
    private List<ListProduct> listProducts;
    private AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        recyclerShopping = findViewById(R.id.listShoppingRecyclerView);
        recyclerShopping.setLayoutManager(new LinearLayoutManager(this));

        db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "shopping-list-database")
                .allowMainThreadQueries()
                .build();

        listProducts = new ArrayList<>();

        ListProduct lp = new ListProduct();
        lp.setName("Test product");
        lp.setDescription("Test description");
        lp.setDate("01.01.2000");
        listProducts.add(lp);

        shoppingListAdapter = new ShoppingListAdapter(listProducts);
        recyclerShopping.setAdapter(shoppingListAdapter);

        loadShoppingLists();

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                    int position = viewHolder.getAdapterPosition();
                    ListProduct lp = listProducts.get(position);

            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX,
                                    float dY, int actionState, boolean isCurrentlyActivity) {
                if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
                    View itemView = viewHolder.itemView;

                    float alpha = 1 - Math.abs(dX) / itemView.getWidth();
                    itemView.setAlpha(alpha);
                }

                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActivity);
            }
        }).attachToRecyclerView(recyclerShopping);
    }

    public void loadShoppingLists() {
        listProducts = db.listProductDao().getAllListProducts();
        shoppingListAdapter.notifyDataSetChanged();
    }
}