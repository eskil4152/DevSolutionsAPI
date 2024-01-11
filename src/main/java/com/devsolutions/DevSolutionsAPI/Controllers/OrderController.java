package com.devsolutions.DevSolutionsAPI.Controllers;

import com.devsolutions.DevSolutionsAPI.Entities.Orders;
import com.devsolutions.DevSolutionsAPI.Entities.Users;
import com.devsolutions.DevSolutionsAPI.RequestBodies.OrderRequest;
import com.devsolutions.DevSolutionsAPI.Services.OrderService;
import com.devsolutions.DevSolutionsAPI.Services.UserOrderService;
import com.devsolutions.DevSolutionsAPI.Services.UserService;
import com.devsolutions.DevSolutionsAPI.Tools.CheckCookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
public class OrderController {

    private final OrderService orderService;
    private final UserOrderService userOrderService;
    private final UserService userService;
    private final CheckCookie checkCookie;

    public OrderController(OrderService orderService, UserOrderService userOrderService, UserService userService){
        this.orderService = orderService;
        this.userOrderService = userOrderService;
        this.userService = userService;
        this.checkCookie = new CheckCookie(userService);
    }

    @PostMapping("/api/order/new")
    public ResponseEntity<Optional<Orders>> createNewOrder(@RequestBody OrderRequest orderRequest, HttpServletRequest request){
        Optional<Users> user = checkCookie.CheckCookieForUser(request);

        if (user.isEmpty()){
            return ResponseEntity.status(401).build();
        }

        Optional<Orders> order = orderService.newOrder(orderRequest, user.get());

        if (order.isEmpty())
            return ResponseEntity.status(422).build();

        return ResponseEntity.ok(order);
    }

    @GetMapping("/api/order/all")
    public ResponseEntity<List<Orders>> fetchOrderByUser(HttpServletRequest request){
        Optional<Users> users = checkCookie.CheckCookieForUser(request);

        if (users.isEmpty())
            return ResponseEntity.status(401).build();

        //Optional<List<Orders>> orders = userOrderService.getOrders(users.get().getUsername());
        List<Orders> orders = orderService.getOrder(users.get());

        return ResponseEntity.ok(orders);
    }

    @GetMapping("/api/order/{id}")
    public ResponseEntity<String> getOneOrder(){
        return ResponseEntity.status(501).body("NOT IMPLEMENTED");
    }

    @GetMapping("/api/order/update/{id}")
    public ResponseEntity<String> updateOrder(){
        return ResponseEntity.status(501).body("NOT IMPLEMENTED");
    }

    @GetMapping("/api/order/cancel/{id}")
    public ResponseEntity<String> cancelOrder(){
        return ResponseEntity.status(501).body("NOT IMPLEMENTED");
    }

    @GetMapping("/api/order/admin/all")
    public ResponseEntity<String> getAllOrders(){
        return ResponseEntity.status(501).body("NOT IMPLEMENTED");
    }
}