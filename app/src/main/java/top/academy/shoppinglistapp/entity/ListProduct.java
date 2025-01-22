package top.academy.shoppinglistapp.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "listProducts")
public class ListProduct {
    @PrimaryKey(autoGenerate = true)
    private long id;
    private String name;
    private String description;
    private String date;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
