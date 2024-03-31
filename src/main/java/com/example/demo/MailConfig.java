package com.example.demo;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MailConfig {

    @Value("${spring.mail.username}")
    private String username;

    @Value("${mail.display-name}")
    private String displayName;

    public String getUsername() {
        return username;
    }

    public String getDisplayName() {
        return displayName;
    }
}