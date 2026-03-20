package com.project_management_system.user_service.service;

import com.project_management_system.user_service.dto.AuthResponseDto;
import com.project_management_system.user_service.dto.LoginRequestDto;
import com.project_management_system.user_service.dto.RegisterRequestDto;
import com.project_management_system.user_service.dto.UserDto;
import com.project_management_system.user_service.entity.User;
import com.project_management_system.user_service.exception.UserAlreadyExistsException;
import com.project_management_system.user_service.exception.UserNotFoundException;
import com.project_management_system.user_service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService{
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final ModelMapper modelMapper;

    @Override
    public AuthResponseDto register(RegisterRequestDto registerRequestDto){
        if(userRepository.existsByUsername(registerRequestDto.getUsername())){
            throw new UserAlreadyExistsException("Username already exists!");
        }
        if (userRepository.existsByEmail(registerRequestDto.getEmail())){
            throw new UserAlreadyExistsException("Email already exists!");
        }
        User user = modelMapper.map(registerRequestDto ,User.class);

        user.setPassword(passwordEncoder.encode(registerRequestDto.getPassword()));

        User savedUser = userRepository.save(user);

//        UserDetails userDetails = userDetailsService.loadUserByUsername(savedUser.getUsername());
//         this above line have issue of calling duplicate query 1 at saveduser and another at loadUserByUsername() so for optimize it we use below userDetails
        UserDetails userDetails = org.springframework.security.core.userdetails.User
                .withUsername(savedUser.getUsername())
                .password(savedUser.getPassword())
                .authorities("Role_"+savedUser.getRole().name())
                .build() ;

        String jwtToken = jwtService.generateToken(userDetails);

        return new AuthResponseDto(
                jwtToken,
                savedUser.getId(),
                savedUser.getUsername(),
                savedUser.getEmail(),
                savedUser.getRole().name()
        );
    }

    @Override
    public AuthResponseDto login(LoginRequestDto loginRequestDto) {

        // Find user by username or email
        User user = loginRequestDto.getUsernameOrEmail().contains("@")
                ? userRepository.findByEmail(loginRequestDto.getUsernameOrEmail())
                .orElseThrow(() -> new UserNotFoundException("User not found!"))
                : userRepository.findByUsername(loginRequestDto.getUsernameOrEmail())
                .orElseThrow(() -> new UserNotFoundException("User not found!"));

        // Authenticate using actual username
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        user.getUsername(),   // always use username for auth
                        loginRequestDto.getPassword()
                )
        );

        // Generate JWT Token
        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getUsername());
        String jwtToken = jwtService.generateToken(userDetails);

        return new AuthResponseDto(
                jwtToken,
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getRole().name()
        );
    }

    @Override
    @Transactional(readOnly = true)
    public UserDto getUserById(Long id) {
        User user =userRepository.findById(id)
                .orElseThrow(()->new UserNotFoundException("User not found with id: "+ id));
//       //Map user entity to UserDto using ModelMapper
        return modelMapper.map(user, UserDto.class);
    }

    @Override
    @Transactional(readOnly = true)
    public UserDto getUserByUsername(String username) {
       User user = userRepository.findByUsername(username)
               .orElseThrow(()->new UserNotFoundException("User not found with Username: "+username));
       return modelMapper.map(user,UserDto.class);
    }

    @Override
    public UserDto updateUser(Long id, UserDto userDto) {
        User user = userRepository.findById(id)
                .orElseThrow(()->new UserNotFoundException("User not found with id: "+id));

        //Map only non-null fields from UserDto to User entity
        modelMapper.map(userDto,user);

        //Don't update password here (separate end point for password change)
        //Don't update username and email if not changed

        User updatedUser = userRepository.save(user);
        return modelMapper.map(updatedUser,UserDto.class);
    }


    @Override
    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(()->new UserNotFoundException("User not found with id: "+id));
        userRepository.delete(user);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserDto> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(user -> modelMapper.map(user,UserDto.class))
                .collect(Collectors.toList());
    }
    @Override
    @Transactional(readOnly = true)
    public UserDto getCurrentUser(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(()->new UserNotFoundException("User not found!"));
        return modelMapper.map(user,UserDto.class);
    }
}
