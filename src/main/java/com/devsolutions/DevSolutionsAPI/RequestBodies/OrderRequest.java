package com.devsolutions.DevSolutionsAPI.RequestBodies;

import com.devsolutions.DevSolutionsAPI.Enums.OrderStatus;
import com.devsolutions.DevSolutionsAPI.Enums.PaymentStatus;

public class OrderRequest {
    private String productId;
    private double price;
    private String notes;
    private String paymentMethod;
    private String billingAddress;
    private PaymentStatus paymentStatus;
    private OrderStatus orderStatus;

    public String getProductId() {
        return productId;
    }

    public double getPrice() {
        return price;
    }

    public String getNotes() {
        return notes;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public String getBillingAddress() {
        return billingAddress;
    }

    public PaymentStatus getPaymentStatus() {
        return paymentStatus;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }
}
