package com.greeting.repo;

import com.greeting.dto.CustomerDto;
import com.greeting.entity.Customer;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CustomerRepo extends JpaRepository<Customer, Long> {

    @Query("SELECT new com.greeting.dto.CustomerDto(c.id,c.name,c.email,c.mobile) FROM Customer c")
    List<CustomerDto> fetchAllCustomer(Pageable pageable);

    @Query("SELECT new com.greeting.dto.CustomerDto(c.id,c.name,c.email,c.mobile) FROM Customer c WHERE c.id=:id")
    Optional<CustomerDto> fetchCustomerById(Long id);

    boolean existsByEmail(String email);

}
