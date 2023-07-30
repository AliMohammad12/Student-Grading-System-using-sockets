package client;

import util.InputValidator;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class ClientRegistrationHandler {
    private DataOutputStream toServer = null;
    private DataInputStream fromServer = null;
    private InputValidator inputValidator;


    public ClientRegistrationHandler(DataOutputStream toServer, DataInputStream fromServer) {
        this.toServer = toServer;
        this.fromServer = fromServer;
        inputValidator = new InputValidator();
    }
    public void register() throws IOException {
        responseRegistration();
    }
    private void responseRegistration() throws IOException {
        boolean success = false;
        do {
            System.out.println(fromServer.readUTF());
            System.out.println(fromServer.readUTF());
            String email = inputValidator.getValidString(new ArrayList<>() {{}});
            toServer.writeUTF(email);
            System.out.println(fromServer.readUTF());
            String password = inputValidator.getValidString(new ArrayList<>() {{}});
            toServer.writeUTF(password);
            System.out.println(fromServer.readUTF());
            System.out.println(fromServer.readUTF());
            System.out.println(fromServer.readUTF());
            int role = inputValidator.getValidInteger(1, 2);
            toServer.writeInt(role);
            //..............
            boolean isEmailValid = fromServer.readBoolean();
            if (!isEmailValid) {
                System.out.println(fromServer.readUTF());
                System.out.println(fromServer.readUTF());
                System.out.println(fromServer.readUTF());
                int choice = inputValidator.getValidInteger(1, 2);
                toServer.writeInt(choice);
                if (choice == 2) return;
                continue;
            }
            boolean isEmailUnique = fromServer.readBoolean();
            if (isEmailUnique) {
                System.out.println(fromServer.readUTF());
                success = true;
            } else {
                System.out.println(fromServer.readUTF());
                System.out.println(fromServer.readUTF());
                System.out.println(fromServer.readUTF());
                int choice = inputValidator.getValidInteger(1, 2);
                toServer.writeInt(choice);
                if (choice == 2) return;
            }

        } while (success == false);

        int role = fromServer.readInt();
        if (role == 1) {
            responseStudentRegistration();
        } else {
            responseInstructorRegistration();
        }
    }

    private void responseStudentRegistration() throws IOException {
        System.out.println(fromServer.readUTF());
        System.out.println(fromServer.readUTF()); // first name
        String firstName = inputValidator.getValidString(new ArrayList<>() {{}});
        toServer.writeUTF(firstName);

        System.out.println(fromServer.readUTF()); // last name
        String lastName = inputValidator.getValidString(new ArrayList<>() {{}});
        toServer.writeUTF(lastName);

        System.out.println(fromServer.readUTF()); // Major
        String major = inputValidator.getValidString(new ArrayList<>() {{}});
        toServer.writeUTF(major);

        System.out.println(fromServer.readUTF()); // Academic Year
        int academicYear = inputValidator.getValidInteger(1, 5);
        toServer.writeInt(academicYear);

        System.out.println(fromServer.readUTF());
    }

    private void responseInstructorRegistration() throws IOException {
        System.out.println(fromServer.readUTF());
        System.out.println(fromServer.readUTF()); // first name
        String firstName = inputValidator.getValidString(new ArrayList<>() {{}});
        toServer.writeUTF(firstName);

        System.out.println(fromServer.readUTF()); // last name
        String lastName = inputValidator.getValidString(new ArrayList<>() {{}});
        toServer.writeUTF(lastName);

        System.out.println(fromServer.readUTF()); // please select index
        int numberOfDepartments = fromServer.readInt();
        for (int i = 0; i < numberOfDepartments; i++) {
            System.out.println(fromServer.readUTF());
        }
        int chosenIndex = inputValidator.getValidInteger(1, numberOfDepartments) - 1;
        toServer.writeInt(chosenIndex);
        System.out.println(fromServer.readUTF());
    }
}
