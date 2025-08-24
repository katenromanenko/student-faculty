package com.example.university.domain;

import com.example.university.exception.StudentNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.testng.annotations.Test;

import java.util.List;

class FacultyTest {

    private Faculty sample() {
        return new Faculty("CS", List.of(
                new Student(1, "Ivan",  "Petrov",  "CS-101", "CS"),
                new Student(2, "Anna",  "Sidorova","CS-102", "CS")
        ));
    }

    @Test
    void getByIdOrThrow_found() {
        var f = sample();
        var s = f.getByIdOrThrow(2);
        Assertions.assertEquals(2, s.getId());
        Assertions.assertEquals("Anna", s.getFirstName());
    }

    @Test
    void getByIdOrThrow_notFound() {
        var f = sample();
        Assertions.assertThrows(StudentNotFoundException.class, () -> f.getByIdOrThrow(999));
    }

    @Test
    void addStudent_rejectsWrongFaculty() {
        var f = sample();
        var wrong = new Student(10, "Oleg", "Smirnov", "CS-103", "MATH");
        var ex = Assertions.assertThrows(IllegalArgumentException.class, () -> f.addStudent(wrong));
        Assertions.assertTrue(ex.getMessage().contains("mismatch"));
    }

    @Test
    void addStudent_rejectsDuplicateId() {
        var f = sample();
        var dup = new Student(2, "Any", "Name", "CS-100", "CS");
        Assertions.assertThrows(IllegalArgumentException.class, () -> f.addStudent(dup));
    }
}

