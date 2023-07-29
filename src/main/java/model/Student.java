package model;

public class Student {
    private String firstName;
    private String lastName;
    private String major;
    private int academicYear;
    private String email;
    private int accountId;
    private int studentId;

    public Student(String firstName, String lastName, String major, int academicYear, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.major = major;
        this.academicYear = academicYear;
        this.email = email;
    }
    public Student(String firstName, String lastName, String major, int academicYear, String email, int accountId) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.major = major;
        this.academicYear = academicYear;
        this.email = email;
        this.accountId = accountId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public int getAcademicYear() {
        return academicYear;
    }

    public void setAcademicYear(int academicYear) {
        this.academicYear = academicYear;
    }

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    @Override
    public String toString() {
        String ret;
        ret = "Your Student Information: \n";
        ret += "First Name: " + firstName + "\n";
        ret += "Last Name: " + lastName + "\n";
        ret += "Major: " + major + "\n";
        ret += "Academic Year: " + academicYear + "\n";
        ret += "email: " + email;
        return ret;
    }
}
