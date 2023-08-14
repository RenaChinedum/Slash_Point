package com.ntechinedumvictor.slash_point.model;

import com.ntechinedumvictor.slash_point.dto.CartDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;


    @Column(name = "created_date")
    private LocalDate createdDate;

    @OneToOne
    @JoinColumn(name = "product_id", referencedColumnName = "id")
    private Products products;

    @OneToOne
    @JoinColumn( name = "user_id",
    referencedColumnName = "id")
    private User user;


    private int quantity;


    public Cart(Products products, int quantity, User user){
        this.user = user;
        this.products = products;
        this.quantity = quantity;
        this.createdDate = LocalDate.now();
    }
    public Cart(CartDTO cartDTO){
        this.id = cartDTO.getId();
        this.createdDate = cartDTO.getCreatedDate();
        this.products = cartDTO.getProducts();
        this.user = cartDTO.getUser();
        this.quantity = cartDTO.getQuantity();
    }

    public Cart(int quantity, LocalDate date, Products productId, User userId) {
        this.quantity = quantity;
        this.createdDate = date;
        this.products = productId;
        this.user = userId;
    }
}
