package com.devsolutions.DevSolutionsAPI.Repositories;

import com.devsolutions.DevSolutionsAPI.Entities.Orders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Orders, Long> {
}
