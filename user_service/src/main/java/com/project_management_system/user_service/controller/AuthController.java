package com.project_management_system.user_service.controller;

import com.project_management_system.user_service.dto.AuthResponseDto;
import com.project_management_system.user_service.dto.LoginRequestDto;
import com.project_management_system.user_service.dto.RegisterRequestDto;
import com.project_management_system.user_service.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

//@CorssOrigin(origins ="*")
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService ;

    @PostMapping("/register")
    public ResponseEntity<AuthResponseDto> register(@Valid @RequestBody RegisterRequestDto registerRequestDto){
        AuthResponseDto responseDto = userService.register(registerRequestDto);
        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> login(@Valid @RequestBody LoginRequestDto loginRequestDto){
        AuthResponseDto responseDto = userService.login(loginRequestDto);
        return new ResponseEntity<>(responseDto,HttpStatus.CREATED);
    }

}
