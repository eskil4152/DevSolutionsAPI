package com.devsolutions.DevSolutionsAPI.Responses;

import com.devsolutions.DevSolutionsAPI.Entities.ProductEntity;

public class ProductResponse {
    public ProductEntity[] products;

    public ProductResponse(ProductEntity[] products) {
        this.products = products;
    }
}
