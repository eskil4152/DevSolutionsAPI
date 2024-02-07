package com.devsolutions.DevSolutionsAPI.Controllers.AdminControllers;

import com.devsolutions.DevSolutionsAPI.Entities.Products;
import com.devsolutions.DevSolutionsAPI.Entities.Users;
import com.devsolutions.DevSolutionsAPI.Enums.UserRole;
import com.devsolutions.DevSolutionsAPI.RequestBodies.ProductsRequest;
import com.devsolutions.DevSolutionsAPI.Services.ProductService;
import com.devsolutions.DevSolutionsAPI.Services.UserService;
import com.devsolutions.DevSolutionsAPI.Tools.CheckJwt;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/admin")
public class AdminProductsController {
    private final ProductService productService;

    private final CheckJwt checkJwt;

    @Autowired
    public AdminProductsController(UserService userService, ProductService productService){
        this.productService = productService;

        this.checkJwt = new CheckJwt(userService);
    }

    // Products
    @GetMapping("/products/all")
    public ResponseEntity<Optional<List<Products>>> getAllProductsAdmin(HttpServletRequest request){
        Optional<Users> user = checkJwt.checkJwtForUser(request);

        if (user.isEmpty() || user.get().getRole() != UserRole.ADMIN)
            return ResponseEntity.status(401).body(Optional.empty());

        return ResponseEntity.ok(Optional.ofNullable(productService.getAllProducts()));
    }

    @PostMapping("/products/new")
    public ResponseEntity<Optional<Products>> addProduct(@RequestBody ProductsRequest productsRequest, HttpServletRequest request){
        Optional<Users> user = checkJwt.checkJwtForUser(request);

        if (user.isEmpty() || user.get().getRole() != UserRole.ADMIN)
            return ResponseEntity.status(401).body(Optional.empty());

        Optional<Products> products = productService.saveProduct(productsRequest);

        if (products.isEmpty()) return ResponseEntity.status(409).body(Optional.empty());

        return ResponseEntity.ok(products);
    }

    @DeleteMapping("/products/delete/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable String id, HttpServletRequest request){
        Optional<Users> user = checkJwt.checkJwtForUser(request);

        if (user.isEmpty() || user.get().getRole() != UserRole.ADMIN)
            return ResponseEntity.status(401).body(Optional.empty());

        boolean didDelete = productService.deleteProduct(Long.parseLong(id));

        if (!didDelete)
            return ResponseEntity.notFound().build();

        return ResponseEntity.ok().build();
    }

    @PutMapping("/products/update")
    public ResponseEntity<Optional<Products>> updateProduct(@RequestBody ProductsRequest productsRequest, HttpServletRequest request){
        Optional<Users> user = checkJwt.checkJwtForUser(request);

        if (user.isEmpty() || user.get().getRole() != UserRole.ADMIN)
            return ResponseEntity.status(401).body(Optional.empty());

        Optional<Products> products = productService.updateProduct(productsRequest);

        if (products.isEmpty())
            return ResponseEntity.status(409).body(Optional.empty());

        return ResponseEntity.ok(products);
    }
}
