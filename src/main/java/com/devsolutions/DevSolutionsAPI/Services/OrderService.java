package com.devsolutions.DevSolutionsAPI.Services;

import com.devsolutions.DevSolutionsAPI.Entities.Orders;
import com.devsolutions.DevSolutionsAPI.Entities.Products;
import com.devsolutions.DevSolutionsAPI.Entities.Users;
import com.devsolutions.DevSolutionsAPI.Repositories.OrderRepository;
import com.devsolutions.DevSolutionsAPI.RequestBodies.OrderRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
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
        String billingAddress = orderRequest.getBillingAddress();
        String paymentMethod = orderRequest.getPaymentMethod();

        Date orderDate = new Date();
        Optional<Products> product = productService.getProduct(Long.parseLong(name));

        if (product.isEmpty())
            return Optional.empty();

        Orders order = new Orders(user, product.get(), price, orderDate, notes, paymentMethod, billingAddress);

        orderRepository.save(order);
        userOrderService.saveUserOrder(user, order);

        return Optional.of(order);
    }

    public Optional<Orders> getOrderById(Long id){
        return orderRepository.findById(id);
    }

    public List<Orders> getOrders(Users user){
        return orderRepository.findByUser(user);
    }

    public List<Orders> getAllOrders(){
        return orderRepository.findAll();
    }

    public Optional<Orders> updateOrder(OrderRequest request){
        Optional<Orders> order = orderRepository.findById(request.getOrderId());

        if (order.isEmpty()) {
            return order;
        }

        orderRepository.save(order.get());
        return order;
    }

    public boolean cancelOrder(Long id){
        Optional<Orders> order = orderRepository.findById(id);

        if (order.isEmpty())
            return false;

        orderRepository.delete(order.get());

        return true;
    }
}
