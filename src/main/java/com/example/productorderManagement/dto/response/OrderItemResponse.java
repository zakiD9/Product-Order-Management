package com.example.productorderManagement.dto.response;

import com.example.productorderManagement.model.OrderItem;

import lombok.Data;

@Data
public class OrderItemResponse {
    private Long orderItemId;
    private String productName;
    private Double unitPrice;
    private Integer quantity;
    private Double totalPrice;

    public OrderItemResponse(OrderItem orderItem){
        this.orderItemId = orderItem.getOrderItemId();
        this.productName = orderItem.getProduct().getName();
        this.unitPrice = orderItem.getUnitPrice();
        this.quantity = orderItem.getQuantity(); 
        this.totalPrice = orderItem.getTotalPrice();
    }
    
}

