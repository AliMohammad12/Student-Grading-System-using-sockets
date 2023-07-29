package daoimpl;

import dao.CourseDao;
import model.*;

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

    public List<CourseInfo> getStudentCoursesInfo(Student student) {
        List<CourseInfo> studentCoursesInfo = new ArrayList<>();
        String query = "SELECT " +
                "sc.id AS Student_Course_ID, " +
                "sc.course_id AS Course_ID, " +
                "c.course_name AS Course_Name, " +
                "c.department_name AS Department_Name, " +
                "sc.grade AS Grade " +
                "FROM student_courses sc " +
                "JOIN course c ON sc.course_id = c.id " +
                "WHERE sc.student_id = ?";
        try (Connection connection = getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setInt(1, student.getStudentId());
                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        int studentCourseId = resultSet.getInt("Student_Course_ID");
                        int courseId = resultSet.getInt("Course_ID");
                        String courseName = resultSet.getString("Course_Name");
                        String departmentName = resultSet.getString("Department_Name");
                        String grade = resultSet.getString("Grade");
                        Course course = new Course(courseName, departmentName);
                        course.setCourseId(courseId);
                        CourseInfo courseGrade = new CourseInfo(studentCourseId, grade, course);
                        studentCoursesInfo.add(courseGrade);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return studentCoursesInfo;
    }
    public void deleteStudentCourse(int studentId, int courseId) {
        String query = "DELETE FROM student_courses WHERE student_id = ? AND course_id = ?";
        try (Connection connection = getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setInt(1, studentId);
                statement.setInt(2, courseId);
                int rowsAffected = statement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public List<Course> getAllStudentCourses(Student student) {
        String query = "SELECT " +
                "c.id AS Course_ID, " +
                "c.course_name AS Course_Name, " +
                "c.department_name AS Department_Name " +
                "FROM student_courses sc " +
                "JOIN course c ON sc.course_id = c.id " +
                "WHERE sc.student_id = ?";
        List<Course> studentCourseList = new ArrayList<>();
        try (Connection connection = getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setInt(1, student.getStudentId());
                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        int courseId = resultSet.getInt("Course_ID");
                        String courseName = resultSet.getString("Course_Name");
                        String departmentName = resultSet.getString("Department_Name");
                        studentCourseList.add(new Course(courseName, departmentName));
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return studentCourseList;
    }

    public List<Course> getAllInstructorCourses(Instructor instructor) {
        String query = "SELECT " +
                "c.id AS Course_ID, " +
                "c.course_name AS Course_Name, " +
                "c.department_name AS Department_Name " +
                "FROM instructor_courses ic " +
                "JOIN course c ON ic.course_id = c.id " +
                "WHERE ic.instructor_id = ?";

        List<Course> instructorCourses = new ArrayList<>();
        try (Connection connection = getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setInt(1, instructor.getInstructorId());
                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        int courseId = resultSet.getInt("Course_ID");
                        String courseName = resultSet.getString("Course_Name");
                        String departmentName = resultSet.getString("Department_Name");
                        Course course = new Course(courseName, departmentName);
                        course.setCourseId(courseId);

                        instructorCourses.add(course);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return instructorCourses;
    }
    public List<Course> getUnassignedCoursesFromSameDept(Instructor instructor) {
        String query = "SELECT " +
                "c.id AS Course_ID, " +
                "c.course_name AS Course_Name, " +
                "c.department_name AS Department_Name " +
                "FROM course c " +
                "WHERE c.department_name = (SELECT department_name FROM instructor WHERE id = ?) " +
                "AND c.id NOT IN (SELECT course_id FROM instructor_courses WHERE instructor_id = ?)";
        List<Course> unassignedCourses = new ArrayList<>();
        try (Connection connection = getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                int instructorId = instructor.getInstructorId();
                statement.setInt(1, instructorId);
                statement.setInt(2, instructorId);

                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        int courseId = resultSet.getInt("Course_ID");
                        String courseName = resultSet.getString("Course_Name");
                        String departmentName = resultSet.getString("Department_Name");
                        Course course = new Course(courseName, departmentName);
                        course.setCourseId(courseId);
                        unassignedCourses.add(course);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return unassignedCourses;
    }

    public void assignCourseToInstructor(Course course, Instructor instructor) {
        String query = "INSERT INTO instructor_courses (instructor_id, course_id) VALUES (?, ?)";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, instructor.getInstructorId());
            statement.setInt(2, course.getCourseId());

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Sorry, we encountered an issue while creating your request. Please try again later.");
        }
    }

    public void removeCourseFromInstructor(Course course, Instructor instructor) {
        String query = "DELETE FROM instructor_courses WHERE instructor_id = ? AND course_id = ?";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, instructor.getInstructorId());
            statement.setInt(2, course.getCourseId());

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public CourseEnrollment getCourseEnrollment(Student student, Course course, Instructor instructor) {
        String query = "SELECT id, grade FROM student_courses WHERE student_id = ? AND course_id = ? AND instructor_id = ?";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, student.getStudentId());
            statement.setInt(2, course.getCourseId());
            statement.setInt(3, instructor.getInstructorId());

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    int courseEnrollmentId = resultSet.getInt("id");
                    String grade = resultSet.getString("grade");
                    CourseEnrollment courseEnrollment = new CourseEnrollment(student.getStudentId(),
                            instructor.getInstructorId(), course, grade, courseEnrollmentId);

                    return courseEnrollment;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public void updateCourseEnrollmentGradeById(int courseEnrollmentId, String newGrade) {
        String query = "UPDATE student_courses SET grade = ? WHERE id = ?";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, newGrade);
            statement.setInt(2, courseEnrollmentId);

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
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
