package com.greeting.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.greeting.rquest.CustomerRequest;
import com.greeting.service.CustomerService;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * class {@link CustomerControllerValidationTest} having the Validation for Red Refactor testing,
 * Green Refactor testing is for class {@link CustomerControllerTest}
 */
@WebMvcTest(CustomerController.class)
class CustomerControllerValidationTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    CustomerService customerService;

    @Autowired
    ObjectMapper objectMapper;

    @NullSource
    @ParameterizedTest
    @ValueSource(strings = {"", "  ", "s", "s..", "123", ")*&", "aa bb cc dd ee ff gg hh"})
    void whenCustomerNameHasInvalidDataThenThenReturnBadRequest(String name) throws Exception {
        var customer = new CustomerRequest(name, "tech.sagir@gmail.com", "9052708146");
        mockMvc.perform(post("/customer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(customer)))
                .andExpect(status().isBadRequest()).andReturn();
    }

    @NullSource
    @ParameterizedTest
    @ValueSource(strings = {"", "  ", "s", "s..", "123", ")*&", "aa bb cc dd ee ff gg hh",
            "abc..def@mail.com", ".abc@mail.com"})
    void whenCustomerEmailHasInvalidDataThenThenReturnBadRequest(String email) throws Exception {
        var customer = new CustomerRequest("sagir", email, "9052708146");
        mockMvc.perform(post("/customer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(customer)))
                .andExpect(status().isBadRequest()).andReturn();
    }

    @NullSource
    @ParameterizedTest
    @ValueSource(strings = {"", "  ", "s", "s..", "123", ")*&", "aa bb cc dd ee ff gg hh",
            "abc..def@mail.com", ".abc@mail.com", "12345", "123456789012345"})
    void whenCustomerMobileHasInvalidDataThenReturnBadRequest(String mobile) throws Exception {
        var customer = new CustomerRequest("sagir", "tech.sagir@gmail.com", mobile);
        mockMvc.perform(post("/customer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(customer)))
                .andExpect(status().isBadRequest()).andReturn();
    }
}
