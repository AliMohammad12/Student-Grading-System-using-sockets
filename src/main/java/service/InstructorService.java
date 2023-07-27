package service;

import dao.InstructorDao;
import model.Instructor;

import java.util.List;

public class InstructorService {
    private InstructorDao instructorDao;

    public InstructorService(InstructorDao instructorDao) {
        this.instructorDao = instructorDao;
    }

    public void createInstructor(Instructor instructor) {
        instructorDao.createInstructor(instructor);
    }

    public Instructor getInstructorById(int instructorId) {
        return instructorDao.getInstructorById(instructorId);
    }

    public List<Instructor> getAllInstructors() {
        return instructorDao.getAllInstructors();
    }

    public List<Instructor> getInstructorsByDepartment(int departmentId) {
        return getInstructorsByDepartment(departmentId);
    }

    public void updateInstructor(Instructor instructor) {
        instructorDao.updateInstructor(instructor);
    }

    public void deleteInstructor(int instructorId) {
        instructorDao.deleteInstructor(instructorId);
    }

}
