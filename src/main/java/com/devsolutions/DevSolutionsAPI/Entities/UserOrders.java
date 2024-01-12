package com.devsolutions.DevSolutionsAPI.Entities;

import jakarta.persistence.*;

@Entity
public class UserOrders {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_order_id_generator")
    @SequenceGenerator(
            name = "user_order_id_generator",
            allocationSize = 1
    )
    Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    Users user;

    @ManyToOne
    @JoinColumn(name = "order_id")
    Orders order;

    public Long getId() {
        return id;
    }

    public Users getUser() {
        return user;
    }

    public Orders getOrder() {
        return order;
    }

    public UserOrders() {
    }

    public UserOrders(Users user, Orders order) {
        this.user = user;
        this.order = order;
    }
}
