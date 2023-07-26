package DAO;
import Model.Course;
import java.util.List;

public interface CourseDao {
    void createCourse(Course course);
    Course getCourseById(int courseId);
    List<Course> getAllCourses();
    List<Course> getCoursesByDepartment(int departmentId);
    void updateCourse(Course course);
    void deleteCourse(int courseId);
}
