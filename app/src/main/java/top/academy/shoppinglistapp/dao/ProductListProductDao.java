package top.academy.shoppinglistapp.dao;

import androidx.room.Dao;
import androidx.room.Insert;

import top.academy.shoppinglistapp.entity.ProductListProduct;

@Dao
public interface ProductListProductDao {
    @Insert
    void insert(ProductListProduct productListProduct);
}
