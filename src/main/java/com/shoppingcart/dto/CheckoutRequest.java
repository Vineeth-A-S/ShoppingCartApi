package com.shoppingcart.dto;

public class CheckoutRequest {
    private String customerName;
    private String email;
    private String shippingAddress;
    private String paymentMethod;
    private String couponCode;

    public CheckoutRequest() {
    }

    public CheckoutRequest(String customerName, String email, String shippingAddress, String paymentMethod, String couponCode) {
        this.customerName = customerName;
        this.email = email;
        this.shippingAddress = shippingAddress;
        this.paymentMethod = paymentMethod;
        this.couponCode = couponCode;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(String shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getCouponCode() {
        return couponCode;
    }

    public void setCouponCode(String couponCode) {
        this.couponCode = couponCode;
    }
}
