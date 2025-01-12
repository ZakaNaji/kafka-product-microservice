package com.znaji.productmicroservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Data

public class ProductDTO {

    private String title;
    private BigDecimal price;
    private int quantity;
}
