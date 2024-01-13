package com.NewFeed.backend.payload.Request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ForgetPasswordRequest {
    private String token;
    private String password;
}
