package com.devsolutions.DevSolutionsAPI.Entities;

import jakarta.persistence.*;

@Entity
public class Products {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "id_generator")
    @SequenceGenerator(
            name = "id_generator",
            allocationSize = 1
    )
    Long id;

    String name;
    String description;
    Double price;

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Double getPrice() {
        return price;
    }
}
