package com.example.demo.controller;

import com.example.demo.entity.Contact;
import com.example.demo.entity.Student;
import com.example.demo.entity.StudentResponse;
import com.example.demo.repository.ContactRepository;
import com.example.demo.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@RestController
public class MyController {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private ContactRepository contactRepository;

    @GetMapping("/students")
    public ResponseEntity<List<StudentResponse>> getStudents(
            @RequestParam(required = false, defaultValue = "") String name
    ) {
        var students = studentRepository.findByNameLikeIgnoreCase("%" + name + "%");
        var studentContactMap = students
                .stream()
                .collect(Collectors.toMap(Function.identity(), s -> s.getContact().getId()));

        var contacts = contactRepository.findAllById(studentContactMap.values());
        var contactMap = contacts
                .stream()
                .collect(Collectors.toMap(Contact::getId, Function.identity()));

        var responses = students
                .stream()
                .map(s -> {
                    var contactId = studentContactMap.get(s);
                    var c = contactMap.get(contactId);
                    return StudentResponse.of(s, c);
                })
                .toList();

        return ResponseEntity.ok(responses);
    }

    @PostMapping("/students")
    public ResponseEntity<Void> createStudent(@RequestBody Student student) {
        student.setId(null);
        studentRepository.save(student);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/students/{id}")
    public ResponseEntity<Void> deleteStudent(@PathVariable Long id) {
        studentRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/students/{id}/contact")
    public ResponseEntity<Void> updateStudentContact(
            @PathVariable Long id, @RequestBody Contact request
    ) {
        var studentOp = studentRepository.findById(id);
        if (studentOp.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        var student = studentOp.get();
        var contact = student.getContact();
        contact.setEmail(request.getEmail());
        contact.setPhone(request.getPhone());
        studentRepository.save(student);

        return ResponseEntity.noContent().build();
    }
}