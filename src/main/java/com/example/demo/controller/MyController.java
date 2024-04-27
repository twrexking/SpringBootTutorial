package com.example.demo.controller;

import com.example.demo.security.UserIdentity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MyController {

    @Autowired
    private UserIdentity userIdentity;

    @GetMapping("/home")
    public String home() {
        if (userIdentity.isAnonymous()) {
            return "你尚未經過身份認證";
        }

        return String.format(
                "嗨，你的編號是%s%n帳號：%s%n暱稱：%s%n信箱：%s%n權限：%s",
                userIdentity.getId(),
                userIdentity.getUsername(),
                userIdentity.getNickname(),
                userIdentity.getEmail(),
                userIdentity.getAuthorities()
        );
    }
}