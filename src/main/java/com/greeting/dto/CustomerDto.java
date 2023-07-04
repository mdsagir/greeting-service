package com.greeting.dto;

public class CustomerDto {
    private String customerId;
    private String name;
    private String email;
    private String mobile;

    public CustomerDto(Long customerId, String name, String email, String mobile) {
        this.customerId = String.valueOf(customerId);
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
}
