package com.lbg.iau.controller;

import com.lbg.iau.model.AuthenticationFinishRequest;
import com.lbg.iau.model.AuthenticationStartRequest;
import com.lbg.iau.service.WebAuthnService;
import com.webauthn4j.data.PublicKeyCredentialRequestOptions;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final WebAuthnService webAuthnService;

    @PostMapping("/begin")
    public PublicKeyCredentialRequestOptions begin(@RequestBody AuthenticationStartRequest req) {
        return webAuthnService.beginAuthentication(req.getUserId());
    }

    @PostMapping("/finish")
    public ResponseEntity<Void> finish(@RequestBody AuthenticationFinishRequest req) {
        webAuthnService.finishAuthentication(req);
        return ResponseEntity.ok().build();
    }
}