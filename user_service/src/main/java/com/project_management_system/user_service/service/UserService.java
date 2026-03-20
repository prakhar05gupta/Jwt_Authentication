package com.project_management_system.user_service.service;

import com.project_management_system.user_service.dto.AuthResponseDto;
import com.project_management_system.user_service.dto.LoginRequestDto;
import com.project_management_system.user_service.dto.RegisterRequestDto;
import com.project_management_system.user_service.dto.UserDto;

import java.util.List;

public interface UserService {
    AuthResponseDto register(RegisterRequestDto registerRequestDto);
    AuthResponseDto login(LoginRequestDto loginRequestDto);
    UserDto getUserById(Long id);
    UserDto getUserByUsername(String username);
    UserDto updateUser(Long id, UserDto userDto);
    void deleteUser(Long id);
    List<UserDto> getAllUsers();
    UserDto getCurrentUser(String username);
}
