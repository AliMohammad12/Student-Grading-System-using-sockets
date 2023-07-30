package client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Scanner;

public class ClientRegistrationHandler {
    private DataOutputStream toServer = null;
    private DataInputStream fromServer = null;

    public ClientRegistrationHandler(DataOutputStream toServer, DataInputStream fromServer) {
        this.toServer = toServer;
        this.fromServer = fromServer;
    }
    public void register() throws IOException {
        responseRegistration();
    }
    private void responseRegistration() throws IOException {
        boolean success = false;
        do {
            Scanner scan = new Scanner(System.in);
            System.out.println(fromServer.readUTF());
            System.out.println(fromServer.readUTF());
            String email = scan.next();
            toServer.writeUTF(email);
            System.out.println(fromServer.readUTF());
            String password = scan.next();
            toServer.writeUTF(password);
            System.out.println(fromServer.readUTF());
            System.out.println(fromServer.readUTF());
            System.out.println(fromServer.readUTF());
            int role = scan.nextInt();
            toServer.writeInt(role);
            //..............
            boolean isEmailValid = fromServer.readBoolean();
            if (!isEmailValid) {
                System.out.println(fromServer.readUTF());
                System.out.println(fromServer.readUTF());
                System.out.println(fromServer.readUTF());
                int choice = scan.nextInt();
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
                int choice = scan.nextInt();
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
        Scanner scan = new Scanner(System.in);
        System.out.println(fromServer.readUTF());
        System.out.println(fromServer.readUTF()); // first name
        String firstName = scan.next();
        toServer.writeUTF(firstName);

        System.out.println(fromServer.readUTF()); // last name
        String lastName = scan.next();
        toServer.writeUTF(lastName);

        System.out.println(fromServer.readUTF()); // Major
        String major = scan.next();
        toServer.writeUTF(major);

        System.out.println(fromServer.readUTF()); // Academic Year
        int academicYear = scan.nextInt();
        toServer.writeInt(academicYear);

        System.out.println(fromServer.readUTF());
    }

    private void responseInstructorRegistration() throws IOException {
        Scanner scan = new Scanner(System.in);
        System.out.println(fromServer.readUTF());
        System.out.println(fromServer.readUTF()); // first name
        String firstName = scan.next();
        toServer.writeUTF(firstName);

        System.out.println(fromServer.readUTF()); // last name
        String lastName = scan.next();
        toServer.writeUTF(lastName);

        System.out.println(fromServer.readUTF()); // please select index
        int numberOfDepartments = fromServer.readInt();
        for (int i = 0; i < numberOfDepartments; i++) {
            System.out.println(fromServer.readUTF());
        }
        int chosenIndex = scan.nextInt();
        toServer.writeInt(chosenIndex);
        System.out.println(fromServer.readUTF());
    }
}
