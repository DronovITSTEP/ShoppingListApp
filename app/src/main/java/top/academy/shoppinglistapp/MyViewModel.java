package top.academy.shoppinglistapp;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.room.Room;

import java.util.List;

import top.academy.shoppinglistapp.database.AppDatabase;
import top.academy.shoppinglistapp.entity.Product;
import top.academy.shoppinglistapp.entity.ShoppingList;

/**
 * ViewModel для управления списками покупок.
 * Этот класс управляет загрузкой, вставкой и удалением списков покупок из базы данных.
 */
public class MyViewModel<T> extends AndroidViewModel {
    private final AppDatabase db;

    private final LiveData<List<T>> lists;
    private Class<T> entityClass;

    /**
     * Конструктор для MyViewModel.
     *
     * @param application Контекст приложения.
     */
    public MyViewModel(Application application, Class<T> entityClass) {
        super(application);

        db = Room.databaseBuilder(application.getBaseContext(), AppDatabase.class, "shopping-list-database")
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build();
        this.entityClass = entityClass;
        lists = new MutableLiveData<>();
        loadList();
    }

    /**
     * Загружает все списки покупок из базы данных.
     */
    private void loadList() {
        new Thread(() ->
        {
            List<T> list = null;
            if (entityClass == ShoppingList.class) {
                list = (List<T>) db.shoppingListDao().getAllShoppingLists();
            }
            else if(entityClass == Product.class) {
                list = (List<T>) db.productsDao().getAllProducts();
            }
            ((MutableLiveData<List<T>>) lists).postValue(list);
        }).start();
    }

    /**
     * Возвращает LiveData списка списков покупок.
     *
     * @return LiveData списка списков покупок.
     */
    public LiveData<List<T>> getLists() {
        return lists;
    }

    /**
     * Вставляет новый список покупок в базу данных.
     *
     * @param entity Список покупок для вставки.
     */
    public void insert(T entity) {
        new Thread(() -> {
            if (entityClass == ShoppingList.class) {
                db.shoppingListDao().insert((ShoppingList) entity);
            }
            else if(entityClass == Product.class) {
                db.productsDao().insert((Product) entity);
            }

            loadList();
        }).start();
    }


}
