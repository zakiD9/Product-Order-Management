package com.example.productorderManagement.dto;

import java.util.List;
import java.util.stream.Collectors;

import com.example.productorderManagement.model.Order;
import com.example.productorderManagement.model.OrderStatus;

import lombok.Data;

@Data
public class OrderDTO {
    private Long orderId;
    private String username;
    private List<OrderItemDTO> orderItems;
    private Double amount;
    private OrderStatus status;

    public OrderDTO(Order order){
        this.orderId = order.getOrderId();
        this.orderItems = order.getOrderItems()
                           .stream()
                           .map(OrderItemDTO::new)
                           .collect(Collectors.toList());
        this.amount = order.getTotalAmount();
        this.status = order.getStatus(); 
    }
    
}