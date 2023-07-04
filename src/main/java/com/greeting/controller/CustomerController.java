package com.greeting.controller;

import com.greeting.dto.CustomerDto;
import com.greeting.rquest.CustomerRequest;
import com.greeting.service.CustomerService;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("customer")
public class CustomerController {


    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @PostMapping
    public ResponseEntity<Void> newCustomer(@Validated @RequestBody CustomerRequest customerRequest, UriComponentsBuilder ucb) {
        var id = customerService.addCustomer(customerRequest);
        var locationOfNewCustomer = ucb
                .path("customer/{customerId}")
                .buildAndExpand(id)
                .toUri();
        return ResponseEntity.created(locationOfNewCustomer).build();
    }

    @GetMapping("/{customerId}")
    public ResponseEntity<CustomerDto> getCustomerById(@PathVariable Long customerId) {
        var customer = customerService.fetchCustomerById(customerId);
        return customer.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());

    }

    @GetMapping
    public ResponseEntity<List<CustomerDto>> getAllCustomer(@ParameterObject Pageable pageable) {
        var customers = customerService.fetchAllCustomer(pageable);
        return new ResponseEntity<>(customers, HttpStatus.OK);
    }
}
