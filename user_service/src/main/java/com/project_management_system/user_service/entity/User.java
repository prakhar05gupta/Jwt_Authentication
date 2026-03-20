package com.project_management_system.user_service.entity;

import com.project_management_system.user_service.entity.type.Role;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;


@Setter
@Getter
@Entity
@Table(name = "users", indexes = {
        @Index(name = "idx_username", columnList = "username"),
        @Index(name = "idx_email", columnList = "email")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy =GenerationType.IDENTITY)
    private Long id ;
    @Column(unique=true,nullable = false)
    private String username;
    @Column(unique = true,nullable = false)
    private String email;

    private String password ;

    private String firstName;
    private String lastName;
    private String phone;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role =Role.MEMBER;

    @Column(nullable = false)
    private Boolean isActive =true ;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt ;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

   }
