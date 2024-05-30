package com.example.demo.model.response;

import com.example.demo.model.entity.Contact;
import com.example.demo.model.entity.Student;

public class StudentResponse {
    private Long id;
    private String name;
    private String email;
    private String phone;

    public static StudentResponse of(Student student, Contact contact) {
        StudentResponse res =  new StudentResponse();
        res.id = student.getId();
        res.name = student.getName();
        res.email = contact.getEmail();
        res.phone = contact.getPhone();

        return res;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}