package com.checkout.service.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.BDDMockito.given;

import java.math.BigDecimal;
import java.util.TreeMap;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.checkout.dao.ProductRepository;
import com.checkout.exceptions.UnknownProductException;
import com.checkout.service.CheckoutService;

/**
 * Unit Tests
 * @author Alex Page
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class CheckoutServiceTests {
	
	@Mock
	private ProductRepository mockProductRepository;
	
	private CheckoutService checkoutService;

	@Before
	public void setUp(){
		checkoutService = new CheckoutService(mockProductRepository);
		TreeMap<Integer, BigDecimal> mockProductPricing = new TreeMap<>();
		mockProductPricing.put(1, new BigDecimal(1.00));
		mockProductPricing.put(6, new BigDecimal(5.00));
		mockProductPricing.put(10, new BigDecimal(7.00));
		given(this.mockProductRepository.getSpecificProductPricing("C")).willReturn(mockProductPricing);
		given(this.mockProductRepository.getSpecificProductPricing("B")).willReturn(null);
	}

	@Test
	public void getProductPricing_ValidProductCode() throws UnknownProductException {
		TreeMap<Integer, BigDecimal> pricing = checkoutService.getProductPricing("C");
		assertEquals(new BigDecimal(1.00), pricing.get(1));
		assertEquals(new BigDecimal(5.00), pricing.get(6));
		assertEquals(new BigDecimal(7.00), pricing.get(10));
	}
	
	@Test
	public void getProductPricing_InvalidProductCode() throws UnknownProductException {
		TreeMap<Integer, BigDecimal> pricing = null;
		try {
			pricing = checkoutService.getProductPricing("B");
		} catch (UnknownProductException upe) {
			assertTrue("B".equals(upe.getMissingProductCode()));
		}
		assertNull(pricing);
	}
	
	@Test
	public void determineCartCost_ValidProductCode_Qty19() throws UnknownProductException {
		BigDecimal itemCost = checkoutService.determineCartCost("CCCCCCCCCCCCCCCCCCC");
		assertEquals(new BigDecimal(15.00), itemCost);
	}
	
	@Test
	public void determineCartCost_ValidProductCode_Qty10() throws UnknownProductException {
		BigDecimal itemCost = checkoutService.determineCartCost("CCCCCCCCCC");
		assertEquals(new BigDecimal(7.00), itemCost);
	}
	
	@Test
	public void determineCartCost_ValidProductCode_Qty1() throws UnknownProductException {
		BigDecimal itemCost = checkoutService.determineCartCost("C");
		assertEquals(new BigDecimal(1.00), itemCost);
	}
	
	@Test
	public void determineCartCost_InvalidProductCode() throws UnknownProductException {
		BigDecimal itemCost = null;
		try {
			itemCost = checkoutService.determineCartCost("B");
		} catch (UnknownProductException upe) {
			assertTrue("B".equals(upe.getMissingProductCode()));
		}
		assertNull(itemCost);
	}
	
}
