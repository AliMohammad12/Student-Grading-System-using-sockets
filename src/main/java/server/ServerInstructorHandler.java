package server;

import model.*;
import service.CourseService;
import service.InstructorService;
import service.StudentService;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ServerInstructorHandler {
    private InstructorService instructorService;
    private CourseService courseService;
    private StudentService studentService;
    private DataInputStream inputFromClient = null;
    private DataOutputStream outputToClient = null;
    public ServerInstructorHandler(InstructorService instructorService, CourseService courseService, StudentService studentService) {
        this.instructorService = instructorService;
        this.courseService = courseService;
        this.studentService = studentService;
    }
    public void handleInstructor(Account account) throws IOException {
        Instructor instructor = instructorService.getInstructorByAccountId(account.getId());
        outputToClient.writeUTF("[Server] Welcome Instructor " + instructor.getFirstName() + " " + instructor.getLastName());
        instructorHandler(instructor);
    }
    private void instructorHandler(Instructor instructor) throws IOException {
        int choice = 0;
        do {
            instructorMenu();
            choice = inputFromClient.readInt();
            if (choice == 1) {
                displayInstructorInformation(instructor);
            } else if (choice == 2) {
                List<Course> instructorCourses = getInstructorCourses(instructor);
                displayInstructorCourses(instructorCourses);
            } else if (choice == 3) {
                teachNewCourse(instructor);
            } else if (choice == 4) {
                removeCourseFromInstructor(instructor);
            } else if (choice == 5) {
                updateStudentsGrades(instructor);
            }
        } while (choice != 6);
    }
    private void displayInstructorInformation(Instructor instructor) throws IOException { // option 1
        outputToClient.writeUTF("[Server] " + instructor.toString());
    }
    private List<Course> getInstructorCourses(Instructor instructor) { // option 2
        return courseService.getAllInstructorCourses(instructor);
    }
    private void displayInstructorCourses(List<Course> instructorCourses) throws IOException { // option 2
        int numberOfCourses = instructorCourses.size();
        outputToClient.writeUTF("\n[Server] You are currently teaching these courses: ");
        outputToClient.writeInt(numberOfCourses);
        for (int i = 0; i < numberOfCourses; i++) {
            Course course = instructorCourses.get(i);
            outputToClient.writeUTF((i + 1) + ": " + course.toString());
        }
    }
    private void teachNewCourse(Instructor instructor) throws IOException { // option 3
        List<Course> availableCourses = getAvailableCoursesToTeach(instructor);
        displayAvailableCoursesToTeach(availableCourses);
        int selectedCourseId = selectCourseIdToTeach();
        Course selectedCourse = availableCourses.get(selectedCourseId);
        courseService.assignCourseToInstructor(selectedCourse, instructor);
        outputToClient.writeUTF("\n[Server] You course " + selectedCourse.getCourseName() + " has been successfully assigned to you!");
    }
    private List<Course> getAvailableCoursesToTeach(Instructor instructor) { // option 3
        return courseService.getUnassignedCoursesFromSameDept(instructor);
    }
    private void displayAvailableCoursesToTeach(List<Course> availableCourses) throws IOException { // option 3
        outputToClient.writeUTF("\n[Server] Available Courses: ");
        int numberOfAvailableCourses = availableCourses.size();
        outputToClient.writeInt(numberOfAvailableCourses);
        for (int i = 0; i < numberOfAvailableCourses; i++) {
            Course course = availableCourses.get(i);
            outputToClient.writeUTF((i + 1) + ": " + course.toString());
        }
    }
    private int selectCourseIdToTeach() throws IOException { // option 3
        outputToClient.writeUTF("\n[Server] Please enter the id of the course you want to teach: ");
        return inputFromClient.readInt() - 1;
    }
    private void removeCourseFromInstructor(Instructor instructor) throws IOException { // option 4
        List<Course> instructorCourses = getInstructorCourses(instructor);
        displayInstructorCourses(instructorCourses);
        int courseIdToRemove = selectCourseIdToRemove();
        Course selectedCourse = instructorCourses.get(courseIdToRemove);
        courseService.removeCourseFromInstructor(selectedCourse, instructor);
        outputToClient.writeUTF("[Server] The selected course has been successfully removed!");
    }
    private int selectCourseIdToRemove() throws IOException { // option 4
        outputToClient.writeUTF("[Server] Please enter the id of the course you want to withdraw from: ");
        return inputFromClient.readInt() - 1;
    }
    private void updateStudentsGrades(Instructor instructor) throws IOException { // option 5
        List<Course> instructorCourses = getInstructorCourses(instructor);
        displayInstructorCourses(instructorCourses);
        int selectedCourseId = selectIdOfTheCourse();
        Course selectedCourse = instructorCourses.get(selectedCourseId);
        List<Student> studentList = getStudentsOfTheSelectedCourse(selectedCourse, instructor);
        displayStudentsOfTheSelectedCourse(studentList);
        int selectedStudentId = selectIdOfStudent();
        Student selectedStudent = studentList.get(selectedStudentId);
        displayStudentSelection(selectedStudent);

        CourseEnrollment courseEnrollment = getCourseEnrollment(selectedStudent, selectedCourse, instructor);
        displayGrade(courseEnrollment.getGrade());
        updateGrade(courseEnrollment);
    }
    private int selectIdOfTheCourse() throws IOException {
        outputToClient.writeUTF("[Server] Please enter the id of the course: ");
        return inputFromClient.readInt() - 1;
    }
    private List<Student> getStudentsOfTheSelectedCourse(Course course, Instructor instructor) throws IOException {
        return studentService.findStudentsByCourseAndInstructor(course, instructor);
    }
    private void displayStudentsOfTheSelectedCourse(List<Student> studentList) throws IOException {
        outputToClient.writeUTF("[Server] Students enrolled in the selected course:");
        int numberOfStudents = studentList.size();
        outputToClient.writeInt(numberOfStudents);
        for (int i = 0; i < numberOfStudents; i++) {
            Student student = studentList.get(i);
            outputToClient.writeUTF((i + 1) + ": " + student.getFirstName() + " " +
                    student.getLastName() + " | Email: " + student.getEmail());
        }
    }
    private int selectIdOfStudent() throws IOException {
        outputToClient.writeUTF("[Server] Please enter the id of the student you want to update their grade: ");
        return inputFromClient.readInt() - 1;
    }
    private void displayStudentSelection(Student student) throws IOException {
        outputToClient.writeUTF("[Server] You selected student " + student.getFirstName() + " " + student.getLastName());
    }
    private CourseEnrollment getCourseEnrollment(Student selectedStudent, Course selectedCourse, Instructor instructor) {
        return courseService.getCourseEnrollment(selectedStudent, selectedCourse, instructor);
    }
    private void displayGrade(String grade) throws IOException {
        outputToClient.writeUTF("[Server] Current grade: " + grade);
    }
    private void updateGrade(CourseEnrollment courseEnrollment) throws IOException {
        outputToClient.writeUTF("[Server] Please enter the new grade for the student: ");
        String newGrade = inputFromClient.readUTF();
        courseService.updateCourseEnrollmentGradeById(courseEnrollment.getCourseEnrollmentId(), newGrade);
        outputToClient.writeUTF("[Server] The grade has been updated successfully!");
    }
    private void instructorMenu() throws IOException {
        outputToClient.writeUTF("\n[Server] Please choose what you want to do: ");
        outputToClient.writeUTF("1- See your instructor information.");
        outputToClient.writeUTF("2- See the courses you are teaching. ");
        outputToClient.writeUTF("3- Teach a new course?");
        outputToClient.writeUTF("4- Withdraw teaching a course?");
        outputToClient.writeUTF("5- Update students marks.");
        outputToClient.writeUTF("6- logout");
    }
    public void setInputFromClient(DataInputStream inputFromClient) {
        this.inputFromClient = inputFromClient;
    }

    public void setOutputToClient(DataOutputStream outputToClient) {
        this.outputToClient = outputToClient;
    }
}
