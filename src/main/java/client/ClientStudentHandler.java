package client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Scanner;

public class ClientStudentHandler {
    private DataOutputStream toServer = null;
    private DataInputStream fromServer = null;

    public ClientStudentHandler(DataOutputStream toServer, DataInputStream fromServer) {
        this.toServer = toServer;
        this.fromServer = fromServer;
    }
    public void handleStudent() throws IOException {
        System.out.println(fromServer.readUTF());
        responseStudentHandler();
    }
    private void responseStudentHandler() throws IOException {
        Scanner scan = new Scanner(System.in);
        int choice = 0;
        do {
            responseStudentMenu();
            choice = scan.nextInt();
            toServer.writeInt(choice);

            if (choice == 1) {
                System.out.println(fromServer.readUTF());
            } else if (choice == 2) {
                handleEnrollingInCourse();
            }
        } while (choice != 5);
    }
    private void handleEnrollingInCourse() throws IOException {
        System.out.println(fromServer.readUTF());
        handleShowingAllCoursesWithTeachers();
    }
    private void handleShowingAllCoursesWithTeachers() throws IOException {
        Scanner scan = new Scanner(System.in);
        System.out.println(fromServer.readUTF());
        int numberOfCourses = fromServer.readInt();
        for (int i = 0; i < numberOfCourses; i++) {
            System.out.println(fromServer.readUTF());
        }

        int selectedCourseId = scan.nextInt();
        toServer.writeInt(selectedCourseId);
        System.out.println(fromServer.readUTF());
        int numberOfInstructors = fromServer.readInt();
        for (int i = 0; i < numberOfInstructors; i++) {
            System.out.println(fromServer.readUTF());
        }
        int selectedInstructorId = scan.nextInt();
        toServer.writeInt(selectedInstructorId);

        System.out.println(fromServer.readUTF());
        System.out.println(fromServer.readUTF());
    }

    private void responseStudentMenu() throws IOException {
        System.out.println(fromServer.readUTF());
        System.out.println(fromServer.readUTF());
        System.out.println(fromServer.readUTF());
        System.out.println(fromServer.readUTF());
        System.out.println(fromServer.readUTF());
        System.out.println(fromServer.readUTF());
    }
}
