package top.academy.shoppinglistapp.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import top.academy.shoppinglistapp.dao.ShoppingListDao;
import top.academy.shoppinglistapp.dao.ProductDao;
import top.academy.shoppinglistapp.dao.ShoppingListProductDao;
import top.academy.shoppinglistapp.entity.ShoppingList;
import top.academy.shoppinglistapp.entity.Product;
import top.academy.shoppinglistapp.entity.ShoppingListProduct;

/**
 * База данных для приложения списка покупок.
 * Этот класс использует библиотеку Room для управления сущностями и DAO.
 */
@Database(entities = {Product.class, ShoppingList.class, ShoppingListProduct.class}, version = 2, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    /**
     * Возвращает DAO для работы с сущностью Product.
     *
     * @return DAO для работы с сущностью Product.
     */
    public abstract ProductDao productDao();

    /**
     * Возвращает DAO для работы с сущностью ShoppingList.
     *
     * @return DAO для работы с сущностью ShoppingList.
     */
    public abstract ShoppingListDao shoppingListDao();

    /**
     * Возвращает DAO для работы с сущностью ShoppingListProduct.
     *
     * @return DAO для работы с сущностью ShoppingListProduct.
     */
    public abstract ShoppingListProductDao shoppingListProduct();
}
