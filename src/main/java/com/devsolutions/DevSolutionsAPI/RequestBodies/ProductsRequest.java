package com.devsolutions.DevSolutionsAPI.RequestBodies;

import com.devsolutions.DevSolutionsAPI.Enums.OrderStatus;
import com.devsolutions.DevSolutionsAPI.Enums.PaymentStatus;

public class ProductsRequest {
    private Long id;
    private String productName;
    private String description;
    private Double price;

    public Long getId() {
        return id;
    }

    public String getProductName() {
        return productName;
    }

    public String getDescription() {
        return description;
    }

    public Double getPrice() {
        return price;
    }
}
