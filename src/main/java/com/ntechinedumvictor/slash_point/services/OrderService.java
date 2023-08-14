package com.ntechinedumvictor.slash_point.services;

import com.ntechinedumvictor.slash_point.dto.OrderDTO;

import java.util.List;


public interface OrderService {

    public List<OrderDTO> viewOrder(int userID);


    void addToOrder(int userID);
}
