package server;

import model.Course;
import model.Department;
import model.Instructor;
import model.Student;
import service.*;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.List;

public class ServerAdminHandler {
    private InstructorService instructorService;
    private StudentService studentService;
    private CourseService courseService;
    private AccountService accountService;
    private DepartmentService departmentService;
    private DataInputStream inputFromClient = null;
    private DataOutputStream outputToClient = null;

    public ServerAdminHandler(InstructorService instructorService, StudentService studentService, CourseService courseService,
                              AccountService accountService, DepartmentService departmentService) {
        this.instructorService = instructorService;
        this.studentService = studentService;
        this.courseService = courseService;
        this.accountService = accountService;
        this.departmentService = departmentService;
    }

    public void handleAdmin() throws IOException {
        outputToClient.writeUTF("[Server] Welcome Admin");
        adminHandler();
    }

    private void adminHandler() throws IOException {
        int choice = 0;
        do {
            adminMenu();
            choice = inputFromClient.readInt();
            if (choice == 1) {
                displayAllStudents();
            } else if (choice == 2) {
                displayAllInstructors();
            } else if (choice == 3) {
                displayAllDepartments();
            } else if (choice == 4) {
                displayAllCourses();
            } else if (choice == 5) {
                addNewDepartment();
            } else if (choice == 6) {
                addNewCourse();
            } else if (choice == 7) {
                deleteDepartment();
            } else if (choice == 8) {
                deleteCourse();
            } else if (choice == 9) {
                deleteStudent();
            } else if (choice == 10) {
                deleteInstructor();
            }
        } while (choice != 11);
    }

    private void displayAllStudents() throws IOException {
        List<Student> studentList = studentService.getAllStudents();
        int numberOfStudents = studentList.size();
        outputToClient.writeInt(numberOfStudents);
        outputToClient.writeUTF("\n[Server] All students list: ");
        for (int i = 0; i < numberOfStudents; i++) {
            Student student = studentList.get(i);
            outputToClient.writeUTF((i + 1) + "- First name: " + student.getFirstName() + " | Last name: " + student.getLastName()
            + " | Email: " + student.getEmail() + " | Major: " + student.getMajor() + " | Academic year: " + student.getAcademicYear());
        }
    }

    private void displayAllInstructors() throws IOException {
        List<Instructor> instructorList = instructorService.getAllInstructors();
        int numberOfInstructors = instructorList.size();
        outputToClient.writeInt(numberOfInstructors);
        outputToClient.writeUTF("\n[Server] All instructors list: ");
        for (int i = 0; i < numberOfInstructors; i++) {
            Instructor instructor = instructorList.get(i);
            outputToClient.writeUTF((i + 1) + "- First name: " + instructor.getFirstName() + " | Last name: " + instructor.getLastName()
                    + " | Email: " + instructor.getEmail() + " | Department: " + instructor.getDepartment().getName());
        }
    }

    private void displayAllDepartments() throws IOException {
        List<Department> departmentList = departmentService.getAllDepartments();
        int numberOfDepartments = departmentList.size();
        outputToClient.writeInt(numberOfDepartments);
        outputToClient.writeUTF("\n[Server] All departments list: ");
        for (int i = 0; i < numberOfDepartments; i++) {
            Department department = departmentList.get(i);
            outputToClient.writeUTF((i + 1) + ": " + department.getName());
        }
    }

    private void displayAllCourses() throws IOException {
        List<Course> courseList = courseService.getAllCourses();
        int numberOfCourses = courseList.size();
        outputToClient.writeInt(numberOfCourses);
        outputToClient.writeUTF("\n[Server] All courses list: ");
        for (int i = 0; i < numberOfCourses; i++) {
            Course course = courseList.get(i);
            outputToClient.writeUTF((i + 1) + "- Name: " + course.getCourseName() + " | Department: " + course.getDepartment().getName());
        }
    }

    private void addNewDepartment() throws IOException {
        outputToClient.writeUTF("[Server] You chose to add new department.. ");
        outputToClient.writeUTF("[Server] Enter new department name: ");
        String newDepartmentName = inputFromClient.readUTF();
        Department newDepartment = new Department(newDepartmentName);
        departmentService.createDepartment(newDepartment);
        outputToClient.writeUTF("[Server] Department " + newDepartmentName + " has been added successfully!");
    }

    private void addNewCourse() throws IOException {
        outputToClient.writeUTF("[Server] You choose to add new course!");
        List<Department> departmentList = departmentService.getAllDepartments();
        outputToClient.writeUTF("[Server] Select the department you want the course to be a part of: ");
        int numberOfDepartments = departmentList.size();
        outputToClient.writeInt(numberOfDepartments);
        for (int i = 0; i < numberOfDepartments; i++) {
            Department department = departmentList.get(i);
            outputToClient.writeUTF((i + 1) + ": " + department.getName());
        }
        int selectedDepartmentId = inputFromClient.readInt() - 1;
        Department department = departmentList.get(selectedDepartmentId);
        outputToClient.writeUTF("[Server] Enter the name of the new course: ");
        String newCourseName = inputFromClient.readUTF();
        Course newCourse = new Course(newCourseName, department);
        courseService.createCourse(newCourse);
        outputToClient.writeUTF("[Server] The course " + newCourseName + " has been successfully added to department " + department.getName());
    }

