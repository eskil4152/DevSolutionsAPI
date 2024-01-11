package com.devsolutions.DevSolutionsAPI.Services;

import com.devsolutions.DevSolutionsAPI.Repositories.UserOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserOrderService {

    private final UserOrderRepository userOrderRepository;

    @Autowired
    public UserOrderService(UserOrderRepository userOrderRepository) {
        this.userOrderRepository = userOrderRepository;
    }

    public void getOrders(){

    }
}
