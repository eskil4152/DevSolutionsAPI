package com.devsolutions.DevSolutionsAPI.Controllers.AdminControllers;

import com.devsolutions.DevSolutionsAPI.Entities.Users;
import com.devsolutions.DevSolutionsAPI.Enums.UserRole;
import com.devsolutions.DevSolutionsAPI.RequestBodies.RoleChangeRequest;
import com.devsolutions.DevSolutionsAPI.Services.OrderService;
import com.devsolutions.DevSolutionsAPI.Services.ProductService;
import com.devsolutions.DevSolutionsAPI.Services.UserService;
import com.devsolutions.DevSolutionsAPI.Tools.CheckCookie;
import com.devsolutions.DevSolutionsAPI.Tools.CheckJwt;
import jakarta.servlet.http.HttpServletRequest;
import org.checkerframework.checker.units.qual.C;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/admin")
public class AdminUsersController {
    private final UserService userService;
    private final OrderService orderService;
    private final ProductService productService;

    private final CheckCookie checkCookie;

    @Autowired
    public AdminUsersController(UserService userService, OrderService orderService, ProductService productService){
        this.userService = userService;
        this.orderService = orderService;
        this.productService = productService;

        this.checkCookie = new CheckCookie(userService);
    }

    @GetMapping("/users")
    public ResponseEntity<Optional<List<Users>>> getAllUsers(HttpServletRequest request){
        Optional<UserRole> role = checkCookie.CheckCookieForRole(request);

        if (role.isEmpty() || (role.get() != UserRole.ADMIN && role.get() != UserRole.OWNER)) {
            return ResponseEntity.status(401).body(Optional.empty());
        }

        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/mods")
    public ResponseEntity<Optional<List<Users>>> getAllModerators(HttpServletRequest request){
        Optional<UserRole> role = checkCookie.CheckCookieForRole(request);

        if (role.isEmpty() || (role.get() != UserRole.ADMIN && role.get() != UserRole.OWNER))
            return ResponseEntity.status(401).body(Optional.empty());

        return ResponseEntity.ok(userService.getAllModerators());
    }

    // TODO - Case sensitive, consider change
    @PostMapping("/roleChange")
    public ResponseEntity<?> changeRole(@RequestBody RoleChangeRequest changeRequest, HttpServletRequest request){
        Optional<UserRole> role = checkCookie.CheckCookieForRole(request);

        if (role.isEmpty() || (role.get() != UserRole.ADMIN && role.get() != UserRole.OWNER))
            return ResponseEntity.status(401).body(Optional.empty());

        int res = userService.changeUserRole(changeRequest, role.get());

        return ResponseEntity.status(res).build();
    }
}
