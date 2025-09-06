package com.example.productorderManagement.service;

import java.time.LocalDate;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.productorderManagement.dto.response.OrderResponse;
import com.example.productorderManagement.exception.BadRequestException;
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

    @Cacheable(value = "orders", key = "'page:' + #page + ':size:' + #size")
    public Page<OrderResponse> getAllOrders(int page, int size) {
    Pageable pageable = PageRequest.of(page, size, Sort.by("orderDate").descending());

    return orderRepository.findAll(pageable)
                          .map(OrderResponse::new);
    }


    @Cacheable(value = "orders", key = "'user:' + #userId + ':page:' + #page + ':size:' + #size")
    public Page<OrderResponse> getOrdersForUser(Long userId, int page, int size) {
    Pageable pageable = PageRequest.of(page, size);
    return orderRepository.findByUser_UserId(userId, pageable)
                          .map(OrderResponse::new);
    }

    @Cacheable(value = "orders", key = "#orderId")
    public OrderResponse getOrderById(Long orderId, Long userId) {
    Order order = orderRepository.findById(orderId)
            .orElseThrow(() -> new ResourceNotFoundException("Order not found: " + orderId));

    if (!order.getUser().getUserId().equals(userId)) {
        throw new UnauthorizedException("You cannot access this order");
    }
    
    return new OrderResponse(order);
    }


    @Transactional
    @CacheEvict(value = "orders", allEntries = true)
    public OrderResponse createOrder(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
    if (orderRepository.existsByUser_UserIdAndStatus(userId, OrderStatus.PENDING)) {
    throw new BadRequestException("User already has a pending order");
    }

        Order order = new Order();
        order.setUser(user);
        order.setStatus(OrderStatus.PENDING);
        order.setOrderDate(LocalDate.now());
        order.setDeliveredDate(null);
        order.setTotalAmount(0.0);
        order.setOrderItems(new java.util.ArrayList<>());

        Order saved = orderRepository.save(order);
        return new OrderResponse(saved);
    }

    @Transactional
    @CacheEvict(value = "orders", allEntries = true)
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

    order.getOrderItems().add(orderItem);

    order.setTotalAmount(orderItem.getTotalPrice());
    orderRepository.save(order);

    product.setQuantity(product.getQuantity() - quantity);
    productRepository.save(product);

    return order;
}
    
    @Transactional
    @CacheEvict(value = "orders", allEntries = true)
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
    @CachePut(value = "orders", key = "#orderId") 
    @CacheEvict(value = "orders", key = "'user:' + #orderId") 
    public OrderResponse updateOrderStatus(Long orderId, OrderStatus newStatus) {
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
    return new OrderResponse(saved);
    }

    @Transactional
    @CachePut(value = "orders", key = "#orderId")
    @CacheEvict(value = "orders", key = "'user:' + #userId + ':page:' + '*'", allEntries = true)
    public OrderResponse checkoutOrder(Long orderId, Long userId) {
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

    if(order.getStatus() == OrderStatus.PROCESSING){
        throw new BadRequestException("This order is already checked out");
    }

    order.setStatus(OrderStatus.PROCESSING);

    double total = order.getOrderItems()
            .stream()
            .mapToDouble(OrderItem::getTotalPrice)
            .sum();
    order.setTotalAmount(total);

    orderRepository.save(order);

    return new OrderResponse(order);
}



}

