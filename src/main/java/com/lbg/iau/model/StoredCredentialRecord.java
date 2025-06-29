package com.lbg.iau.model;

import com.webauthn4j.credential.CredentialRecord;
import com.webauthn4j.data.attestation.authenticator.AttestedCredentialData;

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

    @Override
    public Boolean isUvInitialized() {
        // Return false or implement according to your logic
        return Boolean.FALSE;
    }

    @Override
    public void setUvInitialized(boolean b) {

    }

    @org.jetbrains.annotations.Nullable
    @Override
    public Boolean isBackupEligible() {
        return null;
    }

    @Override
    public void setBackupEligible(boolean b) {

    }

    @Nullable
    @Override
    public Boolean isBackedUp() {
        return null;
    }

    @Override
    public void setBackedUp(boolean b) {

    }
    @Nullable
    @Override
    public byte[] getClientData() {
        // Return null or implement according to your logic
        return null;
    }

    @Override
    public @org.jetbrains.annotations.NotNull AttestedCredentialData getAttestedCredentialData() {
        return null;
    }

    @Override
    public long getCounter() {
        return 0;
    }

    @Override
    public void setCounter(long l) {

    }
}