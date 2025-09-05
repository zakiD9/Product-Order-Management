package com.example.productorderManagement.dto.response;

import java.util.List;
import java.util.stream.Collectors;

import com.example.productorderManagement.model.Order;
import com.example.productorderManagement.model.OrderStatus;

import lombok.Data;

@Data
public class OrderResponse {
    private Long orderId;
    private String username;
    private List<OrderItemResponse> orderItems;
    private Double amount;
    private OrderStatus status;

    public OrderResponse(Order order){
        this.orderId = order.getOrderId();
        this.orderItems = order.getOrderItems()
                           .stream()
                           .map(OrderItemResponse::new)
                           .collect(Collectors.toList());
        this.amount = order.getTotalAmount();
        this.status = order.getStatus(); 
    }
    
}