package com.checkout.service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.TreeMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.checkout.dao.ProductRepository;
import com.checkout.exceptions.UnknownProductException;

@Component
public class CheckoutService {

	private ProductRepository productRepository;
	
	@Autowired
	public CheckoutService(ProductRepository productRepository) {
		this.productRepository = productRepository;
	}
	
	/**
	 * Attempts to fetch the pricing for a given product code from the data source.
	 * If the product is not found, an UnknownProductException is thrown, so callers
	 * of this method must handle that
	 * @param productCode
	 * @return productPricing
	 * @throws UnknownProductException
	 */
	public TreeMap<Integer, BigDecimal> getProductPricing(String productCode) throws UnknownProductException {
		TreeMap<Integer, BigDecimal> productPricing = productRepository.getSpecificProductPricing(productCode);
		if (productPricing == null) throw new UnknownProductException(productCode);
		return productPricing;
	}

	/**
	 * Populates an organized map from the cart item string provided on input
	 * and traverses the map to cross reference the quantity of each item with
	 * the available volume pricing for that item in order to calculate the total cost
	 * @param cartItems
	 * @return cartTotalCost
	 * @throws UnknownProductException
	 */
	public BigDecimal determineCartCost(String cartItems) throws UnknownProductException {
		HashMap<String, Integer> cartItemMap = populateCartItemMap(cartItems);
		BigDecimal cartTotalCost = BigDecimal.ZERO;
		
		// Loop through all products in my cart, and add the cost
		// based on the applicable volume pricing to determine total cart cost
		for (String productCode : cartItemMap.keySet()) {
			// Utilizing the reverse order of the TreeMap representing the volume pricing sheet,
			// we guarantee that we will get the best pricing for the customer based on
			// the available volume pricing and the quantity the customer is purchasing
			TreeMap<Integer, BigDecimal> productPricing = getProductPricing(productCode);
			
			// Calculate the total cost for this item, and add it to the running total
			BigDecimal itemTotalCost = calculateItemTotalCost(productPricing, cartItemMap.get(productCode));
			cartTotalCost = cartTotalCost.add(itemTotalCost);
		}
		
		return cartTotalCost;
	}

	/**
	 * This method takes the unordered string representation of the items in
	 * the cart and populates a map that is utilized in determining the total 
	 * cost based on the volume pricing that applies
	 * @param cartItems
	 * @return cartItemMap
	 */
	private HashMap<String, Integer> populateCartItemMap(String cartItems) {
		HashMap<String, Integer> cartItemMap = new HashMap<>();
		
		for (String productCode : cartItems.split("")) {
			Integer itemCount = cartItemMap.get(productCode);
			cartItemMap.put(productCode, (itemCount != null ? itemCount + 1 : 1));
		}
		
		return cartItemMap;
	}

	/**
	 * This method will take in the product pricing and the quantity of the product.
	 * It will iterate through the pricing sheet, starting with the highest volume price
	 * and descending down. This ensures the best pricing will be applied.
	 * @param productPricing
	 * @param quantityLeftToCalculate
	 * @return itemTotalCost
	 */
	private BigDecimal calculateItemTotalCost(TreeMap<Integer, BigDecimal> productPricing, Integer quantityLeftToCalculate) {
		BigDecimal itemTotalCost = BigDecimal.ZERO;
		
		for (Integer volumeQuantity : productPricing.descendingKeySet()) {
			// The below 2 if statements aren't necessary, but allow us to short-circuit 
			// which slightly reduces unnecessary processing
			if (quantityLeftToCalculate == 0) break;
			if (volumeQuantity > quantityLeftToCalculate) continue;
			
			// Calculate the volume pricing for this item at this price
			Integer currVolumeBlocks = quantityLeftToCalculate / volumeQuantity;
			BigDecimal blockPrice = productPricing.get(volumeQuantity).multiply(new BigDecimal(currVolumeBlocks));
			
			// Add to running total for this item
			itemTotalCost = itemTotalCost.add(blockPrice);
			// Subtract the remaining quantity of this item I have left to calculate
			quantityLeftToCalculate -= currVolumeBlocks * volumeQuantity;
		}
		
		return itemTotalCost;
	}
}
