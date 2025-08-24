import com.example.university.domain.Faculty;
import com.example.university.domain.Student;
import com.example.university.exception.StudentNotFoundException;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

import org.slf4j.LoggerFactory;
import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;

/**
 * Тесты Faculty: поиск студента по ID.
 * В @BeforeEach включаю DEBUG и печатаю читаемое имя теста.
 */
@DisplayName("Тесты Faculty: поиск студента по ID")
class FacultyTest {

    private Faculty faculty;

    @BeforeEach
    void init(TestInfo testInfo) {
        // Включаем DEBUG для корневого логгера, чтобы видеть сообщения из Faculty
        Logger root = (Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
        root.setLevel(Level.DEBUG);

        root.debug("==> Запуск теста: {}", testInfo.getDisplayName());

        faculty = new Faculty("CS", java.util.List.of(
                new Student(1, "Ivan",  "Petrov",  "CS-101", "CS"),
                new Student(2, "Anna",  "Sidorova","CS-102", "CS")
        ));
    }

    @Test
    @DisplayName("Поиск студента по ID — студент найден")
    void getByIdOrThrow_found() {
        var s = faculty.getByIdOrThrow(2L);
        assertEquals(2L, s.getId());
        assertEquals("Anna", s.getFirstName());
    }

    @Test
    @DisplayName("Поиск студента по ID — студент не найден")
    void getByIdOrThrow_notFound() {
        assertThrows(StudentNotFoundException.class, () -> faculty.getByIdOrThrow(999L));
    }

    @Test
    @DisplayName("addStudent: отклоняет студента с другим факультетом")
    void addStudent_rejectsWrongFaculty() {
        var wrong = new Student(10, "Oleg", "Smirnov", "CS-103", "MATH");
        var ex = assertThrows(IllegalArgumentException.class, () -> faculty.addStudent(wrong));
        assertTrue(ex.getMessage().contains("mismatch"));
    }

    @Test
    @DisplayName("addStudent: отклоняет дубликат ID")
    void addStudent_rejectsDuplicateId() {
        var dup = new Student(2, "Any", "Name", "CS-100", "CS");
        assertThrows(IllegalArgumentException.class, () -> faculty.addStudent(dup));
    }

    @Test
    @DisplayName("findById: возвращает Optional.empty, если студента нет")
    void findById_emptyWhenMissing() {
        assertTrue(faculty.findById(12345L).isEmpty());
    }
}

