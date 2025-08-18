package com.example.productorderManagement.dto;

import com.example.productorderManagement.model.OrderItem;

import lombok.Data;

@Data
public class OrderItemDTO {
    private Long orderItemId;
    private String productName;
    private Double unitPrice;
    private Integer quantity;
    private Double totalPrice;

    public OrderItemDTO(OrderItem orderItem){
        this.orderItemId = orderItem.getOrderItemId();
        this.productName = orderItem.getProduct().getName();
        this.unitPrice = orderItem.getUnitPrice();
        this.quantity = orderItem.getQuantity(); 
        this.totalPrice = orderItem.getTotalPrice();
    }
    
}

