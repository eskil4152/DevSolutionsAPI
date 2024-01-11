package com.devsolutions.DevSolutionsAPI.Controllers;

import com.devsolutions.DevSolutionsAPI.Entities.BillingInfo;
import com.devsolutions.DevSolutionsAPI.Entities.Orders;
import com.devsolutions.DevSolutionsAPI.Entities.Users;
import com.devsolutions.DevSolutionsAPI.RequestBodies.OrderRequest;
import com.devsolutions.DevSolutionsAPI.Security.JwtUtil;
import com.devsolutions.DevSolutionsAPI.Services.OrderService;
import com.devsolutions.DevSolutionsAPI.Services.UserOrderService;
import com.devsolutions.DevSolutionsAPI.Services.UserService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
public class OrderController {

    private final OrderService orderService;
    private final UserOrderService userOrderService;
    private final UserService userService;

    public OrderController(OrderService orderService, UserOrderService userOrderService, UserService userService){
        this.orderService = orderService;
        this.userOrderService = userOrderService;
        this.userService = userService;
    }

    @GetMapping("/api/order/new")
    public ResponseEntity<Optional<Orders>> createNewOrder(@RequestBody OrderRequest orderRequest, HttpServletRequest request){
        Cookie[] cookies = request.getCookies();

        if (cookies == null) {
            System.out.println("Null cookie");
            return ResponseEntity.status(401).body(Optional.empty());
        }

        Optional<Users> cookieUser = Optional.empty();

        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("Authentication")) {
                Claims claims = JwtUtil.parseToken(cookie.getValue());

                String username = claims.getSubject();

                cookieUser = userService.getUser(username);

                if (cookieUser.isPresent()) {
                    break;
                }
            }
        }

        if (cookieUser.isEmpty()) {
            System.out.println("Empty user");
            return ResponseEntity.status(401).body(Optional.empty());
        }

        Optional<Orders> order = orderService.newOrder(orderRequest, cookieUser.get());

        if (order.isEmpty())
            return ResponseEntity.status(422).build();

        return ResponseEntity.ok(order);
    }

    @GetMapping("/api/order")
    public ResponseEntity<String> getAllOrders(){
        return ResponseEntity.status(501).build();
    }

    @GetMapping("/api/order")
    public ResponseEntity<String> getOneOrder(){
        return ResponseEntity.status(501).build();
    }

    @GetMapping("/api/order")
    public ResponseEntity<String> updateOrder(){
        return ResponseEntity.status(501).build();
    }

    @GetMapping("/api/order")
    public ResponseEntity<String> cancelOrder(){
        return ResponseEntity.status(501).build();
    }

    @GetMapping("/api/order")
    public ResponseEntity<String> fetchOrderByUser(){
        return ResponseEntity.status(501).build();
    }
}