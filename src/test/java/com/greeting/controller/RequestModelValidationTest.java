package com.greeting.controller;

import com.greeting.rquest.CustomerRequest;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import static jakarta.validation.Validation.buildDefaultValidatorFactory;
import static org.assertj.core.api.Assertions.assertThat;


class RequestModelValidationTest {

    private static Validator validator;

    @BeforeAll
    static void setUp() {
        try (ValidatorFactory factory = buildDefaultValidatorFactory()) {
            validator = factory.getValidator();
        }
    }

    @NullSource
    @ParameterizedTest
    @ValueSource(strings = {"", "  ", "s", "s..", "123", ")*&", "aa bb cc dd ee ff gg hh"})
    void whenCustomerNameHasInvalidDataThenValidationFailed(String name) {
        var customerRequest = new CustomerRequest(name, "tech.sagir@gmail.com", "9052708146");
        var validate = validator.validate(customerRequest);
        assertThat(validate).isNotEmpty();
    }

    @ParameterizedTest
    @ValueSource(strings = {"tech.sagir", "sagir"})
    void whenCustomerNameHasValidDataThenValidationFailed() {
        var customerRequest = new CustomerRequest("sagir", "tech.sagir@gmail.com", "9052708146");
        var validate = validator.validate(customerRequest);
        assertThat(validate).isEmpty();
    }

    @NullSource
    @ParameterizedTest
    @ValueSource(strings = {"", "  ", "s", "s..", "123", ")*&", "aa bb cc dd ee ff gg hh",
            "abc..def@mail.com", ".abc@mail.com"})
    void whenCustomerEmailHasInvalidDataThenValidationFailed(String email) {
        var customerRequest = new CustomerRequest("sagir", email, "9052708146");
        var validate = validator.validate(customerRequest);
        assertThat(validate).isNotEmpty();
    }

    @ParameterizedTest
    @ValueSource(strings = {"tech.sagir@gmail.com", "sagir@gmail.com"})
    void whenCustomerEmailHasValidDataThenValidationFailed(String email) {
        var customerRequest = new CustomerRequest("sagir", email, "9052708146");
        var validate = validator.validate(customerRequest);
        assertThat(validate).isEmpty();
    }

    @NullSource
    @ParameterizedTest
    @ValueSource(strings = {"", "  ", "s", "s..", "123", ")*&", "aa bb cc dd ee ff gg hh",
            "abc..def@mail.com", ".abc@mail.com", "12345", "123456789012345"})
    void whenCustomerMobileHasInvalidDataThenValidationFailed(String mobile) {
        var customerRequest = new CustomerRequest("sagir", "tech.sagir@gail.com", mobile);
        var validate = validator.validate(customerRequest);
        assertThat(validate).isNotEmpty();
    }

    @ParameterizedTest
    @ValueSource(strings = {"123455", "12345678901"})
    void whenCustomerMobileHasValidDataThenValidationFailed(String mobile) {
        var customerRequest = new CustomerRequest("sagir", "tech.sagir@gmail.com",mobile );
        var validate = validator.validate(customerRequest);
        assertThat(validate).isEmpty();
    }
}
