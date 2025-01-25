package top.academy.shoppinglistapp.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import top.academy.shoppinglistapp.entity.ShoppingList;

@Dao
public interface ShoppingListDao {
    @Insert
    void insert(ShoppingList shoppingList);

    @Query("select * from shoppingLists")
    List<ShoppingList> getAllShoppingLists();

    @Delete
    void delete(ShoppingList shoppingList);
}
