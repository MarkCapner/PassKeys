package com.lbg.iau.model;

import lombok.Data;

@Data
public class RegistrationFinishRequest {
    private String userId;
    private String id;
    private String rawId;
    private String type;
    private AuthenticatorAttestationResponse response;
}
