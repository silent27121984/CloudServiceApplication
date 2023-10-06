package com.example.cloudserviceapplication.controller;

import com.example.cloudserviceapplication.dto.LoginRequest;
import com.example.cloudserviceapplication.dto.LoginResponse;
import com.example.cloudserviceapplication.exceptions.BadCredentialsException;
import com.example.cloudserviceapplication.service.AuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/login")
public class LoginController {
    private final AuthenticationService authenticationService;

    @PostMapping
    public LoginResponse login(@Valid @RequestBody LoginRequest loginRequest) throws BadCredentialsException {
        return authenticationService.login(loginRequest);
    }
}
