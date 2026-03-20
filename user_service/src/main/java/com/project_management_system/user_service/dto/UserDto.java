package com.project_management_system.user_service.dto;

import com.project_management_system.user_service.entity.type.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private Long id ;
    private String username ;
    private String email ;
    private String firstname;
    private String lastname ;
    private String phone ;
    private Role role ;
    private Boolean isActive ;
    private LocalDateTime createdAt;
}
