package com.NewFeed.backend.payload.Request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignUpRequest {
    @NotBlank
    @Size(min = 3, max = 60)
    private String email;

    @NotBlank
    @Size(min = 6, max = 60)
    private String password;
}
