package com.ey.springboot3security.controller;

import com.ey.springboot3security.dto.AuthRequest;
import com.ey.springboot3security.entity.Response;
import com.ey.springboot3security.entity.UserInfo;
import com.ey.springboot3security.service.JwtService;
import com.ey.springboot3security.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private UserInfoService service;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/generateToken")
    public ResponseEntity<Response<String>> authenticateAndGetToken(@RequestBody AuthRequest authRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword())
            );

            if (authentication.isAuthenticated()) {
                // Generate token and wrap it in a success response
                Response<String> token = jwtService.generateToken(authRequest.getUsername());
                return ResponseEntity.status(HttpStatus.OK).body(token);
            } else {
                // Return a standardized error response if authentication fails
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Response.error("invalid user credentials", HttpStatus.BAD_REQUEST.value()));
            }
        } catch (Exception ex) {
            // Catch any exceptions and return a standard error response
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Response.error("authentication failed: " + ex.getMessage(), HttpStatus.BAD_REQUEST.value()));
        }
    }
}
