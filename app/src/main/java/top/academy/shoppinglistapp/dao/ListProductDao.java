package top.academy.shoppinglistapp.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import top.academy.shoppinglistapp.entity.ListProduct;

@Dao
public interface ListProductDao {
    @Insert
    void insert(ListProduct listProduct );

    @Query("select * from listProducts")
    List<ListProduct> getAllListProducts();
}
