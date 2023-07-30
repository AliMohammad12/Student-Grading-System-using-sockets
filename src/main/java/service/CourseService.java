package service;

import dao.CourseDao;
import model.*;

import java.util.List;
import java.util.Map;

public class CourseService {
    private CourseDao courseDao;
    public CourseService(CourseDao courseDao) {
        this.courseDao = courseDao;
    }
    public void createCourse(Course course) {
        courseDao.createCourse(course);
    }
    public Course getCourseById(int courseId) {
        return courseDao.getCourseById(courseId);
    }
    public Map<Course, List<Instructor>> getCourseDetailsWithInstructors() {
        return courseDao.getCourseDetailsWithInstructors();
    }
    public List<Course> getAllStudentCourses(Student student) {
        return courseDao.getAllStudentCourses(student);
    }
    public List<Course> getAllCourses() {
        return courseDao.getAllCourses();
    }
    public List<CourseEnrollment> getStudentCourseEnrollments(Student student) {
        return courseDao.getStudentCourseEnrollments(student);
    }
    public CourseEnrollment getCourseEnrollment(Student student, Course course, Instructor instructor) {
        return courseDao.getCourseEnrollment(student, course, instructor);
    }
    public void updateCourseEnrollmentGradeById(int courseEnrollmentId, String newGrade) {
        courseDao.updateCourseEnrollmentGradeById(courseEnrollmentId, newGrade);
    }
    public void deleteStudentCourse(int studentId, int courseId) {
        courseDao.deleteStudentCourse(studentId, courseId);
    }
    public void enrollStudentInCourse(Course course, Instructor instructor, int studentId) {
        courseDao.enrollStudentInCourse(course, instructor, studentId);
    }
    public void assignCourseToInstructor(Course course, Instructor instructor) {
        courseDao.assignCourseToInstructor(course, instructor);
    }
    public void removeCourseFromInstructor(Course course, Instructor instructor) {
        courseDao.removeCourseFromInstructor(course, instructor);
    }
    public List<Course> getAllInstructorCourses(Instructor instructor) {
        return courseDao.getAllInstructorCourses(instructor);
    }
    public List<Course> getUnassignedCoursesFromSameDept(Instructor instructor) {
        return courseDao.getUnassignedCoursesFromSameDept(instructor);
    }
    public List<Course> getCoursesByDepartment(int departmentId) {
        return courseDao.getCoursesByDepartment(departmentId);
    }
    public void updateCourse(Course course) {
        courseDao.updateCourse(course);
    }
    public void deleteCourse(int courseId) {
        courseDao.deleteCourse(courseId);
    }
    public void deleteCourseEnrollmentByStudentId(int studentId) {
        courseDao.deleteCourseEnrollmentByStudentId(studentId);
    }
    public void deleteInstructorCoursesByInstructorId(int instructorId) {
        courseDao.deleteInstructorCoursesByInstructorId(instructorId);
    }
    public void deleteStudentCoursesByInstructorId(int instructorId) {
        courseDao.deleteStudentCoursesByInstructorId(instructorId);
    }
}
