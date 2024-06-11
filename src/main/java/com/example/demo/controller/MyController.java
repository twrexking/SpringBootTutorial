package com.example.demo.controller;

import com.example.demo.model.entity.Student;
import com.example.demo.model.StudentAggregate;
import com.example.demo.repository.StudentRepository;
import com.querydsl.core.types.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.StreamSupport;

@RestController
public class MyController {

    @Autowired
    private StudentRepository studentRepository;

    @GetMapping("/students")
    public ResponseEntity<List<StudentAggregate>> getStudents(
            @QuerydslPredicate(root = Student.class) Predicate predicate
    ) {
        var studentIt = studentRepository.findAll(predicate);
        var students = StreamSupport.stream(studentIt.spliterator(), false)
                .map(s -> {
                    StudentAggregate student = new StudentAggregate();
                    student.setId(s.getId());
                    student.setName(s.getName());
                    student.setDepartmentName(s.getDepartment().getName());
                    student.setGrade(s.getGrade());

                    return student;
                })
                .toList();

        return ResponseEntity.ok(students);
    }
}