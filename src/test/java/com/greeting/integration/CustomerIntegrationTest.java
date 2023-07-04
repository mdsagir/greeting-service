package com.greeting.integration;

import com.greeting.config.TestContainerConfig;
import com.greeting.dto.CustomerDto;
import com.greeting.rquest.CustomerRequest;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@Import(TestContainerConfig.class)
@DirtiesContext(classMode = AFTER_EACH_TEST_METHOD)
class CustomerIntegrationTest {
    private static final Logger log = LoggerFactory.getLogger(CustomerIntegrationTest.class);

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void shouldCreateANewCustomer() {
        var customer = new CustomerRequest("sagir", "tech.sagir@gmail.com", "9052708146");
        var createResponse = restTemplate
                .postForEntity("/customer", customer, Void.class);
        assertThat(createResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);
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

        var customerResponse = restTemplate.getForEntity("/customer/1", CustomerDto.class);
        assertThat(customerResponse.getStatusCode()).isEqualTo(NOT_FOUND);
        var responseBody = customerResponse.getBody();
        assertThat(responseBody).isNull();
    }
}
