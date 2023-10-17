package com.example.cloudserviceapplication.service;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import com.example.cloudserviceapplication.dto.LoginRequest;
import com.example.cloudserviceapplication.entity.UserEntity;
import com.example.cloudserviceapplication.repository.TokenRepository;
import com.example.cloudserviceapplication.repository.UserRepository;

import java.util.Optional;

import static org.mockito.Mockito.when;

class AuthorizationServiceTest {
    public static final String AUTH_TOKEN = "Java 999";
    public static final String UNKNOWN_AUTH_TOKEN = "Java";
    public static final String EXISTING_USER = "silent27";
    public static final String NOT_EXISTING_USER = "eva";
    public static final String CORRECT_PASSWORD = "sIleNt27";

    private final UserRepository userRepository = createUserRepositoryMock();
    private final TokenRepository tokenRepository = createTokenRepositoryMock();

    private UserRepository createUserRepositoryMock() {
        final UserRepository userRepository = Mockito.mock(UserRepository.class);
        when(userRepository.findById(EXISTING_USER)).thenReturn(Optional.of(new UserEntity(EXISTING_USER, CORRECT_PASSWORD)));
        when(userRepository.findById(NOT_EXISTING_USER)).thenReturn(Optional.empty());
        return userRepository;
    }

    private TokenRepository createTokenRepositoryMock() {
        final TokenRepository tokenRepository = Mockito.mock(TokenRepository.class);
        when(tokenRepository.existsById(AUTH_TOKEN.split(" ")[1].trim())).thenReturn(true);
        when(tokenRepository.existsById(UNKNOWN_AUTH_TOKEN)).thenReturn(false);
        return tokenRepository;
    }

    @Test
    void login() {
        final AuthenticationService authenticationService = new AuthenticationService(userRepository, tokenRepository);
        Assertions.assertDoesNotThrow(() -> authenticationService.login(new LoginRequest(EXISTING_USER, CORRECT_PASSWORD)));
    }

    @Test
    void login_userNotFound() {
        final AuthenticationService authenticationService = new AuthenticationService(userRepository, tokenRepository);
        Assertions.assertThrows(RuntimeException.class, () -> authenticationService.login(new LoginRequest(NOT_EXISTING_USER, CORRECT_PASSWORD)));
    }

    @Test
    void login_incorrectPassword() {
        final AuthenticationService authenticationService = new AuthenticationService(userRepository, tokenRepository);
        Assertions.assertThrows(RuntimeException.class, () -> authenticationService.login(new LoginRequest(EXISTING_USER, "123456789")));
    }

    @Test
    void logout() {
        final AuthenticationService authenticationService = new AuthenticationService(userRepository, tokenRepository);
        Assertions.assertDoesNotThrow(() -> authenticationService.logout(AUTH_TOKEN));
    }

    @Test
    void checkToken() {
        final AuthenticationService authenticationService = new AuthenticationService(userRepository, tokenRepository);
        Assertions.assertDoesNotThrow(() -> authenticationService.checkToken(AUTH_TOKEN));
    }

    @Test
    void checkToken_failed() {
        final AuthenticationService authenticationService = new AuthenticationService(userRepository, tokenRepository);
        Assertions.assertThrows(RuntimeException.class, () -> authenticationService.checkToken(UNKNOWN_AUTH_TOKEN));
    }
}