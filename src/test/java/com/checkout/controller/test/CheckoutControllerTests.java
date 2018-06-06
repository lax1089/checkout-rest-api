package com.checkout.controller.test;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Integration Tests
 * Assumes the mocked data exists as provided in original requirements.
 * If a real data source would be used, the tests may fail based on the assumptions
 * they make in their current state, defined below
 * @author Alex Page
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class CheckoutControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void scanProduct_NoParam_ShouldReturn400() throws Exception {
        this.mockMvc.perform(get("/scanProduct")).andDo(print())
        		.andExpect(status().isBadRequest());
    }
    
    @Test
    public void scanProduct_InvalidProduct_ShouldReturn400() throws Exception {
        this.mockMvc.perform(get("/scanProduct?productCode=N")).andDo(print())
        		.andExpect(status().isBadRequest());
    }
    
    @Test
    public void scanProduct_ValidProduct_ShouldReturnProductPricing() throws Exception {
        this.mockMvc.perform(get("/scanProduct?productCode=C")).andDo(print())
        		.andExpect(status().isOk())
                .andExpect(jsonPath("$.productCode", is("C")))
                .andExpect(jsonPath("$.pricing.1", is(1)))
                .andExpect(jsonPath("$.pricing.6", is(5)));
    }
    
    @Test
    public void calculateTotal_NoParam_ShouldReturn400() throws Exception {
        this.mockMvc.perform(get("/calculateTotal")).andDo(print())
        		.andExpect(status().isBadRequest());
    }
    
    @Test
    public void calculateTotal_InvalidCartItemCode_ShouldReturn400() throws Exception {
        this.mockMvc.perform(get("/calculateTotal?cartItems=ABCDN")).andDo(print())
        		.andExpect(status().isBadRequest());
    }
    
    @Test
    public void calculateTotal_InvalidCartItemCharacter_ShouldReturn400() throws Exception {
        this.mockMvc.perform(get("/calculateTotal?cartItems=ABCD-")).andDo(print())
        		.andExpect(status().isBadRequest());
    }
    
    @Test
    public void calculateTotal_ValidCartItems_ShouldReturnTotalCost_ABCDABA() throws Exception {
        this.mockMvc.perform(get("/calculateTotal?cartItems=ABCDABA")).andDo(print())
        		.andExpect(status().isOk())
                .andExpect(jsonPath("$.cartItems", is("ABCDABA")))
                .andExpect(jsonPath("$.total", is(13.25)));
    }
    
    @Test
    public void calculateTotal_ValidCartItems_ShouldReturnTotalCost_CCCCCCC() throws Exception {
        this.mockMvc.perform(get("/calculateTotal?cartItems=CCCCCCC")).andDo(print())
        		.andExpect(status().isOk())
                .andExpect(jsonPath("$.cartItems", is("CCCCCCC")))
                .andExpect(jsonPath("$.total", is(6)));
    }
    
    @Test
    public void calculateTotal_ValidCartItems_ShouldReturnTotalCost_ABCD() throws Exception {
        this.mockMvc.perform(get("/calculateTotal?cartItems=ABCD")).andDo(print())
        		.andExpect(status().isOk())
                .andExpect(jsonPath("$.cartItems", is("ABCD")))
                .andExpect(jsonPath("$.total", is(7.25)));
    }
    
    @Test
    public void calculateTotal_ValidCartItems_ShouldReturnTotalCost_AAABBCCCCCCDDACCCCCC() throws Exception {
        this.mockMvc.perform(get("/calculateTotal?cartItems=AAABBCCCCCCDDACCCCCC")).andDo(print())
        		.andExpect(status().isOk())
                .andExpect(jsonPath("$.cartItems", is("AAABBCCCCCCDDACCCCCC")))
                .andExpect(jsonPath("$.total", is(24.25)));
    }

}
