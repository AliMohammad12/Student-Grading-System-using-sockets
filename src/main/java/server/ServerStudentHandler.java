package server;

import model.Account;
import model.Course;
import model.Instructor;
import model.Student;
import service.CourseService;
import service.StudentService;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ServerStudentHandler {
    private DataInputStream inputFromClient = null;
    private DataOutputStream outputToClient = null;
    private StudentService studentService;
    private CourseService courseService;

    public ServerStudentHandler(StudentService studentService, CourseService courseService) {
        this.studentService = studentService;
        this.courseService = courseService;
    }

    public void handleStudent(Account account) throws IOException {
        Student student = studentService.getStudentByAccountId(account.getId());

        outputToClient.writeUTF("[Server] Welcome Student " + student.getFirstName() + " " + student.getLastName());
        studentHandler(student);
    }
    private void studentHandler(Student student) throws IOException {

        int choice = 0;
        do {
            studentMenu();
            choice = inputFromClient.readInt();
            if (choice == 1) {
                outputToClient.writeUTF("[Server] " + student.toString());
            } else if (choice == 2) {
                enrollInCourse(student);
            } else if (choice == 3) {

            } else if (choice == 4) {

            }
        } while (choice != 5);
    }

    private void enrollInCourse(Student student) throws IOException {
        outputToClient.writeUTF("[Server] Enrolling in a Course !");
        Map<Course, List<Instructor>> coursesWithInstructorsList = courseService.getCourseDetailsWithInstructors();
        List<Course> coursesList = new ArrayList<>(coursesWithInstructorsList.keySet());

        showAllCourses(coursesList);

        int selectedCourseId = selectedCourseId();
        Course selectedCourse = coursesList.get(selectedCourseId);
        List<Instructor> instructorsOfTheChosenCourse = coursesWithInstructorsList.get(selectedCourse);

        showAllInstructorsOfCourse(instructorsOfTheChosenCourse);

        int selectedInstructorId = selectedInstructorId();
        Instructor selectedInstructor = instructorsOfTheChosenCourse.get(selectedInstructorId);
        courseService.enrollStudentInCourse(selectedCourse, selectedInstructor, student.getStudentId());

        outputToClient.writeUTF("[Server] Congratulations " + student.getFirstName() + " " + student.getLastName());
        outputToClient.writeUTF("You have enrolled in course " + selectedCourse.getCourseName() + " successfully!!\n");
    }
    private void showAllCourses(List<Course>  coursesList) throws IOException {
        outputToClient.writeUTF("[Server] Please select the course Id from the list below: ");
        int numberOfCourses = coursesList.size();
        outputToClient.writeInt(numberOfCourses);
        for (int i = 0; i < numberOfCourses; i++) {;
            Course course = coursesList.get(i);
            System.out.println(coursesList.get(i).getCourseName() + " " +  coursesList.get(i).getDepartmentName());
            outputToClient.writeUTF((i + 1) + "- Course Name: " + course.getCourseName() + " |  Course Department: " + course.getDepartmentName());
        }
    }
    private int selectedCourseId() throws IOException {
        return inputFromClient.readInt() - 1;
    }

    private void showAllInstructorsOfCourse(List<Instructor> instructors) throws IOException {
        outputToClient.writeUTF("[Server] Please choose your instructor: ");
        int numberOfInstructors = instructors.size();
        outputToClient.writeInt(numberOfInstructors);
        for (int i = 0; i < numberOfInstructors; i++) {
            outputToClient.writeUTF((i + 1) + " : " + instructors.get(i).toString());
        }
    }

    private int selectedInstructorId() throws IOException {
        return inputFromClient.readInt() - 1;
    }

    private void studentMenu() throws IOException {
        outputToClient.writeUTF("[Server] Please choose what you want to do!");
        outputToClient.writeUTF("1- See your student information");
        outputToClient.writeUTF("2- Enroll in a course.");
        outputToClient.writeUTF("3- Withdraw from a course.");
        outputToClient.writeUTF("4- See courses marks.");
        outputToClient.writeUTF("5- logout");
    }

    public void setInputFromClient(DataInputStream inputFromClient) {
        this.inputFromClient = inputFromClient;
    }

    public void setOutputToClient(DataOutputStream outputToClient) {
        this.outputToClient = outputToClient;
    }
}
