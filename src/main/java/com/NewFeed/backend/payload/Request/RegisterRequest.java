package com.NewFeed.backend.payload.Request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterRequest {
    @NotBlank
    @Size(min = 3, max = 60)
    private String username;
    @NotBlank
    @Size(min = 3, max = 60)
    private String email;

    @NotBlank
    @Size(min = 6, max = 60)
    private String password;
}
