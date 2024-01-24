package com.devsolutions.DevSolutionsAPI.Controllers;

import com.devsolutions.DevSolutionsAPI.Entities.Orders;
import com.devsolutions.DevSolutionsAPI.Entities.Products;
import com.devsolutions.DevSolutionsAPI.Entities.Users;
import com.devsolutions.DevSolutionsAPI.Enums.UserRole;
import com.devsolutions.DevSolutionsAPI.RequestBodies.ProductsRequest;
import com.devsolutions.DevSolutionsAPI.Services.OrderService;
import com.devsolutions.DevSolutionsAPI.Services.ProductService;
import com.devsolutions.DevSolutionsAPI.Services.UserService;
import com.devsolutions.DevSolutionsAPI.Tools.CheckJwt;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class AdminController {
    private final UserService userService;
    private final OrderService orderService;
    private final ProductService productService;

    private final CheckJwt checkJwt;

    @Autowired
    public AdminController(UserService userService, OrderService orderService, ProductService productService){
        this.userService = userService;
        this.orderService = orderService;
        this.productService = productService;

        this.checkJwt = new CheckJwt(userService);
    }

    // Orders
    @GetMapping("/admin/order/all")
    public ResponseEntity<Optional<List<Orders>>> getAllOrders(HttpServletRequest request){
        Optional<Users> user = checkJwt.checkJwtForUser(request);

        if (user.isEmpty() || user.get().getRole() != UserRole.ADMIN)
            return ResponseEntity.status(401).body(Optional.empty());

        return ResponseEntity.ok(Optional.of(orderService.getAllOrders()));
    }

    @PostMapping("/admin/order/update")
    public ResponseEntity<String> updateOrder(){
        return ResponseEntity.status(501).body("NOT IMPLEMENTED");
    }

    @PostMapping("/admin/order/cancel/{id}")
    public ResponseEntity<String> cancelOrder(){
        return ResponseEntity.status(501).body("NOT IMPLEMENTED");
    }

    // Products
    @PostMapping("/admin/products/new")
    public ResponseEntity<Optional<Products>> addProduct(@RequestBody ProductsRequest productsRequest, HttpServletRequest request){
        Optional<Users> user = checkJwt.checkJwtForUser(request);

        if (user.isEmpty() || user.get().getRole() != UserRole.ADMIN)
            return ResponseEntity.status(401).body(Optional.empty());

        Optional<Products> products = productService.saveProduct(productsRequest);

        if (products.isEmpty())
            return ResponseEntity.status(409).body(Optional.empty());

        return ResponseEntity.ok(products);
    }

    @PostMapping("/admin/products/delete/{id}")
    public ResponseEntity<?> deleteProduct(@RequestBody ProductsRequest productsRequest, HttpServletRequest request){
        Optional<Users> user = checkJwt.checkJwtForUser(request);

        if (user.isEmpty() || user.get().getRole() != UserRole.ADMIN)
            return ResponseEntity.status(401).body(Optional.empty());

        boolean didDelete = productService.deleteProduct(productsRequest);

        if (!didDelete)
            return ResponseEntity.notFound().build();
        
        return ResponseEntity.ok().build();
    }

    @PutMapping("/admin/products/update/{id}")
    public ResponseEntity<Optional<Products>> updateProduct(@RequestBody ProductsRequest productsRequest, HttpServletRequest request){
        Optional<Users> user = checkJwt.checkJwtForUser(request);

        if (user.isEmpty() || user.get().getRole() != UserRole.ADMIN)
            return ResponseEntity.status(401).body(Optional.empty());

        Optional<Products> products = productService.updateProduct(productsRequest);

        if (products.isEmpty())
            return ResponseEntity.status(409).body(Optional.empty());

        return ResponseEntity.ok(products);
    }

    // FAQ

    // Users And Mods
    @GetMapping("/admin/users")
    public ResponseEntity<Optional<List<Users>>> getAllUsers(HttpServletRequest request){
        Optional<Users> user = checkJwt.checkJwtForUser(request);

        if (user.isEmpty() || user.get().getRole() != UserRole.ADMIN)
            return ResponseEntity.status(401).body(Optional.empty());


        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/admin/mods")
    public ResponseEntity<Optional<List<Users>>> getAllModerators(HttpServletRequest request){
        Optional<Users> user = checkJwt.checkJwtForUser(request);

        if (user.isEmpty() || user.get().getRole() != UserRole.ADMIN)
            return ResponseEntity.status(401).body(Optional.empty());

        return ResponseEntity.ok(userService.getAllModerators());
    }
}
