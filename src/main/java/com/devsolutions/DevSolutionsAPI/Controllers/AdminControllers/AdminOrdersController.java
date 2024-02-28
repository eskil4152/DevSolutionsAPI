package com.devsolutions.DevSolutionsAPI.Controllers.AdminControllers;

import com.devsolutions.DevSolutionsAPI.Entities.Orders;
import com.devsolutions.DevSolutionsAPI.Entities.Products;
import com.devsolutions.DevSolutionsAPI.Enums.UserRole;
import com.devsolutions.DevSolutionsAPI.RequestBodies.OrderRequest;
import com.devsolutions.DevSolutionsAPI.Services.OrderService;
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
public class AdminOrdersController {
    private final OrderService orderService;

    private final CheckCookie checkCookie;

    @Autowired
    public AdminOrdersController(OrderService orderService, UserService userService){
        this.orderService = orderService;

        this.checkCookie = new CheckCookie(userService);
    }

    @GetMapping("/order/all")
    public ResponseEntity<Optional<List<Orders>>> getAllOrders(HttpServletRequest request){
        Optional<UserRole> role = checkCookie.CheckCookieForRole(request);

        if (role.isEmpty() || (role.get() != UserRole.ADMIN && role.get() != UserRole.OWNER))
            return ResponseEntity.status(401).body(Optional.empty());

        return ResponseEntity.ok(Optional.of(orderService.getAllOrders()));
    }

    @PutMapping("/order/update")
    public ResponseEntity<Optional<Orders>> updateOrder(@RequestBody OrderRequest orderRequest, HttpServletRequest request){
        Optional<UserRole> role = checkCookie.CheckCookieForRole(request);

        if (role.isEmpty() || (role.get() != UserRole.ADMIN && role.get() != UserRole.OWNER))
            return ResponseEntity.status(401).body(Optional.empty());

        Optional<Orders> orders = orderService.updateOrder(orderRequest);

        if (orders.isEmpty())
            return ResponseEntity.status(409).body(Optional.empty());

        return ResponseEntity.ok(orders);
    }

    @DeleteMapping("/order/cancel/{id}")
    public ResponseEntity<?> cancelOrder(@PathVariable String id, HttpServletRequest request){
        Optional<UserRole> role = checkCookie.CheckCookieForRole(request);

        if (role.isEmpty() || (role.get() != UserRole.ADMIN && role.get() != UserRole.OWNER))
            return ResponseEntity.status(401).body(Optional.empty());

        boolean didDelete = orderService.cancelOrder(Long.parseLong(id));

        if (!didDelete)
            return ResponseEntity.notFound().build();

        return ResponseEntity.ok().build();
    }
}
