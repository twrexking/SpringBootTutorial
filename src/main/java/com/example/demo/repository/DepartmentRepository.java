package com.example.demo.repository;

import com.example.demo.model.entity.Department;
import com.example.demo.model.entity.QDepartment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.stereotype.Repository;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long>,
        QuerydslPredicateExecutor<Department>, QuerydslBinderCustomizer<QDepartment> {

    @Override
    default void customize(QuerydslBindings bindings, QDepartment root) {
    }
}