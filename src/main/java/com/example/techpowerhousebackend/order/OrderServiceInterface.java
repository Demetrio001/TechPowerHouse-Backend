package com.example.techpowerhousebackend.order;


import com.example.techpowerhousebackend.support.exceptions.OrderNotFoundException;

import java.util.List;

public interface OrderServiceInterface {
    Order findOne(int id) throws OrderNotFoundException;
    List<Order> findAll(int pageNumber, int pageSize);
    List<Order> findByUser(String email, int pageNumber, int pageSize);

}
