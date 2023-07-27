package model;

public class Instructor {
    private String firstName;
    private String lastName;
    private String departmentName;
    private String email;
    private int accountId;
    private int instructorId;
    public Instructor(String firstName, String lastName, String departmentName, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.departmentName = departmentName;
        this.email = email;
    }

    public Instructor(String firstName, String lastName, String departmentName, String email, int accountId) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.departmentName = departmentName;
        this.email = email;
        this.accountId = accountId;
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

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getInstructorId() {
        return instructorId;
    }
    public void setInstructorId(int instructorId) {
        this.instructorId = instructorId;
    }

    @Override
    public String toString() {
        String ret = "";
        ret += "Name: " + firstName + " " + lastName +  "| Email : " + email;
        return ret;
    }
}
