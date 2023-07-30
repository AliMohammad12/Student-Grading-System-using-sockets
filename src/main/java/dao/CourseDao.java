package dao;
import model.*;

import java.util.List;
import java.util.Map;

public interface CourseDao {
    void createCourse(Course course);
    Course getCourseById(int courseId);
    Map<Course, List<Instructor>>  getCourseDetailsWithInstructors();
    List<CourseEnrollment> getStudentCourseEnrollments(Student student); // change this to Course Enrollment
    List<Course> getAllStudentCourses(Student student);
    List<Course> getAllInstructorCourses(Instructor instructor);
    List<Course> getUnassignedCoursesFromSameDept(Instructor instructor);
    void assignCourseToInstructor(Course course, Instructor instructor);
    void removeCourseFromInstructor(Course course, Instructor instructor);
    List<Course> getAllCourses();
    List<Course> getCoursesByDepartment(int departmentId);
    CourseEnrollment getCourseEnrollment(Student student, Course course, Instructor instructor); // course enrollment
    void updateCourseEnrollmentGradeById(int courseEnrollmentId, String newGrade); // courseEnrollment
    void deleteStudentCourse(int studentId, int courseId);
    void updateCourse(Course course);
    void deleteCourse(int courseId);
    void deleteCourseEnrollmentByStudentId(int studentId);
    void enrollStudentInCourse(Course course, Instructor instructor, int studentId);
    void deleteInstructorCoursesByInstructorId(int instructorId);
    void deleteStudentCoursesByInstructorId(int instructorId);
}
