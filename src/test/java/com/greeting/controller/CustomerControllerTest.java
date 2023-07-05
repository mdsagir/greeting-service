package com.greeting.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.greeting.dto.ResponseDto;
import com.greeting.exception.ConflictException;
import com.greeting.rquest.CustomerRequest;
import com.greeting.service.CustomerService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static com.greeting.dto.ResponseDto.responseMessage;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CustomerController.class)
class CustomerControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    CustomerService customerService;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    void whenNewCustomerThenReturn201() throws Exception {
        Long id = 10L;
        var customer = new CustomerRequest("sagir", "tech.sagir@gmail.com", "9052708146");
        given(customerService.addCustomer(customer)).willReturn(String.valueOf(id));
        var mvcResult = mockMvc.perform(post("/customer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(customer)))
                .andExpect(status().isCreated()).andReturn();
        var location = mvcResult.getResponse().getHeader("Location");
        assertThat(location).isNotNull().contains("/customer/" + id);

    }

    @Test
    void whenAddNewCustomerWithThrowConflictExceptionThenReturn409() throws Exception {
        String errorMessage = "email already exists";
        var customer = new CustomerRequest("sagir", "tech.sagir@gmail.com", "9052708146");
        given(customerService.addCustomer(customer)).willThrow(new ConflictException(errorMessage));
        var mvcResult = mockMvc.perform(post("/customer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(customer)))
                .andExpect(status().isConflict()).andReturn();
        var location = mvcResult.getResponse().getHeader("Location");
        assertThat(location).isNull();
        var readValue = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), ResponseDto.class);
        assertThat(readValue).isEqualTo(responseMessage(errorMessage));
    }

    @Test
    void whenAddNewCustomerWithThrowRuntimeExceptionThenReturn500() throws Exception {
        var customer = new CustomerRequest("sagir", "tech.sagir@gmail.com", "9052708146");
        given(customerService.addCustomer(customer)).willThrow(RuntimeException.class);
        var mvcResult = mockMvc.perform(post("/customer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(customer)))
                .andExpect(status().isInternalServerError()).andReturn();
        var location = mvcResult.getResponse().getHeader("Location");
        assertThat(location).isNull();
    }

}
