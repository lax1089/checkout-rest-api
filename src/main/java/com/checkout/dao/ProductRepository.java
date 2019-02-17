package com.checkout.dao;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.TreeMap;

import org.springframework.stereotype.Repository;

/**
 * This repository class currently provides mocked data per the instructions of
 * this assignment, but could be quickly altered to actually connect to a real data
 * source (ie: database) with Spring JPA
 * @author Alex Page
 *
 */
@Repository
public class ProductRepository {
	
	private HashMap<String, TreeMap<Integer, BigDecimal>> productPricing;

	public ProductRepository() {
		this.productPricing = populateMockData();
	}

	public HashMap<String, TreeMap<Integer, BigDecimal>> getAllProductPricing() {
		return productPricing;
	}

	public TreeMap<Integer, BigDecimal> getSpecificProductPricing(String productCode) {
		return productPricing.get(productCode);
	}
	
	public String insertNewProduct(String productCode, BigDecimal basePrice) {
		if (productPricing.containsKey(productCode)) return "Failed, product exists";
		else {
			TreeMap<Integer, BigDecimal> newProductPricing = new TreeMap<>();
			newProductPricing.put(1, basePrice);
			productPricing.put(productCode, newProductPricing);
			printProductPricingRepo();
			return "Product "+productCode+" with base price "+basePrice+" successfully added";
		}
	}

	private HashMap<String, TreeMap<Integer, BigDecimal>> populateMockData() {
		TreeMap<Integer, BigDecimal> productPricingA = new TreeMap<>();
		productPricingA.put(1, new BigDecimal(1.25));
		productPricingA.put(3, new BigDecimal(3.00));

		TreeMap<Integer, BigDecimal> productPricingB = new TreeMap<>();
		productPricingB.put(1, new BigDecimal(4.25));

		TreeMap<Integer, BigDecimal> productPricingC = new TreeMap<>();
		productPricingC.put(1, new BigDecimal(1.00));
		productPricingC.put(6, new BigDecimal(5.00));

		TreeMap<Integer, BigDecimal> productPricingD = new TreeMap<>();
		productPricingD.put(1, new BigDecimal(0.75));

		HashMap<String, TreeMap<Integer, BigDecimal>> productPricing = new HashMap<>();
		productPricing.put("A", productPricingA);
		productPricing.put("B", productPricingB);
		productPricing.put("C", productPricingC);
		productPricing.put("D", productPricingD);

		return productPricing;
	}
	
	private void printProductPricingRepo() {
		for (String product : productPricing.keySet()) {
			System.out.println("Product Code: "+product);
			TreeMap<Integer, BigDecimal> currPricing = productPricing.get(product);
			for (Integer priceLine : currPricing.keySet()) {
				System.out.println(priceLine+":"+currPricing.get(priceLine));
			}
		}
	}

}
