package com.devsolutions.DevSolutionsAPI.Controllers;

import com.devsolutions.DevSolutionsAPI.Entities.Products;
import com.devsolutions.DevSolutionsAPI.Services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

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
    public ResponseEntity<Optional<Products>> getProduct(@PathVariable long id) {
        var response = productService.getProduct(id);

        if (response.isPresent()) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(404).body(Optional.empty());
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
