package com.example.university.domain;

import com.example.university.exception.StudentNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * Faculty по ТЗ:
 *  - поле List<Student> students (с геттером/сеттером),
 *  - метод поиска по id.
 * Map как индекс для производительности.
 */
public class Faculty {
    private static final Logger log = LoggerFactory.getLogger(Faculty.class);

    private final String name;

    // Требование ТЗ: хранение в List + геттер/сеттер
    private List<Student> students = new ArrayList<>();

    private final Map<Long, Student> indexById = new HashMap<>();

    public Faculty(String name, List<Student> initial) {
        this.name = requireName(name);
        log.debug("Инициализация факультета '{}'", this.name);                 // факт
        setStudents(initial);
        log.info("Факультет '{}' инициализирован, студентов: {}",               // итог
                this.name, students.size());
    }

    public Faculty(String name) {
        this(name, List.of());
    }

    public String getName() { return name; }

    // ТЗ: геттер
    public List<Student> getStudents() {
        return List.copyOf(students);
    }

    // ТЗ: сеттер
    public void setStudents(List<Student> newStudents) {
        Objects.requireNonNull(newStudents, "students must not be null");
        log.debug("Сеттер students: входной список size={}", newStudents.size()); // факт

        var copy = new ArrayList<Student>(newStudents.size());
        indexById.clear();
        for (Student s : newStudents) {
            Objects.requireNonNull(s, "student must not be null");
            if (!name.equals(s.getFacultyName())) {
                log.error("Факультет студента '{}' не совпадает с факультетом '{}'",
                        s.getFacultyName(), name);
                throw new IllegalArgumentException("Student faculty mismatch: expected " + name);
            }
            if (indexById.putIfAbsent(s.getId(), s) != null) {
                log.error("Дубликат id={} при установке списка студентов", s.getId());
                throw new IllegalArgumentException("Duplicate id: " + s.getId());
            }
            copy.add(s);
        }
        this.students = copy;
        log.info("Сеттер students: установлено {} студентов, индекс построен", // итог
                this.students.size());
    }

    public void addStudent(Student student) {
        Objects.requireNonNull(student, "student must not be null");

        log.debug("Попытка добавить студента: id={}", student.getId());          // факт

        if (!name.equals(student.getFacultyName())) {
            log.error("Факультет студента '{}' не совпадает с факультетом '{}'",
                    student.getFacultyName(), name);
            throw new IllegalArgumentException("Student faculty mismatch: expected " + name);
        }
        if (indexById.containsKey(student.getId())) {
            log.error("Студент с таким id={} уже существует", student.getId());
            throw new IllegalArgumentException("Student id already exists: " + student.getId());
        }

        students.add(student);
        indexById.put(student.getId(), student);
        log.info("Студент добавлен: {}", student);                               // итог
    }

    /** Поиск по ТЗ — через коллекцию студентов. */
    public Optional<Student> findById(long id) {
        log.debug("Поиск студента (List/Stream) id={}", id);                     // факт
        var found = students.stream().filter(s -> s.getId() == id).findFirst();
        if (found.isPresent()) {
            log.info("Студент найден: {}", found.get());                         // итог
        } else {
            log.debug("Студент с id={} не найден (Optional.empty)", id);         // отсутствие результата — не ошибка
        }
        return found;
    }

    /** Вариант с исключением. */
    public Student getByIdOrThrow(long id) {
        log.debug("Поиск студента по id={} (с исключением при отсутствии)", id); // факт
        return findById(id).orElseThrow(() -> {
            log.error("Студент с id={} не найден", id);                          // ошибка
            return new StudentNotFoundException(id);
        });
    }

    private static String requireName(String name) {
        Objects.requireNonNull(name, "name must not be null");
        var t = name.trim();
        if (t.isEmpty()) throw new IllegalArgumentException("name must not be empty");
        return t;
    }

    @Override
    public String toString() {
        return "Faculty{name='%s', students=%d}".formatted(name, students.size());
    }
}


