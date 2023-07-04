package com.greeting.dto;

public record ResponseDto(String message) {
    public static ResponseDto responseMessage(String message) {
        return new ResponseDto(message);
    }
}
