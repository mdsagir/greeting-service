package com.greeting.controller;

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
    private JacksonTester<CustomerRequest[]> jsonList;

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
    void customerListSerializationTest() throws IOException {
        var customerList = Arrays.array(new CustomerRequest("sagir", "tech.sagir@gmail.com", "9052708146"),
                new CustomerRequest("neha", "tech.neha@gmail.com", "1234567890"),
                new CustomerRequest("asim", "tech.asim@gmail.com", "1234567568"),
                new CustomerRequest("sara", "tech.sara@gmail.com", "1234567123"));

        assertThat(jsonList.write(customerList)).isStrictlyEqualToJson("customer-list.json");
    }

    @Test
    void customerListDeserializationTest() throws IOException {
        String expected = """
                [
                  {
                    "name": "sagir",
                    "email": "tech.sagir@gmail.com",
                    "mobile": "9052708146"
                  },
                  {
                    "name": "neha",
                    "email": "tech.neha@gmail.com",
                    "mobile": "1234567890"
                  },
                  {
                    "name": "asim",
                    "email": "tech.asim@gmail.com",
                    "mobile": "1234567568"
                  },
                  {
                    "name": "sara",
                    "email": "tech.sara@gmail.com",
                    "mobile": "1234567123"
                  }
                ]
                """;
        var customerList = Arrays.array(new CustomerRequest("sagir", "tech.sagir@gmail.com", "9052708146"),
                new CustomerRequest("neha", "tech.neha@gmail.com", "1234567890"),
                new CustomerRequest("asim", "tech.asim@gmail.com", "1234567568"),
                new CustomerRequest("sara", "tech.sara@gmail.com", "1234567123"));
        assertThat(jsonList.parse(expected).getObject()).isEqualTo(customerList);
    }

}
