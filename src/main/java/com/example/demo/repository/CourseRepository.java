package com.example.demo.repository;

import com.example.demo.model.entity.Course;
import com.example.demo.model.entity.QCourse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long>,
        QuerydslPredicateExecutor<Course>, QuerydslBinderCustomizer<QCourse> {

    @Override
    default void customize(QuerydslBindings bindings, QCourse root) {
    }
}