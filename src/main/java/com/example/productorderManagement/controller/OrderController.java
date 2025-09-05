package com.example.productorderManagement.controller;

import com.example.productorderManagement.dto.response.OrderResponse;
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
    public ResponseEntity<List<OrderResponse>> getAllOrders() {
        List<OrderResponse> orders = orderService.getAllOrders();
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<OrderResponse>> getOrdersForUser(@PathVariable Long userId) {
        List<OrderResponse> orders = orderService.getOrdersForUser(userId);
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/{orderId}/user/{userId}")
    public ResponseEntity<OrderResponse> getOrderById(@PathVariable Long orderId, @PathVariable Long userId) {
        OrderResponse orderDTO = orderService.getOrderById(orderId, userId);
        return ResponseEntity.ok(orderDTO);
    }

    @PostMapping("/user/{userId}")
    public ResponseEntity<OrderResponse> createOrder(@PathVariable Long userId) {
        OrderResponse orderDTO = orderService.createOrder(userId);
        return ResponseEntity.ok(orderDTO);
    }

    @PostMapping("/buy-now")
    public ResponseEntity<OrderResponse> createBuyNowOrder(
            @RequestParam Long userId,
            @RequestParam Long productId,
            @RequestParam int quantity) {
        var order = orderService.createBuyNowOrder(userId, productId, quantity);
        return ResponseEntity.ok(new OrderResponse(order));
    }

    @PostMapping("/{orderId}/user/{userId}/cancel")
    public ResponseEntity<Void> cancelOrder(@PathVariable Long orderId, @PathVariable Long userId) {
        orderService.cancelOrder(orderId, userId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{orderId}/status")
    public ResponseEntity<OrderResponse> updateOrderStatus(
            @PathVariable Long orderId,
            @RequestParam OrderStatus status) {
        OrderResponse orderDTO = orderService.updateOrderStatus(orderId, status);
        return ResponseEntity.ok(orderDTO);
    }

    @PostMapping("/{orderId}/user/{userId}/checkout")
    public ResponseEntity<OrderResponse> checkoutOrder(@PathVariable Long orderId, @PathVariable Long userId) {
        OrderResponse orderDTO = orderService.checkoutOrder(orderId, userId);
        return ResponseEntity.ok(orderDTO);
    }
}