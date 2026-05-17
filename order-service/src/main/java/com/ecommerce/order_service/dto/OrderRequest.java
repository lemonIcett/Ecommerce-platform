package com.ecommerce.order_service.dto;

import lombok.Data;

@Data
public class OrderRequest {
    private Long productId;
    private String username;
    private Integer quantity;
}