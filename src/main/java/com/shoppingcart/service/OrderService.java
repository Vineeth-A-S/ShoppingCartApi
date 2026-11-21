package com.shoppingcart.service;

import com.shoppingcart.dto.CartSummaryDTO;
import com.shoppingcart.dto.CheckoutRequest;
import com.shoppingcart.exception.BadRequestException;
import com.shoppingcart.exception.InsufficientStockException;
import com.shoppingcart.model.CartItem;
import com.shoppingcart.model.Order;
import com.shoppingcart.model.OrderItem;
import com.shoppingcart.model.Product;
import com.shoppingcart.repository.CartItemRepository;
import com.shoppingcart.repository.OrderRepository;
import com.shoppingcart.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class OrderService {

    private final CartService cartService;
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;

    public OrderService(CartService cartService, CartItemRepository cartItemRepository,
                        ProductRepository productRepository, OrderRepository orderRepository) {
        this.cartService = cartService;
        this.cartItemRepository = cartItemRepository;
        this.productRepository = productRepository;
        this.orderRepository = orderRepository;
    }

    public Order checkout(CheckoutRequest request) {
        if (request.getCustomerName() == null || request.getCustomerName().trim().isEmpty()) {
            throw new BadRequestException("Customer name is required");
        }
        if (request.getEmail() == null || request.getEmail().trim().isEmpty()) {
            throw new BadRequestException("Email is required");
        }
        if (request.getShippingAddress() == null || request.getShippingAddress().trim().isEmpty()) {
            throw new BadRequestException("Shipping address is required");
        }

        List<CartItem> cartItems = cartItemRepository.findAll();
        if (cartItems.isEmpty()) {
            throw new BadRequestException("Cart is empty. Cannot checkout.");
        }

        // Validate stock for all items
        for (CartItem item : cartItems) {
            Product product = item.getProduct();
            if (item.getQuantity() > product.getStockQuantity()) {
                throw new InsufficientStockException("Not enough stock for " + product.getName() +
                        ". Available: " + product.getStockQuantity() + ", requested: " + item.getQuantity());
            }
        }

        // Calculate totals using CartService summary logic
        CartSummaryDTO summary = cartService.getCartSummary(request.getCouponCode());

        String orderNumber = "ORD-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        String paymentMethod = (request.getPaymentMethod() != null && !request.getPaymentMethod().trim().isEmpty())
                ? request.getPaymentMethod().trim()
                : "Credit Card";

        Order order = new Order(
                orderNumber,
                request.getCustomerName().trim(),
                request.getEmail().trim(),
                request.getShippingAddress().trim(),
                paymentMethod,
                summary.getSubtotal(),
                summary.getDiscount(),
                summary.getTax(),
                summary.getShippingCost(),
                summary.getGrandTotal(),
                LocalDateTime.now()
        );

        // Deduct inventory and attach order items
        for (CartItem cartItem : cartItems) {
            Product product = cartItem.getProduct();
            product.setStockQuantity(product.getStockQuantity() - cartItem.getQuantity());
            productRepository.save(product);

            OrderItem orderItem = new OrderItem(
                    product.getId(),
                    product.getName(),
                    product.getImageUrl(),
                    product.getPrice(),
                    cartItem.getQuantity(),
                    Math.round(product.getPrice() * cartItem.getQuantity() * 100.0) / 100.0
            );
            order.addItem(orderItem);
        }

        Order savedOrder = orderRepository.save(order);

        // Clear cart after checkout
        cartService.clearCart();

        return savedOrder;
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAllByOrderByCreatedAtDesc();
    }

    public Order getOrderById(Long id) {
        return orderRepository.findById(id).orElse(null);
    }
}
