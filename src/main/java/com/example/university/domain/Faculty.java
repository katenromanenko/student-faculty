package com.example.university.domain;

import com.example.university.exception.StudentNotFoundException;

import java.util.*;

public class Faculty {
    private final String name;
    private final Map<Long, Student> indexById = new HashMap<>();

    public Faculty(String name, List<Student> initial) {
        this.name = requireName(name);
        if (initial != null) {
            for (Student student : initial) addStudent(student);
        }
    }

    public Faculty(String name) {
        this(name, List.of());
    }

    public String getName() { return name; }

    public List<Student> getStudents() {
        return List.copyOf(indexById.values());
    }

    public void addStudent(Student student) {
        Objects.requireNonNull(student, "Student must not be null");
        if (!name.equals(student.getFacultyName())){
            throw new IllegalArgumentException("Student faculty mismatch: expected " + name);
        }
        var prev = indexById.putIfAbsent(student.getId(), student);
        if (prev != null) {
            throw new IllegalArgumentException("Student id already exists: " + student.getId());
        }
    }
    /** Поиск с Optional — без исключений */
    public Optional<Student> findById(long id) {
        return Optional.ofNullable(indexById.get(id));
    }

    /** Поиск с исключением - по заданию */
    public Student getByIdOrThrow(long id) {
        var student = indexById.get(id);
        if (student == null) throw new StudentNotFoundException(id);
        return student;
    }

    private static String requireName(String name) {
        Objects.requireNonNull(name, "Name must not be null");
        var trimmedName = name.trim();
        if (trimmedName.isEmpty()) throw new IllegalArgumentException("Name must not be empty");
        return trimmedName;
    }

    @Override
    public String toString() {
        return "Faculty{name='%s', students=%d}".formatted(name, indexById.size());
    }



}
