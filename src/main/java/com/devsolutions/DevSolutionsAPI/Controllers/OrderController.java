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

    // Makes a new order, provided user and product is provided and exists
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

    // Gets all orders a user has made
    @GetMapping("/all")
    public ResponseEntity<Optional<List<Orders>>> fetchOrderByUser(HttpServletRequest request){
        Optional<Users> users = checkJwt.checkJwtForUser(request);

        if (users.isEmpty())
            return ResponseEntity.status(401).body(Optional.empty());

        List<Orders> orders = orderService.getOrders(users.get());

        return ResponseEntity.ok(Optional.ofNullable(orders));
    }

    // Gets order by id, checks if user exists, if order exists, and if order is made by the user
    @GetMapping("/{id}")
    public ResponseEntity<Optional<Orders>> getOneOrder(@RequestParam Long id, HttpServletRequest request){
        Optional<Users> user = checkJwt.checkJwtForUser(request);

        if (user.isEmpty())
            return ResponseEntity.status(401).body(Optional.empty());

        Optional<Orders> order = orderService.getOrderById(id);

        if (order.isEmpty())
            return ResponseEntity.status(404).body(Optional.empty());

        if (user.get() != order.get().getUser())
            return ResponseEntity.status(401).body(Optional.empty());

        return ResponseEntity.ok(order);
    }
}