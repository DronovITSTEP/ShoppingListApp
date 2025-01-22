package top.academy.shoppinglistapp.entity;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "productListProducts",
foreignKeys = {
        @ForeignKey(entity = Product.class,
        parentColumns = "id",
        childColumns = "productId",
        onDelete = ForeignKey.CASCADE),
        @ForeignKey(entity = ListProduct.class,
        parentColumns = "id",
        childColumns = "listProductId",
        onDelete = ForeignKey.CASCADE)
},
indices = {@Index("productId"), @Index("listProductId")})
public class ProductListProduct {
    @PrimaryKey(autoGenerate = true)
    private long id;
    private long productId;
    private long listProductId;
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

    public long getListProductId() {
        return listProductId;
    }

    public void setListProductId(long listProductId) {
        this.listProductId = listProductId;
    }

    public boolean isBought() {
        return isBought;
    }

    public void setBought(boolean bought) {
        isBought = bought;
    }
}
