package server;

import model.Account;
import model.Department;
import model.Instructor;
import model.Student;
import service.AccountService;
import service.DepartmentService;
import service.InstructorService;
import service.StudentService;
import util.PasswordHasher;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ServerRegistrationHandler {
    private AccountService accountService;
    private StudentService studentService;
    private InstructorService instructorService;
    private DepartmentService departmentService;
    private DataInputStream inputFromClient = null;
    private DataOutputStream outputToClient = null;

    public ServerRegistrationHandler(AccountService accountService, StudentService studentService, InstructorService instructorService,
                               DepartmentService departmentService) {
        this.accountService = accountService;
        this.studentService = studentService;
        this.instructorService = instructorService;
        this.departmentService = departmentService;
    }

    public void register() throws IOException {
        handleRegistration();
    }

    private void handleRegistration() throws IOException {
        boolean success = false;
        int role = 0;
        int accountId = -1;
        String email = "";
        do {
            outputToClient.writeUTF("[Server] To register new account, please enter the following information: ");
            outputToClient.writeUTF("- Email: ");
            email = inputFromClient.readUTF();
            outputToClient.writeUTF("- Password: ");
            String password = inputFromClient.readUTF();

            outputToClient.writeUTF("[Server] What are you registering as ? ");
            outputToClient.writeUTF("1- Student");
            outputToClient.writeUTF("2- Instructor");
            role = inputFromClient.readInt();
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
                Account account = new Account(email, hashedPassword, (role == 1 ? "Student" : "Instructor"));
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

        outputToClient.writeInt(role);
        if (role == 1) {
            handleStudentRegistration(accountId, email);
        } else {
            handleInstructorRegistration(accountId, email);
        }
    }
    private boolean isEmailValid(String email) {
        String regexPattern = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
                + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
        Pattern pattern = Pattern.compile(regexPattern);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    private void handleStudentRegistration(int accountId, String email) throws IOException {
        outputToClient.writeUTF("[Server] Please enter your student information: ");
        outputToClient.writeUTF("- First Name: ");
        String firstName = inputFromClient.readUTF();

        outputToClient.writeUTF("- Last Name: ");
        String lastName = inputFromClient.readUTF();

        outputToClient.writeUTF("- Major: ");
        String major = inputFromClient.readUTF(); // make it read line (remember)

        outputToClient.writeUTF("- Academic Year: ");
        int academicYear = inputFromClient.readInt();

        Student student = new Student(firstName, lastName, major, academicYear, email, accountId);
        studentService.createStudent(student);
        outputToClient.writeUTF("[Server] Congratulations " + firstName + " " + lastName + " your account is now ready!");
    }

    private void handleInstructorRegistration(int accountId, String email) throws IOException {
        outputToClient.writeUTF("[Server] Please enter your instructor information: ");
        outputToClient.writeUTF("- First Name: ");
        String firstName = inputFromClient.readUTF();

        outputToClient.writeUTF("- Last Name: ");
        String lastName = inputFromClient.readUTF();

        outputToClient.writeUTF("[Server] Please select the id of the department you want to be a part of. ");
        List<Department> departmentList = departmentService.getAllDepartments();
        outputToClient.writeInt((int)departmentList.size());
        for (int i = 0; i < departmentList.size(); i++) {
            outputToClient.writeUTF((i + 1) + ": " + departmentList.get(i).getName());
        }
        int selectedIndex = inputFromClient.readInt() - 1;
        Department selectedDepartment = departmentList.get(selectedIndex);

        Instructor instructor = new Instructor(firstName, lastName, selectedDepartment, email, accountId);
        instructorService.createInstructor(instructor);
        outputToClient.writeUTF("[Server] Congratulations " + firstName + " " + lastName + " your account is now ready!");
    }


    public void setInputFromClient(DataInputStream inputFromClient) {
        this.inputFromClient = inputFromClient;
    }

    public void setOutputToClient(DataOutputStream outputToClient) {
        this.outputToClient = outputToClient;
    }
}
