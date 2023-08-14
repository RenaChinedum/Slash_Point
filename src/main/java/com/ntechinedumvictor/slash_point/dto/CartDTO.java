package com.ntechinedumvictor.slash_point.dto;

import com.ntechinedumvictor.slash_point.model.Cart;
import com.ntechinedumvictor.slash_point.model.Products;
import com.ntechinedumvictor.slash_point.model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartDTO {

    private Integer id;



    private LocalDate createdDate;


    private Products products;


    private User user;


    private int quantity;



    public CartDTO(Products products, int quantity, User user){
        this.user = user;
        this.products = products;
        this.quantity = quantity;
        this.createdDate = LocalDate.now();
    }

    public CartDTO(Cart cart){
      this.id = cart.getId();
      this.createdDate = cart.getCreatedDate();
      this.products = cart.getProducts();
      this.quantity = cart.getQuantity();
      this.user = cart.getUser();
    }

}
