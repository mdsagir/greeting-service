package com.greeting.mapper;

import com.greeting.entity.Customer;
import com.greeting.rquest.CustomerRequest;
import org.springframework.stereotype.Component;

@Component
public class CustomerMapper {

    public Customer newCustomerEntity(CustomerRequest customerRequest) {
        return new Customer(customerRequest.name(), customerRequest.email(), customerRequest.mobile());
    }
}
