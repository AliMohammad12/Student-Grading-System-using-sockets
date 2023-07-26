package org.example;

import DAO.*;
import DAOimpl.*;
import Model.Account;
import Model.Student;
import Service.*;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Server {
    private AccountService accountService;
    private CourseService courseService;
    private DepartmentService departmentService;
    private InstructorService instructorService;
    private StudentService studentService;
    private CourseEnrollmentService courseEnrollmentService;

    DataInputStream inputFromClient = null;
    DataOutputStream outputToClient = null;
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

    }
    public void startServer(int port) {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server is running and listening on port " + port);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("New client connected: " + clientSocket.getInetAddress().getHostAddress());

                inputFromClient = new DataInputStream(clientSocket.getInputStream());
                outputToClient = new DataOutputStream(clientSocket.getOutputStream());

                Thread clientThread = new ClientHandler(clientSocket);
                clientThread.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }




    private class ClientHandler extends Thread {
        private Socket clientSocket;
        public ClientHandler(Socket clientSocket) {
            this.clientSocket = clientSocket;
        }

        @Override
        public void run() {
            try {
                Account account = null;
                do {
                    showMenu();
                    int choice = inputFromClient.readInt();
                    if (choice == 1) {
                        account = handleLoggingIn();
                    } else if (choice == 2) {
                        handleRegistration();
                    } else {
                        clientSocket.close();
                        return;
                    }

                } while (account == null);

                // I have the account (Logged in)
                String role = account.getRole();
                outputToClient.writeUTF(role);
                if (role == "Student") {
                    // fetch student data
                    // select from student table where
                    System.out.println("Welcome Student ");
                } else {

                }
                clientSocket.close();

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
        private Account handleLoggingIn() throws IOException {
            outputToClient.writeUTF("[Server] Signing In!");
            boolean loggedIn = false;
            Account account = null;
            while (!loggedIn) {
                outputToClient.writeUTF("- Please enter your email: ");
                String email = inputFromClient.readUTF();
                outputToClient.writeUTF("- Please enter your password: ");
                String password = inputFromClient.readUTF();

                boolean found = accountService.emailExists(email);
                boolean success = false;
                if (found) {
                    account = accountService.getAccountByEmail(email);
                    if (PasswordHasher.verifyPassword(account.getHashedPassword(), password)) {
                        success = true;
                        loggedIn = true;
                    }
                }
                outputToClient.writeInt(success ? 1 : 0);
                if (!success){
                    outputToClient.writeUTF("[Server] Wrong login credential!");
                    outputToClient.writeUTF("- Press 1 to try again.");
                    outputToClient.writeUTF("- Press 2 to go back.");

                    int choice = inputFromClient.readInt();
                    if (choice == 2) return null;
                }
            }
            outputToClient.writeUTF("[Server] You have been logged in successfully");
            return account;
        }

        private void handleRegistration() throws IOException {
            boolean success = false;
            String role = "";
            int accountId = -1;
            do {
                outputToClient.writeUTF("[Server] To register new account, please enter the following information: ");
                outputToClient.writeUTF("- Email: ");
                String email = inputFromClient.readUTF();
                outputToClient.writeUTF("- Password: ");
                String password = inputFromClient.readUTF();
                outputToClient.writeUTF("- Are you registering as Student or Instructor ? ");
                role = inputFromClient.readUTF();
                // ............

                boolean isEmailValid = isEmailValid(email);
                outputToClient.writeBoolean(isEmailValid);
                if (!isEmailValid) {
                    outputToClient.writeUTF("[Server] Email format is not allowed, please retry again");
                    outputToClient.writeUTF("- press 1 to try again!");
                    outputToClient.writeUTF("- press 2 to back!");
                    int choice = inputFromClient.readInt();
                    if (choice == 2) return;
                    continue;
                }

                boolean isEmailUnique = !accountService.emailExists(email);
                outputToClient.writeBoolean(isEmailUnique);

                if (isEmailUnique) {
                    String hashedPassword = PasswordHasher.hashPassword(password);
                    Account account = new Account(email, hashedPassword, role);
                    accountService.createAccount(account);
                    outputToClient.writeUTF("[Server] You have registered a new account successfully!");
                    accountId = accountService.getAccountIdByEmail(email);
                    success = true;
                } else {
                    outputToClient.writeUTF("[Server] Email is already used!");
                    outputToClient.writeUTF("- press 1 to try again!");
                    outputToClient.writeUTF("- press 2 to back!");
                    int choice = inputFromClient.readInt();
                    if (choice == 2) return;
                }

            } while (success == false);

            outputToClient.writeUTF(role);
            if (role.equals("Student")) {
                handleStudentRegistration(accountId);
            } else {
                handleInstructorRegistration(accountId);
            }
        }
        private boolean isEmailValid(String email) {
            String regexPattern = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
                    + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
            Pattern pattern = Pattern.compile(regexPattern);
            Matcher matcher = pattern.matcher(email);
            return matcher.matches();
        }

        private void handleStudentRegistration(int accountId) throws IOException {
            outputToClient.writeUTF("[Server] Please enter your student information: ");
            outputToClient.writeUTF("- First Name: ");
            String firstName = inputFromClient.readUTF();

            outputToClient.writeUTF("- Last Name: ");
            String lastName = inputFromClient.readUTF();

            outputToClient.writeUTF("- Major: ");
            String major = inputFromClient.readUTF(); // make it read line (remember)

            outputToClient.writeUTF("- Academic Year: ");
            int academicYear = inputFromClient.readInt();

            Student student = new Student(firstName, lastName, major, academicYear, accountId);
            studentService.createStudent(student);

            outputToClient.writeUTF("[Server] Congratulations " + firstName + " " + lastName + " your account is now ready!");
        }

        private void handleInstructorRegistration(int accountId) {

        }


        private void handleStudentLogin(PrintWriter writer, String username, String password) {
            if (authenticateStudent(username, password)) {
                writer.println("SUCCESS");
            } else {
                writer.println("FAILURE");
            }
        }
        private void handleInstructorLogin(PrintWriter writer, String username, String password) {
            if (authenticateTeacher(username, password)) {
                writer.println("SUCCESS");
            } else {
                writer.println("FAILURE");
            }
        }
        private void handleGetStudentMarks(PrintWriter writer, String username) {
            String marks = getStudentMarks(username);
            writer.println(marks);
        }
        private void handleEditStudentMarks(PrintWriter writer, String username) {
            // Allow teacher to edit student marks
            // Perform the necessary operations to edit student marks...
        }
        private boolean authenticateStudent(String username, String password) {
            //     String storedPassword = studentDatabase.get(username);
            //     return storedPassword != null && storedPassword.equals(password);
            return true;
        }
        private boolean authenticateTeacher(String username, String password) {
            //    String storedPassword = teacherDatabase.get(username);
            //    return storedPassword != null && storedPassword.equals(password);
            return true;
        }
        private String getStudentMarks(String username) {
            // Retrieve student marks from the database based on the username
            // Return the marks as a string (for simplicity in this example)
            return "Math: 90, Science: 85, English: 95";
        }
    }


    public static void main( String[] args ) {

        int port = 8080; // Choose a suitable port number
        Server server = new Server();
        server.startServer(port);


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
