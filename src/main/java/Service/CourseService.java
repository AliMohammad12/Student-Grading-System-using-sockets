package Service;

import DAO.CourseDao;
import Model.Course;

import java.util.List;

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

    public List<Course> getAllCourses() {
        return courseDao.getAllCourses();
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
