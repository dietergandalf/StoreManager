package com.dietergandalf.store_manager.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dietergandalf.store_manager.dto.CheckoutRequestDto;
import com.dietergandalf.store_manager.dto.OrderDto;
import com.dietergandalf.store_manager.model.CartItem;
import com.dietergandalf.store_manager.model.Customer;
import com.dietergandalf.store_manager.model.Order;
import com.dietergandalf.store_manager.model.OrderItem;
import com.dietergandalf.store_manager.model.OrderStatus;
import com.dietergandalf.store_manager.model.ProductStock;
import com.dietergandalf.store_manager.model.ShoppingCart;
import com.dietergandalf.store_manager.repository.CartItemRepository;
import com.dietergandalf.store_manager.repository.CustomerRepository;
import com.dietergandalf.store_manager.repository.OrderItemRepository;
import com.dietergandalf.store_manager.repository.OrderRepository;
import com.dietergandalf.store_manager.repository.ProductStockRepository;
import com.dietergandalf.store_manager.util.OrderItemMapper;
import com.dietergandalf.store_manager.util.OrderMapper;

@Service
public class OrderService {
    
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final CustomerRepository customerRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductStockRepository productStockRepository;
    private final OrderMapper orderMapper;
    private final OrderItemMapper orderItemMapper;
    
    @Autowired
    public OrderService(OrderRepository orderRepository,
                       OrderItemRepository orderItemRepository,
                       CustomerRepository customerRepository,
                       CartItemRepository cartItemRepository,
                       ProductStockRepository productStockRepository,
                       OrderMapper orderMapper,
                       OrderItemMapper orderItemMapper) {
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.customerRepository = customerRepository;
        this.cartItemRepository = cartItemRepository;
        this.productStockRepository = productStockRepository;
        this.orderMapper = orderMapper;
        this.orderItemMapper = orderItemMapper;
    }
    
    @Transactional
    public OrderDto checkout(Long customerId, CheckoutRequestDto checkoutRequest) {
        Optional<Customer> optionalCustomer = customerRepository.findById(customerId);
        if (optionalCustomer.isEmpty()) {
            throw new RuntimeException("Customer not found");
        }
        
        Customer customer = optionalCustomer.get();
        ShoppingCart cart = customer.getCart();
        
        if (cart == null) {
            throw new RuntimeException("Shopping cart not found");
        }
        
        List<CartItem> cartItems = cartItemRepository.findByCart(cart);
        if (cartItems.isEmpty()) {
            throw new RuntimeException("Shopping cart is empty");
        }
        
        // Validate stock availability
        for (CartItem cartItem : cartItems) {
            ProductStock productStock = cartItem.getProductStock();
            if (productStock.getAmount() < cartItem.getQuantity()) {
                throw new RuntimeException("Insufficient stock for product: " + productStock.getProduct().getName());
            }
        }
        
        // Create order
        Order order = orderMapper.fromCartItemsAndRequest(customer, cartItems, checkoutRequest);
        Order savedOrder = orderRepository.save(order);
        
        // Create order items
        List<OrderItem> orderItems = orderItemMapper.fromCartItems(cartItems, savedOrder);
        orderItemRepository.saveAll(orderItems);
        savedOrder.setOrderItems(orderItems);
        
        // Update product stock
        for (CartItem cartItem : cartItems) {
            ProductStock productStock = cartItem.getProductStock();
            productStock.setAmount(productStock.getAmount() - cartItem.getQuantity());
            productStockRepository.save(productStock);
        }
        
        // Clear the shopping cart
        cartItemRepository.deleteByCart(cart);
        
        // Simulate payment processing
        savedOrder.setPaymentStatus("CONFIRMED");
        savedOrder.setStatus(OrderStatus.CONFIRMED);
        savedOrder = orderRepository.save(savedOrder);
        
        return orderMapper.toDto(savedOrder);
    }
    
    public List<OrderDto> getCustomerOrders(Long customerId) {
        List<Order> orders = orderRepository.findByCustomerPersonIdOrderByOrderDateDesc(customerId);
        return orderMapper.toDtoList(orders);
    }
    
    public OrderDto getOrderById(Long orderId) {
        Optional<Order> optionalOrder = orderRepository.findById(orderId);
        if (optionalOrder.isEmpty()) {
            return null;
        }
        return orderMapper.toDto(optionalOrder.get());
    }
    
    @Transactional
    public OrderDto updateOrderStatus(Long orderId, OrderStatus newStatus) {
        Optional<Order> optionalOrder = orderRepository.findById(orderId);
        if (optionalOrder.isEmpty()) {
            throw new RuntimeException("Order not found");
        }
        
        Order order = optionalOrder.get();
        order.setStatus(newStatus);
        Order updatedOrder = orderRepository.save(order);
        
        return orderMapper.toDto(updatedOrder);
    }
    
    public List<OrderDto> getAllOrders() {
        List<Order> orders = orderRepository.findAll();
        return orderMapper.toDtoList(orders);
    }
}
