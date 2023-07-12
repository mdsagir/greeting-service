package com.greeting.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "greeting")
public record GreetingConfig(/* Config user message*/String message) {
}
