package service;

import dao.CourseDao;
import model.Course;
import model.Instructor;

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
    public List<Course> getAllCourses() {
        return courseDao.getAllCourses();
    }

    public void enrollStudentInCourse(Course course, Instructor instructor, int studentId) {
        courseDao.enrollStudentInCourse(course, instructor, studentId);
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
}
