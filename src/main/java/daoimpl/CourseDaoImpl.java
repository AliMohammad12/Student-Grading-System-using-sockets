package daoimpl;

import dao.CourseDao;
import model.Account;
import model.Course;
import model.Instructor;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CourseDaoImpl implements CourseDao {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/student_grading_system_db";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "root";

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }

    public void createCourse(Course course) {
    }
    public Course getCourseById(int courseId) {
        return null;
    }

    public Map<Course, List<Instructor>> getCourseDetailsWithInstructors() {
        String query = "SELECT " +
                "c.id AS Course_ID, " +
                "c.course_name AS Course_Name, " +
                "i.first_name AS First_Name, " +
                "i.last_name AS Last_Name, " +
                "i.email AS Instructor_Email, " +
                "i.department_name AS Department_Name, " +
                "i.account_id AS Account_ID, " +
                "i.id AS Instructor_ID " +
                "FROM course c " +
                "JOIN instructor_courses ic ON c.id = ic.course_id " +
                "JOIN instructor i ON ic.instructor_id = i.id";


        Map<Course, List<Instructor>> coursesWithInstructors = new HashMap<>();
        try (Connection connection = getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                int courseId = resultSet.getInt("Course_ID");
                String courseName = resultSet.getString("Course_Name");
                String firstName = resultSet.getString("First_Name");
                String lastName = resultSet.getString("Last_Name");
                String instructorEmail = resultSet.getString("Instructor_Email");
                String departmentName = resultSet.getString("Department_Name");
                int accountId = resultSet.getInt("Account_ID");
                int instructorId = resultSet.getInt("Instructor_ID");

                Instructor courseInstructor = new Instructor(firstName, lastName, departmentName, instructorEmail);
                Course course = new Course(courseName, departmentName);
                course.setCourseId(courseId);
                courseInstructor.setInstructorId(instructorId);

                if (coursesWithInstructors.containsKey(course)) {
                    List<Instructor> existingList = coursesWithInstructors.get(course);
                    existingList.add(courseInstructor);
                } else {
                    List<Instructor> newList = new ArrayList<>();
                    newList.add(courseInstructor);
                    coursesWithInstructors.put(course, newList);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

//        for (Map.Entry<Course, List<Instructor>> entry : coursesWithInstructors.entrySet()) {
//            Course course = entry.getKey();
//            List<Instructor> instructors = entry.getValue();
//
//            System.out.println("Course: " + course.getCourseName() + " | Department: " + course.getDepartmentName());
//            System.out.println("Instructors:");
//
//            for (Instructor instructor : instructors) {
//                System.out.println(" - " + instructor.toString());
//            }
//
//            System.out.println();
//        }

        return coursesWithInstructors;
    }

    public void enrollStudentInCourse(Course course, Instructor instructor, int studentId) {
        int courseId = course.getCourseId();
        int instructorId = instructor.getInstructorId();
        String query = "INSERT INTO student_courses (student_id, course_id, instructor_id) VALUES (?, ?, ?)";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, studentId);
            statement.setInt(2, courseId);
            statement.setInt(3, instructorId);

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Sorry, we encountered an issue while creating your request. Please try again later.");
        }
    }

    public List<Course> getAllCourses() {
        return null;
    }

    public List<Course> getCoursesByDepartment(int departmentId) {
        return null;
    }

    public void updateCourse(Course course) {
    }
    public void deleteCourse(int courseId) {

    }
}
