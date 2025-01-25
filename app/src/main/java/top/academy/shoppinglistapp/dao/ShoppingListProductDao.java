package top.academy.shoppinglistapp.dao;

import androidx.room.Dao;
import androidx.room.Insert;

import top.academy.shoppinglistapp.entity.ShoppingListProduct;

@Dao
public interface ShoppingListProductDao {
    @Insert
    void insert(ShoppingListProduct shoppingListProduct);
}
