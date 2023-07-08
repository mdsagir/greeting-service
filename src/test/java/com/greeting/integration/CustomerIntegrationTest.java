package com.greeting.integration;

import com.greeting.config.TestContainerConfig;
import com.greeting.dto.CustomerDto;
import com.greeting.dto.ResponseDto;
import com.greeting.repo.CustomerRepo;
import com.greeting.rquest.CustomerRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Import;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.HttpStatus.*;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@Import(TestContainerConfig.class)
class CustomerIntegrationTest {


    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private CustomerRepo customerRepo;

    @BeforeEach
    void cleanUp() {
        customerRepo.deleteAll();
    }

    @Test
    void shouldCreateANewCustomer() {
        var customer = new CustomerRequest("sagir", "tech.sagir@gmail.com", "9052708146");
        var createResponse = restTemplate
                .postForEntity("/customer", customer, Void.class);
        assertThat(createResponse.getStatusCode()).isEqualTo(CREATED);
        var uriLocation = createResponse.getHeaders().getLocation();
        var customerResponse = restTemplate.getForEntity(uriLocation, CustomerDto.class);
        assertThat(customerResponse.getStatusCode()).isEqualTo(OK);
        var responseBody = customerResponse.getBody();
        assertThat(responseBody).isNotNull();
        assertThat(responseBody.getCustomerId()).isNotNull();
        assertThat(responseBody.getName()).isNotNull().isEqualTo(customer.name());
        assertThat(responseBody.getEmail()).isNotNull().isEqualTo(customer.email());
        assertThat(responseBody.getMobile()).isNotNull().isEqualTo(customer.mobile());

    }

    @Test
    void shouldReturnNotFoundIfByGivenCustomerIdNotPresent() {

        var customerResponse = restTemplate.getForEntity("/customer/1", ResponseDto.class);
        assertThat(customerResponse.getStatusCode()).isEqualTo(NOT_FOUND);
        ResponseDto responseBody = customerResponse.getBody();
        assertThat(responseBody).isNotNull();
        assertThat(responseBody.message()).isEqualTo("INVALID ID");
    }
}
