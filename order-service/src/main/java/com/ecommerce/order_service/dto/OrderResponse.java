package com.ecommerce.order_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class OrderResponse {
    private Long id;
    private Long productId;
    private String username;
    private Integer quantity;
    private String status;
    private LocalDateTime createdAt;
}
