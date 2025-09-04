package com.example.productorderManagement.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.productorderManagement.dto.OrderItemDTO;
import com.example.productorderManagement.exception.BadRequestException;
import com.example.productorderManagement.exception.ResourceNotFoundException;
import com.example.productorderManagement.model.Order;
import com.example.productorderManagement.model.OrderItem;
import com.example.productorderManagement.model.OrderStatus;
import com.example.productorderManagement.model.Product;
import com.example.productorderManagement.repository.OrderItemRepository;
import com.example.productorderManagement.repository.OrderRepository;
import com.example.productorderManagement.repository.ProductRepository;

import jakarta.transaction.Transactional;

@Service
public class OrderItemService {

    private final OrderItemRepository orderItemRepository;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    public OrderItemService(OrderItemRepository orderItemRepository,
                            OrderRepository orderRepository,
                            ProductRepository productRepository) {
        this.orderItemRepository = orderItemRepository;
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
    }

    public OrderItemDTO addOrderItem(Long productId, Long orderId, Integer quantity) {
    Product product = productRepository.findById(productId)
            .orElseThrow(() -> new ResourceNotFoundException("The product doesn't exist."));
    Order order = orderRepository.findById(orderId)
            .orElseThrow(() -> new ResourceNotFoundException("The order doesn't exist."));

    if (order.getStatus() != OrderStatus.PENDING) {
        throw new BadRequestException("You can only modify items for orders that are PENDING.");
    }

    if (product.getQuantity() < quantity) {
        throw new BadRequestException("The quantity you want isn't available.");
    }

    Optional<OrderItem> existingItem = orderItemRepository.findByOrderAndProduct(order, product);

    OrderItem orderItem;
    if (existingItem.isPresent()) {
        orderItem = existingItem.get();
        orderItem.setQuantity(orderItem.getQuantity() + quantity);
    } else {
        orderItem = new OrderItem();
        orderItem.setOrder(order);
        orderItem.setProduct(product);
        orderItem.setQuantity(quantity);
        orderItem.setUnitPrice(product.getPrice());
    }

    OrderItem savedItem = orderItemRepository.save(orderItem);

    double newTotal = order.getOrderItems()
            .stream()
            .mapToDouble(item -> item.getUnitPrice() * item.getQuantity())
            .sum();
    order.setTotalAmount(newTotal);
    orderRepository.save(order);

    return new OrderItemDTO(savedItem);
    }


    @Transactional
public OrderItemDTO updateOrderItem(Long orderItemId, Integer newQuantity) {
    OrderItem orderItem = orderItemRepository.findById(orderItemId)
            .orElseThrow(() -> new ResourceNotFoundException("This order item doesn't exist"));

    Order order = orderItem.getOrder();

    if (order.getStatus() != OrderStatus.PENDING) {
        throw new BadRequestException("You can only modify items for orders that are PENDING.");
    }

    if (newQuantity <= 0) {
        throw new BadRequestException("Quantity must be greater than 0");
    }

    if (newQuantity.equals(orderItem.getQuantity())) {
        throw new BadRequestException("This quantity is already the current one");
    }

    Product product = orderItem.getProduct();

    if (newQuantity > product.getQuantity()) {
        throw new BadRequestException("Quantity you want isn't available");
    }

    orderItem.setQuantity(newQuantity);
    orderItem.setTotalPrice(orderItem.getUnitPrice() * newQuantity); 
    OrderItem savedOrderItem = orderItemRepository.save(orderItem);

    double newTotal = order.getOrderItems()
            .stream()
            .mapToDouble(OrderItem::getTotalPrice)
            .sum();
    order.setTotalAmount(newTotal);
    orderRepository.save(order);

    return new OrderItemDTO(savedOrderItem);
}



    public void deleteOrderItem(Long orderItemId) {
        OrderItem orderItem = orderItemRepository.findById(orderItemId)
                .orElseThrow(() -> new ResourceNotFoundException("OrderItem not found with id: " + orderItemId));

        Order order = orderItem.getOrder();

        if (order.getStatus() != OrderStatus.PENDING) {
            throw new BadRequestException("You can only remove items from orders that are PENDING.");
        }

        orderItemRepository.delete(orderItem);

        double newTotal = order.getOrderItems()
                .stream()
                .mapToDouble(OrderItem::getTotalPrice)
                .sum();
        order.setTotalAmount(newTotal);
        orderRepository.save(order);
    }
}
