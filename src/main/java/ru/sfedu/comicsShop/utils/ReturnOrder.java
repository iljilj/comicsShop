package ru.sfedu.comicsShop.utils;

public class ReturnOrder {
    private final Long orderId;
    private final Status orderStatus;

    public ReturnOrder(Long orderId, Status orderStatus) {
        this.orderId = orderId;
        this.orderStatus = orderStatus;
    }

    public Long getOrderId() {
        return orderId;
    }

    public Status getOrderStatus() {
        return orderStatus;
    }
}
