package com.ecommerce.order_service;

import com.ecommerce.order_service.dto.OrderRequest;
import com.ecommerce.order_service.dto.OrderResponse;
import com.ecommerce.order_service.model.Order;
import com.ecommerce.order_service.repository.OrderRepository;
import com.ecommerce.order_service.service.OrderService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private OrderService orderService;

    @Test
    void placeOrder_ShouldReturnConfirmed_WhenStockAvailable() {
        OrderRequest request = new OrderRequest();
        request.setProductId(1L);
        request.setUsername("testuser");
        request.setQuantity(2);

        Order savedOrder = new Order(1L, 1L, "testuser", 2,
                "CONFIRMED", LocalDateTime.now());

        when(restTemplate.exchange(anyString(), eq(HttpMethod.PUT),
                isNull(), eq(Boolean.class)))
                .thenReturn(ResponseEntity.ok(true));
        when(orderRepository.save(any(Order.class))).thenReturn(savedOrder);

        OrderResponse response = orderService.placeOrder(request);

        assertNotNull(response);
        assertEquals("CONFIRMED", response.getStatus());
        assertEquals("testuser", response.getUsername());
        assertEquals(1L, response.getProductId());
    }

    @Test
    void placeOrder_ShouldReturnFailed_WhenStockUnavailable() {
        OrderRequest request = new OrderRequest();
        request.setProductId(1L);
        request.setUsername("testuser");
        request.setQuantity(100);

        Order savedOrder = new Order(1L, 1L, "testuser", 100,
                "FAILED", LocalDateTime.now());

        when(restTemplate.exchange(anyString(), eq(HttpMethod.PUT),
                isNull(), eq(Boolean.class)))
                .thenReturn(ResponseEntity.ok(false));
        when(orderRepository.save(any(Order.class))).thenReturn(savedOrder);

        OrderResponse response = orderService.placeOrder(request);

        assertNotNull(response);
        assertEquals("FAILED", response.getStatus());
    }

    @Test
    void getAllOrders_ShouldReturnAllOrders() {
        List<Order> orders = Arrays.asList(
                new Order(1L, 1L, "user1", 2, "CONFIRMED", LocalDateTime.now()),
                new Order(2L, 2L, "user2", 1, "CONFIRMED", LocalDateTime.now())
        );
        when(orderRepository.findAll()).thenReturn(orders);

        List<OrderResponse> result = orderService.getAllOrders();

        assertEquals(2, result.size());
        verify(orderRepository, times(1)).findAll();
    }

    @Test
    void getOrderById_ShouldReturnOrder_WhenExists() {
        Order order = new Order(1L, 1L, "testuser", 2,
                "CONFIRMED", LocalDateTime.now());
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        OrderResponse result = orderService.getOrderById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("testuser", result.getUsername());
    }

    @Test
    void getOrderById_ShouldThrowException_WhenNotFound() {
        when(orderRepository.findById(99L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> orderService.getOrderById(99L));

        assertEquals("Order not found with id: 99", exception.getMessage());
    }

    @Test
    void getOrdersByUsername_ShouldReturnUserOrders() {
        List<Order> orders = Arrays.asList(
                new Order(1L, 1L, "testuser", 2, "CONFIRMED", LocalDateTime.now()),
                new Order(2L, 2L, "testuser", 1, "CONFIRMED", LocalDateTime.now())
        );
        when(orderRepository.findByUsername("testuser")).thenReturn(orders);

        List<OrderResponse> result = orderService.getOrdersByUsername("testuser");

        assertEquals(2, result.size());
        verify(orderRepository, times(1)).findByUsername("testuser");
    }
}