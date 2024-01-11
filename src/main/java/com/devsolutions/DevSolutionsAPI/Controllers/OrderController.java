package com.devsolutions.DevSolutionsAPI.Controllers;

import com.devsolutions.DevSolutionsAPI.Services.OrderService;
import com.devsolutions.DevSolutionsAPI.Services.UserOrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OrderController {

    private final OrderService orderService;
    private final UserOrderService userOrderService;

    public OrderController(OrderService orderService, UserOrderService userOrderService){
        this.orderService = orderService;
        this.userOrderService = userOrderService;
    }

    @GetMapping("/api/order/new")
    public ResponseEntity<> createNewOrder(){
        var foo = orderService.getOrder();
        return ResponseEntity.ok();
    }

    @GetMapping("/api/order")
    public ResponseEntity<> getAllOrders(){
        return ResponseEntity.ok();
    }

    @GetMapping("/api/order")
    public ResponseEntity<> getOneOrder(){
        return ResponseEntity.ok();
    }

    @GetMapping("/api/order")
    public ResponseEntity<> updateOrder(){
        return ResponseEntity.ok();
    }

    @GetMapping("/api/order")
    public ResponseEntity<> cancelOrder(){
        return ResponseEntity.ok();
    }

    @GetMapping("/api/order")
    public ResponseEntity<> fetchOrderByUser(){
        return ResponseEntity.ok();
    }
}