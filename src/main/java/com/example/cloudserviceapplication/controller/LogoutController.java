package com.example.cloudserviceapplication.controller;

import com.example.cloudserviceapplication.service.AuthenticationService;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/logout")
@Validated
public class LogoutController {
    private final AuthenticationService authenticationService;

    @PostMapping
    public void logout(@RequestHeader("auth-token") @NotBlank String authToken) {
        authenticationService.logout(authToken);
    }
}
