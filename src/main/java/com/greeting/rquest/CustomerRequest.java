package com.greeting.rquest;

import com.greeting.annotation.EmailValidator;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record CustomerRequest(@NotBlank @Size(min = 2, max = 22) @Pattern(regexp = "[a-zA-Z]+\\.?") String name,
                              @NotBlank @EmailValidator String email,
                              @NotBlank @Size(min = 6, max = 14) @Pattern(regexp = "[0-9]+") String mobile) {
}
