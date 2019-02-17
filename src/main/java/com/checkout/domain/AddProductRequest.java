package com.checkout.domain;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class AddProductRequest {
	
	private String productCode;
	private BigDecimal productPrice;

}
