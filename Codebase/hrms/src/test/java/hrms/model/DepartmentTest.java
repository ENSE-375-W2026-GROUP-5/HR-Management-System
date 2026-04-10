package hrms.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;

public class DepartmentTest {

    @Test
    void shouldCreateDepartmentSuccessfully() {
        Department department = new Department("D001", "IT", "Information Technology");

        assertEquals("D001", department.getDepartmentId());
        assertEquals("IT", department.getName());
        assertEquals("Information Technology", department.getDescription());
    }

    @Test
    void shouldThrowExceptionWhenDepartmentIdIsBlank() {
        assertThrows(IllegalArgumentException.class, () ->
                new Department("", "IT", "Information Technology")
        );
    }

    @Test
    void shouldThrowExceptionWhenDepartmentNameIsBlank() {
        assertThrows(IllegalArgumentException.class, () ->
                new Department("D001", "", "Information Technology")
        );
    }

    @Test
    void shouldUpdateDepartmentName() {
        Department department = new Department("D001", "IT", "Information Technology");

        department.setName("HR");

        assertEquals("HR", department.getName());
    }

    @Test
    void shouldThrowExceptionWhenSettingBlankDepartmentName() {
        Department department = new Department("D001", "IT", "Information Technology");

        assertThrows(IllegalArgumentException.class, () ->
                department.setName(" ")
        );
    }

    @Test
    void shouldUpdateDepartmentDescription() {
        Department department = new Department("D001", "IT", "Information Technology");

        department.setDescription("New Description");

        assertEquals("New Description", department.getDescription());
    }
}