package com.dietergandalf.store_manager.service;

import com.dietergandalf.store_manager.dto.AddToCartRequestDto;
import com.dietergandalf.store_manager.dto.CustomerDto;
import com.dietergandalf.store_manager.dto.ProductStockDto;
import com.dietergandalf.store_manager.dto.RegisterRequestDto;
import com.dietergandalf.store_manager.dto.ShoppingCartDto;
import com.dietergandalf.store_manager.dto.UpdateProfileRequestDto;
import com.dietergandalf.store_manager.model.CartItem;
import com.dietergandalf.store_manager.model.Customer;
import com.dietergandalf.store_manager.model.ProductStock;
import com.dietergandalf.store_manager.model.ShoppingCart;
import com.dietergandalf.store_manager.repository.CartItemRepository;
import com.dietergandalf.store_manager.repository.CustomerRepository;
import com.dietergandalf.store_manager.repository.ProductStockRepository;
import com.dietergandalf.store_manager.repository.ShoppingCartRepository;
import com.dietergandalf.store_manager.util.CartItemMapper;
import com.dietergandalf.store_manager.util.CustomerMapper;
import com.dietergandalf.store_manager.util.ProductMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class CustomerService {
    private final CustomerRepository customerRepository;
    private final ProductStockRepository productStockRepository;
    private final ShoppingCartRepository shoppingCartRepository;
    private final CartItemRepository cartItemRepository;
    private final CustomerMapper customerMapper;
    private final ProductMapper productMapper;
    private final CartItemMapper cartItemMapper;

    @Autowired
    public CustomerService(CustomerRepository customerRepository, 
                          ProductStockRepository productStockRepository,
                          ShoppingCartRepository shoppingCartRepository,
                          CartItemRepository cartItemRepository,
                          CustomerMapper customerMapper,
                          ProductMapper productMapper,
                          CartItemMapper cartItemMapper) {
        this.customerRepository = customerRepository;
        this.productStockRepository = productStockRepository;
        this.shoppingCartRepository = shoppingCartRepository;
        this.cartItemRepository = cartItemRepository;
        this.customerMapper = customerMapper;
        this.productMapper = productMapper;
        this.cartItemMapper = cartItemMapper;
    }

    public List<CustomerDto> getAllCustomers() {
        List<Customer> customers = customerRepository.findAll();
        return customerMapper.toDtoList(customers);
    }

    public CustomerDto getCustomerById(Long id) {
        Optional<Customer> customer = customerRepository.findById(id);
        return customer.map(customerMapper::toDto).orElse(null);
    }

    public CustomerDto createCustomer(RegisterRequestDto registerRequest) {
        if (customerRepository.existsByEmail(registerRequest.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        Customer customer = customerMapper.fromRegisterRequest(registerRequest);
        Customer savedCustomer = customerRepository.save(customer);
        return customerMapper.toDto(savedCustomer);
    }

    public CustomerDto updateCustomer(Long id, UpdateProfileRequestDto updateRequest) {
        Optional<Customer> optionalCustomer = customerRepository.findById(id);
        if (optionalCustomer.isEmpty()) {
            return null;
        }

        Customer customer = optionalCustomer.get();
        customerMapper.updateFromDto(customer, updateRequest);
        Customer updatedCustomer = customerRepository.save(customer);
        return customerMapper.toDto(updatedCustomer);
    }

    public boolean deleteCustomer(Long id) {
        if (customerRepository.existsById(id)) {
            customerRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public List<ProductStockDto> getAllAvailableProducts() {
        List<ProductStock> availableProducts = productStockRepository.findByAmountGreaterThan(0);
        return productMapper.toStockDtoList(availableProducts);
    }

    @Transactional
    public ShoppingCartDto addToCart(Long customerId, AddToCartRequestDto addToCartRequest) {
        Optional<Customer> optionalCustomer = customerRepository.findById(customerId);
        if (optionalCustomer.isEmpty()) {
            throw new RuntimeException("Customer not found");
        }

        Optional<ProductStock> optionalProductStock = productStockRepository.findById(addToCartRequest.getProductStockId());
        if (optionalProductStock.isEmpty()) {
            throw new RuntimeException("Product not found");
        }

        Customer customer = optionalCustomer.get();
        ProductStock productStock = optionalProductStock.get();

        if (productStock.getAmount() < addToCartRequest.getQuantity()) {
            throw new RuntimeException("Insufficient stock");
        }

        // Get or create shopping cart
        ShoppingCart cart = customer.getCart();
        if (cart == null) {
            cart = new ShoppingCart();
            cart.setCustomer(customer);
            customer.setCart(cart);
            cart = shoppingCartRepository.save(cart);
        }

        // Check if this product is already in the cart
        Optional<CartItem> existingCartItem = cartItemRepository.findByCartAndProductStock(cart, productStock);
        
        if (existingCartItem.isPresent()) {
            // Update quantity of existing item
            CartItem cartItem = existingCartItem.get();
            int newQuantity = cartItem.getQuantity() + addToCartRequest.getQuantity();
            
            // Check if we still have enough stock
            if (productStock.getAmount() < newQuantity) {
                throw new RuntimeException("Insufficient stock for requested quantity");
            }
            
            cartItem.setQuantity(newQuantity);
            cartItemRepository.save(cartItem);
        } else {
            // Create new cart item
            CartItem cartItem = new CartItem();
            cartItem.setCart(cart);
            cartItem.setProductStock(productStock);
            cartItem.setQuantity(addToCartRequest.getQuantity());
            cartItem.setPriceAtTimeOfAdd(productStock.getProduct().getPrice());
            cartItemRepository.save(cartItem);
        }

        return getCartDto(cart);
    }

    public ShoppingCartDto getCart(Long customerId) {
        Optional<Customer> optionalCustomer = customerRepository.findById(customerId);
        if (optionalCustomer.isEmpty()) {
            return null;
        }

        Customer customer = optionalCustomer.get();
        ShoppingCart cart = customer.getCart();
        
        if (cart == null) {
            return null;
        }

        return getCartDto(cart);
    }

    @Transactional
    public boolean clearCart(Long customerId) {
        Optional<Customer> optionalCustomer = customerRepository.findById(customerId);
        if (optionalCustomer.isEmpty()) {
            return false;
        }

        Customer customer = optionalCustomer.get();
        ShoppingCart cart = customer.getCart();
        
        if (cart != null) {
            cartItemRepository.deleteByCart(cart);
            return true;
        }
        
        return false;
    }

    @Transactional
    public ShoppingCartDto removeFromCart(Long customerId, Long cartItemId) {
        Optional<Customer> optionalCustomer = customerRepository.findById(customerId);
        if (optionalCustomer.isEmpty()) {
            throw new RuntimeException("Customer not found");
        }

        Optional<CartItem> optionalCartItem = cartItemRepository.findById(cartItemId);
        if (optionalCartItem.isEmpty()) {
            throw new RuntimeException("Cart item not found");
        }

        Customer customer = optionalCustomer.get();
        CartItem cartItem = optionalCartItem.get();

        // Verify the cart item belongs to this customer
        if (!cartItem.getCart().getCustomer().getPerson_id().equals(customerId)) {
            throw new RuntimeException("Cart item does not belong to this customer");
        }

        cartItemRepository.delete(cartItem);
        return getCartDto(customer.getCart());
    }

    @Transactional
    public ShoppingCartDto updateCartItemQuantity(Long customerId, Long cartItemId, Integer newQuantity) {
        if (newQuantity <= 0) {
            return removeFromCart(customerId, cartItemId);
        }

        Optional<Customer> optionalCustomer = customerRepository.findById(customerId);
        if (optionalCustomer.isEmpty()) {
            throw new RuntimeException("Customer not found");
        }

        Optional<CartItem> optionalCartItem = cartItemRepository.findById(cartItemId);
        if (optionalCartItem.isEmpty()) {
            throw new RuntimeException("Cart item not found");
        }

        Customer customer = optionalCustomer.get();
        CartItem cartItem = optionalCartItem.get();

        // Verify the cart item belongs to this customer
        if (!cartItem.getCart().getCustomer().getPerson_id().equals(customerId)) {
            throw new RuntimeException("Cart item does not belong to this customer");
        }

        // Check if we have enough stock
        ProductStock productStock = cartItem.getProductStock();
        if (productStock.getAmount() < newQuantity) {
            throw new RuntimeException("Insufficient stock for requested quantity");
        }

        cartItem.setQuantity(newQuantity);
        cartItemRepository.save(cartItem);

        return getCartDto(customer.getCart());
    }

    private ShoppingCartDto getCartDto(ShoppingCart cart) {
        List<CartItem> cartItems = cartItemRepository.findByCart(cart);
        
        double totalAmount = cartItems.stream()
                .mapToDouble(item -> item.getQuantity() * item.getPriceAtTimeOfAdd())
                .sum();
        
        int totalItems = cartItems.stream()
                .mapToInt(CartItem::getQuantity)
                .sum();

        return ShoppingCartDto.builder()
                .cartId(cart.getCart_id())
                .customerId(cart.getCustomer().getPerson_id())
                .customerName(cart.getCustomer().getFirst_name() + " " + cart.getCustomer().getLast_name())
                .cartItems(cartItemMapper.toDtoList(cartItems))
                .totalAmount(totalAmount)
                .totalItems(totalItems)
                .build();
    }
}
