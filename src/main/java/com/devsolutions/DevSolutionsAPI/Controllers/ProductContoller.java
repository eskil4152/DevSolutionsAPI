package com.devsolutions.DevSolutionsAPI.Controllers;

import com.devsolutions.DevSolutionsAPI.Entities.ProductEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProductContoller {

    @GetMapping("/all")
    public ResponseEntity<ProductEntity> getAllProducts(){
        ProductEntity list = new ProductEntity();

        return ResponseEntity.ok(list);
    }
}
