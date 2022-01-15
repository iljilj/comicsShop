package ru.sfedu.comicsShop.model;

import com.opencsv.bean.CsvBindByName;
import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import java.util.Objects;

@Root
public class Order {
    @Attribute
    @CsvBindByName(column = "id")
    private long id;
    @Element
    @CsvBindByName(column = "userId")
    private long userId;
    @Element
    @CsvBindByName(column = "address")
    private String address;
    @Element
    @CsvBindByName(column = "totalPrice")
    private float totalPrice;
    @Element(required = false)
    @CsvBindByName(column = "discountCodeId")
    private Long discountCodeId;
    @Element
    @CsvBindByName(column = "discountTotalPrice")
    private float discountTotalPrice;
    @Element
    @CsvBindByName(column = "itemIds")
    private String itemIds;
    @Element
    @CsvBindByName(column = "itemAmounts")
    private String itemAmounts;

    public Order() {
    }

    public Order(long id, long userId, String address, float totalPrice, Long discountCodeId, float discountTotalPrice, String itemIds, String itemAmounts) {
        this.id = id;
        this.userId = userId;
        this.address = address;
        this.totalPrice = totalPrice;
        this.discountCodeId = discountCodeId;
        this.discountTotalPrice = discountTotalPrice;
        this.itemIds = itemIds;
        this.itemAmounts = itemAmounts;
    }

    public float getDiscountTotalPrice() {
        return discountTotalPrice;
    }

    public void setDiscountTotalPrice(float discountTotalPrice) {
        this.discountTotalPrice = discountTotalPrice;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public float getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(float totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Long getDiscountCodeId() {
        return discountCodeId;
    }

    public void setDiscountCodeId(Long discountCodeId) {
        this.discountCodeId = discountCodeId;
    }

    public String getItemIds() {
        return itemIds;
    }

    public void setItemIds(String itemIds) {
        this.itemIds = itemIds;
    }

    public String getItemAmounts() {
        return itemAmounts;
    }

    public void setItemAmounts(String itemAmounts) {
        this.itemAmounts = itemAmounts;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return id == order.id && userId == order.userId && Float.compare(order.totalPrice, totalPrice) == 0 && Float.compare(order.discountTotalPrice, discountTotalPrice) == 0 && address.equals(order.address) && discountCodeId.equals(order.discountCodeId) && itemIds.equals(order.itemIds) && itemAmounts.equals(order.itemAmounts);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userId, address, totalPrice, discountCodeId, discountTotalPrice, itemIds, itemAmounts);
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", userId=" + userId +
                ", address='" + address + '\'' +
                ", totalPrice=" + totalPrice +
                ", discountCodeId=" + discountCodeId +
                ", discountTotalPrice=" + discountTotalPrice +
                ", itemIds=" + itemIds +
                ", itemAmounts=" + itemAmounts +
                '}';
    }
}
