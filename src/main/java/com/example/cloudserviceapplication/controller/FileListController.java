package com.example.cloudserviceapplication.controller;

import com.example.cloudserviceapplication.dto.FileDescriptionResponse;
import com.example.cloudserviceapplication.service.AuthenticationService;
import com.example.cloudserviceapplication.service.FileService;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.naming.AuthenticationException;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/list")
@Validated
public class FileListController {
    private final FileService fileService;
    private final AuthenticationService authenticationService;

    @GetMapping
    public List<FileDescriptionResponse> getFileList(@RequestHeader("auth-token") @NotBlank String authToken, @Min(1) int limit) throws AuthenticationException {
        authenticationService.checkToken(authToken);
        return fileService.getFileList(limit);
    }
}
