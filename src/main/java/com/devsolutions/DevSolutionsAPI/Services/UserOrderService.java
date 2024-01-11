package com.devsolutions.DevSolutionsAPI.Services;

import com.devsolutions.DevSolutionsAPI.Entities.Orders;
import com.devsolutions.DevSolutionsAPI.Entities.UserOrders;
import com.devsolutions.DevSolutionsAPI.Entities.Users;
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

    public void saveUserOrder(Users user, Orders order){
        UserOrders userOrder = new UserOrders(user, order);
        userOrderRepository.save(userOrder);
    }

    public void getOrders(){

    }
}
