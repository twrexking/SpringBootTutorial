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
import java.util.Map;
import java.util.Optional;
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
        List<Student> students = studentRepository.findByNameLikeIgnoreCase("%" + name + "%");
        Map<Student, Long> studentContactMap = students
                .stream()
                .collect(Collectors.toMap(Function.identity(), s -> s.getContact().getId()));

        List<Contact> contacts = contactRepository.findAllById(studentContactMap.values());
        Map<Long, Contact> contactMap = contacts
                .stream()
                .collect(Collectors.toMap(Contact::getId, Function.identity()));

        List<StudentResponse> responses = students
                .stream()
                .map(s -> {
                    Long contactId = studentContactMap.get(s);
                    Contact c = contactMap.get(contactId);
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
        Optional<Student> studentOp = studentRepository.findById(id);
        if (studentOp.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Student student = studentOp.get();
        Contact contact = student.getContact();
        contact.setEmail(request.getEmail());
        contact.setPhone(request.getPhone());
        studentRepository.save(student);

        return ResponseEntity.noContent().build();
    }
}