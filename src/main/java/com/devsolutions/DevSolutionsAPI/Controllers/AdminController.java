package com.devsolutions.DevSolutionsAPI.Controllers;

import com.devsolutions.DevSolutionsAPI.Entities.Products;
import com.devsolutions.DevSolutionsAPI.Entities.Users;
import com.devsolutions.DevSolutionsAPI.Enums.UserRole;
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
    private final CheckJwt checkJwt;

    @Autowired
    public AdminController(UserService userService){
        this.userService = userService;
        this.checkJwt = new CheckJwt(userService);
    }

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
