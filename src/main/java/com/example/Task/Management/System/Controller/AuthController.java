package com.example.Task.Management.System.Controller;

import com.example.Task.Management.System.DTO.LoginRequestDTO;
import com.example.Task.Management.System.DTO.LoginResponseDTO;
import com.example.Task.Management.System.DTO.RegisterRequestDTO;
import com.example.Task.Management.System.DTO.UserResponseDTO;
import com.example.Task.Management.System.Entites.User;
import com.example.Task.Management.System.Repository.UserRepository;
import com.example.Task.Management.System.Service.UserService;

import jakarta.validation.Valid;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;
    private final UserRepository userRepository;

    public AuthController(
            UserService userService,
            UserRepository userRepository
    ) {
        this.userService = userService;
        this.userRepository = userRepository;
    }


    // REGISTER

    @PostMapping("/register")
    public UserResponseDTO register(
            @Valid @RequestBody RegisterRequestDTO request
    ) {
        return userService.register(request);
    }


    // LOGIN

    @PostMapping("/login")
    public LoginResponseDTO login(
            @Valid @RequestBody LoginRequestDTO request
    ) {
        return userService.login(request);
    }


    // CURRENT LOGGED-IN USER

    @GetMapping("/me")
    public UserResponseDTO getCurrentUser(
            Authentication authentication
    ) {

        String email = authentication.getName();

        User user = userRepository
                .findByEmail(email)
                .orElseThrow(
                        () -> new RuntimeException(
                                "User not found"
                        )
                );

        return new UserResponseDTO(
                user.getId(),
                user.getUsername(),
                user.getEmail()
        );
    }
}