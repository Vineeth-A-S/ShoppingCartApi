package com.shoppingcart.service;

import com.shoppingcart.dto.CartItemDTO;
import com.shoppingcart.dto.CartSummaryDTO;
import com.shoppingcart.exception.BadRequestException;
import com.shoppingcart.exception.InsufficientStockException;
import com.shoppingcart.exception.ResourceNotFoundException;
import com.shoppingcart.model.CartItem;
import com.shoppingcart.model.Product;
import com.shoppingcart.repository.CartItemRepository;
import com.shoppingcart.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class CartService {

    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;

    public CartService(CartItemRepository cartItemRepository, ProductRepository productRepository) {
        this.cartItemRepository = cartItemRepository;
        this.productRepository = productRepository;
    }

    public CartSummaryDTO getCartSummary(String couponCode) {
        List<CartItem> items = cartItemRepository.findAll();

        List<CartItemDTO> dtos = items.stream().map(item -> {
            Product p = item.getProduct();
            double lineTotal = round(p.getPrice() * item.getQuantity());
            return new CartItemDTO(
                    item.getId(),
                    p.getId(),
                    p.getName(),
                    p.getDescription(),
                    p.getPrice(),
                    p.getImageUrl(),
                    p.getCategory(),
                    p.getStockQuantity(),
                    item.getQuantity(),
                    lineTotal
            );
        }).collect(Collectors.toList());

        int totalItems = items.stream().mapToInt(CartItem::getQuantity).sum();
        double subtotal = round(dtos.stream().mapToDouble(CartItemDTO::getLineTotal).sum());

        double discount = 0.0;
        boolean couponApplied = false;
        String couponMessage = null;

        if (couponCode != null && !couponCode.trim().isEmpty() && subtotal > 0) {
            String code = couponCode.trim().toUpperCase();
            if ("SAVE10".equals(code)) {
                discount = round(subtotal * 0.10);
                couponApplied = true;
                couponMessage = "10% discount applied successfully!";
            } else if ("SAVE20".equals(code)) {
                discount = round(subtotal * 0.20);
                couponApplied = true;
                couponMessage = "20% special discount applied!";
            } else if ("FREESHIP".equals(code)) {
                couponApplied = true;
                couponMessage = "Free shipping unlocked!";
            } else {
                couponMessage = "Invalid coupon code";
            }
        }

        double discountedSubtotal = Math.max(0.0, subtotal - discount);

        double standardShipping = 9.99;
        double shippingCost = 0.0;
        if (subtotal > 0) {
            if (subtotal >= 100.0 || (couponApplied && "FREESHIP".equalsIgnoreCase(couponCode))) {
                shippingCost = 0.0;
            } else {
                shippingCost = standardShipping;
            }
        }

        double tax = round(discountedSubtotal * 0.08); // 8% sales tax
        double grandTotal = round(discountedSubtotal + tax + shippingCost);

        CartSummaryDTO summary = new CartSummaryDTO();
        summary.setItems(dtos);
        summary.setTotalItems(totalItems);
        summary.setSubtotal(subtotal);
        summary.setDiscount(discount);
        summary.setCouponCode(couponCode);
        summary.setCouponApplied(couponApplied);
        summary.setCouponMessage(couponMessage);
        summary.setTax(tax);
        summary.setShippingCost(shippingCost);
        summary.setGrandTotal(grandTotal);

        return summary;
    }

    public CartItemDTO addToCart(Long productId, Integer quantity) {
        if (quantity == null || quantity <= 0) {
            throw new BadRequestException("Quantity must be greater than 0");
        }

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + productId));

        Optional<CartItem> existingItemOpt = cartItemRepository.findByProductId(productId);

        CartItem itemToSave;
        if (existingItemOpt.isPresent()) {
            CartItem existingItem = existingItemOpt.get();
            int newQty = existingItem.getQuantity() + quantity;
            if (newQty > product.getStockQuantity()) {
                throw new InsufficientStockException(
                        "Cannot add " + quantity + " more. Only " + product.getStockQuantity() +
                                " available in stock, and you already have " + existingItem.getQuantity() + " in your cart."
                );
            }
            existingItem.setQuantity(newQty);
            itemToSave = existingItem;
        } else {
            if (quantity > product.getStockQuantity()) {
                throw new InsufficientStockException(
                        "Requested quantity (" + quantity + ") exceeds available stock (" + product.getStockQuantity() + ")."
                );
            }
            itemToSave = new CartItem(product, quantity);
        }

        CartItem saved = cartItemRepository.save(itemToSave);
        return new CartItemDTO(
                saved.getId(),
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getImageUrl(),
                product.getCategory(),
                product.getStockQuantity(),
                saved.getQuantity(),
                round(product.getPrice() * saved.getQuantity())
        );
    }

    public CartItemDTO updateItemQuantity(Long cartItemId, Integer quantity) {
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart item not found with id: " + cartItemId));

        if (quantity == null || quantity <= 0) {
            cartItemRepository.delete(cartItem);
            return null;
        }

        Product product = cartItem.getProduct();
        if (quantity > product.getStockQuantity()) {
            throw new InsufficientStockException(
                    "Cannot set quantity to " + quantity + ". Only " + product.getStockQuantity() + " items available in stock."
            );
        }

        cartItem.setQuantity(quantity);
        CartItem saved = cartItemRepository.save(cartItem);
        return new CartItemDTO(
                saved.getId(),
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getImageUrl(),
                product.getCategory(),
                product.getStockQuantity(),
                saved.getQuantity(),
                round(product.getPrice() * saved.getQuantity())
        );
    }

    public void removeFromCart(Long cartItemId) {
        if (!cartItemRepository.existsById(cartItemId)) {
            throw new ResourceNotFoundException("Cart item not found with id: " + cartItemId);
        }
        cartItemRepository.deleteById(cartItemId);
    }

    public void clearCart() {
        cartItemRepository.deleteAll();
    }

    private double round(double value) {
        return BigDecimal.valueOf(value).setScale(2, RoundingMode.HALF_UP).doubleValue();
    }
}
