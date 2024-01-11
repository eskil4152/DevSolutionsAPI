package com.devsolutions.DevSolutionsAPI.RequestBodies;

import com.devsolutions.DevSolutionsAPI.Entities.BillingInfo;

public class OrderRequest {
    private String productId;
    private double price;
    private String notes;
    private BillingInfo billingInfo;

    public String getProductId() {
        return productId;
    }

    public double getPrice() {
        return price;
    }

    public String getNotes() {
        return notes;
    }

    public BillingInfo getBillingInfo() {
        return billingInfo;
    }
}
