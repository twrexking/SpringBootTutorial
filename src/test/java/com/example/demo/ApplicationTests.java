package com.example.demo;

import com.example.demo.model.entity.*;
import com.example.demo.model.StudentAggregate;
import com.example.demo.repository.CourseRepository;
import com.example.demo.repository.DepartmentRepository;
import com.example.demo.repository.StudentRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.*;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ApplicationTests {

    @Autowired
    private JPAQueryFactory queryFactory;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Before
    public void init() {
        studentRepository.deleteAll();
        departmentRepository.deleteAll();
        courseRepository.deleteAll();
    }

    private Student insertStudent(String name, int grade) {
        var s = new Student();
        s.setName(name);
        s.setGrade(grade);
        studentRepository.save(s);
        return studentRepository.findById(s.getId()).orElseThrow();
    }

    private Course insertCourse(String name, int credit) {
        var c = new Course();
        c.setName(name);
        c.setCredit(credit);
        courseRepository.save(c);
        return courseRepository.findById(c.getId()).orElseThrow();
    }

    private Department insertDepartment(String name) {
        var d = new Department();
        d.setName(name);
        departmentRepository.save(d);
        return departmentRepository.findById(d.getId()).orElseThrow();
    }

    @Test
    public void testFindByName() {
        var s1 = insertStudent("Vincent", 2);
        var s2 = insertStudent("Ivy", 3);

        var students = queryFactory
                .selectFrom(QStudent.student)
                .where(QStudent.student.name.eq("Vincent"))
                .fetch();
        assertEquals(1, students.size());
        assertEquals(s1.getId(), students.get(0).getId());

        students = queryFactory
                .selectFrom(QStudent.student)
                .where(QStudent.student.name.containsIgnoreCase("i"))
                .fetch();
        assertEquals(2, students.size());

        students = queryFactory
                .selectFrom(QStudent.student)
                .where(QStudent.student.name.in(List.of("Vincent", "Ivy")))
                .fetch();
        assertEquals(2, students.size());
    }

    @Test
    public void testFindByGradeRange() {
        var s1 = insertStudent("Vincent", 2);
        var s2 = insertStudent("Ivy", 3);

        var students = queryFactory
                .selectFrom(QStudent.student)
                .where(QStudent.student.grade.gt(2))
                .fetch();
        assertEquals(1, students.size());
        assertEquals(s2.getId(), students.get(0).getId());

        students = queryFactory
                .selectFrom(QStudent.student)
                .where(QStudent.student.grade.goe(2))
                .fetch();
        assertEquals(2, students.size());

        students = queryFactory
                .selectFrom(QStudent.student)
                .where(QStudent.student.grade.between(2, 3))
                .fetch();
        assertEquals(2, students.size());
    }

    @Test
    public void testManyToOneJoin() {
        var s1 = insertStudent("Vincent", 2);
        var s2 = insertStudent("Ivy", 3);
        var s3 = insertStudent("Linda", 4);
        var d1 = insertDepartment("資訊管理");
        var d2 = insertDepartment("企業管理");
        s1.setDepartment(d1);
        s2.setDepartment(d1);
        s3.setDepartment(d2);
        studentRepository.saveAll(List.of(s1, s2, s3));

        var students = queryFactory
                .selectFrom(QStudent.student)
                .join(QDepartment.department)
                .on(QStudent.student.department.id.eq(QDepartment.department.id))
                .where(QDepartment.department.name.containsIgnoreCase("資訊"))
                .fetch();

        var actualIds = students.stream().map(Student::getId).toList();
        var expectedIds = List.of(s1.getId(), s2.getId());
        assertEquals(expectedIds, actualIds);
    }

    @Test
    public void testManyToManyJoin() {
        var s1 = insertStudent("Vincent", 2);
        var s2 = insertStudent("Ivy", 3);
        var c1 = insertCourse("Java", 3);
        var c2 = insertCourse("Database", 5);
        var c3 = insertCourse("Data Structure", 7);

        s1.setCourses(Set.of(c1, c2));
        s2.setCourses(Set.of(c2, c3));
        studentRepository.saveAll(List.of(s1, s2));

        var courses = queryFactory
                .select(QCourse.course)
                .from(QStudent.student)
                .join(QStudent.student.courses, QCourse.course)
                .where(QStudent.student.id.eq(s1.getId()))
                .fetch();

        var actualIds = courses.stream().map(Course::getId).collect(Collectors.toSet());
        var expectedIds = s1.getCourses().stream().map(Course::getId).collect(Collectors.toSet());
        assertEquals(expectedIds, actualIds);
    }

    @Test
    public void testAddDepartmentNameField() {
        var s1 = insertStudent("Vincent", 2);
        var s2 = insertStudent("Ivy", 3);
        var d1 = insertDepartment("資訊管理");
        var d2 = insertDepartment("企業管理");

        s1.setDepartment(d1);
        s2.setDepartment(d2);
        studentRepository.saveAll(List.of(s1, s2));

        var tuples = queryFactory
                .select(QStudent.student, QDepartment.department.name)
                .from(QStudent.student)
                .join(QDepartment.department)
                .on(QStudent.student.department.id.eq(QDepartment.department.id))
                .fetch();

        var students = tuples
                .stream()
                .map(tuple -> {
                    var student = new StudentAggregate();
                    student.setName(tuple.get(QStudent.student.name));
                    student.setDepartmentName(tuple.get(QDepartment.department.name));

                    var studentId = Optional.ofNullable(tuple.get(QStudent.student))
                            .map(Student::getId)
                            .orElse(null);
                    student.setId(studentId);

                    var grade = Optional.ofNullable(tuple.get(QStudent.student.grade)).orElse(0);
                    student.setGrade(grade);

                    return student;
                })
                .toList();

        var studentDeptNameMap = students
                .stream()
                .collect(Collectors.toMap(StudentAggregate::getId, StudentAggregate::getDepartmentName));
        assertEquals(d1.getName(), studentDeptNameMap.get(s1.getId()));
        assertEquals(d2.getName(), studentDeptNameMap.get(s2.getId()));
    }

    @Test
    public void testFindByTotalCourseCredits() {
        var s1 = insertStudent("Vincent", 2);
        var s2 = insertStudent("Ivy", 3);
        var c1 = insertCourse("Java", 3);
        var c2 = insertCourse("Database", 5);
        var c3 = insertCourse("Data Structure", 7);

        s1.setCourses(Set.of(c1, c2));
        s2.setCourses(Set.of(c2, c3));
        studentRepository.saveAll(List.of(s1, s2));

        var tuples = queryFactory
                .select(QStudent.student, QCourse.course.credit.sum())
                .from(QStudent.student)
                .join(QStudent.student.courses, QCourse.course)
                .groupBy(QStudent.student.id)
                .having(QCourse.course.credit.sum().gt(5))
                .fetch();

        var studentCreditsMap = new HashMap<Long, Integer>();
        tuples.forEach(tuple -> {
            var studentId = Optional.ofNullable(tuple.get(QStudent.student))
                    .map(Student::getId)
                    .orElse(null);
            var credit = Optional.ofNullable(tuple.get(QCourse.course.credit.sum())).orElse(0);
            studentCreditsMap.put(studentId, credit);
        });

        assertEquals(8, studentCreditsMap.get(s1.getId()).intValue());
        assertEquals(12, studentCreditsMap.get(s2.getId()).intValue());
    }
}