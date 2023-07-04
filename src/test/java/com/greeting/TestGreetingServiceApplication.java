package com.greeting;


import com.greeting.config.TestContainerConfig;
import org.springframework.boot.SpringApplication;


public class TestGreetingServiceApplication {

    public static void main(String[] args) {
        SpringApplication.from(GreetingServiceApplication::main)
                .with(TestContainerConfig.class).run(args);
    }
}
