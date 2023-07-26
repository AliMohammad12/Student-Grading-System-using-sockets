package Model;

public class Student {
    private String firstName;
    private String lastName;
    private String major;
    private int academicYear;

    private int accountId;

    public Student(String firstName, String lastName, String major, int academicYear, int accountId) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.major = major;
        this.academicYear = academicYear;
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
}
