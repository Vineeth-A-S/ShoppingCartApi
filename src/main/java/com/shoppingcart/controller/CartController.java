package com.shoppingcart.controller;

import com.shoppingcart.dto.AddToCartRequest;
import com.shoppingcart.dto.CartItemDTO;
import com.shoppingcart.dto.CartSummaryDTO;
import com.shoppingcart.dto.UpdateCartQuantityRequest;
import com.shoppingcart.service.CartService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping
    public ResponseEntity<CartSummaryDTO> getCart(@RequestParam(required = false) String coupon) {
        return ResponseEntity.ok(cartService.getCartSummary(coupon));
    }

    @PostMapping("/items")
    public ResponseEntity<CartItemDTO> addToCart(@RequestBody AddToCartRequest request) {
        CartItemDTO item = cartService.addToCart(request.getProductId(), request.getQuantity());
        return new ResponseEntity<>(item, HttpStatus.CREATED);
    }

    @PutMapping("/items/{id}")
    public ResponseEntity<CartItemDTO> updateQuantity(
            @PathVariable Long id,
            @RequestBody UpdateCartQuantityRequest request) {
        CartItemDTO item = cartService.updateItemQuantity(id, request.getQuantity());
        if (item == null) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(item);
    }

    @DeleteMapping("/items/{id}")
    public ResponseEntity<Void> removeFromCart(@PathVariable Long id) {
        cartService.removeFromCart(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> clearCart() {
        cartService.clearCart();
        return ResponseEntity.noContent().build();
    }
}
