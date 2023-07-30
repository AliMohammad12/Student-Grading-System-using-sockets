package client;

import util.InputValidator;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Scanner;

public class ClientAdminHandler {
    private DataOutputStream toServer = null;
    private DataInputStream fromServer = null;
    private InputValidator inputValidator;
    private int menuSize;
    public ClientAdminHandler(DataOutputStream toServer, DataInputStream fromServer) {
        this.toServer = toServer;
        this.fromServer = fromServer;
        inputValidator = new InputValidator();
        menuSize = 11;
    }

    public void handleAdmin() throws IOException {
        System.out.println(fromServer.readUTF());
        responseAdminHandler();
    }

    private void responseAdminHandler() throws IOException {
        int choice = 0;
        do {
            responseInstructorMenu();
            choice = inputValidator.getValidInteger(1, 11);
            toServer.writeInt(choice);
            if (choice == 1) {
                int numberOfStudents = fromServer.readInt();
                handleDisplayingList(numberOfStudents);
            } else if (choice == 2) {
                int numberOfInstructors = fromServer.readInt();
                handleDisplayingList(numberOfInstructors);
            } else if (choice == 3) {
                int numberOfDepartments = fromServer.readInt();
                handleDisplayingList(numberOfDepartments);
            } else if (choice == 4) {
                int numberOfCourses = fromServer.readInt();
                handleDisplayingList(numberOfCourses);
            } else if (choice == 5) {
                handleAddingNewDepartment();
            } else if (choice == 6) {
                handleAddingNewCourse();
            } else if (choice == 7) {
                handleDeletingDepartment();
            } else if (choice == 8) {
                handleDeletingCourse();
            } else if (choice == 9) {
                handleDeletingStudent();
            } else if (choice == 10) {
                handleDeletingInstructor();
            }
        } while (choice != 11);
    }
    private void handleDisplayingList(int listSize) throws IOException {
        System.out.println(fromServer.readUTF());
        for (int i = 0; i < listSize; i++) {
            System.out.println(fromServer.readUTF());
        }
    }
    private void handleAddingNewDepartment() throws IOException {
        System.out.println(fromServer.readUTF());
        System.out.println(fromServer.readUTF());
        String newDepartmentName = inputValidator.getValidString();
        toServer.writeUTF(newDepartmentName);
        System.out.println(fromServer.readUTF());
    }
    private void handleAddingNewCourse() throws IOException {
        System.out.println(fromServer.readUTF());
        System.out.println(fromServer.readUTF());
        int numberOfDepartments = fromServer.readInt();
        for (int i = 0; i < numberOfDepartments; i++) {
            System.out.println(fromServer.readUTF());
        }
        int selectedDepartmentId = inputValidator.getValidInteger(1, numberOfDepartments) - 1;
        toServer.writeInt(selectedDepartmentId);
        System.out.println(fromServer.readUTF());
        String newCourseName = inputValidator.getValidString();
        toServer.writeUTF(newCourseName);
        System.out.println(fromServer.readUTF());
    }

    private void handleDeletingDepartment() throws IOException { // same as below
        System.out.println(fromServer.readUTF());
        System.out.println(fromServer.readUTF());
        int numberOfDepartments = fromServer.readInt();
        for (int i = 0; i < numberOfDepartments; i++) {
            System.out.println(fromServer.readUTF());
        }
        System.out.println(fromServer.readUTF());
        int selectedId = inputValidator.getValidInteger(1, numberOfDepartments) - 1;
        toServer.writeInt(selectedId);
        System.out.println(fromServer.readUTF());
    }

    private void handleDeletingCourse() throws IOException { // same
        System.out.println(fromServer.readUTF());
        System.out.println(fromServer.readUTF());
        int numberOfCourses = fromServer.readInt();
        for (int i = 0; i < numberOfCourses; i++) {
            System.out.println(fromServer.readUTF());
        }
        System.out.println(fromServer.readUTF());
        int selectedId = inputValidator.getValidInteger(1, numberOfCourses) - 1;
        toServer.writeInt(selectedId);
        System.out.println(fromServer.readUTF());
    }
    private void handleDeletingStudent() throws IOException {
        System.out.println(fromServer.readUTF());
        System.out.println(fromServer.readUTF());
        int numberOfStudents = fromServer.readInt();
        for (int i = 0; i < numberOfStudents; i++) {
            System.out.println(fromServer.readUTF());
        }
        System.out.println(fromServer.readUTF());
        int selectedId = inputValidator.getValidInteger(1, numberOfStudents) - 1;
        toServer.writeInt(selectedId);
        System.out.println(fromServer.readUTF());
    }
    private void handleDeletingInstructor() throws IOException {
        System.out.println(fromServer.readUTF());
        System.out.println(fromServer.readUTF());
        int numberOfInstructors = fromServer.readInt();
        for (int i = 0; i < numberOfInstructors; i++) {
            System.out.println(fromServer.readUTF());
        }
        System.out.println(fromServer.readUTF());
        int selectedId = inputValidator.getValidInteger(1, numberOfInstructors) - 1;
        toServer.writeInt(selectedId);
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
        System.out.println(fromServer.readUTF());
        System.out.println(fromServer.readUTF());

        System.out.println(fromServer.readUTF());
        System.out.println(fromServer.readUTF());
        System.out.println(fromServer.readUTF());
    }
}
