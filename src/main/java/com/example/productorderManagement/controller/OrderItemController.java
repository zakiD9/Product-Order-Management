package com.example.productorderManagement.controller;

import com.example.productorderManagement.dto.response.OrderItemResponse;
import com.example.productorderManagement.service.OrderItemService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/order-item")
public class OrderItemController {

    private final OrderItemService orderItemService;

    public OrderItemController(OrderItemService orderItemService) {
        this.orderItemService = orderItemService;
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or #userId == principal.id")
    public ResponseEntity<OrderItemResponse> addOrderItem(
            @RequestParam Long productId,
            @RequestParam Long orderId,
            @RequestParam Integer quantity) {
        OrderItemResponse orderItemDTO = orderItemService.addOrderItem(productId, orderId, quantity);
        return ResponseEntity.ok(orderItemDTO);
    }

    @PutMapping("/{orderItemId}")
    @PreAuthorize("hasRole('ADMIN') or @orderItemService.isOrderItemOwner(#orderItemId, principal.id)")
    public ResponseEntity<OrderItemResponse> updateOrderItem(
            @PathVariable Long orderItemId,
            @RequestParam Integer quantity) {
        OrderItemResponse orderItemDTO = orderItemService.updateOrderItem(orderItemId, quantity);
        return ResponseEntity.ok(orderItemDTO);
    }

    @DeleteMapping("/{orderItemId}")
    @PreAuthorize("hasRole('ADMIN') or @orderItemService.isOrderItemOwner(#orderItemId, principal.id)")
    public ResponseEntity<Void> deleteOrderItem(@PathVariable Long orderItemId) {
        orderItemService.deleteOrderItem(orderItemId);
        return ResponseEntity.noContent().build();
    }
}