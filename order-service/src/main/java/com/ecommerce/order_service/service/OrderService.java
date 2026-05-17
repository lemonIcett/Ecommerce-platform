package com.ecommerce.order_service.service;

import com.ecommerce.order_service.dto.OrderRequest;
import com.ecommerce.order_service.dto.OrderResponse;
import com.ecommerce.order_service.model.Order;
import com.ecommerce.order_service.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final RestTemplate restTemplate;

    @Value("${product-service.url}")
    private String productServiceUrl;

    public OrderResponse placeOrder(OrderRequest request) {
        // Call Product Service to reduce stock
        String url = productServiceUrl + "/api/products/" + request.getProductId()
                + "/reduce-stock?quantity=" + request.getQuantity();

        org.springframework.http.ResponseEntity<Boolean> response = restTemplate.exchange(
        url,
        org.springframework.http.HttpMethod.PUT,
        null,
        Boolean.class
);
Boolean stockReduced = response.getBody();

        String status = (stockReduced != null && stockReduced) ? "CONFIRMED" : "FAILED";

        Order order = new Order();
        order.setProductId(request.getProductId());
        order.setUsername(request.getUsername());
        order.setQuantity(request.getQuantity());
        order.setStatus(status);

        Order saved = orderRepository.save(order);
        return mapToResponse(saved);
    }

    public List<OrderResponse> getAllOrders() {
        return orderRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public OrderResponse getOrderById(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + id));
        return mapToResponse(order);
    }

    public List<OrderResponse> getOrdersByUsername(String username) {
        return orderRepository.findByUsername(username)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private OrderResponse mapToResponse(Order order) {
        return new OrderResponse(
                order.getId(),
                order.getProductId(),
                order.getUsername(),
                order.getQuantity(),
                order.getStatus(),
                order.getCreatedAt()
        );
    }
}