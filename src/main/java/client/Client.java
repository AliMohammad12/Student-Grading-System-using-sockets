package client;

import util.InputValidator;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    private Socket socket;
    private ClientLoginHandler clientLoginHandler;
    private ClientRegistrationHandler clientRegistrationHandler;
    private ClientStudentHandler clientStudentHandler;
    private ClientInstructorHandler clientInstructorHandler;
    private ClientAdminHandler clientAdminHandler;
    private InputValidator inputValidator;
    private DataOutputStream toServer = null;
    private DataInputStream fromServer = null;
    private int menuSize;
    public Client(String host, int port) throws IOException {
        socket = new Socket(host, port);

        fromServer = new DataInputStream(socket.getInputStream());
        toServer = new DataOutputStream(socket.getOutputStream());
        clientLoginHandler = new ClientLoginHandler(toServer, fromServer);
        clientRegistrationHandler = new ClientRegistrationHandler(toServer, fromServer);
        clientStudentHandler = new ClientStudentHandler(toServer, fromServer);
        clientInstructorHandler = new ClientInstructorHandler(toServer, fromServer);
        clientAdminHandler = new ClientAdminHandler(toServer, fromServer);
        inputValidator = new InputValidator();
        menuSize = 3;

        System.out.println("Connected to " + host + " at port = " + port);
        start();
    }

    public void start() throws IOException {
        do {
            boolean loggedIn = false;
            do {
                responseMainMenu();
                int choice = inputValidator.getValidInteger(1, menuSize);
                toServer.writeInt(choice);

                if (choice == 1) {
                    loggedIn = clientLoginHandler.login();
                } else if (choice == 2) {
                    clientRegistrationHandler.register();
                } else {
                    toServer.flush();
                    return;
                }
            } while (loggedIn == false);

            String role = fromServer.readUTF();
            if (role.equals("Student")) {
                clientStudentHandler.handleStudent();
            } else if (role.equals("Instructor")){
                clientInstructorHandler.handleInstructor();
            } else if (role.equals("ADMIN")) {
                clientAdminHandler.handleAdmin();
            }
        } while (true);
    }

    public void responseMainMenu() throws IOException {
        System.out.println(fromServer.readUTF()); // choose option
        System.out.println(fromServer.readUTF()); // login
        System.out.println(fromServer.readUTF()); // register
        System.out.println(fromServer.readUTF()); // exit
    }

    public static void main(String[] args) throws IOException {
        Client client = new Client("localhost", 8080);
    }
}
