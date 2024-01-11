package com.devsolutions.DevSolutionsAPI.Entities;

import jakarta.persistence.*;

import java.sql.Date;

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
    BillingInfo billingInfo;
}

@Embeddable
class BillingInfo {
    String paymentMethod;
    String paymentStatus;
    String billingAddress;
}
