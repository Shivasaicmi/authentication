package com.shivasai.authentication.controllers;

import com.shivasai.authentication.Configuration.JwtService;
import com.shivasai.authentication.Entities.Repository;
import com.shivasai.authentication.Entities.UserEntity;
import com.shivasai.authentication.Enums.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/authentication")
public class AuthenticationController {

    @Autowired
    private final Repository userRepository;
    @Autowired
    private final PasswordEncoder passwordEncoder;

    @Autowired
    private final JwtService jwtService;

    @Autowired
    private final AuthenticationManager authenticationManager;

    public AuthenticationController(Repository userRepository, PasswordEncoder passwordEncoder,JwtService jwtService,AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UserEntity userEntity){
        var user = UserEntity.builder()
                .firstName(userEntity.getFirstName())
                .lastName(userEntity.getLastName())
                .email(userEntity.getEmail())
                .password(passwordEncoder.encode(userEntity.getPassword()))
                .role(Role.USER)
                .build();
        userRepository.save(user);

        var jwtToken = jwtService.generateToken(user);

        return new ResponseEntity<>(jwtToken,HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(){
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                      "shivasai@gmail.com",
                        "shivasai"
                )
        );
        var user = userRepository.findByEmail("shivasai@gmail.com").orElseThrow();
        var jwtToken = jwtService.generateToken(user);
        return new ResponseEntity<>(jwtToken,HttpStatus.OK);
    }

}
