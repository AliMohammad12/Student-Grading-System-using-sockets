package org.example;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    Socket socket;
    DataOutputStream toServer = null;
    DataInputStream fromServer = null;

    public Client(String host, int port) throws IOException {
        socket = new Socket(host, port);

        fromServer = new DataInputStream(socket.getInputStream());
        toServer = new DataOutputStream(socket.getOutputStream());

        System.out.println("Connected to " + host + " at port = " + port);
        start();
    }

    public void start() throws IOException {
        boolean loggedIn = false;
        do {
            responseMenu();
            Scanner scan = new Scanner(System.in);
            int choice = scan.nextInt();
            toServer.writeInt(choice);

            if (choice == 1) {
                loggedIn = responseLoggingIn();
            } else if (choice == 2) {
                responseRegistration();
            } else {
                toServer.flush();
                return;
            }
        } while (loggedIn == false);


        // I have the account (Logged in)
        String role = fromServer.readUTF();
        if (role == "Student") {

        } else {

        }
        toServer.flush();
    }

    public void responseMenu() throws IOException {
        System.out.println(fromServer.readUTF()); // choose option
        System.out.println(fromServer.readUTF()); // login
        System.out.println(fromServer.readUTF()); // register
        System.out.println(fromServer.readUTF()); // exit
    }

    private boolean responseLoggingIn() throws IOException {
        System.out.println(fromServer.readUTF());
        int loggedIn = 0;
        while (loggedIn == 0) {
            Scanner scan = new Scanner(System.in);
            System.out.println(fromServer.readUTF());
            String email = scan.next();
            toServer.writeUTF(email);

            System.out.println(fromServer.readUTF());
            String password = scan.next();
            toServer.writeUTF(password);

            loggedIn = fromServer.readInt();
            if (loggedIn == 0) {
                System.out.println(fromServer.readUTF());
                System.out.println(fromServer.readUTF());
                System.out.println(fromServer.readUTF());

                int choice = scan.nextInt();
                toServer.writeInt(choice);
                if (choice == 2) return false;
            }
        }
        System.out.println(fromServer.readUTF());
        return true;
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
            String role = scan.next();
            toServer.writeUTF(role);
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

        String role = fromServer.readUTF();
        if (role.equals("Student")) {
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

    private void responseInstructorRegistration() {

    }

    public static void main(String[] args) throws IOException {
        Client client = new Client("localhost", 8080);


    }
}
