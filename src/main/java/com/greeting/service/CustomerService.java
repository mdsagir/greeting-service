package com.greeting.service;


import com.greeting.dto.CustomerDto;
import com.greeting.rquest.CustomerRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface CustomerService {

    Long addCustomer(CustomerRequest customerRequest);

    List<CustomerDto> fetchAllCustomer(Pageable pageable);

    Optional<CustomerDto> fetchCustomerById(String customerId);

}
