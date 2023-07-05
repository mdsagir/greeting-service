package com.greeting.dto;

import com.greeting.util.EncryptionUtil;

import java.util.Objects;

import static com.greeting.util.EncryptionUtil.encode;
import static java.lang.String.valueOf;

public class CustomerDto {
    private String customerId;
    private String name;
    private String email;
    private String mobile;

    public CustomerDto(Long customerId, String name, String email, String mobile) {
        this.customerId = encode(customerId);
        this.name = name;
        this.email = email;
        this.mobile = mobile;
    }

    public CustomerDto(String customerId, String name, String email, String mobile) {
        this.customerId = customerId;
        this.name = name;
        this.email = email;
        this.mobile = mobile;
    }

    public CustomerDto() {
    }

    public static CustomerDto just() {
        return new CustomerDto();
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CustomerDto that = (CustomerDto) o;
        return Objects.equals(customerId, that.customerId) && Objects.equals(name, that.name) && Objects.equals(email, that.email) && Objects.equals(mobile, that.mobile);
    }

    @Override
    public int hashCode() {
        return Objects.hash(customerId, name, email, mobile);
    }
}
