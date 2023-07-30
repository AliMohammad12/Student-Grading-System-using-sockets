package service;

import dao.DepartmentDao;
import model.Department;

import java.util.List;

public class DepartmentService {
    private DepartmentDao departmentDao;

    public DepartmentService(DepartmentDao departmentDao) {
        this.departmentDao = departmentDao;
    }
    public void createDepartment(Department department) {
        departmentDao.createDepartment(department);
    }
    public Department getDepartmentById(int departmentId) {
        return departmentDao.getDepartmentById(departmentId);
    }

    public List<Department> getAllDepartments() {
        return departmentDao.getAllDepartments();
    }

    public void updateDepartment(Department department) {
        departmentDao.updateDepartment(department);
    }
    public void deleteDepartment(int departmentId) {
        departmentDao.deleteDepartment(departmentId);
    }
}
