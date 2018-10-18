package com.checkout.domain;

import java.math.BigDecimal;

public class AddProductRequest {
	
	private String productCode;
	private BigDecimal productPrice;

	public String getProductCode() {
		return productCode;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}

	public BigDecimal getProductPrice() {
		return productPrice;
	}

	public void setProductPrice(BigDecimal productPrice) {
		this.productPrice = productPrice;
	}

}
