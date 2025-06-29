package com.lbg.iau.model;


import lombok.Data;

@Data
public class AuthenticatorAssertionResponse {
    private String clientDataJSON;
    private String authenticatorData;
    private String signature;
    private String userHandle;
}