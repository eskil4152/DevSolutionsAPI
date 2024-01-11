package com.devsolutions.DevSolutionsAPI.Entities;

import com.devsolutions.DevSolutionsAPI.Entities.Orders;
import com.devsolutions.DevSolutionsAPI.Entities.Users;
import jakarta.persistence.*;

@Entity
public class UserOrders {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "accountorder_id_generator")
    @SequenceGenerator(
            name = "accountorder_id_generator",
            allocationSize = 1
    )
    Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    Users user;

    @ManyToOne
    @JoinColumn(name = "order_id")
    Orders order;
}
