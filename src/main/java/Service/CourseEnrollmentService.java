package Service;

import DAO.CourseEnrollmentDao;
import Model.Course;
import Model.CourseEnrollment;

import java.util.List;

public class CourseEnrollmentService {
    private CourseEnrollmentDao courseEnrollmentDao;

    public CourseEnrollmentService(CourseEnrollmentDao courseEnrollmentDao) {
        this.courseEnrollmentDao = courseEnrollmentDao;
    }

    public void createCourseEnrollment(CourseEnrollment enrollment) {
        courseEnrollmentDao.createCourseEnrollment(enrollment);
    }

    public CourseEnrollment getCourseEnrollmentById(int enrollmentId) {
        return courseEnrollmentDao.getCourseEnrollmentById(enrollmentId);
    }

    public List<CourseEnrollment> getEnrollmentsByStudent(int studentId) {
        return courseEnrollmentDao.getEnrollmentsByStudent(studentId);
    }

    public List<CourseEnrollment> getEnrollmentsByCourse(int courseId) {
        return courseEnrollmentDao.getEnrollmentsByCourse(courseId);
    }

    public void updateCourseEnrollment(CourseEnrollment enrollment) {
        courseEnrollmentDao.updateCourseEnrollment(enrollment);
    }

    public void deleteCourseEnrollment(int enrollmentId) {
        courseEnrollmentDao.deleteCourseEnrollment(enrollmentId);
    }
}
