package com.example.university.app;

import com.example.university.domain.Faculty;
import com.example.university.domain.Student;
import com.example.university.exception.StudentNotFoundException;

import java.util.List;
import java.util.Scanner;

public final class Main {
    public static void main(String[] args) {
        // Демо-данные
        var faculty = new Faculty("CS", List.of(
                new Student(1, "Ivan",  "Petrov",  "CS-101", "CS"),
                new Student(2, "Anna",  "Sidorova","CS-102", "CS"),
                new Student(3, "Pavel", "Ivanov",  "CS-101", "CS")
        ));

        System.out.println("Faculty: " + faculty);
        System.out.print("Enter student id to search: ");

        try (Scanner in = new Scanner(System.in)) {
            if (!in.hasNextLine()) {
                System.err.println("No input provided.");
                return;
            }

            String line = in.nextLine().trim();
            if (line.isEmpty()) {
                System.err.println("Empty input. Please enter a number id.");
                return;
            }

            long id;
            try {
                id = Long.parseLong(line);
            } catch (NumberFormatException e) {
                System.err.println("Invalid id format. Please enter a number.");
                return;
            }

            try {
                var s = faculty.getByIdOrThrow(id);
                System.out.println("Found: " + s);
            } catch (StudentNotFoundException e) {
                System.err.println(e.getMessage());
                return;
            }
        }
    }
}


