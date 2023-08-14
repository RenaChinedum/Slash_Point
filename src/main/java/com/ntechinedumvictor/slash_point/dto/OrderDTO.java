package com.ntechinedumvictor.slash_point.dto;

import com.ntechinedumvictor.slash_point.enums.Status;
import com.ntechinedumvictor.slash_point.model.Cart;
import com.ntechinedumvictor.slash_point.model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.time.LocalDate;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderDTO {

    private Long order_id;
    private LocalDate createdAt;
    private String referenceID;

    private int quantity;

    private Double price;

    private Double amount;

    private Status status;

    private User user;

    private Cart cart;

}
