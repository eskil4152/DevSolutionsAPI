package com.devsolutions.DevSolutionsAPI.Controllers;

import com.devsolutions.DevSolutionsAPI.Entities.Products;
import com.devsolutions.DevSolutionsAPI.Services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ProductController {

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }


    @GetMapping("/api/product/all")
    public ResponseEntity<List<Products>> getAllProducts(){
        return ResponseEntity.ok(productService.getAllProducts());
    }

    @GetMapping("/api/product/{id}")
    public ResponseEntity<List<Products>> getProduct(@PathVariable long id) {
        var response = productService.getProduct(id);

        if (response.isPresent()) {
            List<Products> productList = List.of(response.get());
            return ResponseEntity.ok(productList);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/api/product/new")
    public ResponseEntity<Products> addProduct(){
        return ResponseEntity.ok(new Products());
    }

    @PostMapping("/api/product/delete/{id}")
    public ResponseEntity<String> deleteProduct(){
        return ResponseEntity.ok("");
    }

    @PutMapping("/api/product/update/{id}")
    public ResponseEntity<Products> updateProduct(){
        return ResponseEntity.ok(new Products());
    }
}
