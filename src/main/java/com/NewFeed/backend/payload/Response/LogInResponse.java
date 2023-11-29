package com.NewFeed.backend.payload.Response;

import lombok.*;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class LogInResponse {
    private Set<String> roles;
    private String accessToken;
}