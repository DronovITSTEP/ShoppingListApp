package top.academy.shoppinglistapp.dao;

import androidx.room.Dao;
import androidx.room.Insert;

import top.academy.shoppinglistapp.entity.Product;

@Dao
public interface ProductDao {
    @Insert
    void insert(Product product);
}
