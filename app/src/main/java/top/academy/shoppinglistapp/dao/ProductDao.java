package top.academy.shoppinglistapp.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import top.academy.shoppinglistapp.entity.Product;

@Dao
public interface ProductDao {
    @Insert
    void insert(Product product);

    @Delete
    void delete(Product product);

    @Query("select * from products")
    List<Product> getAllProducts();
}
