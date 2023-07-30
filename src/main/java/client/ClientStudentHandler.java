package client;

import util.InputValidator;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Scanner;

public class ClientStudentHandler {
    private DataOutputStream toServer = null;
    private DataInputStream fromServer = null;
    private InputValidator inputValidator;
    int menuSize;
    public ClientStudentHandler(DataOutputStream toServer, DataInputStream fromServer) {
        this.toServer = toServer;
        this.fromServer = fromServer;
        inputValidator = new InputValidator();
        menuSize = 6;
    }
    public void handleStudent() throws IOException {
        System.out.println(fromServer.readUTF());
        responseStudentHandler();
    }
    private void responseStudentHandler() throws IOException {
        int choice = 0;
        do {
            responseStudentMenu();
            choice = inputValidator.getValidInteger(1, menuSize);
            toServer.writeInt(choice);
            if (choice == 1) {
                handleDisplayingStudentInformation();
            } else if (choice == 2) {
                handleDisplayingStudentCourses();
            } else if (choice == 3) {
                handleEnrollingInCourse();
            } else if (choice == 4) {
                handleWithdrawingFromCourse();
            } else if (choice == 5) {
                viewCourseGrade();
            }
        } while (choice != 6);
    }
    private void handleDisplayingStudentInformation() throws IOException {
        System.out.println(fromServer.readUTF());
    }
    private void handleDisplayingStudentCourses() throws IOException {
        System.out.println(fromServer.readUTF());
        int numberOfCourses = fromServer.readInt();
        for (int i = 0; i < numberOfCourses; i++) {
            System.out.println(fromServer.readUTF());
        }
    }
    private void handleEnrollingInCourse() throws IOException {
        System.out.println(fromServer.readUTF());
        handleShowingAllCoursesWithTeachers();
    }
    private void handleShowingAllCoursesWithTeachers() throws IOException {
        System.out.println(fromServer.readUTF());
        int numberOfCourses = fromServer.readInt();
        for (int i = 0; i < numberOfCourses; i++) {
            System.out.println(fromServer.readUTF());
        }

        int selectedCourseId = inputValidator.getValidInteger(1, numberOfCourses) - 1;
        toServer.writeInt(selectedCourseId);
        System.out.println(fromServer.readUTF());
        int numberOfInstructors = fromServer.readInt();
        for (int i = 0; i < numberOfInstructors; i++) {
            System.out.println(fromServer.readUTF());
        }
        int selectedInstructorId = inputValidator.getValidInteger(1, numberOfInstructors) - 1;
        toServer.writeInt(selectedInstructorId);

        System.out.println(fromServer.readUTF());
        System.out.println(fromServer.readUTF());
    }

    private void handleWithdrawingFromCourse() throws IOException {
        System.out.println(fromServer.readUTF());
        int numberOfCourses = fromServer.readInt();
        for (int i = 0; i < numberOfCourses; i++) {
            System.out.println(fromServer.readUTF());
        }
        int selectedCourseId = inputValidator.getValidInteger(1, numberOfCourses) - 1;
        toServer.writeInt(selectedCourseId);
        System.out.println(fromServer.readUTF());
    }

    private void viewCourseGrade() throws IOException {
        System.out.println(fromServer.readUTF());
        int numberOfCourses = fromServer.readInt();
        for (int i = 0; i < numberOfCourses; i++) {
            System.out.println(fromServer.readUTF());
        }
        int selectedCourseId = inputValidator.getValidInteger(1, numberOfCourses) - 1;
        toServer.writeInt(selectedCourseId);
        System.out.println(fromServer.readUTF());
    }

    private void responseStudentMenu() throws IOException {
        System.out.println(fromServer.readUTF());
        System.out.println(fromServer.readUTF());
        System.out.println(fromServer.readUTF());
        System.out.println(fromServer.readUTF());
        System.out.println(fromServer.readUTF());
        System.out.println(fromServer.readUTF());
        System.out.println(fromServer.readUTF());
    }
}
