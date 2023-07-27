package dao;
import model.Course;
import model.Instructor;

import java.util.List;
import java.util.Map;

public interface CourseDao {
    void createCourse(Course course);
    Course getCourseById(int courseId);
    Map<Course, List<Instructor>>  getCourseDetailsWithInstructors();
    List<Course> getAllCourses();
    List<Course> getCoursesByDepartment(int departmentId);
    void updateCourse(Course course);
    void deleteCourse(int courseId);
    void enrollStudentInCourse(Course course, Instructor instructor, int studentId);
}