    private void deleteDepartment() throws IOException {
        outputToClient.writeUTF("[Server] You choose to delete a department!");
        outputToClient.writeUTF("[Server] Departments List: ");
        List<Department> departmentList = departmentService.getAllDepartments();
        int numberOfDepartments = departmentList.size();
        outputToClient.writeInt(numberOfDepartments);
        for (int i = 0; i < numberOfDepartments; i++) {
            Department department = departmentList.get(i);
            outputToClient.writeUTF((i + 1) + ": " + department.getName());
        }
        outputToClient.writeUTF("[Server] Please enter department Id from the list above to delete: ");
        int departmentIdToDelete = inputFromClient.readInt() - 1;
        Department departmentToDelete = departmentList.get(departmentIdToDelete);

        // delete courses and instructors with same department
        int departmentId = departmentToDelete.getId();
//        courseService.deleteCourseByDepartmentId(departmentId);
//        instructorService.deleteInstructorByDepartmentId(departmentId);
        departmentService.deleteDepartment(departmentId);
        outputToClient.writeUTF("[Server] You have successfully deleted " + departmentToDelete.getName());
    }

    private void deleteCourse() throws IOException {
        outputToClient.writeUTF("[Server] You choose to delete a course!");
        outputToClient.writeUTF("[Server] Courses List: ");
        List<Course> courseList = courseService.getAllCourses();
        int numberOfCourses = courseList.size();
        outputToClient.writeInt(numberOfCourses);
        for (int i = 0; i < numberOfCourses; i++) {
            Course course = courseList.get(i);
            outputToClient.writeUTF((i + 1) + ": Name: " + course.getCourseName() + " | Department: " + course.getDepartment().getName());
        }
        outputToClient.writeUTF("[Server] Please enter course Id from the list above to delete: ");
        int courseIdToDelete = inputFromClient.readInt() - 1;
        Course courseToDelete = courseList.get(courseIdToDelete);

        int courseId = courseToDelete.getCourseId();
//        courseService.deleteStudentCoursesByCourseId(courseId);
//        courseService.deleteInstructorCoursesByCourseId(courseId);
        courseService.deleteCourse(courseId);
        outputToClient.writeUTF("[Server] You have successfully deleted " + courseToDelete.getCourseName());
    }

    private void deleteStudent() throws IOException {
        outputToClient.writeUTF("[Server] You choose to delete a student!");
        List<Student> studentList = studentService.getAllStudents();
        int numberOfStudents = studentList.size();
        outputToClient.writeUTF("\n[Server] All students list: ");
        outputToClient.writeInt(numberOfStudents);
        for (int i = 0; i < numberOfStudents; i++) {
            Student student = studentList.get(i);
            outputToClient.writeUTF((i + 1) + "- First name: " + student.getFirstName() + " | Last name: " + student.getLastName()
                    + " | Email: " + student.getEmail() + " | Major: " + student.getMajor() + " | Academic year: " + student.getAcademicYear());
        }
        outputToClient.writeUTF("[Server] Please enter student Id from the list above to delete: ");
        int studentIdToDelete = inputFromClient.readInt() - 1;
        Student studentToDelete = studentList.get(studentIdToDelete);
        int accountId = studentToDelete.getAccountId();

        courseService.deleteCourseEnrollmentByStudentId(studentToDelete.getStudentId());
        studentService.deleteStudent(studentToDelete.getStudentId());
        accountService.deleteAccount(accountId);
        outputToClient.writeUTF("[Server] You have successfully deleted everything related to the selected student!");
    }

    private void deleteInstructor() throws IOException {
        outputToClient.writeUTF("[Server] You choose to delete instructor!");
        List<Instructor> instructorList = instructorService.getAllInstructors();
        int numberOfInstructors = instructorList.size();
        outputToClient.writeUTF("\n[Server] All Instructors list: ");
        outputToClient.writeInt(numberOfInstructors);
        for (int i = 0; i < numberOfInstructors; i++) {
            Instructor instructor = instructorList.get(i);
            outputToClient.writeUTF((i + 1) + "- First name: " + instructor.getFirstName() + " | Last name: " + instructor.getLastName()
                    + " | Email: " + instructor.getEmail() + " | Department: " + instructor.getDepartment().getName());
        }
        outputToClient.writeUTF("[Server] Please enter instructor Id from the list above to delete: ");
        int instructorIdToDelete = inputFromClient.readInt() - 1;
        Instructor instructorToDelete = instructorList.get(instructorIdToDelete);
        int accountId = instructorToDelete.getAccountId();
        int instructorId = instructorToDelete.getInstructorId();

        courseService.deleteStudentCoursesByInstructorId(instructorId);
        courseService.deleteInstructorCoursesByInstructorId(instructorId);
        instructorService.deleteInstructor(instructorId);
        accountService.deleteAccount(accountId);
        outputToClient.writeUTF("[Server] You have successfully deleted everything related to the selected instructor!");
    }

    private void adminMenu() throws IOException {
        outputToClient.writeUTF("\n[Server] Please choose what you want to do: ");
        outputToClient.writeUTF("1- See all students.");
        outputToClient.writeUTF("2- See all instructors.");
        outputToClient.writeUTF("3- See all departments.");
        outputToClient.writeUTF("4- See all courses.");
        outputToClient.writeUTF("5- Add new department.");
        outputToClient.writeUTF("6- Add new course.");
        outputToClient.writeUTF("7- Delete department.");
        outputToClient.writeUTF("8- Delete course.");
        outputToClient.writeUTF("9- Delete student.");
        outputToClient.writeUTF("10- Delete instructor.");
        outputToClient.writeUTF("11- Logout");
    }
    public void setInputFromClient(DataInputStream inputFromClient) {
        this.inputFromClient = inputFromClient;
    }
    public void setOutputToClient(DataOutputStream outputToClient) {
        this.outputToClient = outputToClient;
    }
}
