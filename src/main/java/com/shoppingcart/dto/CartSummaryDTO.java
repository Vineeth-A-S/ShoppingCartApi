package com.shoppingcart.dto;

import java.util.ArrayList;
import java.util.List;

public class CartSummaryDTO {
    private List<CartItemDTO> items = new ArrayList<>();
    private Integer totalItems = 0;
    private Double subtotal = 0.0;
    private Double discount = 0.0;
    private String couponCode;
    private Boolean couponApplied = false;
    private String couponMessage;
    private Double tax = 0.0;
    private Double shippingCost = 0.0;
    private Double grandTotal = 0.0;

    public CartSummaryDTO() {
    }

    public List<CartItemDTO> getItems() {
        return items;
    }

    public void setItems(List<CartItemDTO> items) {
        this.items = items;
    }

    public Integer getTotalItems() {
        return totalItems;
    }

    public void setTotalItems(Integer totalItems) {
        this.totalItems = totalItems;
    }

    public Double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(Double subtotal) {
        this.subtotal = subtotal;
    }

    public Double getDiscount() {
        return discount;
    }

    public void setDiscount(Double discount) {
        this.discount = discount;
    }

    public String getCouponCode() {
        return couponCode;
    }

    public void setCouponCode(String couponCode) {
        this.couponCode = couponCode;
    }

    public Boolean getCouponApplied() {
        return couponApplied;
    }

    public void setCouponApplied(Boolean couponApplied) {
        this.couponApplied = couponApplied;
    }

    public String getCouponMessage() {
        return couponMessage;
    }

    public void setCouponMessage(String couponMessage) {
        this.couponMessage = couponMessage;
    }

    public Double getTax() {
        return tax;
    }

    public void setTax(Double tax) {
        this.tax = tax;
    }

    public Double getShippingCost() {
        return shippingCost;
    }

    public void setShippingCost(Double shippingCost) {
        this.shippingCost = shippingCost;
    }

    public Double getGrandTotal() {
        return grandTotal;
    }

    public void setGrandTotal(Double grandTotal) {
        this.grandTotal = grandTotal;
    }
}
