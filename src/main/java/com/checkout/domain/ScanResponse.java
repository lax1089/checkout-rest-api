package com.checkout.domain;

import java.math.BigDecimal;
import java.util.TreeMap;

public class ScanResponse {
	
	private String productCode;
	private TreeMap<Integer, BigDecimal> pricing;
	
	public String getProductCode() {
		return productCode;
	}
	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}

	public TreeMap<Integer, BigDecimal> getPricing() {
		return pricing;
	}
	public void setPricing(TreeMap<Integer, BigDecimal> pricing) {
		this.pricing = pricing;
	}

}
