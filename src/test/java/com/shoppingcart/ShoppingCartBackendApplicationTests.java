package com.shoppingcart;

import com.shoppingcart.dto.CartItemDTO;
import com.shoppingcart.dto.CartSummaryDTO;
import com.shoppingcart.dto.CheckoutRequest;
import com.shoppingcart.model.Order;
import com.shoppingcart.model.Product;
import com.shoppingcart.repository.ProductRepository;
import com.shoppingcart.service.CartService;
import com.shoppingcart.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ShoppingCartBackendApplicationTests {

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private CartService cartService;

	@Autowired
	private OrderService orderService;

	@BeforeEach
	void setUp() {
		cartService.clearCart();
	}

	@Test
	void contextLoads() {
		assertTrue(productRepository.count() > 0, "Initial product catalog should not be empty");
	}

	@Test
	void testCartFlowAndCheckout() {
		Product product = productRepository.findAll().get(0);
		int initialStock = product.getStockQuantity();

		// 1. Add to cart
		CartItemDTO item = cartService.addToCart(product.getId(), 2);
		assertNotNull(item);
		assertEquals(2, item.getQuantity());

		// 2. Verify cart summary
		CartSummaryDTO summary = cartService.getCartSummary("SAVE10");
		assertEquals(1, summary.getItems().size());
		assertEquals(2, summary.getTotalItems());
		assertTrue(summary.getCouponApplied());
		assertTrue(summary.getDiscount() > 0);

		// 3. Update quantity
		CartItemDTO updated = cartService.updateItemQuantity(item.getId(), 3);
		assertEquals(3, updated.getQuantity());

		// 4. Checkout
		CheckoutRequest req = new CheckoutRequest(
				"John Doe",
				"john@example.com",
				"123 Tech Boulevard, Silicon Valley, CA",
				"Credit Card",
				"SAVE10"
		);

		Order order = orderService.checkout(req);
		assertNotNull(order);
		assertNotNull(order.getOrderNumber());
		assertEquals("John Doe", order.getCustomerName());

		// 5. Verify stock deducted
		Product refreshed = productRepository.findById(product.getId()).orElseThrow();
		assertEquals(initialStock - 3, refreshed.getStockQuantity());

		// 6. Verify cart is empty after checkout
		CartSummaryDTO emptySummary = cartService.getCartSummary(null);
		assertEquals(0, emptySummary.getTotalItems());
	}
}
