package com.devsolutions.DevSolutionsAPI.Controllers;

import com.devsolutions.DevSolutionsAPI.Entities.ProductEntity;
import com.devsolutions.DevSolutionsAPI.Services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ProductContoller {

    private final ProductService productService;

    @Autowired
    public ProductContoller(ProductService productService) {
        this.productService = productService;
    }


    @GetMapping("/api/product/all")
    public ResponseEntity<List<ProductEntity>> getAllProducts(){
        var response = productService.getAllProducts();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/api/product/{id}")
    public ResponseEntity<List<ProductEntity>> getProduct(@PathVariable long id) {
        var response = productService.getProduct(id);

        if (response.isPresent()) {
            List<ProductEntity> productList = List.of(response.get());
            return ResponseEntity.ok(productList);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
