package com.example.productorderManagement.controller;

import com.example.productorderManagement.dto.response.OrderResponse;
import com.example.productorderManagement.model.OrderStatus;
import com.example.productorderManagement.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<OrderResponse>> getAllOrders() {
        List<OrderResponse> orders = orderService.getAllOrders();
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/user/{userId}")
    @PreAuthorize("hasRole('ADMIN') or #userId == authentication.principal.id")
    public ResponseEntity<List<OrderResponse>> getOrdersForUser(@PathVariable Long userId) {
        List<OrderResponse> orders = orderService.getOrdersForUser(userId);
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/{orderId}/user/{userId}")
    @PreAuthorize("hasRole('ADMIN') or #userId == authentication.principal.id")
    public ResponseEntity<OrderResponse> getOrderById(@PathVariable Long orderId, @PathVariable Long userId) {
        OrderResponse orderDTO = orderService.getOrderById(orderId, userId);
        return ResponseEntity.ok(orderDTO);
    }

    @PostMapping("/user/{userId}")
    @PreAuthorize("hasRole('ADMIN') or #userId == authentication.principal.id")
    public ResponseEntity<OrderResponse> createOrder(@PathVariable Long userId) {
        OrderResponse orderDTO = orderService.createOrder(userId);
        return ResponseEntity.ok(orderDTO);
    }

    @PostMapping("/buy-now")
    @PreAuthorize("hasRole('ADMIN') or #userId == authentication.principal.id")
    public ResponseEntity<OrderResponse> createBuyNowOrder(
            @RequestParam Long userId,
            @RequestParam Long productId,
            @RequestParam int quantity) {
        var order = orderService.createBuyNowOrder(userId, productId, quantity);
        return ResponseEntity.ok(new OrderResponse(order));
    }


    @PostMapping("/{orderId}/user/{userId}/cancel")
    @PreAuthorize("hasRole('ADMIN') or #userId == authentication.principal.id")
    public ResponseEntity<Void> cancelOrder(@PathVariable Long orderId, @PathVariable Long userId) {
        orderService.cancelOrder(orderId, userId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{orderId}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<OrderResponse> updateOrderStatus(
            @PathVariable Long orderId,
            @RequestParam OrderStatus status) {
        OrderResponse orderDTO = orderService.updateOrderStatus(orderId, status);
        return ResponseEntity.ok(orderDTO);
    }

    @PostMapping("/{orderId}/user/{userId}/checkout")
    @PreAuthorize("hasRole('ADMIN') or #userId == authentication.principal.id")
    public ResponseEntity<OrderResponse> checkoutOrder(@PathVariable Long orderId, @PathVariable Long userId) {
        OrderResponse orderDTO = orderService.checkoutOrder(orderId, userId);
        return ResponseEntity.ok(orderDTO);
    }
}