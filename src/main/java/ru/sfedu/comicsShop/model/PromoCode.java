package ru.sfedu.comicsShop.model;

import com.opencsv.bean.CsvBindByName;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import java.util.Objects;

@Root
public class PromoCode extends DiscountCode{
    @Element
    @CsvBindByName(column = "minTotalPrice")
    private float minTotalPrice;
    @Element
    @CsvBindByName(column = "discountPercent")
    private long discountPercent;

    public PromoCode() {
    }

    public PromoCode(long id, String name, Boolean currentlyAvailable, float minTotalPrice, long discountPercent) {
        super(id, name, currentlyAvailable);
        this.minTotalPrice = minTotalPrice;
        this.discountPercent = discountPercent;
    }

    public float getMinTotalPrice() {
        return minTotalPrice;
    }

    public void setMinTotalPrice(float minTotalPrice) {
        this.minTotalPrice = minTotalPrice;
    }

    public long getDiscountPercent() {
        return discountPercent;
    }

    public void setDiscountPercent(long discountPercent) {
        this.discountPercent = discountPercent;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        PromoCode promoCode = (PromoCode) o;
        return Float.compare(promoCode.minTotalPrice, minTotalPrice) == 0 && discountPercent == promoCode.discountPercent;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), minTotalPrice, discountPercent);
    }

    @Override
    public String toString() {
        return "PromoCode{" +
                "id=" + this.getId() +
                ", name=" + this.getName() +
                ", currentlyAvailable=" + this.getCurrentlyAvailable() +
                ", minTotalPrice=" + minTotalPrice +
                ", discountPercent=" + discountPercent +
                '}';
    }
}
