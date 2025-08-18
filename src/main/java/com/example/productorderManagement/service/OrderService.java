package com.example.productorderManagement.service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.jaxb.SpringDataJaxb.OrderDto;
import org.springframework.stereotype.Service;

import com.example.productorderManagement.dto.OrderDTO;
import com.example.productorderManagement.exception.ResourceNotFoundException;
import com.example.productorderManagement.exception.UnauthorizedException;
import com.example.productorderManagement.exception.ValidationException;
import com.example.productorderManagement.model.Order;
import com.example.productorderManagement.model.OrderItem;
import com.example.productorderManagement.model.OrderStatus;
import com.example.productorderManagement.model.Product;
import com.example.productorderManagement.model.User;
import com.example.productorderManagement.repository.OrderItemRepository;
import com.example.productorderManagement.repository.OrderRepository;
import com.example.productorderManagement.repository.ProductRepository;
import com.example.productorderManagement.repository.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    

    public OrderService(OrderRepository orderRepository,OrderItemRepository orderItemRepository, UserRepository userRepository,ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.orderItemRepository = orderItemRepository;
    }

    public List<OrderDTO> getAllOrders() {
    return orderRepository.findAll()
                          .stream()
                          .map(OrderDTO::new)
                          .toList();
    }


    public List<OrderDTO> getOrdersForUser(Long userId) {
    userRepository.findById(userId)
           .orElseThrow(() -> new ResourceNotFoundException("User not found: " + userId));

    List<Order> orders = orderRepository.findAllByUser_UserId(userId);
    return orders.stream()
                 .map(OrderDTO::new) 
                 .toList();
    }


    public OrderDTO getOrderById(Long orderId, Long userId) {
    Order order = orderRepository.findById(orderId)
            .orElseThrow(() -> new ResourceNotFoundException("Order not found: " + orderId));

    if (!order.getUser().getUserId().equals(userId)) {
        throw new UnauthorizedException("You cannot access this order");
    }

    return new OrderDTO(order);
    }


    @Transactional
    public OrderDTO createOrder(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
        Order order = new Order();
        order.setUser(user);
        order.setStatus(OrderStatus.PENDING);
        order.setOrderDate(LocalDate.now());
        order.setDeliveredDate(null);
        order.setTotalAmount(0.0);
        order.setOrderItems(new java.util.ArrayList<>());

        Order saved = orderRepository.save(order);
        return new OrderDTO(saved);
    }

    @Transactional
    public Order createBuyNowOrder(Long userId, Long productId, int quantity) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        if (product.getQuantity() < quantity) {
            throw new RuntimeException("Not enough stock available");
        }

        Order order = new Order();
        order.setUser(user);
        order.setStatus(OrderStatus.PENDING);
        order.setOrderDate(LocalDate.now());
        orderRepository.save(order);

        OrderItem orderItem = new OrderItem();
        orderItem.setOrder(order);
        orderItem.setProduct(product);
        orderItem.setQuantity(quantity);
        orderItem.setUnitPrice(product.getPrice());
        orderItem.setTotalPrice(product.getPrice() * quantity);
        orderItemRepository.save(orderItem);

        product.setQuantity(product.getQuantity() - quantity);
        productRepository.save(product);

        return order;
    }
    
    public void cancelOrder(Long orderId, Long userId) {
    Order order = orderRepository.findById(orderId)
            .orElseThrow(() -> new ResourceNotFoundException("Order not found"));

    if (!order.getStatus().equals(OrderStatus.PENDING) &&
        !order.getStatus().equals(OrderStatus.PROCESSING)) {
        throw new ValidationException("Order cannot be canceled at this stage");
    }
    for (OrderItem item : order.getOrderItems()) {
        Product product = item.getProduct();
        product.setQuantity(product.getQuantity() + item.getQuantity());
        productRepository.save(product);
    }
    order.setStatus(OrderStatus.CANCELLED);
    orderRepository.save(order);
    }

    @Transactional
    public OrderDTO updateOrderStatus(Long orderId, OrderStatus newStatus) {
    Order order = orderRepository.findById(orderId)
            .orElseThrow(() -> new ResourceNotFoundException("Order not found"));

    if (order.getStatus() == OrderStatus.CANCELLED) {
        throw new ValidationException("Cannot update a cancelled order");
    }

    order.setStatus(newStatus);

    if (newStatus == OrderStatus.DELIVERED) {
        order.setDeliveredDate(LocalDate.now());
    }

    Order saved = orderRepository.save(order);
    return new OrderDTO(saved);
    }

    @Transactional
    public OrderDTO checkoutOrder(Long orderId, Long userId) {
    Order order = orderRepository.findById(orderId)
            .orElseThrow(() -> new ResourceNotFoundException("Order not found"));

    if (!order.getUser().getUserId().equals(userId)) {
        throw new UnauthorizedException("You cannot checkout this order");
    }
    if (order.getOrderItems().isEmpty()) {
        throw new ValidationException("Order has no items");
    }

    for (OrderItem item : order.getOrderItems()) {
        Product product = item.getProduct();
        if (product.getQuantity() < item.getQuantity()) {
            throw new ValidationException("Not enough stock for product: " + product.getName());
        }
        product.setQuantity(product.getQuantity() - item.getQuantity());
        productRepository.save(product);
    }

    order.setStatus(OrderStatus.PROCESSING);
    orderRepository.save(order);

    return new OrderDTO(order);
    }


}

