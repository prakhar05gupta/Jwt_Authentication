package com.project_management_system.user_service.dto;

import com.project_management_system.user_service.entity.type.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterRequestDto {

    @NotBlank(message="Username is Required")
    @Size(min=3,max=20,message="Username must be between 3 to 20 characters")
    private String username ;

    @NotBlank(message="Email is required")
    @Email(message="Email should be valid")
    private String email;

    @NotBlank(message="Password is required")
    @Size(min=6,message="Password must be at least 6 characters")
    private String password ;

    @NotBlank(message="First name is required")
    private String firstName ;

    @NotBlank(message ="Last name is required")
    private String lastName ;

    private String phone ;

    private Role role = Role.MEMBER ;

}
