package com.supplychain.auth.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "OAuth2")
@RestController
@RequestMapping("/oauth2")
public class OAuth2Controller {

    @GetMapping("/callback")
    public ResponseEntity<String> callback(
            @RequestParam String token) {
        return ResponseEntity.ok(token);
    }
}