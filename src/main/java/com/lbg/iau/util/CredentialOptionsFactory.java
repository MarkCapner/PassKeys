package com.lbg.iau.util;

import com.webauthn4j.data.*;
import com.webauthn4j.data.attestation.statement.COSEAlgorithmIdentifier;
import com.webauthn4j.data.client.challenge.Challenge;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class CredentialOptionsFactory {

    public static PublicKeyCredentialCreationOptions buildCredentialCreationOptions(
            String rpId,
            String rpName,
            String userId,
            String username,
            String displayName,
            Challenge challenge) {

        PublicKeyCredentialRpEntity rp = new PublicKeyCredentialRpEntity(rpId, rpName);
        PublicKeyCredentialUserEntity user = new PublicKeyCredentialUserEntity(
                userId.getBytes(StandardCharsets.UTF_8), username, displayName
        );
        List<PublicKeyCredentialParameters> pubKeyCredParams = List.of(
                new PublicKeyCredentialParameters(PublicKeyCredentialType.PUBLIC_KEY, COSEAlgorithmIdentifier.ES256)
        );
        AuthenticatorSelectionCriteria selection = new AuthenticatorSelectionCriteria(
                        null, false, UserVerificationRequirement.PREFERRED
                );

        return new PublicKeyCredentialCreationOptions(
                rp, user, challenge, pubKeyCredParams, null, null, selection,
                AttestationConveyancePreference.NONE, null
        );
    }
}