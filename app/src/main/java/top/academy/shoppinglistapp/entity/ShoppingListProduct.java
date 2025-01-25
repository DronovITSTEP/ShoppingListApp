package top.academy.shoppinglistapp.entity;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "shoppingListProducts",
foreignKeys = {
        @ForeignKey(entity = Product.class,
        parentColumns = "id",
        childColumns = "productId",
        onDelete = ForeignKey.CASCADE),
        @ForeignKey(entity = ShoppingList.class,
        parentColumns = "id",
        childColumns = "shoppingListId",
        onDelete = ForeignKey.CASCADE)
},
indices = {@Index("productId"), @Index("shoppingListId")})
public class ShoppingListProduct {
    @PrimaryKey(autoGenerate = true)
    private long id;
    private long productId;
    private long shoppingListId;
    private boolean isBought;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getProductId() {
        return productId;
    }

    public void setProductId(long productId) {
        this.productId = productId;
    }

    public long getShoppingListId() {
        return shoppingListId;
    }

    public void setShoppingListId(long shoppingListId) {
        this.shoppingListId = shoppingListId;
    }

    public boolean isBought() {
        return isBought;
    }

    public void setBought(boolean bought) {
        isBought = bought;
    }
}
