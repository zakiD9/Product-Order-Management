package com.example.productorderManagement.controller;

import com.example.productorderManagement.dto.OrderItemDTO;
import com.example.productorderManagement.service.OrderItemService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/order-item")
public class OrderItemController {

    private final OrderItemService orderItemService;

    public OrderItemController(OrderItemService orderItemService) {
        this.orderItemService = orderItemService;
    }

    @PostMapping
    public ResponseEntity<OrderItemDTO> addOrderItem(
            @RequestParam Long productId,
            @RequestParam Long orderId,
            @RequestParam Integer quantity) {
        OrderItemDTO orderItemDTO = orderItemService.addOrderItem(productId, orderId, quantity);
        return ResponseEntity.ok(orderItemDTO);
    }

    @PutMapping("/{orderItemId}")
    public ResponseEntity<OrderItemDTO> updateOrderItem(
            @PathVariable Long orderItemId,
            @RequestParam Integer quantity) {
        OrderItemDTO orderItemDTO = orderItemService.updateOrderItem(orderItemId, quantity);
        return ResponseEntity.ok(orderItemDTO);
    }

    @DeleteMapping("/{orderItemId}")
    public ResponseEntity<Void> deleteOrderItem(@PathVariable Long orderItemId) {
        orderItemService.deleteOrderItem(orderItemId);
        return ResponseEntity.noContent().build();
    }
}