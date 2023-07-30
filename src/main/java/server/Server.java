package server;

import dao.*;
import daoimpl.*;
import model.Account;
import model.Course;
import model.Instructor;
import model.Student;
import service.*;
import util.PasswordHasher;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Server {
    private AccountService accountService;
    private CourseService courseService;
    private DepartmentService departmentService;
    private InstructorService instructorService;
    private StudentService studentService;
    private ServerLoginHandler loginHandler;
    private ServerRegistrationHandler registrationHandler;
    private ServerStudentHandler serverStudentHandler;
    private ServerInstructorHandler serverInstructorHandler;
    private ServerAdminHandler serverAdminHandler;
    private DataInputStream inputFromClient = null;
    private DataOutputStream outputToClient = null;
    public Server() {
        AccountDao accountDao = new AccountDaoImpl();
        CourseDao courseDao = new CourseDaoImpl();
        DepartmentDao departmentDao = new DepartmentDaoImpl();
        InstructorDao instructorDao = new InstructorDaoImpl();
        StudentDao studentDao = new StudentDaoImpl();

        accountService = new AccountService(accountDao);
        courseService = new CourseService(courseDao);
        departmentService = new DepartmentService(departmentDao);
        instructorService = new InstructorService(instructorDao);
        studentService = new StudentService(studentDao);

        loginHandler = new ServerLoginHandler(accountService);

        registrationHandler = new ServerRegistrationHandler(accountService,
                studentService, instructorService, departmentService);

        serverStudentHandler = new ServerStudentHandler(studentService, courseService);
        serverInstructorHandler = new ServerInstructorHandler(instructorService, courseService, studentService);
        serverAdminHandler = new ServerAdminHandler(instructorService, studentService, courseService, accountService, departmentService);
    }
    public void startServer(int port) {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server is running and listening on port " + port);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("New client connected: " + clientSocket.getInetAddress().getHostAddress());
                initialize(clientSocket);

                Thread clientThread = new ClientHandler(clientSocket);
                clientThread.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void initialize(Socket clientSocket) throws IOException {
        inputFromClient = new DataInputStream(clientSocket.getInputStream());
        outputToClient = new DataOutputStream(clientSocket.getOutputStream());

        loginHandler.setInputFromClient(inputFromClient);
        loginHandler.setOutputToClient(outputToClient);

        registrationHandler.setInputFromClient(inputFromClient);
        registrationHandler.setOutputToClient(outputToClient);

        serverStudentHandler.setInputFromClient(inputFromClient);
        serverStudentHandler.setOutputToClient(outputToClient);

        serverInstructorHandler.setInputFromClient(inputFromClient);
        serverInstructorHandler.setOutputToClient(outputToClient);

        serverAdminHandler.setInputFromClient(inputFromClient);
        serverAdminHandler.setOutputToClient(outputToClient);
    }

    private class ClientHandler extends Thread {
        private Socket clientSocket;
        public ClientHandler(Socket clientSocket) {
            this.clientSocket = clientSocket;
        }

        @Override
        public void run() {
            try {
                do {
                    Account account = null;
                    boolean loggedIn = false;
                    do {
                        showMenu();
                        int choice = inputFromClient.readInt();
                        if (choice == 1) {
                            account = loginHandler.login();
                            loggedIn = true;
                        } else if (choice == 2) {
                            registrationHandler.register();
                        } else {
                            clientSocket.close();
                            return;
                        }
                    } while (loggedIn == false);

                    String role = account.getRole();
                    outputToClient.writeUTF(role);
                    if (role.equals("Student")) {
                        serverStudentHandler.handleStudent(account);
                    } else if (role.equals("Instructor")){
                        serverInstructorHandler.handleInstructor(account);
                    } else if (role.equals("ADMIN")) {
                        serverAdminHandler.handleAdmin();
                    }
                } while (true);
            } catch(IOException e){
                e.printStackTrace();
            }
        }

        private void showMenu() throws IOException {
            outputToClient.writeUTF("[Server] Please choose an option:");
            outputToClient.writeUTF("1. Login");
            outputToClient.writeUTF("2. Register");
            outputToClient.writeUTF("3. Exit");

        }
    }

    public static void main( String[] args ) {
//        String password = "ADMIN";
//        String hashedPassword = PasswordHasher.hashPassword(password);
//        System.out.println(hashedPassword);
//        Account account = new Account("ADMIN", hashedPassword, "ADMIN");
//        AccountDao accountDao = new AccountDaoImpl();
//        AccountService accountService = new AccountService(accountDao);
//        accountService.createAccount(account);
        int port = 8080;
        Server server = new Server();
        server.startServer(port);


    }
}
