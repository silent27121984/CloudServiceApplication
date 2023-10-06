package com.example.cloudserviceapplication.service;

import com.example.cloudserviceapplication.dto.LoginRequest;
import com.example.cloudserviceapplication.dto.LoginResponse;
import com.example.cloudserviceapplication.entity.TokenEntity;
import com.example.cloudserviceapplication.entity.UserEntity;
import com.example.cloudserviceapplication.exceptions.BadCredentialsException;
import com.example.cloudserviceapplication.repository.TokenRepository;
import com.example.cloudserviceapplication.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.naming.AuthenticationException;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final Logger logger = LoggerFactory.getLogger(AuthenticationService.class);
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;

    private final Random random = new Random();

    public LoginResponse login(LoginRequest loginRequest) throws BadCredentialsException {
        final String newLoginRequest = loginRequest.getLogin();
        final UserEntity user = userRepository.findById(newLoginRequest).orElseThrow(() -> new BadCredentialsException("User with login " + newLoginRequest + " not found"));

        if (!user.getPassword().equals(loginRequest.getPassword())) {
            throw new BadCredentialsException("Incorrect password for user " + newLoginRequest);
        }
        final String authToken = String.valueOf(random.nextLong());
        tokenRepository.save(new TokenEntity(authToken));
        logger.info("User " + newLoginRequest + " entered with token " + authToken);
        return new LoginResponse(authToken);
    }

    public void logout(String authToken) {
        tokenRepository.deleteById(authToken.split(" ")[1].trim());
    }

    public void checkToken(String authToken) throws AuthenticationException {
        if (!tokenRepository.existsById(authToken.split(" ")[1].trim())) {
            throw new AuthenticationException();
        }
    }
}
