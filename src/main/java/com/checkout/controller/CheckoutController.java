package com.checkout.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.checkout.domain.CheckoutResponse;
import com.checkout.domain.ErrorResponse;
import com.checkout.domain.ScanResponse;
import com.checkout.exceptions.UnknownProductException;
import com.checkout.service.CheckoutService;

/**
 * Exposes two endpoints to external clients:
 * 1. scanProduct: allows for the scanning of a single product and 
 * returns the pricing structure of that product
 * 2. calculateTotal: takes in a string representation of the items in
 * a cart and returns the total cost after applying volume prices
 * @author Alex Page
 *
 */
@RestController
public class CheckoutController {

    private CheckoutService checkoutService;

    @Autowired
    private CheckoutController(CheckoutService checkoutService) {
    	this.checkoutService = checkoutService;
    }

    @RequestMapping(value = "/scanProduct",  method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<?> scanProduct(@RequestParam(value="productCode") String productCode) {
        ScanResponse response = new ScanResponse();
        
        response.setProductCode(productCode);
        try {
	        response.setPricing(checkoutService.getProductPricing(productCode));
        } catch (UnknownProductException upe) {
        	HttpStatus status = HttpStatus.BAD_REQUEST;
			return ResponseEntity.badRequest().body(new ErrorResponse(status.value(), status.getReasonPhrase(),
					"Invalid product code: " + upe.getMissingProductCode(), "/scanProduct"));
    	}
        
        return ResponseEntity.ok(response);
    }
    
    @RequestMapping(value = "/calculateTotal",  method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<?> calculateTotal(@RequestParam(value="cartItems") String cartItems) {
    	CheckoutResponse response = new CheckoutResponse();
        
    	response.setCartItems(cartItems);
    	try {
    		response.setTotal(checkoutService.determineCartCost(cartItems));
    	} catch (UnknownProductException upe) {
    		HttpStatus status = HttpStatus.BAD_REQUEST;
			return ResponseEntity.badRequest().body(new ErrorResponse(status.value(), status.getReasonPhrase(),
					"Invalid product code: " + upe.getMissingProductCode(), "/calculateTotal"));
    	}
    	
    	return ResponseEntity.ok(response);
    }
    
}
