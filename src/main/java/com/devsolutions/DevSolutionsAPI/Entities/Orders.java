package com.devsolutions.DevSolutionsAPI.Entities;

import com.devsolutions.DevSolutionsAPI.Enums.OrderStatus;
import com.devsolutions.DevSolutionsAPI.Enums.PaymentStatus;
import jakarta.persistence.*;

import java.util.Date;

@Entity
public class Orders {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "order_id_generator")
    @SequenceGenerator(
            name = "order_id_generator",
            allocationSize = 1
    )
    Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    Users user;

    @ManyToOne
    @JoinColumn(name = "product_id")
    Products products;

    Double price;
    Date orderDate;
    String notes;

    String paymentMethod;
    String billingAddress;

    @Enumerated(EnumType.STRING)
    PaymentStatus paymentStatus;

    @Enumerated(EnumType.STRING)
    OrderStatus orderStatus;

    public Orders(){

    }

    public Orders(Users user, Products products, Double price, Date orderDate, String notes, String paymentMethod, String billingAddress) {
        this.user = user;
        this.products = products;
        this.price = price;
        this.orderDate = orderDate;
        this.notes = notes;
        this.paymentMethod = paymentMethod;
        this.billingAddress = billingAddress;
        this.paymentStatus = PaymentStatus.AWAITING_PAYMENT;
        this.orderStatus = OrderStatus.NOT_STARTED;
    }

    public Long getId() {
        return id;
    }

    public Users getUser() {
        return user;
    }

    public Products getProducts() {
        return products;
    }

    public Double getPrice() {
        return price;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public String getNotes() {
        return notes;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public String getBillingAddress() {
        return billingAddress;
    }

    public PaymentStatus getPaymentStatus() {
        return paymentStatus;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }
}