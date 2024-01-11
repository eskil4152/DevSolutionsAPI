package com.devsolutions.DevSolutionsAPI.Services;

import com.devsolutions.DevSolutionsAPI.Entities.*;
import com.devsolutions.DevSolutionsAPI.Repositories.OrderRepository;
import com.devsolutions.DevSolutionsAPI.RequestBodies.OrderRequest;
import com.devsolutions.DevSolutionsAPI.Security.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
public class OrderService {

    private final OrderRepository orderRepository;

    private final ProductService productService;
    private final UserOrderService userOrderService;

    @Autowired
    public OrderService(OrderRepository orderRepository, ProductService productService, UserOrderService userOrderService) {
        this.orderRepository = orderRepository;
        this.productService = productService;
        this.userOrderService = userOrderService;
    }

    public Optional<Orders> newOrder(OrderRequest orderRequest, Users user){
        String name = orderRequest.getProductId();
        Double price = orderRequest.getPrice();
        String notes = orderRequest.getNotes();
        BillingInfo info = orderRequest.getBillingInfo();
        Date orderDate = new Date();
        Optional<Products> product = productService.getProduct(Long.parseLong(name));

        if (product.isEmpty())
            return Optional.empty();

        Orders order = new Orders(user, product.get(), price, orderDate, notes, info);

        orderRepository.save(order);
        userOrderService.saveUserOrder(user, order);

        return Optional.of(order);
    }

    public void getAllOrders(){
        return;
    }

    public void getOrder(){

    }

    public void updateOrder(){

    }

    public void cancelOrder(){

    }
}
