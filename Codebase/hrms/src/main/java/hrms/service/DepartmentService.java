package hrms.service;

import java.util.ArrayList;
import java.util.List;

import hrms.model.Department;
import hrms.repository.DepartmentRepository;

/*
 * Service layer for handling department-related business logic.
 */
public class DepartmentService {

    private final DepartmentRepository departmentRepository;
    public DepartmentService(DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
    }

    /*
     * Adds a new department to the system.
     *
     * @param departmentId - unique department ID
     * @param name - department name
     * @param description - optional description
     * @return created Department object
     */
    public Department addDepartment(String departmentId, String name, String description) {

        if (departmentRepository.existsById(departmentId)) {
            throw new IllegalArgumentException("Department ID already exists.");
        }

        Department department = new Department(departmentId, name, description);
        departmentRepository.save(department);

        return department;
    }

    /*
     * Retrieves a department by its ID.
     *
     * @param departmentId - ID of the department
     * @return Department object if found
     */
    public Department getDepartmentById(String departmentId) {

        Department department = departmentRepository.findById(departmentId);

        if (department == null) {
            throw new IllegalArgumentException("Department not found.");
        }

        return department;
    }

    /*
     * Retrieves all departments in the system.
     *
     * @return list of Department objects
     */
    public List<Department> getAllDepartments() {

        return new ArrayList<>(departmentRepository.findAll());
    }

    /*
     * Deletes a department by its ID.
     *
     * @param departmentId - ID of the department to delete
     */
    public void deleteDepartment(String departmentId) {

        if (!departmentRepository.existsById(departmentId)) {
            throw new IllegalArgumentException("Department not found.");
        }

        departmentRepository.deleteById(departmentId);
    }
}