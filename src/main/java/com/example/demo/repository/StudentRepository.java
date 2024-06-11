package com.example.demo.repository;

import com.example.demo.model.entity.QCourse;
import com.example.demo.model.entity.QStudent;
import com.example.demo.model.entity.Student;
import com.querydsl.core.types.dsl.StringExpression;
import com.querydsl.jpa.JPAExpressions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long>,
        QuerydslPredicateExecutor<Student>, QuerydslBinderCustomizer<QStudent> {

    @Override
    default void customize(QuerydslBindings bindings, QStudent root) {
        bindings.bind(root.name)
                .first(StringExpression::containsIgnoreCase);

        bindings.bind(root.grade)
                .all((path, value) -> Optional.ofNullable(path.in(value)));

        bindings.bind(root.department.name)
                .as("deptName")
                .first(StringExpression::containsIgnoreCase);

        bindings.bind(root.id)
                .as("minTotalCredits")
                .first((path, value) -> {
                    var creditSubQuery = JPAExpressions
                            .select(root.id)
                            .from(root)
                            .join(root.courses, QCourse.course)
                            .groupBy(root.id)
                            .having(QCourse.course.credit.sum().goe(value));
                    return path.in(creditSubQuery);
                });
    }
}