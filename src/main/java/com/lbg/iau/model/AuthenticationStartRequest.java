package com.lbg.iau.model;

import com.webauthn4j.data.AuthenticatorAssertionResponse;

import lombok.Data;

@Data
public class AuthenticationStartRequest {
    private String userId;
}
