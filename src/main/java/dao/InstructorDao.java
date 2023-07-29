package dao;

import model.Instructor;

import java.util.List;

public interface InstructorDao {
    void createInstructor(Instructor instructor);
    Instructor getInstructorById(int instructorId);
    Instructor getInstructorByAccountId(int accountId);
    List<Instructor> getAllInstructors();
    List<Instructor> getInstructorsByDepartment(int departmentId);
    void updateInstructor(Instructor instructor);
    void deleteInstructor(int instructorId);
}
