package com.lbg.iau.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import com.lbg.iau.model.AuthenticationFinishRequest;
import com.lbg.iau.model.RegistrationFinishRequest;
import com.lbg.iau.model.RegistrationStartRequest;
import com.lbg.iau.model.UserCredential;
import com.lbg.iau.repository.UserCredentialRepository;
import com.lbg.iau.util.CredentialOptionsFactory;
import com.lbg.iau.util.CredentialRequestOptionsFactory;
import com.webauthn4j.WebAuthnManager;
import com.webauthn4j.authenticator.AuthenticatorImpl;
import com.webauthn4j.data.*;
import com.webauthn4j.data.attestation.authenticator.AttestedCredentialData;
import com.webauthn4j.data.attestation.authenticator.COSEKey;
import com.webauthn4j.data.client.Origin;
import com.webauthn4j.data.client.challenge.Challenge;
import com.webauthn4j.data.client.challenge.DefaultChallenge;
import com.webauthn4j.server.ServerProperty;
import com.webauthn4j.util.Base64UrlUtil;
import lombok.RequiredArgsConstructor;
import lombok.var;
import org.springframework.stereotype.Service;

import java.util.Base64;

@Service
@RequiredArgsConstructor
public class WebAuthnService {

    private final UserCredentialRepository repo;

    private final Map<String, Challenge> challengeMap = new ConcurrentHashMap<>();

    private static final String RP_ID = "example.com";
    private static final String ORIGIN = "https://example.com";

    public PublicKeyCredentialCreationOptions beginRegistration(RegistrationStartRequest request) {
        Challenge challenge = new DefaultChallenge();
        challengeMap.put(request.getUserId(), challenge);
        return CredentialOptionsFactory.buildCredentialCreationOptions(
                RP_ID, "Passkey Demo", request.getUserId(), request.getUsername(), request.getUsername(), challenge
        );
    }

    public void finishRegistration(RegistrationFinishRequest request) {
        byte[] attestationObject = Base64.getUrlDecoder().decode(request.getResponse().getAttestationObject());
        byte[] clientDataJSON = Base64.getUrlDecoder().decode(request.getResponse().getClientDataJSON());

        RegistrationRequest registrationRequest = new RegistrationRequest(attestationObject, clientDataJSON);
        ServerProperty serverProperty = new ServerProperty(
                new Origin(ORIGIN),
                RP_ID,
                challengeMap.get(request.getUserId()),
                null
        );

        RegistrationParameters parameters = new RegistrationParameters(
                serverProperty,
                false, // userVerificationRequired
                true   // userPresenceRequired
        );

        WebAuthnManager manager = WebAuthnManager.createNonStrictWebAuthnManager();
        var registrationData = manager.parse(registrationRequest);
        manager.validate(registrationData, parameters);

        AttestedCredentialData credData = registrationData.getAttestationObject()
                .getAuthenticatorData()
                .getAttestedCredentialData();

        COSEKey coseKey = credData.getCOSEKey();

        UserCredential cred = new UserCredential();
        cred.setCredentialId(Base64UrlUtil.encodeToString(credData.getCredentialId()));
        cred.setUserId(request.getUserId());
        cred.setCredentialPublicKey(coseKey.getPublicKey().getEncoded());
        cred.setSignCount(registrationData.getAttestationObject().getAuthenticatorData().getSignCount());
        cred.setAttestationFormat(registrationData.getAttestationObject().getFormat());

        repo.save(cred);
    }
    public PublicKeyCredentialRequestOptions beginAuthentication(String userId) {
        Challenge challenge = new DefaultChallenge();
        challengeMap.put(userId, challenge);

        List<PublicKeyCredentialDescriptor> descriptors = new ArrayList<>();
        for (UserCredential c : repo.findByUserId(userId)) {
            PublicKeyCredentialDescriptor publicKeyCredentialDescriptor = new PublicKeyCredentialDescriptor(
                    PublicKeyCredentialType.PUBLIC_KEY,
                    Base64.getUrlDecoder().decode(c.getCredentialId()),
                    null
            );
            descriptors.add(publicKeyCredentialDescriptor);
        }

        return CredentialRequestOptionsFactory.buildCredentialRequestOptions(
                RP_ID, descriptors, challenge
        );
    }

    public void finishAuthentication(AuthenticationFinishRequest request) {
        byte[] credentialId = Base64.getUrlDecoder().decode(request.getRawId());
        byte[] clientDataJSON = Base64.getUrlDecoder().decode(request.getResponse().getClientDataJSON());
        byte[] authenticatorData = Base64.getUrlDecoder().decode(request.getResponse().getAuthenticatorData());
        byte[] signature = Base64.getUrlDecoder().decode(request.getResponse().getSignature());

        UserCredential credential = repo.findById(Base64UrlUtil.encodeToString(credentialId))
                .orElseThrow(() -> new RuntimeException("Credential not found"));

        ServerProperty serverProperty = new ServerProperty(
                new Origin(ORIGIN),
                RP_ID,
                challengeMap.get(request.getUserId()),
                null
        );

        AuthenticationRequest authenticationRequest = new AuthenticationRequest(
                credentialId,
                authenticatorData,
                clientDataJSON,
                signature,
                null
        );

        CredentialRecord credentialRecord = new StoredCredentialRecord(
                credentialId,
                credential.getCredentialPublicKey(),
                credential.getSignCount()
        );

        AuthenticationParameters parameters = new AuthenticationParameters(
                serverProperty,
                credentialRecord,
                false, // userVerificationRequired
                true   // userPresenceRequired
        );


        WebAuthnManager manager = WebAuthnManager.createNonStrictWebAuthnManager();
        var result = manager.parse(authenticationRequest);
        manager.validate(result, parameters);

        // Optional: update sign count
        credential.setSignCount(result.getAuthenticatorData().getSignCount());
        repo.save(credential);
    }
}
