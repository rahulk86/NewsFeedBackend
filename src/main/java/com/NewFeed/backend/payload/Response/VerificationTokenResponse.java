package com.NewFeed.backend.payload.Response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class VerificationTokenResponse {
    private String message;
    private String token;
}
