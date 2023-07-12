package com.greeting.controller;

import com.greeting.config.GreetingConfig;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class HelloController {

    private final GreetingConfig greetingConfig;

    public HelloController(GreetingConfig greetingConfig) {
        this.greetingConfig = greetingConfig;
    }

    @GetMapping("/greeting")
    public Map<String, String> greeting() {
        return Map.of("message", greetingConfig.message());
    }
}
