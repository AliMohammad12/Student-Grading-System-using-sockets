package server;

import model.*;
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
                displayStudentInformation(student);
            } else if (choice == 2) {
                displayStudentCourses(student);
            } else if (choice == 3) {
                enrollInCourse(student);
            } else if (choice == 4) {
                withdrawFromCourse(student);
            } else if (choice == 5) {
                viewCourseGrade(student);
            }
        } while (choice != 6);
    }
    private void displayStudentInformation(Student student) throws IOException {
        outputToClient.writeUTF("[Server] " + student.toString());
    }
    private void displayStudentCourses(Student student) throws IOException {
        outputToClient.writeUTF("[Server] You are enrolled in the following courses:");
        List<Course> courseList = courseService.getAllStudentCourses(student);
        int numberOfCourses = courseList.size();
        outputToClient.writeInt(numberOfCourses);
        for (int i = 0; i < numberOfCourses; i++) {
            outputToClient.writeUTF((i + 1) + ": " + courseList.get(i).toString());
        }
    }
    // todo: change the query to be: we need to make the student enroll in a course that he hasn't enrolled BEFORE.
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

    private void withdrawFromCourse(Student student) throws IOException {
        outputToClient.writeUTF("[Server] Please select the course you want to withdraw from: ");
        List<CourseInfo> courseInfoList = courseService.getStudentCoursesInfo(student);
        int numberOfCourses = courseInfoList.size();
        outputToClient.writeInt(numberOfCourses);
        for (int i = 0; i < numberOfCourses; i++) {
            Course course = courseInfoList.get(i).getCourse();
            outputToClient.writeUTF((i + 1) + ": " + course.toString());
        }
        int selectedCourseId = inputFromClient.readInt() - 1;
        CourseInfo courseInfo = courseInfoList.get(selectedCourseId);
        Course withdrawnCourse = courseInfo.getCourse();
        courseService.deleteStudentCourse(student.getStudentId(), withdrawnCourse.getCourseId());

        outputToClient.writeUTF("You have successfully withdrawn from " + withdrawnCourse.getCourseName());
    }

    private void viewCourseGrade(Student student) throws IOException {
        outputToClient.writeUTF("[Server] Please select the course you want to view it's grade: ");
        List<CourseInfo> courseInfoList = courseService.getStudentCoursesInfo(student);
        int numberOfCourses = courseInfoList.size();
        outputToClient.writeInt(numberOfCourses);
        for (int i = 0; i < numberOfCourses; i++) {
            Course course = courseInfoList.get(i).getCourse();
            outputToClient.writeUTF((i + 1) + ": " + course.toString());
        }
        int selectedCourseId = inputFromClient.readInt();
        CourseInfo courseInfo = courseInfoList.get(selectedCourseId);
        outputToClient.writeUTF("[Server] Your grade is for " + courseInfo.getCourse().getCourseName() + " is " + courseInfo.getGrade());
    }

    private void studentMenu() throws IOException {
        outputToClient.writeUTF("\n[Server] Please choose what you want to do!");
        outputToClient.writeUTF("1- See your student information.");
        outputToClient.writeUTF("2- See your courses.");
        outputToClient.writeUTF("3- Enroll in a course.");
        outputToClient.writeUTF("4- Withdraw from a course.");
        outputToClient.writeUTF("5- See courses marks.");
        outputToClient.writeUTF("6- logout");
    }

    public void setInputFromClient(DataInputStream inputFromClient) {
        this.inputFromClient = inputFromClient;
    }

    public void setOutputToClient(DataOutputStream outputToClient) {
        this.outputToClient = outputToClient;
    }
}
