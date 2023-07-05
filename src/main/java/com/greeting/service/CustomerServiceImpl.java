package com.greeting.service;

import com.greeting.dto.CustomerDto;
import com.greeting.exception.ConflictException;
import com.greeting.exception.SomethingWentWrong;
import com.greeting.mapper.CustomerMapper;
import com.greeting.repo.CustomerRepo;
import com.greeting.rquest.CustomerRequest;
import com.greeting.util.EncryptionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.greeting.util.EncryptionUtil.decode;

@Service
public class CustomerServiceImpl implements CustomerService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomerServiceImpl.class);
    private final CustomerRepo customerRepo;
    private final CustomerMapper customerMapper;

    public CustomerServiceImpl(CustomerRepo customerRepo, CustomerMapper customerMapper) {
        this.customerRepo = customerRepo;
        this.customerMapper = customerMapper;
    }

    @Override
    public Long addCustomer(CustomerRequest customerRequest) {
        try {
            var email = customerRequest.email();
            var exists = customerRepo.existsByEmail(email);
            if (exists) {
                throw new ConflictException("Email already exist");
            }
            var customer = customerMapper.newCustomerEntity(customerRequest);
            var save = customerRepo.save(customer);
            LOGGER.info("Added new user name: {}", customerRequest.name());
            return save.getId();
        } catch (Exception exception) {
            if (exception instanceof ConflictException conflictException) {
                LOGGER.warn("Error: {}", conflictException.getMessage());
                throw conflictException;
            }
            LOGGER.error("Error while adding new customer {}", exception.toString());
            throw new SomethingWentWrong("Unable to save the customer");
        }
    }

    @Override
    public List<CustomerDto> fetchAllCustomer(Pageable pageable) {
        return customerRepo.fetchAllCustomer(pageable);
    }

    @Override
    public Optional<CustomerDto> fetchCustomerById(String customerId) {
        return customerRepo.fetchCustomerById(decode(customerId));
    }


}
