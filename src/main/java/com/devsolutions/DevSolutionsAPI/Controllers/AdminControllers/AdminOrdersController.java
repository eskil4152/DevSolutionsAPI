package com.devsolutions.DevSolutionsAPI.Controllers.AdminControllers;

import com.devsolutions.DevSolutionsAPI.Entities.Orders;
import com.devsolutions.DevSolutionsAPI.Entities.Users;
import com.devsolutions.DevSolutionsAPI.Enums.UserRole;
import com.devsolutions.DevSolutionsAPI.Services.OrderService;
import com.devsolutions.DevSolutionsAPI.Services.ProductService;
import com.devsolutions.DevSolutionsAPI.Services.UserService;
import com.devsolutions.DevSolutionsAPI.Tools.CheckJwt;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/admin")
public class AdminOrdersController {
    private final OrderService orderService;

    private final CheckJwt checkJwt;

    @Autowired
    public AdminOrdersController(OrderService orderService, CheckJwt checkJwt){
        this.orderService = orderService;

        this.checkJwt = checkJwt;
    }

    // Orders
    @GetMapping("/order/all")
    public ResponseEntity<Optional<List<Orders>>> getAllOrders(HttpServletRequest request){
        Optional<UserRole> role = checkJwt.checkJwtForRole(request);

        if (role.isEmpty() || role.get() != UserRole.ADMIN)
            return ResponseEntity.status(401).body(Optional.empty());

        return ResponseEntity.ok(Optional.of(orderService.getAllOrders()));
    }

    @PostMapping("/order/update")
    public ResponseEntity<String> updateOrder(){
        return ResponseEntity.status(501).body("NOT IMPLEMENTED");
    }

    @PostMapping("/order/cancel/{id}")
    public ResponseEntity<String> cancelOrder(){
        return ResponseEntity.status(501).body("NOT IMPLEMENTED");
    }
}
