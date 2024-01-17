package com.devsolutions.DevSolutionsAPI.Controllers;

import com.devsolutions.DevSolutionsAPI.Entities.Orders;
import com.devsolutions.DevSolutionsAPI.Entities.Users;
import com.devsolutions.DevSolutionsAPI.RequestBodies.OrderRequest;
import com.devsolutions.DevSolutionsAPI.Services.OrderService;
import com.devsolutions.DevSolutionsAPI.Services.UserOrderService;
import com.devsolutions.DevSolutionsAPI.Services.UserService;
import com.devsolutions.DevSolutionsAPI.Tools.CheckJwt;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/order")
public class OrderController {

    private final OrderService orderService;
    private final UserOrderService userOrderService;
    private final UserService userService;
    private final CheckJwt checkJwt;

    public OrderController(OrderService orderService, UserOrderService userOrderService, UserService userService){
        this.orderService = orderService;
        this.userOrderService = userOrderService;
        this.userService = userService;
        this.checkJwt = new CheckJwt(userService);
    }

    @PostMapping("/new")
    public ResponseEntity<Optional<Orders>> createNewOrder(@RequestBody OrderRequest orderRequest, HttpServletRequest request){
        Optional<Users> user = checkJwt.checkJwtForUser(request);

        if (user.isEmpty()){
            return ResponseEntity.status(401).build();
        }

        Optional<Orders> order = orderService.newOrder(orderRequest, user.get());

        if (order.isEmpty())
            return ResponseEntity.status(422).build();

        return ResponseEntity.ok(order);
    }

    @GetMapping("/all")
    public ResponseEntity<Optional<List<Orders>>> fetchOrderByUser(HttpServletRequest request){
        Optional<Users> users = checkJwt.checkJwtForUser(request);

        if (users.isEmpty())
            return ResponseEntity.status(401).body(Optional.empty());

        List<Orders> orders = orderService.getOrder(users.get());

        return ResponseEntity.ok(Optional.ofNullable(orders));
    }

    @GetMapping("/{id}")
    public ResponseEntity<String> getOneOrder(){
        return ResponseEntity.status(501).body("NOT IMPLEMENTED");
    }

    @GetMapping("/update/{id}")
    public ResponseEntity<String> updateOrder(){
        return ResponseEntity.status(501).body("NOT IMPLEMENTED");
    }

    @GetMapping("/cancel/{id}")
    public ResponseEntity<String> cancelOrder(){
        return ResponseEntity.status(501).body("NOT IMPLEMENTED");
    }

    @GetMapping("/admin/all")
    public ResponseEntity<String> getAllOrders(){
        return ResponseEntity.status(501).body("NOT IMPLEMENTED");
    }
}