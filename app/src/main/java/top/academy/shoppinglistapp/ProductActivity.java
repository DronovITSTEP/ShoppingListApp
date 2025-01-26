package top.academy.shoppinglistapp;

import android.graphics.Canvas;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

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

import top.academy.shoppinglistapp.adapter.ProductAdapter;
import top.academy.shoppinglistapp.entity.Product;
import top.academy.shoppinglistapp.swipe.SwipeController;
import top.academy.shoppinglistapp.swipe.SwipeControllerActions;

public class ProductActivity extends AppCompatActivity {

    private RecyclerView listProductRecyclerView;
    private ProductAdapter productAdapter;
    private SwipeController swipeController;
    private MyViewModel<Product> viewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_product);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.products), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        listProductRecyclerView = findViewById(R.id.listProductRecyclerView);
        listProductRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        viewModel = new ViewModelProvider(this, new MyViewModelFactory<>(getApplication(), Product.class))
                .get(MyViewModel.class);
        viewModel.getLists().observe(this, products -> {
            productAdapter = new ProductAdapter(products, this);
            listProductRecyclerView.setAdapter(productAdapter);
        });

        Button addProductButton = findViewById(R.id.addProductButton);
        addProductButton.setOnClickListener((v) -> showAddShoppingListDialog());

        swipeController = new SwipeController(new SwipeControllerActions() {
            @Override
            public void onRightClicked(int position) {
                productAdapter.removeItem(position);
                productAdapter.notifyItemRemoved(position);
                productAdapter.notifyItemRangeChanged(position, productAdapter.getItemCount());
            }
        });

        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeController);
        itemTouchhelper.attachToRecyclerView(listProductRecyclerView);

        listProductRecyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
                swipeController.onDraw(c);
            }
        });
    }

    private void showAddShoppingListDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.dialogAddShoppingListText);

        View dialogView = LayoutInflater.from(this).inflate(R.layout.add_product, null);
        builder.setView(dialogView);

        AutoCompleteTextView titleAutoComplete = dialogView.findViewById(R.id.titleAutoComplete);

        builder.setPositiveButton(R.string.addText, (dialog, witch) -> {
            String title = titleAutoComplete.getText().toString();

            Product product = new Product();
            product.setName(title);

            viewModel.insert(product);
        });
        builder.setNegativeButton(R.string.cancelText, (dialog, witch) -> dialog.dismiss());

        builder.show();
    }
}