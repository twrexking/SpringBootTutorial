package com.example.demo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MyController {

    @GetMapping("/home")
    public String home() {
        return "系統首頁";
    }

    @GetMapping("/courses")
    public String getCourses() {
        return "課程列表";
    }

    @PostMapping("/select-course")
    public String selectCourse() {
        return "選課成功";
    }

    @PutMapping("/courses")
    public String updateCourse() {
        return "更新課程成功";
    }
}