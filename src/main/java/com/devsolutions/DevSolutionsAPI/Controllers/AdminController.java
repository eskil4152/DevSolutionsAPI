package com.devsolutions.DevSolutionsAPI.Controllers;

import com.devsolutions.DevSolutionsAPI.Entities.Products;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class AdminController {
    // Orders
    @PostMapping("/admin/order/update")
    public ResponseEntity<String> updateOrder(){
        return ResponseEntity.status(501).body("NOT IMPLEMENTED");
    }

    @PostMapping("/admin/order/cancel/{id}")
    public ResponseEntity<String> cancelOrder(){
        return ResponseEntity.status(501).body("NOT IMPLEMENTED");
    }

    @GetMapping("/admin/order/all")
    public ResponseEntity<String> getAllOrders(){
        return ResponseEntity.status(501).body("NOT IMPLEMENTED");
    }

    // Products
    @PostMapping("/admin/products/new")
    public ResponseEntity<Products> addProduct(){
        return ResponseEntity.ok(new Products());
    }

    @PostMapping("/admin/products/delete/{id}")
    public ResponseEntity<String> deleteProduct(){
        return ResponseEntity.ok("");
    }

    @PutMapping("/admin/products/update/{id}")
    public ResponseEntity<Products> updateProduct(){
        return ResponseEntity.ok(new Products());
    }

    // FAQ
}
