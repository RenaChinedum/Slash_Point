package com.ntechinedumvictor.slash_point.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductDTO {
    private int id;
    private String productName;
    private Double price;
    private int quantity;
    private String category;
    private byte[] imageUrl;

}
