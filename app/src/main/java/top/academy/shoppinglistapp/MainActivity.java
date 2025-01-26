package top.academy.shoppinglistapp;

import android.graphics.Canvas;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Date;

import top.academy.shoppinglistapp.adapter.ShoppingListAdapter;
import top.academy.shoppinglistapp.entity.ShoppingList;
import top.academy.shoppinglistapp.swipe.SwipeController;
import top.academy.shoppinglistapp.swipe.SwipeControllerActions;

/**
 * Главная активность приложения для управления списками покупок.
 * Эта активность отображает список списков покупок и позволяет добавлять новые списки.
 */
public class MainActivity extends AppCompatActivity {
    private RecyclerView listShoppingRecyclerView;
    private ShoppingListAdapter shoppingListAdapter;
    private MyViewModel<ShoppingList> viewModel;
    private SwipeController swipeController;

    /**
     * Метод, вызываемый при создании активности.
     *
     * @param savedInstanceState Сохраненное состояние активности.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Установка обработчика для применения отступов окон
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        listShoppingRecyclerView = findViewById(R.id.listShoppingRecyclerView);
        listShoppingRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        viewModel = new ViewModelProvider(this, new MyViewModelFactory<>(getApplication(), ShoppingList.class))
                .get(MyViewModel.class);

        viewModel.getLists().observe(this, shoppingLists -> {
            shoppingListAdapter = new ShoppingListAdapter(shoppingLists, this);
            listShoppingRecyclerView.setAdapter(shoppingListAdapter);
        });

        Button addShoppingListButton = findViewById(R.id.addShoppingListButton);
        addShoppingListButton.setOnClickListener((v) -> showAddShoppingListDialog());

        swipeController = new SwipeController(new SwipeControllerActions() {
            @Override
            public void onRightClicked(int position) {
                shoppingListAdapter.removeItem(position);
                shoppingListAdapter.notifyItemRemoved(position);
                shoppingListAdapter.notifyItemRangeChanged(position, shoppingListAdapter.getItemCount());
            }
        });

        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeController);
        itemTouchhelper.attachToRecyclerView(listShoppingRecyclerView);

        listShoppingRecyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
                swipeController.onDraw(c);
            }
        });
    }

    /**
     * Отображает диалоговое окно для добавления нового списка покупок.
     */
    private void showAddShoppingListDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.dialogAddShoppingListText);

        View dialogView = LayoutInflater.from(this).inflate(R.layout.add_shopping_list, null);
        builder.setView(dialogView);

        EditText titleEditText = dialogView.findViewById(R.id.titleEditText);
        EditText descriptionEditText = dialogView.findViewById(R.id.descriptionEditText);

        builder.setPositiveButton(R.string.addText, (dialog, witch) -> {
            String title = titleEditText.getText().toString();
            String description = descriptionEditText.getText().toString();

            ShoppingList shoppingList = new ShoppingList();
            shoppingList.setTitle(title);
            shoppingList.setDescription(description);
            shoppingList.setDate(new Date().toString());

            viewModel.insert(shoppingList);
        });
        builder.setNegativeButton(R.string.cancelText, (dialog, witch) -> dialog.dismiss());

        builder.show();
    }
}