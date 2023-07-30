package client;

import model.Course;
import util.InputValidator;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class ClientInstructorHandler {
    private DataOutputStream toServer = null;
    private DataInputStream fromServer = null;
    private InputValidator inputValidator;
    private int menuSize;

    public ClientInstructorHandler(DataOutputStream toServer, DataInputStream fromServer) {
        this.toServer = toServer;
        this.fromServer = fromServer;
        inputValidator = new InputValidator();
        menuSize = 6;
    }
    public void handleInstructor() throws IOException {
        System.out.println(fromServer.readUTF());
        responseInstructorHandler();
    }
    private void responseInstructorHandler() throws IOException {
        int choice = 0;
        do {
            responseInstructorMenu();
            choice = inputValidator.getValidInteger(1, menuSize);
            toServer.writeInt(choice);
            if (choice == 1) {
                handleDisplayingInstructorInformation();
            } else if (choice == 2) {
                handleDisplayingInstructorCourses();
            } else if (choice == 3) {
                handleTeachNewCourse();
            } else if (choice == 4) {
                handleRemovingCourseFromInstructor();
            } else if (choice == 5) {
                handleUpdatingStudentsGrades();
            }
        } while (choice != 6);
    }
    private void handleDisplayingInstructorInformation() throws IOException { // 1
        System.out.println(fromServer.readUTF());
    }
    private  handleDisplayingInstructorCourses() throws IOException { // 2
        System.out.println(fromServer.readUTF());
        int numberOfCourses = fromServer.readInt();
        for (int i = 0; i < numberOfCourses; i++) {
            System.out.println(fromServer.readUTF());
        }
        return numberOfCourses
    }
    private void handleTeachNewCourse() throws IOException { // 3
        int numberOfAvailableCourses = handleDisplayAvailableCoursesToTeach();
        handleSelectingId(numberOfAvailableCourses);
        System.out.println(fromServer.readUTF());
    }
    private int handleDisplayAvailableCoursesToTeach() throws IOException { // 3
        System.out.println(fromServer.readUTF());
        int numberOfAvailableCourses = fromServer.readInt();
        for (int i = 0; i < numberOfAvailableCourses; i++) {
            System.out.println(fromServer.readUTF());
        }
        return numberOfAvailableCourses;
    }
    private void handleSelectingId(int limit) throws IOException { // 3
        System.out.println(fromServer.readUTF());
        int selectedId = inputValidator.getValidInteger(1, limit) - 1;
        toServer.writeInt(selectedId);
    }
    private void handleRemovingCourseFromInstructor() throws IOException { // 4
        handleDisplayingInstructorCourses();
        handleSelectingId();
        System.out.println(fromServer.readUTF());
    }
    private void handleUpdatingStudentsGrades() throws IOException { // 5
        handleDisplayingInstructorCourses();
        handleSelectingId();
        handleDisplayingStudentsOfTheSelectedCourse();
        handleSelectingId();
        handleDisplayingStudentSelection();
        handleDisplayingGrade();
        handleUpdateGrade();
    }
    private void handleDisplayingStudentsOfTheSelectedCourse() throws IOException { // 5
        System.out.println(fromServer.readUTF());
        int numberOfStudents = fromServer.readInt();
        for (int i = 0; i < numberOfStudents; i++) {
            System.out.println(fromServer.readUTF());
        }
    }
    private void handleDisplayingStudentSelection() throws IOException { // 5
        System.out.println(fromServer.readUTF());
    }
    private void handleDisplayingGrade() throws IOException {
        System.out.println(fromServer.readUTF());
    }
    private void handleUpdateGrade() throws IOException {
        System.out.println(fromServer.readUTF());
        String newGrade = scan.next();
        toServer.writeUTF(newGrade);
        System.out.println(fromServer.readUTF());
    }
    private void responseInstructorMenu() throws IOException {
        System.out.println(fromServer.readUTF());
        System.out.println(fromServer.readUTF());
        System.out.println(fromServer.readUTF());
        System.out.println(fromServer.readUTF());
        System.out.println(fromServer.readUTF());
        System.out.println(fromServer.readUTF());
        System.out.println(fromServer.readUTF());
    }
}
