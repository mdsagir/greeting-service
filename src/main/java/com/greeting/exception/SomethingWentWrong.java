package com.greeting.exception;

public class SomethingWentWrong extends RuntimeException {
    public SomethingWentWrong(String message) {
        super(message);
    }
}
