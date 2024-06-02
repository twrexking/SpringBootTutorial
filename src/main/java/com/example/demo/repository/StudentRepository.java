package com.example.demo.repository;

import com.example.demo.model.entity.Student;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    List<Student> findByNameLikeIgnoreCase(String name);

    @EntityGraph(attributePaths = {"contact"})
    Optional<Student> findById(Long id);

    @Query(
            nativeQuery = true,
            value = "SELECT * FROM `student` WHERE `dept_id` = ?1"
    )
    List<Student> findByDepartmentId(Long id);
}