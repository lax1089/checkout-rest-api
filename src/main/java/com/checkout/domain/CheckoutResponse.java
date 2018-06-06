package com.checkout.domain;

import java.math.BigDecimal;

public class CheckoutResponse {

	private String cartItems;
	private BigDecimal total;
	
	public String getCartItems() {
		return cartItems;
	}
	public void setCartItems(String cartItems) {
		this.cartItems = cartItems;
	}
	
	public BigDecimal getTotal() {
		return total;
	}
	public void setTotal(BigDecimal total) {
		this.total = total;
	}
	
}
