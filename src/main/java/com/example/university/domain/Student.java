package com.example.university.domain;

import java.util.Objects;

public final class Student {
    private final long id;
    private final String firstName;
    private final String lastName;
    private final String groupNumber;
    private final String facultyName;

    public Student(long id,
                   String firstName,
                   String lastName,
                   String groupNumber,
                   String facultyName) {

        if(id <= 0){
            throw new IllegalArgumentException("id must be positive");
        }
        this.firstName = requireText(firstName, "firstName");
        this.lastName = requireText(lastName, "lastName");
        this.groupNumber = requireText(groupNumber, "groupNumber");
        this.facultyName = requireText(facultyName, "facultyName");
        this.id = id;
    }

    private String requireText(String text, String name) {
        Objects.requireNonNull(text,name + " must not be null");
        var trimmedText = text.trim();
        if(trimmedText.isEmpty()){
            throw new IllegalArgumentException(name + " must not be empty");
        }
        return trimmedText;
    }

    public long getId() { return id; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getGroupNumber() { return groupNumber; }
    public String getFacultyName() { return facultyName; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        return (o instanceof Student student) && student.id == id;
    }

    @Override
    public int hashCode() { return Long.hashCode(id); }

    @Override
    public String toString() {
        return "Student{id=%d, firstName=%s, lastName=%s, group=%s, faculty=%s}"
                .formatted(id, firstName, lastName, groupNumber, facultyName);
    }
}
