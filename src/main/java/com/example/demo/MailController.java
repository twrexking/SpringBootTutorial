package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MailController {

    @Autowired
    private JavaMailSender sender;

    @Autowired
    private MailConfig mailConfig;

    @PostMapping("/mail")
    public ResponseEntity<Void> sendPlainText(@RequestBody SendMailRequest request) {
        var msg = new SimpleMailMessage();
        msg.setFrom(String.format("%s<%s>", mailConfig.getDisplayName(), mailConfig.getUsername()));
        msg.setTo(request.getReceivers());
        msg.setSubject(request.getSubject());
        msg.setText(request.getContent());

        sender.send(msg);

        return ResponseEntity.noContent().build();
    }
}