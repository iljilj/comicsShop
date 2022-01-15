package ru.sfedu.comicsShop.utils;

public class ReturnDiscount{
    private final Long discountId;
    private final float discountPrice;
    private final Status discountStatus;


    public ReturnDiscount(Long discountId, float discountPrice, Status discountStatus) {
        this.discountId = discountId;
        this.discountPrice = discountPrice;
        this.discountStatus = discountStatus;
    }

    public Long getDiscountId() {
        return discountId;
    }

    public Status getDiscountStatus() {
        return discountStatus;
    }

    public float getDiscountPrice() {
        return discountPrice;
    }
}
