package com.example.cloudserviceapplication;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import com.example.cloudserviceapplication.dto.FileNameRequest;
import com.example.cloudserviceapplication.dto.LoginRequest;
import com.example.cloudserviceapplication.dto.LoginResponse;
import com.example.cloudserviceapplication.entity.FileEntity;
import com.example.cloudserviceapplication.entity.TokenEntity;
import com.example.cloudserviceapplication.entity.UserEntity;
import com.example.cloudserviceapplication.repository.FileRepository;
import com.example.cloudserviceapplication.repository.TokenRepository;
import com.example.cloudserviceapplication.repository.UserRepository;

import java.util.Optional;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = "classpath:test.properties")
class CloudServiceApplicationTests {
    @Autowired
    TestRestTemplate restTemplate;

    @Autowired
    UserRepository userRepository;

    @Autowired
    FileRepository fileRepository;

    @Autowired
    TokenRepository tokenRepository;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
        fileRepository.deleteAll();
        tokenRepository.deleteAll();
    }

    @Test
    public void testLogin() {
        userRepository.save(new UserEntity("silent27", "sIleNt27"));
        final HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");

        final LoginRequest operation = new LoginRequest("silent27", "sIleNt27");
        final HttpEntity<LoginRequest> request = new HttpEntity<>(operation, headers);

        final ResponseEntity<LoginResponse> result = this.restTemplate.postForEntity("/login", request, LoginResponse.class);
        Assertions.assertNotNull(result.getBody());
        Assertions.assertNotNull(result.getBody().getAuthToken());
    }

    @Test
    public void testLogout() {
        final String authToken = "Java 999";
        tokenRepository.save(new TokenEntity(authToken));

        final HttpHeaders headers = new HttpHeaders();
        headers.set("auth-token", authToken);
        final HttpEntity<Void> request = new HttpEntity<>(null, headers);

        this.restTemplate.postForEntity("/logout", request, Void.class);
        Assertions.assertFalse(tokenRepository.existsById(authToken.split(" ")[1].trim()));
    }

    @Test
    public void testUploadFile() {
        final String authToken = "Java 999";
        tokenRepository.save(new TokenEntity(authToken.split(" ")[1].trim()));

        final HttpHeaders headers = new HttpHeaders();
        headers.set("auth-token", authToken);

        final MultiValueMap<String, Object> parts = new LinkedMultiValueMap<>();
        parts.add("file", new ClassPathResource("testing.txt"));

        final HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(parts, headers);

        this.restTemplate.postForEntity("/file?filename=testing.txt", request, Void.class);

        final Optional<FileEntity> fileInRepository = fileRepository.findById("testing.txt");
        Assertions.assertTrue(fileInRepository.isPresent());
        Assertions.assertEquals(new FileEntity("testing.txt", new byte[]{49, 51, 50}), fileInRepository.get());
    }

    @Test
    public void testDeleteFile() {
        fileRepository.save(new FileEntity("testing.txt", new byte[]{49, 51, 50}));

        final String authToken = "Java 999";
        tokenRepository.save(new TokenEntity(authToken.split(" ")[1].trim()));

        final HttpHeaders headers = new HttpHeaders();
        headers.set("auth-token", authToken);

        final HttpEntity<Void> request = new HttpEntity<>(null, headers);

        this.restTemplate.exchange("/file?filename=testing.txt", HttpMethod.DELETE, request, Void.class);

        Assertions.assertFalse(fileRepository.existsById("testing.txt"));
    }

    @Test
    public void testGetFile() {
        fileRepository.save(new FileEntity("testing.txt", new byte[]{49, 51, 50}));

        final String authToken = "Java 999";
        tokenRepository.save(new TokenEntity(authToken.split(" ")[1].trim()));

        final HttpHeaders headers = new HttpHeaders();
        headers.set("auth-token", authToken);

        final HttpEntity<Void> request = new HttpEntity<>(null, headers);

        final ResponseEntity<byte[]> result = this.restTemplate.exchange("/file?filename=testing.txt", HttpMethod.GET, request, byte[].class);

        Assertions.assertNotNull(result.getBody());
        Assertions.assertArrayEquals(new byte[]{49, 51, 50}, result.getBody());
    }

    @Test
    public void testEditFile() {
        fileRepository.save(new FileEntity("testing.txt", new byte[]{49, 51, 50}));

        final String authToken = "Java 999";
        tokenRepository.save(new TokenEntity(authToken.split(" ")[1].trim()));

        final HttpHeaders headers = new HttpHeaders();
        headers.set("auth-token", authToken);

        final HttpEntity<FileNameRequest> request =
                new HttpEntity<>(new FileNameRequest("testFile.txt"), headers);

        this.restTemplate.exchange("/file?filename=testing.txt", HttpMethod.PUT, request, Void.class);

        Assertions.assertFalse(fileRepository.existsById("testing.txt"));
        final Optional<FileEntity> fileInRepository = fileRepository.findById("testFile.txt");
        Assertions.assertTrue(fileInRepository.isPresent());
        Assertions.assertEquals(new FileEntity("testFile.txt", new byte[]{49, 51, 50}), fileInRepository.get());
    }

    @Test
    public void testGetFileList() {
        fileRepository.save(new FileEntity("testing.txt", new byte[]{49, 51, 50}));

        final String authToken = "Java 999";
        tokenRepository.save(new TokenEntity(authToken.split(" ")[1].trim()));

        final HttpHeaders headers = new HttpHeaders();
        headers.set("auth-token", authToken);

        final HttpEntity<Void> request = new HttpEntity<>(null, headers);

        final ResponseEntity<Object> result = this.restTemplate.exchange("/list?limit=10", HttpMethod.GET, request, Object.class);

        Assertions.assertNotNull(result.getBody());
        Assertions.assertEquals("[{filename=testing.txt, size=3}]", result.getBody().toString());
    }
}
