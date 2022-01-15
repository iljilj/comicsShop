package ru.sfedu.comicsShop.model;

import com.opencsv.bean.CsvBindByName;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import java.util.Objects;

@Root
public class GiftCertificate extends DiscountCode{
    @Element
    @CsvBindByName(column = "userId")
    private long userId;
    @Element
    @CsvBindByName(column = "discountTotal")
    private float discountTotal;

    public GiftCertificate() {
    }

    public GiftCertificate(long id, String name, Boolean currentlyAvailable, long userId, float discountTotal) {
        super(id, name, currentlyAvailable);
        this.userId = userId;
        this.discountTotal = discountTotal;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public float getDiscountTotal() {
        return discountTotal;
    }

    public void setDiscountTotal(float discountTotal) {
        this.discountTotal = discountTotal;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        GiftCertificate that = (GiftCertificate) o;
        return userId == that.userId && Float.compare(that.discountTotal, discountTotal) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), userId, discountTotal);
    }

    @Override
    public String toString() {
        return "GiftCertificate{" +
                "id=" + this.getId() +
                ", name=" + this.getName() +
                ", currentlyAvailable=" + this.getCurrentlyAvailable() +
                ", userId=" + userId +
                ", discountTotal=" + discountTotal +
                '}';
    }
}
