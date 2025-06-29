package com.lbg.iau.model;

import com.webauthn4j.data.AuthenticatorAttestationResponse;
import lombok.Data;

@Data
public class RegistrationStartRequest {
    private String userId;
    private String username;
}


