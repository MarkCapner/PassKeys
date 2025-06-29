package com.lbg.iau.controller;

import com.lbg.iau.model.RegistrationFinishRequest;
import com.lbg.iau.model.RegistrationStartRequest;
import com.lbg.iau.service.WebAuthnService;
import com.webauthn4j.data.PublicKeyCredentialCreationOptions;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/register")
@RequiredArgsConstructor
public class RegistrationController {

    private final WebAuthnService webAuthnService;

    @PostMapping("/begin")
    public PublicKeyCredentialCreationOptions begin(@RequestBody RegistrationStartRequest req) {
        return webAuthnService.beginRegistration(req);
    }

    @PostMapping("/finish")
    public ResponseEntity<Void> finish(@RequestBody RegistrationFinishRequest req) {
        webAuthnService.finishRegistration(req);
        return ResponseEntity.ok().build();
    }
}
