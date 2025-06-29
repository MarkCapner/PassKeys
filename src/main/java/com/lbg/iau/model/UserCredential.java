package com.lbg.iau.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserCredential {

    @Id
    private String credentialId;

    private String userId;

    @Lob
    private byte[] credentialPublicKey;

    private long signCount;

    private String attestationFormat;
}
