package top.academy.shoppinglistapp;

import android.app.Application;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class MyViewModelFactory<T> implements ViewModelProvider.Factory {
    private Application application;
    private Class<T> entityClass;

    public MyViewModelFactory(Application application, Class<T> entityClass) {
        this.application = application;
        this.entityClass = entityClass;
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        if (modelClass.isAssignableFrom(MyViewModel.class)) {
            return (T) new MyViewModel<>(application, entityClass);
        }
        throw new IllegalArgumentException("unknow ViewModel class");
    }
}
