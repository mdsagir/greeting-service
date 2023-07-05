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

import static com.greeting.util.EncryptionUtil.encode;

@RestController
@RequestMapping("customer")
public class CustomerController {


    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    /**
     * {@code POST /customer} API create a new customer
     * <p>
     * Primarily validate all mandatory fields, then checks database unique constraints (given email existence)
     * after persist {@link CustomerRequest} all property to the database and return status {@code 201 CREATED}.
     *
     * @param customerRequest payload {@link CustomerRequest} Its JSON API request contract send by consumer, It's not be {@literal null}.
     * @return Response entity {@link ResponseEntity} with no data and status {@code 201 (Created)}
     * @throws IllegalArgumentException                  in case the given {@link CustomerRequest requestBody} of its property is {@literal empty-string} or {@literal null}.
     * @throws com.greeting.exception.ConflictException  when anything email are already exist in database
     * @throws com.greeting.exception.SomethingWentWrong when anything went wrong to whole application level like database failure ...
     */
    @PostMapping
    public ResponseEntity<Void> newCustomer(@Validated @RequestBody CustomerRequest customerRequest, UriComponentsBuilder ucb) {
        var id = customerService.addCustomer(customerRequest);
        var locationOfNewCustomer = ucb
                .path("customer/{customerId}")
                .buildAndExpand(encode(id))
                .toUri();
        return ResponseEntity.created(locationOfNewCustomer).build();
    }

    /**
     * {@code GET customer/customerId} get customer info by id
     * <p>
     * In case the given invalid {@literal  id} that no record available and response give the {@code 404 Not found}
     *
     * @param customerId input request parameter, It's not be {@literal null}.
     * @return {@link ResponseEntity} with {@link CustomerDto} contain all information of particular customer with {@code 200} Success
     * @throws IllegalArgumentException                  in case the given {@literal  id parameter} of its property is {@literal empty-string} or {@literal null}
     *                                                   then validation exception are triggered and response give the {@code 400 Bad request}
     * @throws com.greeting.exception.SomethingWentWrong when anything went wrong to whole application level like database failure and response the {@code 500 Internal server error}
     */

    @GetMapping("/{customerId}")
    public ResponseEntity<CustomerDto> getCustomerById(@PathVariable String customerId) {
        var customer = customerService.fetchCustomerById(customerId);
        return customer.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());

    }

    @GetMapping
    public ResponseEntity<List<CustomerDto>> getAllCustomer(@ParameterObject Pageable pageable) {
        var customers = customerService.fetchAllCustomer(pageable);
        return new ResponseEntity<>(customers, HttpStatus.OK);
    }
}
