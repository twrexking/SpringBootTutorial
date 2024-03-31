package com.example.demo;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MailController {

    @PostMapping("/mail")
    public ResponseEntity<Void> sendPlainText(@RequestBody SendMailRequest request) {
        // TODO
        return ResponseEntity.noContent().build();
    }
}