package com.devsolutions.DevSolutionsAPI.Controllers.AdminControllers;

import com.devsolutions.DevSolutionsAPI.Entities.Products;
import com.devsolutions.DevSolutionsAPI.Enums.UserRole;
import com.devsolutions.DevSolutionsAPI.RequestBodies.ProductsRequest;
import com.devsolutions.DevSolutionsAPI.Services.ProductService;
import com.devsolutions.DevSolutionsAPI.Services.UserService;
import com.devsolutions.DevSolutionsAPI.Tools.CheckCookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/admin")
public class AdminProductsController {
    private final ProductService productService;

    private final CheckCookie checkCookie;

    @Autowired
    public AdminProductsController(UserService userService, ProductService productService){
        this.productService = productService;

        this.checkCookie = new CheckCookie(userService);
    }

    // Products
    @GetMapping("/products/all")
    public ResponseEntity<Optional<List<Products>>> getAllProductsAdmin(HttpServletRequest request){
        Optional<UserRole> role = checkCookie.CheckCookieForRole(request);

        if (role.isEmpty() || (role.get() != UserRole.ADMIN && role.get() != UserRole.OWNER))
            return ResponseEntity.status(401).body(Optional.empty());

        return ResponseEntity.ok(Optional.ofNullable(productService.getAllProducts()));
    }

    @PostMapping("/products/new")
    public ResponseEntity<Optional<Products>> addProduct(@RequestBody ProductsRequest productsRequest, HttpServletRequest request){
        Optional<UserRole> role = checkCookie.CheckCookieForRole(request);

        if (role.isEmpty() || (role.get() != UserRole.ADMIN && role.get() != UserRole.OWNER))
            return ResponseEntity.status(401).body(Optional.empty());

        Optional<Products> products = productService.saveProduct(productsRequest);

        if (products.isEmpty()) return ResponseEntity.status(409).body(Optional.empty());

        return ResponseEntity.ok(products);
    }

    // TODO Fix, does not know old name of product
    @PutMapping("/products/update")
    public ResponseEntity<Optional<Products>> updateProduct(@RequestBody ProductsRequest productsRequest, HttpServletRequest request){
        Optional<UserRole> role = checkCookie.CheckCookieForRole(request);

        if (role.isEmpty() || (role.get() != UserRole.ADMIN && role.get() != UserRole.OWNER))
            return ResponseEntity.status(401).body(Optional.empty());

        Optional<Products> products = productService.updateProduct(productsRequest);

        if (products.isEmpty())
            return ResponseEntity.status(409).body(Optional.empty());

        return ResponseEntity.ok(products);
    }

    @DeleteMapping("/products/delete/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable String id, HttpServletRequest request){
        Optional<UserRole> role = checkCookie.CheckCookieForRole(request);

        if (role.isEmpty() || (role.get() != UserRole.ADMIN && role.get() != UserRole.OWNER))
            return ResponseEntity.status(401).body(Optional.empty());

        boolean didDelete = productService.deleteProduct(Long.parseLong(id));

        if (!didDelete)
            return ResponseEntity.notFound().build();

        return ResponseEntity.ok().build();
    }
}
