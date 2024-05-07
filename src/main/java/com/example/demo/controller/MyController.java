package com.example.demo.controller;

import com.example.demo.model.LoginRequest;
import com.example.demo.model.LoginResponse;
import com.example.demo.security.JwtService;
import com.example.demo.security.MemberUserDetails;
import io.jsonwebtoken.JwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class MyController {
    private static final String BEARER_PREFIX = "Bearer ";

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtService jwtService;

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest request) {
        var token = new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword());
        var auth = authenticationManager.authenticate(token);
        var user = (MemberUserDetails) auth.getPrincipal();

        var jwt = jwtService.createLoginAccessToken(user);

        return LoginResponse.of(jwt, user);
    }

    @GetMapping("/who-am-i")
    public Map<String, Object> whoAmI(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorization) {
        var jwt = authorization.substring(BEARER_PREFIX.length());
        try {
            return jwtService.parseToken(jwt);
        } catch (JwtException e) {
            throw new BadCredentialsException(e.getMessage(), e);
        }
    }

    @GetMapping("/home")
    public String home() {
        var principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if ("anonymousUser".equals(principal)) {
            return "你尚未經過身份認證";
        }

        var userDetails = (MemberUserDetails) principal;
        return String.format("嗨，你的編號是%s%n帳號：%s%n暱稱：%s%n權限：%s",
                userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getNickname(),
                userDetails.getAuthorities()
        );
    }
}