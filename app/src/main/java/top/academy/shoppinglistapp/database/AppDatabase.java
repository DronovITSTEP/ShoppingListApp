package top.academy.shoppinglistapp.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import top.academy.shoppinglistapp.dao.ListProductDao;
import top.academy.shoppinglistapp.dao.ProductDao;
import top.academy.shoppinglistapp.dao.ProductListProductDao;
import top.academy.shoppinglistapp.entity.ListProduct;
import top.academy.shoppinglistapp.entity.Product;
import top.academy.shoppinglistapp.entity.ProductListProduct;

@Database(entities = {Product.class, ListProduct.class, ProductListProduct.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract ProductDao productDao();
    public abstract ListProductDao listProductDao();
    public abstract ProductListProductDao productListProductDao();
}
