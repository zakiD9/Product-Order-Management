package com.example.productorderManagement.controller;

import com.example.productorderManagement.dto.OrderDTO;
import com.example.productorderManagement.model.OrderStatus;
import com.example.productorderManagement.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/order")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    public ResponseEntity<List<OrderDTO>> getAllOrders() {
        List<OrderDTO> orders = orderService.getAllOrders();
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<OrderDTO>> getOrdersForUser(@PathVariable Long userId) {
        List<OrderDTO> orders = orderService.getOrdersForUser(userId);
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/{orderId}/user/{userId}")
    public ResponseEntity<OrderDTO> getOrderById(@PathVariable Long orderId, @PathVariable Long userId) {
        OrderDTO orderDTO = orderService.getOrderById(orderId, userId);
        return ResponseEntity.ok(orderDTO);
    }

    @PostMapping("/user/{userId}")
    public ResponseEntity<OrderDTO> createOrder(@PathVariable Long userId) {
        OrderDTO orderDTO = orderService.createOrder(userId);
        return ResponseEntity.ok(orderDTO);
    }

    @PostMapping("/buy-now")
    public ResponseEntity<OrderDTO> createBuyNowOrder(
            @RequestParam Long userId,
            @RequestParam Long productId,
            @RequestParam int quantity) {
        var order = orderService.createBuyNowOrder(userId, productId, quantity);
        return ResponseEntity.ok(new OrderDTO(order));
    }

    @PostMapping("/{orderId}/user/{userId}/cancel")
    public ResponseEntity<Void> cancelOrder(@PathVariable Long orderId, @PathVariable Long userId) {
        orderService.cancelOrder(orderId, userId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{orderId}/status")
    public ResponseEntity<OrderDTO> updateOrderStatus(
            @PathVariable Long orderId,
            @RequestParam OrderStatus status) {
        OrderDTO orderDTO = orderService.updateOrderStatus(orderId, status);
        return ResponseEntity.ok(orderDTO);
    }

    @PostMapping("/{orderId}/user/{userId}/checkout")
    public ResponseEntity<OrderDTO> checkoutOrder(@PathVariable Long orderId, @PathVariable Long userId) {
        OrderDTO orderDTO = orderService.checkoutOrder(orderId, userId);
        return ResponseEntity.ok(orderDTO);
    }
}