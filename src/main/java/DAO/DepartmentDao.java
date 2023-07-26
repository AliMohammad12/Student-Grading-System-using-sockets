package DAO;

import Model.Department;

import java.util.List;

public interface DepartmentDao {
    void createDepartment(Department department);
    Department getDepartmentById(int departmentId);
    List<Department> getAllDepartments();
    void updateDepartment(Department department);
    void deleteDepartment(int departmentId);
}
