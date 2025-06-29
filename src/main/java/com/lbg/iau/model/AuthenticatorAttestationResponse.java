package com.lbg.iau.model;

import lombok.Data;

@Data
public class AuthenticatorAttestationResponse {
    private String clientDataJSON;
    private String attestationObject;
}