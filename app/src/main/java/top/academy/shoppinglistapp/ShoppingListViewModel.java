package top.academy.shoppinglistapp;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.room.Room;

import java.util.List;

import top.academy.shoppinglistapp.database.AppDatabase;
import top.academy.shoppinglistapp.entity.ShoppingList;

/**
 * ViewModel для управления списками покупок.
 * Этот класс управляет загрузкой, вставкой и удалением списков покупок из базы данных.
 */
public class ShoppingListViewModel extends AndroidViewModel {
    private final AppDatabase db;

    private final LiveData<List<ShoppingList>> shoppingLists;

    /**
     * Конструктор для ShoppingListViewModel.
     *
     * @param application Контекст приложения.
     */
    public ShoppingListViewModel(Application application) {
        super(application);

        db = Room.databaseBuilder(application.getBaseContext(), AppDatabase.class, "shopping-list-database")
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build();;
        shoppingLists = new MutableLiveData<>();
        loadShoppingList();
    }

    /**
     * Загружает все списки покупок из базы данных.
     */
    private void loadShoppingList() {
        new Thread(() ->
        {
            List<ShoppingList> list = db.shoppingListDao().getAllShoppingLists();
            ((MutableLiveData<List<ShoppingList>>) shoppingLists).postValue(list);
        }).start();
    }

    /**
     * Возвращает LiveData списка списков покупок.
     *
     * @return LiveData списка списков покупок.
     */
    public LiveData<List<ShoppingList>> getShoppingLists() {
        return shoppingLists;
    }

    /**
     * Вставляет новый список покупок в базу данных.
     *
     * @param shoppingList Список покупок для вставки.
     */
    public void insertShoppingList(ShoppingList shoppingList) {
        new Thread(() -> {
            db.shoppingListDao().insert(shoppingList);
            loadShoppingList();
        }).start();
    }

    /**
     * Удаляет список покупок из базы данных.
     *
     * @param shoppingList Список покупок для удаления.
     */
    public void deleteShoppingList(ShoppingList shoppingList) {
        new Thread(() -> {
            db.shoppingListDao().delete(shoppingList);
            loadShoppingList();
        }).start();
    }
}
