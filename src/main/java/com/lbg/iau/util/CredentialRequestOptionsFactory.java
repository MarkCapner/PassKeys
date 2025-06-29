package com.lbg.iau.util;

import com.webauthn4j.data.*;
import com.webauthn4j.data.client.challenge.Challenge;

import java.util.List;

public class CredentialRequestOptionsFactory {

    public static PublicKeyCredentialRequestOptions buildCredentialRequestOptions(
            String rpId,
            List<PublicKeyCredentialDescriptor> allowedCredentials,
            Challenge challenge) {

        return new PublicKeyCredentialRequestOptions(
                challenge, null, rpId, allowedCredentials,
                UserVerificationRequirement.PREFERRED, null
        );
    }
}
