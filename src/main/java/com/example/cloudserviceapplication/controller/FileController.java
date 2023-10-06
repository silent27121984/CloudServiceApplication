package com.example.cloudserviceapplication.controller;

import com.example.cloudserviceapplication.dto.FileNameRequest;
import com.example.cloudserviceapplication.service.AuthenticationService;
import com.example.cloudserviceapplication.service.FileService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.naming.AuthenticationException;
import java.io.IOException;

@RequiredArgsConstructor
@RestController
@Validated
@RequestMapping("/file")
public class FileController {
    private final FileService fileService;
    private final AuthenticationService authenticationService;

    @PostMapping
    public void uploadFile(@RequestHeader("auth-token") @NotBlank String authToken, @NotBlank String filename, @NotNull @RequestBody MultipartFile file) throws IOException, AuthenticationException {
        authenticationService.checkToken(authToken);
        fileService.addFile(filename, file.getBytes());
    }

    @DeleteMapping
    public void deleteFile(@RequestHeader("auth-token") @NotBlank String authToken, @NotBlank String filename) throws AuthenticationException {
        authenticationService.checkToken(authToken);
        fileService.deleteFile(filename);
    }

    @GetMapping(produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public byte[] getFile(@RequestHeader("auth-token") @NotBlank String authToken, @NotBlank String filename) throws AuthenticationException {
        authenticationService.checkToken(authToken);
        return fileService.getFile(filename);
    }

    @PutMapping
    public void editFile(@RequestHeader("auth-token") @NotBlank String authToken, @NotBlank String filename, @Valid @RequestBody FileNameRequest newFilename) throws AuthenticationException {
        authenticationService.checkToken(authToken);
        fileService.editFileName(filename, newFilename.getFilename());
    }
}
