package com.devsolutions.DevSolutionsAPI.RequestBodies;

import com.devsolutions.DevSolutionsAPI.Enums.OrderStatus;
import com.devsolutions.DevSolutionsAPI.Enums.PaymentStatus;

public class OrderRequest {
    private Long orderId;
    private String productId;
    private double price;
    private String notes;
    private String paymentMethod;
    private String billingAddress;
    private PaymentStatus paymentStatus;

    private OrderStatus orderStatus;

    private String developerFeedback;

    public Long getOrderId() {
        return orderId;
    }

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

    public String getDeveloperFeedback() {
        return developerFeedback;
    }

    public PaymentStatus getPaymentStatus() {
        return paymentStatus;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

}
