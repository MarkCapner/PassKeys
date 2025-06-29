package com.lbg.iau.model;

import com.webauthn4j.credential.CredentialRecord;
import com.webauthn4j.data.client.CollectedClientData;

public class StoredCredentialRecord implements CredentialRecord {

    private final byte[] credentialId;
    private final byte[] credentialPublicKey;
    private final long signCount;

    public StoredCredentialRecord(byte[] credentialId, byte[] credentialPublicKey, long signCount) {
        this.credentialId = credentialId;
        this.credentialPublicKey = credentialPublicKey;
        this.signCount = signCount;
    }

    @Override
    public CollectedClientData getClientData() {
        return null; // or return a stored value if you have one
    }


    @Override
    public byte[] getCredentialId() {
        return credentialId;
    }

    @Override
    public byte[] getCredentialPublicKey() {
        return credentialPublicKey;
    }

    @Override
    public long getSignatureCount() {
        return signCount;
    }

    // Optional: override other methods if needed
}

