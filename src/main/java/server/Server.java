package server;

import dao.*;
import daoimpl.*;
import model.Account;
import model.Course;
import model.Instructor;
import model.Student;
import service.*;

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
    private CourseEnrollmentService courseEnrollmentService;
    private ServerLoginHandler loginHandler;
    private ServerRegistrationHandler registrationHandler;
    private ServerStudentHandler serverStudentHandler;
    private ServerInstructorHandler serverInstructorHandler;
    private DataInputStream inputFromClient = null;
    private DataOutputStream outputToClient = null;
    public Server() {
        AccountDao accountDao = new AccountDaoImpl();
        CourseDao courseDao = new CourseDaoImpl();
        DepartmentDao departmentDao = new DepartmentDaoImpl();
        InstructorDao instructorDao = new InstructorDaoImpl();
        StudentDao studentDao = new StudentDaoImpl();
        CourseEnrollmentDao courseEnrollmentDao = new CourseEnrollmentDaoImpl();

        accountService = new AccountService(accountDao);
        courseService = new CourseService(courseDao);
        departmentService = new DepartmentService(departmentDao);
        instructorService = new InstructorService(instructorDao);
        studentService = new StudentService(studentDao);
        courseEnrollmentService = new CourseEnrollmentService(courseEnrollmentDao);

        loginHandler = new ServerLoginHandler(accountService);

        registrationHandler = new ServerRegistrationHandler(accountService,
                studentService, instructorService, departmentService);

        serverStudentHandler = new ServerStudentHandler(studentService, courseService);
        serverInstructorHandler = new ServerInstructorHandler(instructorService, courseService, studentService);
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

                    // I have the account (Logged in)
                    String role = account.getRole();
                    outputToClient.writeUTF(role);
                    if (role.equals("Student")) {
                        serverStudentHandler.handleStudent(account);
                    } else {
                        serverInstructorHandler.handleInstructor(account);
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

//        Course course1 = new Course("ABC", "123");
//        Course course2 = new Course("ABC", "123");
//        Course course3 = new Course("ABC", "4");
//          CourseDao courseDao = new CourseDaoImpl();
//          CourseService courseService1 = new CourseService(courseDao);
//          courseService1.getCourseDetailsWithInstructors();
        int port = 8080;
        Server server = new Server();
        server.startServer(port);

//        InstructorDao instructorDao = new InstructorDaoImpl();
//        InstructorService instructorService = new InstructorService(instructorDao);
//
//        Instructor inst = new Instructor("Ali", " OMARRR ", "COMPUTER ENGINEERING", 3);
//        instructorService.createInstructor(inst);

//        System.out.println( "Hello World!" );
//        AccountDao accountDao = new AccountDaoImpl();
//        AccountService accountService = new AccountService(accountDao);
////
//        String email = "ahmad@gmail.com";
//        String password = "12321";
//      //  accountDao.createAccount(acco unt);
//        String hashed = PasswordHasher.hashPassword(password);
//        Account account = new Account(email, hashed, "Student");
//        accountService.createAccount(account);
//
//        System.out.println(hashed);
//        System.out.println(password);
    }
}
