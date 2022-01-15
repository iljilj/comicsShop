package ru.sfedu.comicsShop.model;

import com.opencsv.bean.CsvBindByName;
import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import java.util.Objects;

@Root
public class Cart {
    @Attribute
    @CsvBindByName(column = "id")
    private long id;
    @Element
    @CsvBindByName(column = "userId")
    private long userId;
    @Element
    @CsvBindByName(column = "itemId")
    private long itemId;
    @Element
    @CsvBindByName(column = "itemAmount")
    private long itemAmount;

    public Cart() {
    }

    public Cart(long id, long userId, long itemId, long itemAmount) {
        this.id = id;
        this.userId = userId;
        this.itemId = itemId;
        this.itemAmount = itemAmount;
    }



    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getItemId() {
        return itemId;
    }

    public void setItemId(long itemId) {
        this.itemId = itemId;
    }

    public long getItemAmount() {
        return itemAmount;
    }

    public void setItemAmount(long itemAmount) {
        this.itemAmount = itemAmount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cart cart = (Cart) o;
        return id == cart.id && userId == cart.userId && itemId == cart.itemId && itemAmount == cart.itemAmount;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userId, itemId, itemAmount);
    }

    @Override
    public String toString() {
        return "Cart{" +
                "id=" + id +
                ", userId=" + userId +
                ", itemId=" + itemId +
                ", itemAmount=" + itemAmount +
                '}';
    }
}
