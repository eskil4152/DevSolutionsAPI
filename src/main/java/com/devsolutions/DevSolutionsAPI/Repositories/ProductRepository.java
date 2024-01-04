package com.devsolutions.DevSolutionsAPI.Repositories;

import com.devsolutions.DevSolutionsAPI.Entities.Products;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Products, Long> {

}
