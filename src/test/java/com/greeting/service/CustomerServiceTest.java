package com.greeting.service;

import com.greeting.entity.Customer;
import com.greeting.exception.ConflictException;
import com.greeting.exception.SomethingWentWrong;
import com.greeting.mapper.CustomerMapper;
import com.greeting.repo.CustomerRepo;
import com.greeting.rquest.CustomerRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static com.greeting.dto.CustomerDto.just;
import static java.util.List.of;
import static java.util.Optional.empty;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {


    private CustomerService customerService;
    @Mock
    private CustomerMapper customerMapper;
    @Mock
    private CustomerRepo customerRepo;

    @BeforeEach
    public void setup() {
        customerService = new CustomerServiceImpl(customerRepo, customerMapper);
    }

    @Test
    void whenCustomerFoundByIdThenReturnInstance() {
        String id = "bDUa_bWoHACkHduUEgWyFE5wGEfuGzdf";
        when(customerRepo.fetchCustomerById(4L)).thenReturn(Optional.of(just()));
        var customerDto = customerService.fetchCustomerById(id);
        assertThat(customerDto).isNotEmpty();
    }

    @Test
    void whenCustomerIdNotFoundThenReturnEmpty() {
        String id = "bDUa_bWoHACkHduUEgWyFE5wGEfuGzdf";
        when(customerRepo.fetchCustomerById(4L)).thenReturn(empty());
        var customerDto = customerService.fetchCustomerById(id);
        assertThat(customerDto).isEmpty();
    }

    @Test
    void whenFindAllCustomerIdThenReturnListOfCustomerDto() {
        var pageable = Pageable.unpaged();
        when(customerRepo.fetchAllCustomer(pageable)).thenReturn(List.of(just()));
        var customerDto = customerService.fetchAllCustomer(pageable);
        assertThat(customerDto).isNotEmpty().hasSize(1);
    }

    @Test
    void whenFindAllCustomerIdThenReturnEmptyOfCustomerDto() {
        var pageable = Pageable.unpaged();
        when(customerRepo.fetchAllCustomer(pageable)).thenReturn(of());
        var customerDto = customerService.fetchAllCustomer(pageable);
        assertThat(customerDto).isEmpty();
    }

    @Test
    void whenAddNewCustomerThenReturnId() {
        Long customerId = 100L;
        var customerRequest = new CustomerRequest("sagir", "tech.sagir@gmail.com", "9052708146");
        var customer = new Customer("sagir", "tech.sagir@gmail.com", "9052708146");
        customer.setId(customerId);
        when(customerRepo.existsByEmail(customerRequest.email())).thenReturn(false);
        when(customerMapper.newCustomerEntity(customerRequest)).thenReturn(customer);
        when(customerRepo.save(customer)).thenReturn(customer);
        var id = customerService.addCustomer(customerRequest);
        assertThat(id).isNotNull().isEqualTo(String.valueOf(customerId));
    }

    @Test
    void whenAddNewCustomerThenReturnException() {
        var customerRequest = new CustomerRequest("sagir", "tech.sagir@gmail.com", "9052708146");
        when(customerRepo.existsByEmail(customerRequest.email())).thenThrow(NullPointerException.class);
        assertThatThrownBy(() -> customerService.addCustomer(customerRequest))
                .isInstanceOf(SomethingWentWrong.class).hasMessage("Unable to save the customer");
    }

    @Test
    void whenAddNewCustomerWithExistEmailThenReturnException() {
        var customerRequest = new CustomerRequest("sagir", "tech.sagir@gmail.com", "9052708146");
        when(customerRepo.existsByEmail(customerRequest.email())).thenReturn(true);
        assertThatThrownBy(() -> customerService.addCustomer(customerRequest))
                .isInstanceOf(ConflictException.class).hasMessage("Email already exist");
    }

}
