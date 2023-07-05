package com.greeting.controller;

import com.greeting.dto.CustomerDto;
import com.greeting.rquest.CustomerRequest;
import org.assertj.core.util.Arrays;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class ControllerJsonTest {

    @Autowired
    private JacksonTester<CustomerRequest> json;

    @Autowired
    private JacksonTester<CustomerDto> customerDto;
    @Autowired
    private JacksonTester<CustomerDto[]> jsonList;

    @Test
    void customerSerializationTest() throws IOException {
        var customer = new CustomerRequest("sagir", "tech.sagir@gmail.com", "9052708146");
        assertThat(json.write(customer)).isStrictlyEqualToJson("customer.json");
        assertThat(json.write(customer)).extractingJsonPathStringValue("@.name").isEqualTo("sagir");
        assertThat(json.write(customer)).extractingJsonPathStringValue("@.email").isEqualTo("tech.sagir@gmail.com");
        assertThat(json.write(customer)).extractingJsonPathStringValue("@.mobile").isEqualTo("9052708146");
    }

    @Test
    void customerDeserializationTest() throws IOException {
        String expected = """
                {
                  "name": "sagir",
                  "email": "tech.sagir@gmail.com",
                  "mobile": "9052708146"
                }
                """;
        assertThat(json.parse(expected).getObject())
                .isEqualTo(new CustomerRequest("sagir", "tech.sagir@gmail.com", "9052708146"));
        assertThat(json.parseObject(expected).name()).isEqualTo("sagir");
        assertThat(json.parseObject(expected).email()).isEqualTo("tech.sagir@gmail.com");
        assertThat(json.parseObject(expected).mobile()).isEqualTo("9052708146");
    }

    @Test
    void customerDtoListSerializationTest() throws IOException {
        var customerList = Arrays.array(new CustomerDto("1", "sagir", "tech.sagir@gmail.com", "9052708146"),
                new CustomerDto("2", "asim", "tech.asim@gmail.com", "9052708140"));

        assertThat(jsonList.write(customerList)).isStrictlyEqualToJson("customer-dto-list.json");
    }

    @Test
    void customerDtoListDeserializationTest() throws IOException {
        String expected = """
                [
                  {
                    "customerId": "1",
                    "name": "sagir",
                    "email": "tech.sagir@gmail.com",
                    "mobile": "9052708146"
                  },
                  {
                    "customerId": "2",
                    "name": "asim",
                    "email": "tech.asim@gmail.com",
                    "mobile": "9052708140"
                  }
                ]
                """;
        var customerList = Arrays.array(new CustomerDto("1", "sagir", "tech.sagir@gmail.com", "9052708146"),
                new CustomerDto("2", "asim", "tech.asim@gmail.com", "9052708140"));
        assertThat(jsonList.parse(expected).getObject()).isEqualTo(customerList);
    }

    @Test
    void customerDtoSerializationTest() throws IOException {
        var customer = new CustomerDto("1", "sagir", "tech.sagir@gmail.com", "9052708146");

        assertThat(customerDto.write(customer)).isStrictlyEqualToJson("customer-dto.json");
        assertThat(customerDto.write(customer)).extractingJsonPathStringValue("@.name").isEqualTo("sagir");
        assertThat(customerDto.write(customer)).extractingJsonPathStringValue("@.customerId").isEqualTo("1");
        assertThat(customerDto.write(customer)).extractingJsonPathStringValue("@.email").isEqualTo("tech.sagir@gmail.com");
        assertThat(customerDto.write(customer)).extractingJsonPathStringValue("@.mobile").isEqualTo("9052708146");
    }

    @Test
    void customerDtoDeserializationTest() throws IOException {
        String expected = """
                {
                  "customerId": "1",
                  "name": "sagir",
                  "email": "tech.sagir@gmail.com",
                  "mobile": "9052708146"
                }
                """;
        assertThat(customerDto.parse(expected).getObject())
                .isEqualTo(new CustomerDto("1", "sagir", "tech.sagir@gmail.com", "9052708146"));
        assertThat(customerDto.parseObject(expected).getCustomerId()).isEqualTo("1");
        assertThat(customerDto.parseObject(expected).getName()).isEqualTo("sagir");
        assertThat(customerDto.parseObject(expected).getEmail()).isEqualTo("tech.sagir@gmail.com");
        assertThat(customerDto.parseObject(expected).getMobile()).isEqualTo("9052708146");
    }

}
