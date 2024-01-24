package com.devsolutions.DevSolutionsAPI.Repositories;

import com.devsolutions.DevSolutionsAPI.Entities.Products;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Products, Long> {
    Optional<Products> findByName(String name);
}
