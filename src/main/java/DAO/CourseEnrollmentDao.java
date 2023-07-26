package DAO;
import Model.CourseEnrollment;
import java.util.List;

public interface CourseEnrollmentDao {
    void createCourseEnrollment(CourseEnrollment enrollment);
    CourseEnrollment getCourseEnrollmentById(int enrollmentId);
    List<CourseEnrollment> getEnrollmentsByStudent(int studentId);
    List<CourseEnrollment> getEnrollmentsByCourse(int courseId);
    void updateCourseEnrollment(CourseEnrollment enrollment);
    void deleteCourseEnrollment(int enrollmentId);
}
