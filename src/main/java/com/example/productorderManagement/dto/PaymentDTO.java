package com.example.productorderManagement.dto;

import java.time.LocalDate;

import com.example.productorderManagement.model.Payment;
import com.example.productorderManagement.model.PaymentMethod;

import lombok.Data;

@Data
public class PaymentDTO {
    private Long paymentId;
    private Double amount;
    private PaymentMethod method;
    private Long orderId;
    private LocalDate payedAt;

    public PaymentDTO(Payment payment){
        this.paymentId = payment.getPaymentId();
        this.amount = payment.getAmount();
        this.method = payment.getPaymentMethod();
        this.orderId = payment.getOrder().getOrderId(); 
        this.payedAt = payment.getPaymentDate();
    }
    
}